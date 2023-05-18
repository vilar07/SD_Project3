package clientSide.entities;

import interfaces.AssaultPartyInterface;
import interfaces.CollectionSiteInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.ReturnVoid;

import java.rmi.RemoteException;

/**
 * Master Thief, the thief that commands the heist.
 */
public class MasterThief extends Thread {
    /**
     * Initial state of the Master Thief.
     */
    public static final int PLANNING_THE_HEIST = 1000;

    /**
     * State when the Master Thief is deciding what to do and moves to one of the next 3 states.
     */
    public static final int DECIDING_WHAT_TO_DO = 2000;

    /**
     * State when the Master Thief is assembling an Assault Party.
     */
    public static final int ASSEMBLING_A_GROUP = 3000;

    /**
     * State when the Master Thief is waiting for an Assault Party to return.
     */
    public static final int WAITING_FOR_ARRIVAL = 4000;

    /**
     * Final state when the Master Thief presents the report to all the thieves.
     */
    public static final int PRESENTING_THE_REPORT = 5000;

    /**
     * Current state of the Master Thief.
     */
    private int state;

    /**
     * Variable holding the Collection Site shared region.
     */
    private final CollectionSiteInterface collectionSiteStub;

    /**
     * Variable holding the Concentration Site shared region.
     */
    private final ConcentrationSiteInterface concentrationSiteStub;

    /**
     * Array holding the Assault Parties shared regions.
     */
    private final AssaultPartyInterface[] assaultPartyStubs;

    /**
     * Public constructor for Master Thief.
     * Initializes the state as PLANNING_THE_HEIST and her perception that all rooms of the museum
     * are empty.
     * @param collectionSiteStub the Collection Site.
     * @param concentrationSiteStub the Concentration Site.
     * @param assaultPartyStubs the Assault Parties.
     */
    public MasterThief(CollectionSiteInterface collectionSiteStub,
                       ConcentrationSiteInterface concentrationSiteStub, AssaultPartyInterface[] assaultPartyStubs) {
        this.collectionSiteStub = collectionSiteStub;
        this.concentrationSiteStub = concentrationSiteStub;
        this.assaultPartyStubs = assaultPartyStubs;
        setState(PLANNING_THE_HEIST);
    }

    /**
     * Sets the state of the Master Thief.
     * @param state the updated Master Thief state.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Getter for the Master Thief state.
     * @return the state of the Master Thief.
     */
    public int getMasterThiefState() {
        return state;
    }

    /**
     * Lifecycle of the Master Thief.
     */
    @Override
    public void run() {
        startOperations();
        char operation;
        while ((operation = appraiseSit()) != 'E') {
            switch (operation) {
                case 'P':
                    int assaultPartyID = getNextAssaultPartyID();
                    prepareAssaultParty(assaultPartyID);
                    sendAssaultParty(assaultPartyID);
                    break;
                case 'R':
                    takeARest();
                    collectACanvas();
                    break;
            }
        }
        sumUpResults();
    }

    private void startOperations() {
        System.out.println("startOperations");
        ReturnVoid ret = null;                                 // return value
        try {
            ret = collectionSiteStub.startOperations();
        } catch (RemoteException e) {
            System.out.println("Remote exception on startOperations: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
    }

    private char appraiseSit() {
        System.out.println("appraiseSit");
        char ret = 0;                                 // return value
        try {
            ret = collectionSiteStub.appraiseSit();
        } catch (RemoteException e) {
            System.out.println("Remote exception on appraiseSit: " + e.getMessage());
            System.exit (1);
        }
        return ret;
    }

    private int getNextAssaultPartyID() {
        int ret = 0;                                 // return value
        try {
            ret = collectionSiteStub.getNextAssaultPartyID();
        } catch (RemoteException e) {
            System.out.println("Remote exception on getNextAssaultPartyID: " + e.getMessage());
            System.exit (1);
        }
        return ret;
    }

    private void prepareAssaultParty(int assaultParty) {
        System.out.println("initiating prepareAssaultParty " + assaultParty);
        ReturnVoid ret = null;                                 // return value
        try {
            ret = concentrationSiteStub.prepareAssaultParty(assaultParty);
        } catch (RemoteException e) {
            System.out.println("Remote exception on prepareAssaultParty: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        System.out.println("finished prepareAssaultParty " + assaultParty);
    }

    private void sendAssaultParty(int assaultParty) {
        System.out.println("initiating sendAssaultParty " + assaultParty);
        ReturnVoid ret = null;                                 // return value
        try {
            ret = assaultPartyStubs[assaultParty].sendAssaultParty();
        } catch (RemoteException e) {
            System.out.println("Remote exception on sendAssaultParty: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        System.out.println("finished sendAssaultParty " + assaultParty);
    }

    private void takeARest() {
        System.out.println("initiating takeARest");
        ReturnVoid ret = null;                                 // return value
        try {
            ret = collectionSiteStub.takeARest();
        } catch (RemoteException e) {
            System.out.println("Remote exception on takeARest: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        System.out.println("finished takeARest");
    }

    private void collectACanvas() {
        System.out.println("initiating collectACanvas");
        ReturnVoid ret = null;                                 // return value
        try {
            ret = collectionSiteStub.collectACanvas();
        } catch (RemoteException e) {
            System.out.println("Remote exception on collectACanvas: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        System.out.println("finished collectACanvas");
    }

    private void sumUpResults() {
        System.out.println("initiating sumUpResults");
        ReturnVoid ret = null;                                 // return value
        try {
            ret = concentrationSiteStub.sumUpResults();
        } catch (RemoteException e) {
            System.out.println("Remote exception on sumUpResults: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
    }
}
