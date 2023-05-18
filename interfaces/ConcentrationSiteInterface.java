package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Concentration Site where ordinary thieves wait for orders.
 */
public interface ConcentrationSiteInterface extends Remote {
    /**
     * Called by the master thief, when enough ordinary thieves are available and there is still a
     * room with paintings
     * - Synchronization point between Master Thief and every Ordinary Thief constituting the Assault Party.
     * @param assaultParty the Assault Party identification.
     */
    public ReturnVoid prepareAssaultParty(int assaultParty) throws RemoteException;

    /**
     * The Master Thief announces the end of operations
     * and shares the number of paintings acquired in the heist.
     */
    public ReturnVoid sumUpResults() throws RemoteException;

    /**
     * Called by an ordinary thief to wait for orders.
     * @param ordinaryThief the identification of the OrdinaryThief.
     * @return true if needed, false otherwise.
     */
    public boolean amINeeded(int ordinaryThief) throws RemoteException;

    /**
     * Ordinary Thief waits for the Master Thief to dispatch the designed Assault Party.
     * @param ordinaryThief the identification of the OrdinaryThief.
     * @return the Assault Party identification.
     */
    public int prepareExcursion(int ordinaryThief) throws RemoteException;

    /**
     * Sends the signal to the Concentration Site.
     */
    public void shutdown() throws RemoteException;
}
