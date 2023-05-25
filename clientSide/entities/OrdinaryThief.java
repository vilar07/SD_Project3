package clientSide.entities;

import interfaces.*;

import java.rmi.RemoteException;

/**
 * Ordinary Thief, one of the thieves involved in the heist.
 */
public class OrdinaryThief extends Thread {
    /**
     * Initial state of the Ordinary Thief, when they are at the Concentration Site.
     */
    public static final int CONCENTRATION_SITE = 1000;

    /**
     * State of the Ordinary Thief, when they are at the Collection Site.
     */
    public static final int COLLECTION_SITE = 2000;

    /**
     * State of the Ordinary Thief, when they are crawling into the Museum room.
     */
    public static final int CRAWLING_INWARDS = 3000;

    /**
     * State of the Ordinary Thief, when they are inside a room of the Museum.
     */
    public static final int AT_A_ROOM = 4000;

    /**
     * State of the Ordinary Thief, when they are crawling out of the Museum room.
     */
    public static final int CRAWLING_OUTWARDS = 5000;

    /**
     * Current state of the Ordinary Thief.
     */
    private int state;

    /**
     * Thief unique id.
     */
    private final int id;

    /**
     * Maximum displacement of the Ordinary Thief.
     */
    private final int maxDisplacement;

    /**
     * Array holding the Assault Parties shared regions.
     */
    private final AssaultPartyInterface[] assaultPartyStubs;

    /**
     * Variable holding the Concentration Site shared region.
     */
    private final ConcentrationSiteInterface concentrationSiteStub;

    /**
     * Variable holding the Collection Site shared region.
     */
    private final MuseumInterface museumStub;

    /**
     * Variable holding the Collection Site shared region.
     */
    private final CollectionSiteInterface collectionSiteStub;

    /**
     * Ordinary Thief constructor.
     * @param id the identification of the thief.
     * @param museumStub the Museum.
     * @param collectionSiteStub the Collection Site.
     * @param concentrationSiteStub the Concentration Site.
     * @param assaultPartyStubs the Assault Parties array.
     * @param maxDisplacement the maximum displacement.
     */
    public OrdinaryThief(int id, MuseumInterface museumStub, CollectionSiteInterface collectionSiteStub,
                         ConcentrationSiteInterface concentrationSiteStub, AssaultPartyInterface[] assaultPartyStubs,
                         int maxDisplacement) {
        this.id = id;
        this.museumStub = museumStub;
        this.collectionSiteStub = collectionSiteStub;
        this.concentrationSiteStub = concentrationSiteStub;
        this.assaultPartyStubs = assaultPartyStubs;
        this.maxDisplacement = maxDisplacement;
        setState(CONCENTRATION_SITE);
    }

    /**
     * Getter for the identification number of the Ordinary Thief.
     * @return the identification number of the Ordinary Thief.
     */
    public int getID() {
        return id;
    }

    /**
     * Getter for the maximum displacement of the Ordinary Thief.
     * @return the maximum displacement of the Ordinary Thief.
     */
    public int getMaxDisplacement() {
        return maxDisplacement;
    }

    /**
     * Getter for the state of the Ordinary Thief.
     * @return the state of the Ordinary Thief.
     */
    public int getOrdinaryThiefState() {
        return state;
    }

    /**
     * Setter for the state of the thief.
     * @param state the state.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Lifecycle of the Ordinary Thief.
     */
    @Override
    public void run() {
        while(amINeeded()) {
            int assaultPartyID = prepareExcursion();
            while(crawlIn(assaultPartyID));
            rollACanvas(assaultPartyID);
            reverseDirection(assaultPartyID);
            while(crawlOut(assaultPartyID));
            handACanvas(assaultPartyID);
        }
        System.out.println("Ordinary Thief " + id + " terminated");
    }

    private boolean amINeeded() {
        // System.out.println("initiating amINeeded");
        ReturnBoolean ret = null;                                 // return value
        try {
            ret = concentrationSiteStub.amINeeded(id);
        } catch (RemoteException e) {
            System.out.println("Remote exception on amINeeded: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        return ret.getValue();
    }

    private int prepareExcursion() {
        // System.out.println("initiating prepareExcursion");
        int ret = 0;                                 // return value
        try {
            ret = concentrationSiteStub.prepareExcursion(id);
        } catch (RemoteException e) {
            System.out.println("Remote exception on prepareExcursion: " + e.getMessage());
            System.exit (1);
        }
        // System.out.println("finished prepareExcursion");
        return ret;
    }

    private boolean crawlIn(int assaultParty) {
        // System.out.println("initiating crawlIn in party " + assaultParty);
        ReturnBoolean ret = null;                                 // return value
        try {
            ret = assaultPartyStubs[assaultParty].crawlIn(id, maxDisplacement);
        } catch (RemoteException e) {
            System.out.println("Remote exception on crawlIn: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        // System.out.println("finished crawlIn in party " + assaultParty);
        return ret.getValue();
    }

    private void rollACanvas(int assaultParty) {
        // System.out.println("initiating rollACanvas in party " + assaultParty);
        int ret = 0;                                 // return value
        try {
            ret = museumStub.rollACanvas(assaultParty, id);
        } catch (RemoteException e) {
            System.out.println("Remote exception on rollACanvas: " + e.getMessage());
            System.exit (1);
        }
        state = ret;
        // System.out.println("finished rollACanvas in party " + assaultParty);
    }

    private void reverseDirection(int assaultParty) {
        // System.out.println("initiating reverseDirection in party " + assaultParty);
        try {
            assaultPartyStubs[assaultParty].reverseDirection();
        } catch (RemoteException e) {
            System.out.println("Remote exception on reverseDirection: " + e.getMessage());
            System.exit (1);
        }
        // System.out.println("finished reverseDirection in party " + assaultParty);
    }

    private boolean crawlOut(int assaultParty) {
        // System.out.println("initiating crawlOut in party " + assaultParty);
        ReturnBoolean ret = null;                                 // return value
        try {
            ret = assaultPartyStubs[assaultParty].crawlOut(id, maxDisplacement);
        } catch (RemoteException e) {
            System.out.println("Remote exception on crawlOut: " + e.getMessage());
            System.exit (1);
        }
        state = ret.getState();
        // System.out.println("finished crawlOut in party " + assaultParty);
        return ret.getValue();
    }

    private void handACanvas(int assaultParty) {
        // System.out.println("initiating handACanvas in party " + assaultParty);
        int ret = 0;                                 // return value
        try {
            ret = collectionSiteStub.handACanvas(assaultParty, id);
        } catch (RemoteException e) {
            System.out.println("Remote exception on handACanvas: " + e.getMessage());
            System.exit (1);
        }
        state = ret;
        // System.out.println("finished handACanvas in party " + assaultParty);
    }
}
