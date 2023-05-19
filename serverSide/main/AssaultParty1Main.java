package serverSide.main;

import interfaces.AssaultPartyInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.Register;
import serverSide.objects.AssaultParty;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *    Assault Party server of the Heist To The Museum.
 * <p>
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultParty1Main
{
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

       /* get a remote reference to the general repository object */

       String nameEntryGeneralRepository = "GeneralRepository";       // public name of the general repository object
       GeneralRepositoryInterface generalRepositoryStub = null;       // remote reference to the general repository object
       Registry registry = null;                                      // remote reference for registration in the RMI registry service

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

       /* instantiate an assault party object */

       AssaultParty assaultParty = new AssaultParty(1, generalRepositoryStub); // assault party 1 object
       AssaultPartyInterface assaultPartyStub = null; // remote reference to the assault party 1 object

       try {
           assaultPartyStub = (AssaultPartyInterface) UnicastRemoteObject.exportObject(assaultParty, portNumb);
       } catch (RemoteException e) {
           System.out.println("Assault Party 1 stub generation exception: " + e.getMessage());
           e.printStackTrace();
           System.exit(1);
       }
       System.out.println("Stub was generated!");

       /* register it with the general registry service */

       String nameEntryBase = "RegisterHandler";                      // public name of the object that enables the registration
       // of other remote objects
       String nameEntryObject = "AssaultParty1";                      // public name of the assault party 1 object
       Register reg = null;                                           // remote reference to the object that enables the registration
       // of other remote objects

       try {
           reg = (Register) registry.lookup (nameEntryBase);
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
           reg.bind(nameEntryObject, assaultPartyStub);
       } catch (RemoteException e) {
           System.out.println("Assault Party 1 registration exception: " + e.getMessage());
           e.printStackTrace();
           System.exit(1);
       } catch (AlreadyBoundException e) {
           System.out.println("Assault Party 1 already bound exception: " + e.getMessage());
           e.printStackTrace();
           System.exit(1);
       }
       System.out.println("Assault Party 1 object was registered!");

       /* wait for the end of operations */

       System.out.println("Assault Party 1 is in operation!");
       try {
           while (!end)
                synchronized (Class.forName("serverSide.main.AssaultParty1Main")) {
                    try {
                        (Class.forName("serverSide.main.AssaultParty1Main")).wait();
                    } catch (InterruptedException e) {
                        System.out.println("Assault Party 1 main thread was interrupted!");
                    }
                }
       } catch (ClassNotFoundException e) {
           System.out.println("The data type AssaultParty1Main was not found (blocking)!");
           e.printStackTrace();
           System.exit(1);
       }

       /* server shutdown */

       boolean shutdownDone = false; // flag signalling the shutdown of the assault party 1 service

       try {
           reg.unbind(nameEntryObject);
       } catch (RemoteException e) {
           System.out.println("Assault Party 1 deregistration exception: " + e.getMessage());
           e.printStackTrace();
           System.exit(1);
       } catch (NotBoundException e) {
           System.out.println("Assault Party 1 not bound exception: " + e.getMessage());
           e.printStackTrace();
           System.exit(1);
       }
       System.out.println ("Assault Party 1 was deregistered!");

       try {
           shutdownDone = UnicastRemoteObject.unexportObject(assaultParty, true);
       } catch (NoSuchObjectException e) {
           System.out.println("Assault Party 1 unexport exception: " + e.getMessage());
           e.printStackTrace();
           System.exit(1);
       }

       if (shutdownDone)
           System.out.println("Assault Party 1 was shutdown!");
   }

    public static void shutdown() {
        end = true;
        try {
            synchronized (Class.forName("serverSide.main.AssaultParty1Main")) {
                (Class.forName("serverSide.main.AssaultParty1Main")).notify();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("The data type AssaultParty1Main was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
