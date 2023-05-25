package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *  Stub to the Assault Party.
 * <p>
 *    It instantiates a remote reference to the Assault Party.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public interface AssaultPartyInterface extends Remote {
    /**
     * Getter for the identification of the Assault Party.
     * @return the identification of the Assault Party
     * @throws RemoteException if the execution of the remote code failed
     */
    int getID() throws RemoteException;

    /**
     * Called by the Master Thief to send the Assault Party to the museum.
     * After that call, Assault Party can start crawling.
     * @return the updated state of the Master Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int sendAssaultParty() throws RemoteException;

    /**
     * Called by the Ordinary Thief to crawl in.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @param ordinaryThiefMaxDisplacement the maximum displacement of the Ordinary Thief
     * @return a ReturnBoolean reference data type with the value false if they have finished the crawling and the
     * updated state of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    ReturnBoolean crawlIn(int ordinaryThief, int ordinaryThiefMaxDisplacement) throws RemoteException;

    /**
     * Called to awake the first member in the line of Assault Party, by the last party member that rolled a canvas,
     * so that the Assault Party can crawl out.
     * - Synchronization Point between members of the Assault Party.
     * @throws RemoteException if the execution of the remote code failed
     */
    void reverseDirection() throws RemoteException;

    /**
     * Called by the Ordinary Thief to crawl out.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @param ordinaryThiefMaxDisplacement the maximum displacement of the Ordinary Thief
     * @return a ReturnBoolean reference data type with the value false if they have finished the crawling and the
     * updated state of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    ReturnBoolean crawlOut(int ordinaryThief, int ordinaryThiefMaxDisplacement) throws RemoteException;

    /**
     * Sends the shutdown signal to the Assault Party.
     * @throws RemoteException if the execution of the remote code failed
     */
    void shutdown() throws RemoteException;
 
    /**
     * Getter for the room destination.
     * @return the identification of the room
     * @throws RemoteException if the execution of the remote code failed
     */
    int getRoom() throws RemoteException;

    /**
     * Setter for the busy hands attribute of a thief.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @param res true if they have a canvas, false otherwise
     * @throws RemoteException if the execution of the remote code failed
     */
    void setBusyHands(int ordinaryThief, boolean res) throws RemoteException;

    /**
     * Getter for the busy hands attribute of a thief.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return whether they have a canvas
     * @throws RemoteException if the execution of the remote code failed
     */
    boolean hasBusyHands(int ordinaryThief) throws RemoteException;

    /**
     * Removes a member from the Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    void removeMember(int ordinaryThief) throws RemoteException;

    /**
     * Returns whether the Assault Party has no members.
     * @return true if it is empty, false otherwise
     * @throws RemoteException if the execution of the remote code failed
     */
    boolean isEmpty() throws RemoteException;

    /**
     * Sets the Assault Party in operation.
     * @param inOperation true if the Assault Party is in operation, false otherwise
     * @throws RemoteException if the execution of the remote code failed
     */
    void setInOperation(boolean inOperation) throws RemoteException;

    /**
     * Sets the members of the Assault Party.
     * @param ordinaryThieves an array with the identifications of the Ordinary Thieves
     * @throws RemoteException if the execution of the remote code failed
     */
    void setMembers(int[] ordinaryThieves) throws RemoteException;

    /**
     * Getter for if the Assault Party is in operation.
     * @return true if it is in operation, false otherwise
     * @throws RemoteException if the execution of the remote code failed
     */
    boolean isInOperation() throws RemoteException;

    /**
     * Checks if a given thief is a member of the Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return true if affirmative, false otherwise
     * @throws RemoteException if the execution of the remote code failed
     */
    boolean isMember(int ordinaryThief) throws RemoteException;

    /**
     * Sets the room destination of the Assault Party.
     * @param room the identification of the room
     * @param roomDistance the distance to the room
     * @param roomPaintings the number of paintings in the room
     * @throws RemoteException if the execution of the remote code failed
     */
    void setRoom(int room, int roomDistance, int roomPaintings) throws RemoteException;
}
