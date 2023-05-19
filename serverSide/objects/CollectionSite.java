package serverSide.objects;

import java.rmi.RemoteException;
import java.util.*;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.MuseumStub;
import interfaces.AssaultPartyInterface;
import interfaces.CollectionSiteInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import serverSide.entities.CollectionSiteProxyAgent;
import serverSide.main.CollectionSiteMain;
import utils.Constants;

/**
 * Collection Site where intelligence and paintings are stored.
 */
public class CollectionSite implements CollectionSiteInterface {
    /**
     * Number of paintings acquired.
     */
    private int paintings;

    /**
     * Perception of the Master Thief about what rooms are empty.
     */
    private final boolean[] emptyRooms;

    /**
     * FIFO of the available Assault Parties.
     */
    private final Deque<Integer> availableParties;

    /**
     * FIFOs of the arriving Ordinary Thieves (one for each Assault Party).
     */
    private final List<Deque<Integer>> arrivingThieves;

    /**
     * The General Repository where logging occurs.
     */
    private final GeneralRepositoryInterface generalRepositoryStub;

    /**
     * The array holding the Assault Parties shared regions.
     */
    private final AssaultPartyInterface[] assaultPartyStubs;

    /** 
     * The Museum shared region.
     */
    private final MuseumInterface museumStub;

    /**
     * Collection Site constructor.
     * @param generalRepositoryStub the General Repository.
     * @param assaultPartyStubs the Assault Parties.
     * @param museumStub the Museum.
     */
    public CollectionSite(GeneralRepositoryInterface generalRepositoryStub, AssaultPartyInterface[] assaultPartyStubs,
                          MuseumInterface museumStub) {
        this.generalRepositoryStub = generalRepositoryStub;
        this.assaultPartyStubs = assaultPartyStubs;
        this.museumStub = museumStub;
        paintings = 0;
        emptyRooms = new boolean[Constants.NUM_ROOMS];
        Arrays.fill(emptyRooms, false);
        availableParties = new ArrayDeque<>();
        arrivingThieves = new LinkedList<>();
        for (int i = 0; i < Constants.NUM_ASSAULT_PARTIES; i++) {
            availableParties.add(i);
            arrivingThieves.add(new ArrayDeque<>(Constants.ASSAULT_PARTY_SIZE));
        }
    }

    /**
     * Getter for the number of paintings acquired.
     * @return the number of paintings.
     */
    public int getPaintings() {
        return paintings;
    }

    /**
    * This is the first state change in the MasterThief life cycle, it changes the MasterThief state to deciding what to do.
    */
    public int startOperations() {
        setMasterThiefState(MasterThief.DECIDING_WHAT_TO_DO);
        return MasterThief.DECIDING_WHAT_TO_DO;
    }

    /**
     * Called by Master Thief to appraise situation: either to take a rest, prepare assault party or
     * sum up results.
     * @return next situation.
     */
    public synchronized char appraiseSit() {
        boolean empty = true;
        int nEmptyRooms = 0;
        for (boolean emptyRoom: emptyRooms) {
            empty = empty && emptyRoom;
            if (emptyRoom) {
                nEmptyRooms++;
            }
        }
        List<Integer> assaultPartyRooms = new ArrayList<>();
        int room;
        for (AssaultPartyInterface assaultPartyInterface: assaultPartyStubs) {
            room = getAssaultPartyRoom(assaultPartyInterface);
            if (room != -1) {
                assaultPartyRooms.add(room);
            }
        }
        if (empty && this.availableParties.size() >= Constants.NUM_ASSAULT_PARTIES) {
            return 'E';
        }
        if (availableParties.size() == 0 || 
                (assaultPartyRooms.size() == 1 && nEmptyRooms == Constants.NUM_ROOMS - 1 && !emptyRooms[assaultPartyRooms.get(0)])) {
            return 'R';
        }
        if (!empty) {
            return 'P';
        }
        return 'R';
    }

