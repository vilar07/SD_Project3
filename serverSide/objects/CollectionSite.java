package serverSide.objects;

import java.util.*;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.MuseumStub;
import serverSide.entities.CollectionSiteProxyAgent;
import serverSide.main.CollectionSiteMain;
import utils.Constants;

/**
 * Collection Site where intelligence and paintings are stored.
 */
public class CollectionSite {
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
    private final List<Deque<CollectionSiteProxyAgent>> arrivingThieves;

    /**
     * The General Repository where logging occurs.
     */
    private final GeneralRepositoryStub generalRepository;

    /**
     * The array holding the Assault Parties shared regions.
     */
    private final AssaultPartyStub[] assaultParties;

    /** 
     * The Museum shared region.
     */
    private final MuseumStub museum;

    /**
     * Collection Site constructor.
     * @param generalRepository the General Repository.
     * @param assaultParties the Assault Parties.
     * @param museum the Museum.
     */
    public CollectionSite(GeneralRepositoryStub generalRepository, AssaultPartyStub[] assaultParties, MuseumStub museum) {
        this.generalRepository = generalRepository;
        this.assaultParties = assaultParties;
        this.museum = museum;
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
    public void startOperations() {
        ((CollectionSiteProxyAgent) Thread.currentThread()).setMasterThiefState(MasterThief.DECIDING_WHAT_TO_DO);
        generalRepository.setMasterThiefState(((CollectionSiteProxyAgent) Thread.currentThread()).getMasterThiefState());
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
        for (AssaultPartyStub assaultPartyInterface: assaultParties) {
            room = assaultPartyInterface.getRoom();
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
    public synchronized void takeARest() {
        CollectionSiteProxyAgent masterThief = (CollectionSiteProxyAgent) Thread.currentThread();
        masterThief.setMasterThiefState(MasterThief.WAITING_FOR_ARRIVAL);
        generalRepository.setMasterThiefState(masterThief.getMasterThiefState());
        while (this.arrivingThieves.get(0).isEmpty() && this.arrivingThieves.get(1).isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
    }

    /**
     * Called by the Master Thief to collect all available canvas.
     */
    public synchronized void collectACanvas() {
        CollectionSiteProxyAgent masterThief = (CollectionSiteProxyAgent) Thread.currentThread();
        for (int i = 0; i < arrivingThieves.size(); i++) {
            for (CollectionSiteProxyAgent arrivingThief: arrivingThieves.get(i)) {
                if (assaultParties[i].hasBusyHands(arrivingThief.getOrdinaryThiefID())) {
                    paintings++;
                    assaultParties[i].setBusyHands(arrivingThief.getOrdinaryThiefID(), false);
                } else {
                    setEmptyRoom(assaultParties[i].getRoom(), true);
                }
                arrivingThieves.get(i).remove(arrivingThief); 
                synchronized (assaultParties[i]) {
                    assaultParties[i].removeMember(arrivingThief.getOrdinaryThiefID());
                    if (assaultParties[i].isEmpty()) {
                        assaultParties[i].setInOperation(false);
                        generalRepository.disbandAssaultParty(i);
                        if (!availableParties.contains(i)) {
                            availableParties.add(i);
                        }
                    }
                }
            }
        }
        notifyAll();
        masterThief.setMasterThiefState(MasterThief.DECIDING_WHAT_TO_DO);
        generalRepository.setMasterThiefState(masterThief.getMasterThiefState());
    }

    /**
     * Called by the Ordinary Thief to hand a canvas to the Master Thief if they have any
     * - Synchronization point between each Ordinary Thief and the Master Thief.
     * @param party the identification of the Assault Party the thief belongs to.
     */
    public synchronized void handACanvas(int party) {
        CollectionSiteProxyAgent thief = (CollectionSiteProxyAgent) Thread.currentThread();
        thief.setOrdinaryThiefState(OrdinaryThief.COLLECTION_SITE);
        generalRepository.setOrdinaryThiefState(thief.getOrdinaryThiefID(), thief.getOrdinaryThiefState(), thief.getOrdinaryThiefSituation(), thief.getOrdinaryThiefMaxDisplacement());
        this.arrivingThieves.get(party).add(thief);
        notifyAll();
        while (this.arrivingThieves.get(party).contains(thief)) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
    }

    /**
     * Shuts down the Collection Site server.
     */
    public synchronized void shutdown () {
        CollectionSiteMain.waitConnection = false;
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
        return museum.getRoomDistance(room);
    }

    /**
     * Gets the number of paintings in a room.
     * @param room the room identification.
     * @return the number of paintings in the room.
     */
    public int getRoomPaintings(int room) {
        return museum.getRoomPaintings(room);
    }

    /**
     * Setter for the empty rooms.
     * @param room the room identification.
     * @param empty true if it is empty, false otherwise.
     */
    private void setEmptyRoom(int room, boolean empty) {
        emptyRooms[room] = empty;
    }
}
