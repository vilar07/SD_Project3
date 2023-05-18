package serverSide.main;

import serverSide.entities.*;
import serverSide.interfaces.AssaultPartyInterface;
import serverSide.objects.*;
import utils.Constants;
import commInfra.*;
import java.net.*;

import clientSide.stubs.GeneralRepositoryStub;

/**
 *    Assault Party server of the Heist To The Museum.
 * <p>
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultPartyMain
{
  /**
   *  Flag signaling the service is active.
   */
   public static boolean waitConnection;

  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - identification of the Assault Party
   *        args[1] - port number for listening to service requests
   *        args[2] - name of the platform where is located the server for
   *             the general repository
   *        args[3] - port number where the server for the general repository
   *             is listening to service requests
   */
   public static void main (String [] args)
   {
      AssaultParty assaultParty;
      int assaultPartyID = -1;
      ServerCom serverCom, serverComi;                                         // communication channels
      int portNumber = -1;                                             // port number for listening to service requests
      String generalRepositoryHostName = null;
      int generalRepositoryPortNumber = -1;

      if (args.length != 4)
      {  System.out.println("Wrong number of parameters!");
        System.exit(1);
      }
      try {
        assaultPartyID = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        System.out.println(args[0] + " is not a number!");
        System.exit(1);
      }
      if (assaultPartyID < 0 || assaultPartyID > Constants.NUM_ASSAULT_PARTIES) {
        System.out.println(args[0] + " is not a valid identification for the Assault Party!");
        System.exit(1);
      }
      try
      { portNumber = Integer.parseInt (args[1]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[1] + " is not a number!");
        System.exit(1);
      }
      if ((portNumber < 4000) || (portNumber >= 65536))
         { System.out.println(args[1] + " is not a valid port number!");
           System.exit(1);
         }
      generalRepositoryHostName = args[2];
      try
      { 
        generalRepositoryPortNumber = Integer.parseInt (args[3]);
      }
      catch (NumberFormatException e)
      { System.out.println(args[3] + " is not a number!");
        System.exit(1);
      }
      if ((generalRepositoryPortNumber < 4000) || (generalRepositoryPortNumber >= 65536))
        { System.out.println(args[3] + " is not a valid port number!");
          System.exit(1);
        }

      /* service is established */

      // Shared regions initialization
      GeneralRepositoryStub generalRepository = new GeneralRepositoryStub(generalRepositoryHostName, generalRepositoryPortNumber);
      assaultParty = new AssaultParty(assaultPartyID, generalRepository);
      AssaultPartyInterface assaultPartyInterface = new AssaultPartyInterface(assaultParty);

      serverCom = new ServerCom(portNumber);                                         // listening channel at the public port is established
      serverCom.start();
      System.out.println("Service is established!");
      System.out.println("Server is listening for service requests.");

     /* service request processing */

      AssaultPartyProxyAgent proxy;                                // service provider agent

      waitConnection = true;
      while (waitConnection)
      { try
        { serverComi = serverCom.accept();                                    // enter listening procedure
          proxy = new AssaultPartyProxyAgent(serverComi, assaultPartyInterface);    // start a service provider agent to address
          proxy.start();                                                      //   the request of service
        }
        catch (SocketTimeoutException ignored) {}
      }
      serverCom.end();                                                   // operations termination
      System.out.println("Server was shutdown.");
   }
}