    /**
     * Master Thief waits while there are still Assault Parties in operation.
     */
    public synchronized int takeARest() {
        setMasterThiefState(MasterThief.WAITING_FOR_ARRIVAL);
        while (this.arrivingThieves.get(0).isEmpty() && this.arrivingThieves.get(1).isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        return MasterThief.WAITING_FOR_ARRIVAL;
    }

    /**
     * Called by the Master Thief to collect all available canvas.
     */
    public synchronized int collectACanvas() {
        for (int i = 0; i < arrivingThieves.size(); i++) {
            for (int arrivingThief: arrivingThieves.get(i)) {
                if (hasBusyHands(i, arrivingThief)) {
                    paintings++;
                    setBusyHands(i, arrivingThief);
                } else {
                    setEmptyRoom(getAssaultPartyRoom(assaultPartyStubs[i]));
                }
                arrivingThieves.get(i).remove(arrivingThief); 
                synchronized (assaultPartyStubs[i]) {
                    removeAssaultPartyMember(i, arrivingThief);
                    if (isAssaultPartyEmpty(i)) {
                        setAssaultPartyInOperation(i);
                        disbandAssaultParty(i);
                        if (!availableParties.contains(i)) {
                            availableParties.add(i);
                        }
                    }
                }
            }
        }
        notifyAll();
        setMasterThiefState(MasterThief.DECIDING_WHAT_TO_DO);
        return MasterThief.DECIDING_WHAT_TO_DO;
    }

    /**
     * Called by the Ordinary Thief to hand a canvas to the Master Thief if they have any
     * - Synchronization point between each Ordinary Thief and the Master Thief.
     * @param assaultParty the identification of the Assault Party the thief belongs to.
     */
    public synchronized int handACanvas(int assaultParty, int ordinaryThief) {
        setOrdinaryThiefState(ordinaryThief);
        this.arrivingThieves.get(assaultParty).add(ordinaryThief);
        notifyAll();
        while (this.arrivingThieves.get(assaultParty).contains(ordinaryThief)) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        return OrdinaryThief.COLLECTION_SITE;
    }

    /**
     * Shuts down the Collection Site server.
     */
    public synchronized void shutdown () {
        CollectionSiteMain.shutdown();
    }

    /**
     * Get the number of the next Assault Party and remove it from the queue.
     * @return the Assault Party identification.
     */
    public int getNextAssaultPartyID() {
        return availableParties.poll();
    }

    /**
     * Returns the next empty room. Uses the perception of the Master Thief, not the Museum information.
     * @return the room.
     */
    public int getNextRoom() {
        for (int i = 0; i < emptyRooms.length; i++) {
            if (!emptyRooms[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the distance to a room.
     * @param room the room identification.
     * @return the distance to the room.
     */
    public int getRoomDistance(int room) {
        int ret = 0;
        try {
            ret = museumStub.getRoomDistance(room);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.getRoomDistance: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    /**
     * Gets the number of paintings in a room.
     * @param room the room identification.
     * @return the number of paintings in the room.
     */
    public int getRoomPaintings(int room) {
        int ret = 0;
        try {
            ret = museumStub.getRoomPaintings(room);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.getRoomPaintings: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    /**
     * Setter for the empty rooms.
     *
     * @param room the room identification.
     */
    private void setEmptyRoom(int room) {
        emptyRooms[room] = true;
    }

    private void setMasterThiefState(int state) {
        try {
            generalRepositoryStub.setMasterThiefState(state);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.setMasterThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setOrdinaryThiefState(int ordinaryThief) {
        try {
            generalRepositoryStub.setOrdinaryThiefState(ordinaryThief, OrdinaryThief.COLLECTION_SITE);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.setOrdinaryThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private int getAssaultPartyRoom(AssaultPartyInterface assaultParty) {
        int ret = 0;
        try {
            ret = assaultParty.getRoom();
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.getAssaultPartyRoom: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private boolean hasBusyHands(int assaultParty, int ordinaryThief) {
        boolean ret = false;
        try {
            ret = assaultPartyStubs[assaultParty].hasBusyHands(ordinaryThief);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.hasBusyHands: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private void setBusyHands(int assaultParty, int ordinaryThief) {
        try {
            assaultPartyStubs[assaultParty].setBusyHands(ordinaryThief, false);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.setBusyHands: " + e.getMessage());
            System.exit(1);
        }
    }

    private void removeAssaultPartyMember(int assaultParty, int ordinaryThief) {
        try {
            assaultPartyStubs[assaultParty].removeMember(ordinaryThief);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.removeAssaultPartyMember: " + e.getMessage());
            System.exit(1);
        }
    }

    private boolean isAssaultPartyEmpty(int assaultParty) {
        boolean ret = false;
        try {
            ret = assaultPartyStubs[assaultParty].isEmpty();
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.isAssaultPartyEmpty: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private void setAssaultPartyInOperation(int assaultParty) {
        try {
            assaultPartyStubs[assaultParty].setInOperation(false);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.setAssaultPartyInOperation: " + e.getMessage());
            System.exit(1);
        }
    }

    private void disbandAssaultParty(int assaultParty) {
        try {
            generalRepositoryStub.disbandAssaultParty(assaultParty);
        } catch (RemoteException e) {
            System.out.println("Remote exception on CollectionSite.disbandAssaultParty: " + e.getMessage());
            System.exit(1);
        }
    }
}
