package serverSide.main;

import interfaces.AssaultPartyInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import interfaces.Register;
import serverSide.objects.*;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *    General Repository server of the Heist To The Museum.
 * <p>
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class MuseumMain {
    /**
     *  Flag signaling the end of operations.
     */
    private static boolean end = false;

    /**
     *  Main method.
     * <p>
     *        args[0] - port number for listening to service requests
     *        args[1] - name of the platform where is located the RMI registering service
     *        args[2] - port number where the registering service is listening to service requests
     */
    public static void main (String [] args) {
        int portNumb = -1;                                             // port number for listening to service requests
        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests

        if (args.length != 3) {
            System.out.println("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            System.out.println("args[0] is not a valid port number!");
            System.exit(1);
        }
        rmiRegHostName = args[1];
        try {
            rmiRegPortNumb = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("args[2] is not a number!");
            System.exit(1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
            System.out.println("args[2] is not a valid port number!");
            System.exit(1);
        }

        /* create and install the security manager */

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        System.out.println("Security manager was installed!");

        /* get a remote reference to the general repository and assault party objects */
        String nameEntryGeneralRepository = "GeneralRepository";
        GeneralRepositoryInterface generalRepositoryStub = null;
        String nameEntryAssaultParty0 = "AssaultParty0";
        String nameEntryAssaultParty1 = "AssaultParty1";
        AssaultPartyInterface[] assaultPartyStubs = {null, null};

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("RMI registry was created!");

        try {
            generalRepositoryStub = (GeneralRepositoryInterface) registry.lookup(nameEntryGeneralRepository);
        } catch (RemoteException e) {
            System.out.println("General Repository lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("General Repository not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            assaultPartyStubs[0] = (AssaultPartyInterface) registry.lookup(nameEntryAssaultParty0);
        } catch (RemoteException e) {
            System.out.println("Assault Party 0 lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Assault Party 0 not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            assaultPartyStubs[1] = (AssaultPartyInterface) registry.lookup(nameEntryAssaultParty1);
        } catch (RemoteException e) {
            System.out.println("Assault Party 1 lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Assault Party 1 not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* instantiate a museum object */

        Museum museum = new Museum(generalRepositoryStub, assaultPartyStubs);
        MuseumInterface museumStub = null;
        try {
            museumStub = (MuseumInterface) UnicastRemoteObject.exportObject(museum, portNumb);
        } catch (RemoteException e) {
            System.out.println("Museum stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Stub was generated!");

        /* register it with the general registry service */

        String nameEntryBase = "RegisterHandler";                  // public name of the object that enables the registration
        // of other remote objects
        String nameEntryObject = "Museum";                         // public name of the museum object
        Register reg = null;                                       // remote reference to the object that enables the registration
        // of other remote objects
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, museumStub);
        } catch (RemoteException e) {
            System.out.println("Museum registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Museum already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Museum object was registered!");

        /* wait for the end of operations */

        System.out.println("Museum is in operation!");
        try {
            while (!end)
                synchronized (Class.forName("serverSide.main.MuseumMain")) {
                    try {
                        (Class.forName("serverSide.main.MuseumMain")).wait();
                    } catch (InterruptedException e) {
                        System.out.println("Museum main thread was interrupted!");
                    }
                }
        } catch (ClassNotFoundException e) {
            System.out.println("The data type MuseumMain was not found (blocking)!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */

        boolean shutdownDone = false; // flag signalling the shutdown of the museum service

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Museum deregistration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Museum not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println ("Museum was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(museum, true);
        } catch (NoSuchObjectException e) {
            System.out.println("Museum unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone)
            System.out.println("Museum was shutdown!");
    }

    public static void shutdown() {
        end = true;
        try {
            synchronized (Class.forName("serverSide.main.MuseumMain")) {
                (Class.forName("serverSide.main.MuseumMain")).notify();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The data type MuseumMain was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
