package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Museum has rooms inside it. Those rooms have paintings that can be stolen by the Ordinary Thieves of the Assault Party.
 */
public interface MuseumInterface extends Remote {
    /**
     * Roll a canvas.
     * @param assaultParty the identification of the AssaultParty.
     * @param ordinaryThief the identification of the OrdinaryThief.
     */
    public ReturnVoid rollACanvas(int assaultParty, int ordinaryThief) throws RemoteException;

    /**
     * Sends the shutdown signal to the Museum.
     */
    public void shutdown() throws RemoteException;

    /**
     * Getter for the distance to a room.
     * @param room the room identification.
     * @return the distance to the room.
     */
    public int getRoomDistance(int room) throws RemoteException;

    /**
     * Getter for the number of paintings in a room.
     * @param room the room identification.
     * @return the number of paintings in the room.
     */
    public int getRoomPaintings(int room) throws RemoteException;
}
