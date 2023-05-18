package serverSide.main;

import serverSide.entities.*;
import serverSide.interfaces.CollectionSiteInterface;
import serverSide.sharedRegions.*;
import commInfra.*;
import java.net.*;

import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.MuseumStub;

/**
 *    Collection Site server of the Heist To The Museum.
 * <p>
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class CollectionSiteMain
{
  /**
   *  Flag signaling the service is active.
   */
   public static boolean waitConnection;

  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - port number for listening to service requests
   *        args[1] - name of the platform where is located the server for
   *             the general repository
   *        args[2] - port number where the server for the general repository
   *             is listening to service requests
   *        args[3] - name of the platform where is located the server for
   *             the assault party 0
   *        args[4] - port number where the server for the assault party 0
   *             is listening to service requests
   *        args[5] - name of the platform where is located the server for
   *             the assault party 1
   *        args[6] - port number where the server for the assault party 1
   *             is listening to service requests
   *        args[7] - name of the platform where is located the server for
   *             the museum
   *        args[8] - port number where the server for the museum
   *             is listening to service requests
   */
   public static void main (String [] args)
   {
      CollectionSite collectionSite;
      ServerCom serverCom, serverComi;                                         // communication channels
      int portNumber = -1;                                             // port number for listening to service requests
      String generalRepositoryHostName = null;
      int generalRepositoryPortNumber = -1;
      String assaultParty0HostName = null;
      int assaultParty0PortNumber = -1;
      String assaultParty1HostName = null;
      int assaultParty1PortNumber = -1;
      String museumHostName = null;
      int museumPortNumber = -1;

      if (args.length != 9)
         {  System.out.println("Wrong number of parameters!");
           System.exit(1);
         }
      try
      { portNumber = Integer.parseInt (args[0]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[0] + " is not a number!");
        System.exit(1);
      }
      if ((portNumber < 4000) || (portNumber >= 65536))
         { System.out.println(args[0] + " is not a valid port number!");
           System.exit(1);
         }

      generalRepositoryHostName = args[1];
      try
      { 
        generalRepositoryPortNumber = Integer.parseInt (args[2]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[2] + " is not a number!");
        System.exit(1);
      }
      if ((generalRepositoryPortNumber < 4000) || (generalRepositoryPortNumber >= 65536))
        { System.out.println(args[2] + " is not a valid port number!");
          System.exit(1);
        }

      assaultParty0HostName = args[3];
      try
      { 
        assaultParty0PortNumber = Integer.parseInt (args[4]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[4] + " is not a number!");
        System.exit(1);
      }
      if ((assaultParty0PortNumber < 4000) || (assaultParty0PortNumber >= 65536))
        { System.out.println(args[4] + " is not a valid port number!");
          System.exit(1);
        }

      assaultParty1HostName = args[5];
      try
      { 
        assaultParty1PortNumber = Integer.parseInt (args[6]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[6] + " is not a number!");
        System.exit(1);
      }
      if ((assaultParty1PortNumber < 4000) || (assaultParty1PortNumber >= 65536))
          { System.out.println(args[6] + " is not a valid port number!");
            System.exit(1);
          }

      museumHostName = args[7];
      try
      { 
        museumPortNumber = Integer.parseInt (args[8]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[8] + " is not a number!");
        System.exit(1);
      }
      if ((museumPortNumber < 4000) || (museumPortNumber >= 65536))
          { System.out.println(args[8] + " is not a valid port number!");
            System.exit(1);
          }

      /* service is established */

      // Shared regions initialization
      GeneralRepositoryStub generalRepository = new GeneralRepositoryStub(generalRepositoryHostName, generalRepositoryPortNumber);
      AssaultPartyStub[] assaultParties = {new AssaultPartyStub(0, assaultParty0HostName, assaultParty0PortNumber),
                                           new AssaultPartyStub(1, assaultParty1HostName, assaultParty1PortNumber)};
      MuseumStub museum = new MuseumStub(museumHostName, museumPortNumber);
      collectionSite = new CollectionSite(generalRepository, assaultParties, museum);
      CollectionSiteInterface collectionSiteInterface = new CollectionSiteInterface(collectionSite);

      serverCom = new ServerCom(portNumber);                                         // listening channel at the public port is established
      serverCom.start();
      System.out.println("Service is established!");
      System.out.println("Server is listening for service requests.");

     /* service request processing */

      CollectionSiteProxyAgent proxy;                                // service provider agent

      waitConnection = true;
      while (waitConnection)
      { try
        { serverComi = serverCom.accept();                                    // enter listening procedure
          proxy = new CollectionSiteProxyAgent(serverComi, collectionSiteInterface);    // start a service provider agent to address
          proxy.start();                                                      //   the request of service
        }
        catch (SocketTimeoutException ignored) {}
      }
      serverCom.end();                                                   // operations termination
      System.out.println("Server was shutdown.");
   }
}
