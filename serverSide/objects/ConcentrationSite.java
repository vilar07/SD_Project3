package serverSide.objects;

import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.Deque;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import interfaces.*;
import serverSide.main.ConcentrationSiteMain;
import utils.Constants;

/**
 * Concentration Site where ordinary thieves wait for orders.
 */
public class ConcentrationSite implements ConcentrationSiteInterface {
    /**
     * FIFO with the thieves waiting for instructions.
     */
    private final Deque<Integer> thieves;

    /**
     * Boolean variable that is false until the Master Thief announces the end of the heist.
     */
    private boolean finished;

    /**
     * The General Repository where logging occurs.
     */
    private final GeneralRepositoryInterface generalRepositoryStub;

    /**
     * The Assault Party shared regions.
     */
    private final AssaultPartyInterface[] assaultPartyStubs;

    /**
     * The Collection Site shared region.
     */
    private final CollectionSiteInterface collectionSiteStub;

    /**
     * Public constructor for the Concentration Site shared region.
     * @param generalRepositoryStub the General Repository.
     * @param assaultPartyStubs the Assault Parties.
     * @param collectionSiteStub the Collection Site.
     */
    public ConcentrationSite(GeneralRepositoryInterface generalRepositoryStub,
                             AssaultPartyInterface[] assaultPartyStubs, CollectionSiteInterface collectionSiteStub) {
        this.generalRepositoryStub = generalRepositoryStub;
        this.assaultPartyStubs = assaultPartyStubs;
        this.collectionSiteStub = collectionSiteStub;
        thieves = new ArrayDeque<>(Constants.NUM_THIEVES - 1);
        finished = false;
    }

    /**
     * Called by the master thief, when enough ordinary thieves are available and there is still a
     * room with paintings
     * - Synchronization point between Master Thief and every Ordinary Thief constituting the Assault Party.
     * @param assaultParty the Assault Party identification.
     */
    public int prepareAssaultParty(int assaultParty) {
        setMasterThiefState(MasterThief.ASSEMBLING_A_GROUP);
        synchronized (this) {
            while (thieves.size() < Constants.ASSAULT_PARTY_SIZE) {
                try {
                    this.wait();
                } catch (InterruptedException ignored) {}
            } 
        }
        int room = getNextRoom();
        setAssaultPartyRoom(assaultParty, room, getRoomDistance(room), getRoomPaintings(room));
        int[] ordinaryThieves = new int[Constants.ASSAULT_PARTY_SIZE];
        for (int i = 0; i < ordinaryThieves.length; i++) {
            ordinaryThieves[i] = this.thieves.poll();
        }
        setAssaultPartyMembers(assaultParty, ordinaryThieves);
        synchronized (this) {
            notifyAll();
        }
        return MasterThief.ASSEMBLING_A_GROUP;
    }

    /**
     * The Master Thief announces the end of operations
     * and shares the number of paintings acquired in the heist.
     */
    public synchronized int sumUpResults() {
        finished = true;
        notifyAll();
        setMasterThiefState(MasterThief.PRESENTING_THE_REPORT);
        while (thieves.size() < Constants.NUM_THIEVES - 1) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        printRepositoryTail(getTotalPaintings());
        return MasterThief.PRESENTING_THE_REPORT;
    }

    /**
     * Called by an Ordinary Thief to wait for orders.
     * @return true if needed, false otherwise.
     */
    public synchronized ReturnBoolean amINeeded(int ordinaryThief) {
        setOrdinaryThiefState(ordinaryThief);
        thieves.add(ordinaryThief);
        this.notifyAll();
        if (finished) {
            return new ReturnBoolean(false, OrdinaryThief.CONCENTRATION_SITE);
        }
        while (getAssaultParty(ordinaryThief) == -1) {
            try {
                if (finished) {
                    return new ReturnBoolean(false, OrdinaryThief.CONCENTRATION_SITE);
                }
                this.wait();
            } catch (InterruptedException ignored) {}
        }
        return new ReturnBoolean(true, OrdinaryThief.CONCENTRATION_SITE);
    }

