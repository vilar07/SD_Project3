package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Museum has rooms inside it. Those rooms have paintings that can be stolen by the Ordinary Thieves of the Assault Party.
 */
public interface MuseumInterface extends Remote {
    /**
     * Roll a canvas.
     * @param assaultParty the identification of the Assault Party
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return the updated state of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int rollACanvas(int assaultParty, int ordinaryThief) throws RemoteException;

    /**
     * Sets the rooms of the museum.
     * @param paintings an array with the paintings of each room, where the index is its identification
     * @param distances an array with the distances to each room, where the index is the identification of the room
     * @throws RemoteException if the execution of the remote code failed
     */
    void setRooms(int[] paintings, int[] distances) throws RemoteException;

    /**
     * Sends the shutdown signal to the Museum.
     * @throws RemoteException if the execution of the remote code failed
     */
    void shutdown() throws RemoteException;

    /**
     * Getter for the distance to a room.
     * @param room the room identification
     * @return the distance to the room
     * @throws RemoteException if the execution of the remote code failed
     */
    int getRoomDistance(int room) throws RemoteException;

    /**
     * Getter for the number of paintings in a room.
     * @param room the room identification
     * @return the number of paintings in the room
     * @throws RemoteException if the execution of the remote code failed
     */
    int getRoomPaintings(int room) throws RemoteException;
}
