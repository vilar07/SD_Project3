package serverSide.main;

import serverSide.entities.*;
import serverSide.interfaces.MuseumInterface;
import serverSide.objects.*;
import utils.Constants;
import commInfra.*;
import java.net.*;
import java.util.Random;

import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.GeneralRepositoryStub;

/**
 *    Museum server of the Heist To The Museum.
 * <p>
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class MuseumMain
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
   */
   public static void main (String [] args)
   {
      Museum museum;
      ServerCom serverCom, serverComi;                                         // communication channels
      int portNumber = -1;                                             // port number for listening to service requests
      String generalRepositoryHostName = null;
      int generalRepositoryPortNumber = -1;
      String assaultParty0HostName = null;
      int assaultParty0PortNumber = -1;
      String assaultParty1HostName = null;
      int assaultParty1PortNumber = -1;

      if (args.length != 7)
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

      /* service is established */

      // Shared regions initialization
      Random random = new Random(System.currentTimeMillis());
      int[] paintings = new int[Constants.NUM_ROOMS];
      int[] distances = new int[Constants.NUM_ROOMS];
      for(int i = 0; i < Constants.NUM_ROOMS; i++) {
        distances[i] = Constants.MIN_ROOM_DISTANCE + random.nextInt(Constants.MAX_ROOM_DISTANCE - Constants.MIN_ROOM_DISTANCE + 1);
        paintings[i] = Constants.MIN_PAINTINGS + random.nextInt(Constants.MAX_PAINTINGS - Constants.MIN_PAINTINGS + 1);
      }
      GeneralRepositoryStub generalRepository = new GeneralRepositoryStub(generalRepositoryHostName, generalRepositoryPortNumber);
      AssaultPartyStub[] assaultParties = {new AssaultPartyStub(0, assaultParty0HostName, assaultParty0PortNumber),
                                           new AssaultPartyStub(1, assaultParty1HostName, assaultParty1PortNumber)};
      museum = new Museum(generalRepository, assaultParties, paintings, distances);
      MuseumInterface museumInterface = new MuseumInterface(museum);

      serverCom = new ServerCom(portNumber);                                         // listening channel at the public port is established
      serverCom.start();
      System.out.println("Service is established!");
      System.out.println("Server is listening for service requests.");

     /* service request processing */

      MuseumProxyAgent proxy;                                // service provider agent

      waitConnection = true;
      while (waitConnection)
      { try
        { serverComi = serverCom.accept();                                    // enter listening procedure
          proxy = new MuseumProxyAgent(serverComi, museumInterface);    // start a service provider agent to address
          proxy.start();                                                      //   the request of service
        }
        catch (SocketTimeoutException ignored) {}
      }
      serverCom.end();                                                   // operations termination
      System.out.println("Server was shutdown.");
   }
}
