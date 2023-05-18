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
     * @return the identification of the Assault Party.
     */
    public int getID() throws RemoteException;

    /**
     * Called by the Master Thief to send the Assault Party to the museum.
     * After that call, Assault Party can start crawling.
     */
    public void sendAssaultParty() throws RemoteException;

    /**
     * Called by the Ordinary Thief to crawl in.
     * @return false if they have finished the crawling.
     */
    public boolean crawlIn(int ordinaryThief, int ordinaryThiefMaxDisplacement) throws RemoteException;

    /**
     * Called to awake the first member in the line of Assault Party, by the last party member that rolled a canvas,
     * so that the Assault Party can crawl out.
     * - Synchronization Point between members of the Assault Party.
     */
    public void reverseDirection() throws RemoteException;

    /**
     * Called by the Ordinary Thief to crawl out.
     * @return false if they have finished the crawling.
     */
    public boolean crawlOut(int ordinaryThief, int ordinaryThiefMaxDisplacement) throws RemoteException;

    /**
     * Sends the shutdown signal to the Assault Party.
     */
    public void shutdown() throws RemoteException;
 
    /**
     * Getter for the room destination.
     * @return the room identification.
     */
    public int getRoom() throws RemoteException;

    /**
     * Setter for the busy hands attribute of a thief.
     * @param ordinaryThief the identification of the Ordinary Thief.
     * @param res true if they have a canvas, false otherwise.
     */
    public void setBusyHands(int ordinaryThief, boolean res) throws RemoteException;

    /**
     * Getter for the busy hands attribute of a thief.
     * @param ordinaryThief the identification of the Ordinary Thief.
     * @return whether or not they have a canvas.
     */
    public boolean hasBusyHands(int ordinaryThief) throws RemoteException;

    /**
     * Removes a member from the Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief.
     */
    public void removeMember(int ordinaryThief) throws RemoteException;

    /**
     * Returns whether the Assault Party has no members.
     * @return true if it is empty, false otherwise.
     */
    public boolean isEmpty() throws RemoteException;

    /**
     * Sets the Assault Party in operation.
     * @param inOperation true if the Assault Party is in operation, false otherwise.
     */
    public void setInOperation(boolean inOperation) throws RemoteException;

    /**
     * Sets the members of the Assault Party.
     * @param ordinaryThieves an array with the identification of the Ordinary Thieves.
     */
    public void setMembers(int[] ordinaryThieves) throws RemoteException;

    /**
     * Getter for if the Assault Party is in operation.
     * @return true if it is in operation, false otherwise.
     */
    public boolean isInOperation() throws RemoteException;

    /**
     * Checks if a given thief is a member of the Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief.
     * @return true if affirmative, false otherwise.
     */
    public boolean isMember(int ordinaryThief) throws RemoteException;

    /**
     * Sets the room destination of the Assault Party.
     * @param room the room identification.
     * @param roomDistance the distance to the room.
     * @param roomPaintings the number of paintings in the room.
     */
    public void setRoom(int room, int roomDistance, int roomPaintings) throws RemoteException;
}
