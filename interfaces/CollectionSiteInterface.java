package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Collection Site where Master Thief intelligence and paintings are stored.
 */
public interface CollectionSiteInterface extends Remote {
    /**
     * Called by Master Thief to initiate operations.
     * @return the updated state of the Master Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int startOperations() throws RemoteException;

    /**
     * Called by Master Thief to appraise situation: either to take a rest, prepare assault party or
     * sum up results.
     * @return the next situation ('P' for preparing a party, 'R' for taking a rest or 'E' to end the heist and sum up
     * results)
     * @throws RemoteException if the execution of the remote code failed
     */
    char appraiseSit() throws RemoteException;

    /**
     * Master Thief waits while there are still Assault Parties in operation.
     * @return the updated state of the Master Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int takeARest() throws RemoteException;

    /**
     * Called by Master Thief to collect all available canvas
     * - Synchronization point between Master Thief and each individual Ordinary Thief with a canvas.
     * @return the updated state of the Master Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int collectACanvas() throws RemoteException;

    /**
     * Called by the Ordinary Thief to hand a canvas to the Master Thief if they have any
     * - Synchronization point between each Ordinary Thief and the Master Thief.
     * @param assaultParty the identification of the Assault Party the thief belongs to
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return the updated state of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int handACanvas(int assaultParty, int ordinaryThief) throws RemoteException;

    /**
     * Get the number of the next Assault Party and remove it from the queue.
     * @return the identification of the Assault Party
     * @throws RemoteException if the execution of the remote code failed
     */
    int getNextAssaultPartyID() throws RemoteException;

    /**
     * Sends the shutdown signal to the Collection Site.
     * @throws RemoteException if the execution of the remote code failed
     */
    void shutdown() throws RemoteException;

    /**
     * Gets the next room which is not empty.
     * @return the identification of the next room
     * @throws RemoteException if the execution of the remote code failed
     */
    int getNextRoom() throws RemoteException;

    /**
     * Gets the number of paintings for a given room.
     * @param room the room identification
     * @return the number of paintings
     * @throws RemoteException if the execution of the remote code failed
     */
    int getRoomPaintings(int room) throws RemoteException;

    /**
     * Gets the distance to a room.
     * @param room the room identification
     * @return the distance to the room
     * @throws RemoteException if the execution of the remote code failed
     */
    int getRoomDistance(int room) throws RemoteException;

    /**
     * Getter for the total number of paintings acquired.
     * @return the total number of paintings
     * @throws RemoteException if the execution of the remote code failed
     */
    int getPaintings() throws RemoteException;
}
