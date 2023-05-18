package serverSide.interfaces;

/**
 * Interface which provides the states and methods to be overridden by the proxy agents of the Master Thief.
 */
public interface MasterThiefClone {
    int PLANNING_THE_HEIST = 1000;
    int DECIDING_WHAT_TO_DO = 2000;
    int ASSEMBLING_A_GROUP = 3000;
    int WAITING_FOR_ARRIVAL = 4000;
    int PRESENTING_THE_REPORT = 5000;
    
    /**
     * Sets the state of the Master Thief.
     * @param state the updated Master Thief state.
     */
    public void setMasterThiefState(int state);
}
