package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The General Repository, where all information is stored and logged.
 */
public interface GeneralRepositoryInterface extends Remote {
    /**
     * Prints the state of the simulation to the logging file.
     * @throws RemoteException if the execution of the remote code failed
     */
    void printState() throws RemoteException;

    /**
     * Prints the tail of the logging file.
     * @param total number of paintings acquired
     * @throws RemoteException if the execution of the remote code failed
     */
    void printTail(int total) throws RemoteException;

    /**
     * Sets the Master Thief state.
     * @param state the state code to change to
     * @throws RemoteException if the execution of the remote code failed
     */
    void setMasterThiefState(int state) throws RemoteException;

    /**
     * Sets the Ordinary Thief state.
     * @param id the identification of the thief
     * @param state the state code to change to
     * @param maxDisplacement the maximum displacement of the thief
     * @throws RemoteException if the execution of the remote code failed
     */
    void setOrdinaryThiefState(int id, int state, int maxDisplacement) throws RemoteException;

    /**
     * Sets the Ordinary Thief state (version 2).
     * @param id the identification of the thief
     * @param state the state code to change to
     * @throws RemoteException if the execution of the remote code failed
     */
    void setOrdinaryThiefState(int id, int state) throws RemoteException;

    /**
     * Sets the Assault Party room target.
     * @param party the party number
     * @param room the room identification
     * @throws RemoteException if the execution of the remote code failed
     */
    void setAssaultPartyRoom(int party, int room) throws RemoteException;

    /**
     * Sets an Assault Party member.
     * @param party the party number
     * @param thief the identification of the thief
     * @param pos the present position of the thief
     * @param cv 1 if the thief is carrying a canvas, 0 otherwise
     * @throws RemoteException if the execution of the remote code failed
     */
    void setAssaultPartyMember(int party, int thief, int pos, int cv) throws RemoteException;

    /**
     * Removes an Assault Party member.
     * @param party the identification of the Assault Party
     * @param thief the identification of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    void removeAssaultPartyMember(int party, int thief) throws RemoteException;

    /**
     * Resets the Assault Party logging details.
     * @param party the party number
     * @throws RemoteException if the execution of the remote code failed
     */
    void disbandAssaultParty(int party) throws RemoteException;

    /**
     * Sets the room state.
     * @param id the room identification
     * @param paintings the number of paintings
     * @param distance the distance to the outside gathering site
     * @throws RemoteException if the execution of the remote code failed
     */
    void setRoomState(int id, int paintings, int distance) throws RemoteException;

    /**
     * Sets the room state.
     * @param id the room identification
     * @param paintings the number of paintings
     * @throws RemoteException if the execution of the remote code failed
     */
    void setRoomState(int id, int paintings) throws RemoteException;

    /**
     * Sets the initial room states.
     * @param paintings an array with the number of paintings of each room
     * @param distances an array with the distance to each room
     * @throws RemoteException if the execution of the remote code failed
     */
    void setInitialRoomStates(int[] paintings, int[] distances) throws RemoteException;

    /**
     * Sets the initial attributes of the thieves.
     * @param maxDisplacements an array with the maximum displacements of the Ordinary Thieves, where the index of each
     *                         value is the identification of the thief
     * @throws RemoteException if the execution of the remote code failed
     */
    void setAttributesOfOrdinaryThieves(int[] maxDisplacements) throws RemoteException;

    /**
     * Sends the shutdown signal to the General Repository.
     * @throws RemoteException if the execution of the remote code failed
     */
    void shutdown() throws RemoteException;
}
