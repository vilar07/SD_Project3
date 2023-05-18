package serverSide.interfaces;

/**
 * Interface which provides the states and methods to be overridden by the proxy agents of the Ordinary Thief.
 */
public interface OrdinaryThiefClone {
    int CONCENTRATION_SITE = 1000;
    int COLLECTION_SITE = 2000;
    int CRAWLING_INWARDS = 3000;
    int AT_A_ROOM = 4000;
    int CRAWLING_OUTWARDS = 5000;
    
    /**
     * Getter for the identification number of the Ordinary Thief.
     * @return the identification number of the Ordinary Thief.
     */
    public int getOrdinaryThiefID();

    /**
     * Getter for the maximum displacement of the Ordinary Thief.
     * @return the maximum displacement of the Ordinary Thief.
     */
    public int getOrdinaryThiefMaxDisplacement();

    /**
     * Setter for the state of the Ordinary Thief.
     * @param state the state of the Ordinary Thief.
     */
    public void setOrdinaryThiefState(int state);
}