    /**
     * Ordinary Thief waits for the Master Thief to dispatch the designed Assault Party.
     * @return the Assault Party identification.
     */
    public int prepareExcursion(int ordinaryThief) {
        AssaultPartyInterface assaultParty = assaultPartyStubs[getAssaultParty(ordinaryThief)];
        int assaultPartyID = getAssaultPartyID(assaultParty);
        synchronized (assaultParty) {
            while (!isAssaultPartyInOperation(assaultPartyID)) {
                try {
                    // Wait for up to 1 second for the isInOperation flag to be set
                    assaultParty.wait(1000);
                } catch (InterruptedException ignored) {
                    // Restore the interrupted status of the thread
                    Thread.currentThread().interrupt();
                }
            }
        }
        return assaultPartyID;
    }

    /**
     * Shuts down the Concentration Site server.
     */
    public synchronized void shutdown() {
        ConcentrationSiteMain.shutdown();
    }

    /**
     * Returns the Assault Party the Ordinary Thief is a part of.
     * @param ordinaryThief the identification of the Ordinary Thief.
     * @return the identification of the Assault Party the Ordinary Thief belongs to or -1 if none.
     */
    private int getAssaultParty(int ordinaryThief) {
        for (AssaultPartyInterface assaultParty: assaultPartyStubs) {
            if (isMemberOfAssaultParty(assaultParty, ordinaryThief)) {
                return getAssaultPartyID(assaultParty);
            }
        }
        return -1;
    }

    private void setMasterThiefState(int state) {
        try {
            generalRepositoryStub.setMasterThiefState(state);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.setMasterThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setOrdinaryThiefState(int ordinaryThief) {
        try {
            generalRepositoryStub.setOrdinaryThiefState(ordinaryThief, OrdinaryThief.CONCENTRATION_SITE);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.setOrdinaryThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private int getNextRoom() {
        int ret = 0;
        try {
            ret = collectionSiteStub.getNextRoom();
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.getNextRoom: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private int getRoomDistance(int room) {
        int ret = 0;
        try {
            ret = collectionSiteStub.getRoomDistance(room);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.getRoomDistance: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private int getRoomPaintings(int room) {
        int ret = 0;
        try {
            ret = collectionSiteStub.getRoomPaintings(room);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.getRoomPaintings: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private void setAssaultPartyRoom(int assaultParty, int room, int distance, int paintings) {
        try {
            assaultPartyStubs[assaultParty].setRoom(room, distance, paintings);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.setAssaultPartyRoom: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setAssaultPartyMembers(int assaultParty, int[] ordinaryThieves) {
        try {
            assaultPartyStubs[assaultParty].setMembers(ordinaryThieves);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.setAssaultPartyMembers: " + e.getMessage());
            System.exit(1);
        }
    }

    private int getTotalPaintings() {
        int ret = 0;
        try {
            ret = collectionSiteStub.getPaintings();
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.getTotalPaintings: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private void printRepositoryTail(int paintings) {
        try {
            generalRepositoryStub.printTail(paintings);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.printRepositoryTail: " + e.getMessage());
            System.exit(1);
        }
    }

    private int getAssaultPartyID(AssaultPartyInterface assaultParty) {
        int ret = 0;
        try {
            ret = assaultParty.getID();
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.getAssaultPartyID: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private boolean isAssaultPartyInOperation(int assaultParty) {
        boolean ret = false;
        try {
            ret = assaultPartyStubs[assaultParty].isInOperation();
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.isAssaultPartyInOperation: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private boolean isMemberOfAssaultParty(AssaultPartyInterface assaultParty, int ordinaryThief) {
        boolean ret = false;
        try {
            ret = assaultParty.isMember(ordinaryThief);
        } catch (RemoteException e) {
            System.out.println("Remote exception on ConcentrationSite.isMemberOfAssaultParty: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }
}
