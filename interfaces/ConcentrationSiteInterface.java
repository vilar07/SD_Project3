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
     * @param assaultParty the identification of the Assault Party
     * @return the updated state of the Master Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int prepareAssaultParty(int assaultParty) throws RemoteException;

    /**
     * The Master Thief announces the end of operations
     * and shares the number of paintings acquired in the heist.
     * @return the updated state of the Master Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    int sumUpResults() throws RemoteException;

    /**
     * Called by an ordinary thief to wait for orders.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return a ReturnBoolean reference data type with the value true if the thief is needed or false otherwise, and
     * the updated state of the Ordinary Thief
     * @throws RemoteException if the execution of the remote code failed
     */
    ReturnBoolean amINeeded(int ordinaryThief) throws RemoteException;

    /**
     * Ordinary Thief waits for the Master Thief to dispatch the designed Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return the identification of the Assault Party
     * @throws RemoteException if the execution of the remote code failed
     */
    int prepareExcursion(int ordinaryThief) throws RemoteException;

    /**
     * Sends the signal to the Concentration Site.
     * @throws RemoteException if the execution of the remote code failed
     */
    void shutdown() throws RemoteException;
}
