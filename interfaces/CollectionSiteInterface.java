package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Collection Site where Master Thief intelligence and paintings are stored.
 */
public interface CollectionSiteInterface extends Remote {
    /**
     * Called by Master Thief to initiate operations.
     */
    public ReturnVoid startOperations() throws RemoteException;

    /**
     * Called by Master Thief to appraise situation: either to take a rest, prepare assault party or
     * sum up results.
     * @return next situation.
     */
    public char appraiseSit();

    /**
     * Master Thief waits while there are still Assault Parties in operation.
     */
    public ReturnVoid takeARest() throws RemoteException;

    /**
     * Called by Master Thief to collect all available canvas
     * - Synchronization point between Master Thief and each individual Ordinary Thief with a canvas.
     */
    public ReturnVoid collectACanvas() throws RemoteException;

    /**
     * Called by the Ordinary Thief to hand a canvas to the Master Thief if they have any
     * - Synchronization point between each Ordinary Thief and the Master Thief.
     * @param assaultParty the identification of the Assault Party the thief belongs to.
     */
    public ReturnVoid handACanvas(int assaultParty, int ordinaryThief) throws RemoteException;

    /**
     * Get the number of the next Assault Party and remove it from the queue.
     * @return the Assault Party identification.
     */
    public int getNextAssaultPartyID() throws RemoteException;

    /**
     * Sends the shutdown signal to the Collection Site.
     */
    public void shutdown() throws RemoteException;

    /**
     * Gets the next room which is not empty.
     * @return the identification of the next room.
     */
    public int getNextRoom() throws RemoteException;

    /**
     * Gets the number of paintings for a given room.
     * @param room the room identification.
     * @return the number of paintings.
     */
    public int getRoomPaintings(int room) throws RemoteException;

    /**
     * Gets the distance to a room.
     * @param room the room identification.
     * @return the distance to the room.
     */
    public int getRoomDistance(int room) throws RemoteException;

    /**
     * Getter for the total number of paintings acquired.
     * @return the total number of paintings.
     */
    public int getPaintings() throws RemoteException;
}
