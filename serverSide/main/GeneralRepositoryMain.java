package serverSide.main;

import serverSide.entities.*;
import serverSide.interfaces.GeneralRepositoryInterface;
import serverSide.objects.*;
import commInfra.*;
import java.net.*;

/**
 *    General Repository server of the Heist To The Museum.
 * <p>
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralRepositoryMain
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
   */
   public static void main (String [] args)
   {
      GeneralRepository generalRepository;
      ServerCom serverCom, serverComi;                                         // communication channels
      int portNumber = -1;                                             // port number for listening to service requests

      if (args.length != 1)
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

      /* service is established */

      generalRepository = new GeneralRepository();
      GeneralRepositoryInterface generalRepositoryInterface = new GeneralRepositoryInterface(generalRepository);

      serverCom = new ServerCom(portNumber);                                         // listening channel at the public port is established
      serverCom.start();
      System.out.println("Service is established!");
      System.out.println("Server is listening for service requests.");

     /* service request processing */

      GeneralRepositoryProxyAgent proxy;                                // service provider agent

      waitConnection = true;
      while (waitConnection)
      { try
        { serverComi = serverCom.accept();                                    // enter listening procedure
          proxy = new GeneralRepositoryProxyAgent(serverComi, generalRepositoryInterface);    // start a service provider agent to address
          proxy.start();                                                      //   the request of service
        }
        catch (SocketTimeoutException ignored) {}
      }
      serverCom.end();                                                   // operations termination
      System.out.println("Server was shutdown.");
   }
}
