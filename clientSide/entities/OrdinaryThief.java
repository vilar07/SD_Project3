package clientSide.entities;

import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.CollectionSiteStub;
import clientSide.stubs.ConcentrationSiteStub;
import clientSide.stubs.MuseumStub;

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
    private final AssaultPartyStub[] assaultParties;

    /**
     * Variable holding the Concentration Site shared region.
     */
    private final ConcentrationSiteStub concentrationSite;

    /**
     * Variable holding the Collection Site shared region.
     */
    private final MuseumStub museum;

    /**
     * Variable holding the Collection Site shared region.
     */
    private final CollectionSiteStub collectionSite;

    /**
     * Ordinary Thief constructor.
     * @param id the identification of the thief.
     * @param museum the Museum.
     * @param collectionSite the Collection Site.
     * @param concentrationSite the Concentration Site.
     * @param assaultParties the Assault Parties array.
     * @param maxDisplacement the maximum displacement.
     */
    public OrdinaryThief(int id, MuseumStub museum, CollectionSiteStub collectionSite, ConcentrationSiteStub concentrationSite, 
                                AssaultPartyStub[] assaultParties, int maxDisplacement) {
        this.id = id;
        this.museum = museum;
        this.collectionSite = collectionSite;
        this.concentrationSite = concentrationSite;
        this.assaultParties = assaultParties;
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
        while((concentrationSite.amINeeded())){
            System.out.println(id + " - initiating prepareExcursion");
            int assaultPartyID = concentrationSite.prepareExcursion();
            System.out.println(id + " - finished prepareExcursion in party " + assaultPartyID + "; initiating crawlIn in party " + assaultPartyID);
            while(assaultParties[assaultPartyID].crawlIn());
            System.out.println(id + " - finished crawlIn in party " + assaultPartyID + "; initiating rollACanvas in party " + assaultPartyID);
            museum.rollACanvas(assaultPartyID);
            System.out.println(id + " - finished rollACanvas in party " + assaultPartyID + "; initiating reverseDirection in party " + assaultPartyID);
            assaultParties[assaultPartyID].reverseDirection();
            System.out.println(id + " - finished reverseDirection in party " + assaultPartyID + "; initiating crawlOut in party " + assaultPartyID);
            while(assaultParties[assaultPartyID].crawlOut());
            System.out.println(id + " - finished crawlOut in party " + assaultPartyID + "; initiating handACanvas in party " + assaultPartyID);
            collectionSite.handACanvas(assaultPartyID);
            System.out.println(id + " - finished handACanvas in party " + assaultPartyID);
        }
        System.out.println(id + " terminated");
    }    
}
