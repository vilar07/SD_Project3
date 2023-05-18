package clientSide.main;

import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.CollectionSiteStub;
import clientSide.stubs.ConcentrationSiteStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.MuseumStub;
import utils.Constants;

import java.util.Random;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;

/**
 * Distributed version of the Heist To The Museum.
 */
public class HeistToTheMuseum
{   
    /**
     * Main method to start the Heist To The Museum.
     * @param args runtime arguments
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
     *        args[9] - name of the platform where is located the server for
     *             the collection site
     *        args[10] - port number where the server for the collection site
     *             is listening to service requests
     *        args[11] - name of the platform where is located the server for
     *             the concentration site
     *        args[12] - port number where the server for the concentration site
     *             is listening to service requests
     */
    public static void main(String[] args) {
        /* getting problem runtime parameters */
        int portNumber = 0;
        String generalRepositoryHostName = null;
        int generalRepositoryPortNumber = -1;
        String assaultParty0HostName = null;
        int assaultParty0PortNumber = -1;
        String assaultParty1HostName = null;
        int assaultParty1PortNumber = -1;
        String museumHostName = null;
        int museumPortNumber = -1;
        String collectionSiteHostName = null;
        int collectionSitePortNumber = -1;
        String concentrationSiteHostName = null;
        int concentrationSitePortNumber = -1;
        if (args.length != 13)
        { 
            System.out.println("Wrong number of parameters!");
            System.exit(1);
        }
        try
        { 
            portNumber = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e)
        { 
            System.out.println(args[1] + " is not a number!");
            System.exit(1);
        }
        if ((portNumber < 4000) || (portNumber >= 65536))
        { 
            System.out.println(args[1] + " is not a valid port number!");
            System.exit(1);
        }

        generalRepositoryHostName = args[1];
        try
        { 
          generalRepositoryPortNumber = Integer.parseInt (args[2]);
        }
        catch (NumberFormatException e)
        {
            System.out.println(args[2] + " is not a number!");
            System.exit(1);
        }
        if ((generalRepositoryPortNumber < 4000) || (generalRepositoryPortNumber >= 65536))
        { 
            System.out.println(args[2] + " is not a valid port number!");
            System.exit(1);
        }
  
        assaultParty0HostName = args[3];
        try
        { 
            assaultParty0PortNumber = Integer.parseInt (args[4]);
        }
        catch (NumberFormatException e)
        { 
            System.out.println(args[4] + " is not a number!");
            System.exit(1);
        }
        if ((assaultParty0PortNumber < 4000) || (assaultParty0PortNumber >= 65536))
        { 
            System.out.println(args[4] + " is not a valid port number!");
            System.exit(1);
        }
  
        assaultParty1HostName = args[5];
        try
        { 
            assaultParty1PortNumber = Integer.parseInt (args[6]);
        }
        catch (NumberFormatException e)
        { 
            System.out.println(args[6] + " is not a number!");
            System.exit(1);
        }
        if ((assaultParty1PortNumber < 4000) || (assaultParty1PortNumber >= 65536))
        { 
            System.out.println(args[6] + " is not a valid port number!");
            System.exit(1);
        }

        museumHostName = args[7];
        try
        { 
            museumPortNumber = Integer.parseInt (args[8]);
        }
        catch (NumberFormatException e)
        { 
            System.out.println(args[8] + " is not a number!");
            System.exit(1);
        }
        if ((museumPortNumber < 4000) || (museumPortNumber >= 65536))
        { 
            System.out.println(args[8] + " is not a valid port number!");
            System.exit(1);
        }

        collectionSiteHostName = args[9];
        try
        { 
            collectionSitePortNumber = Integer.parseInt (args[10]);
        }
        catch (NumberFormatException e)
        { 
            System.out.println(args[10] + " is not a number!");
            System.exit(1);
        }
        if ((collectionSitePortNumber < 4000) || (collectionSitePortNumber >= 65536))
        { 
            System.out.println(args[10] + " is not a valid port number!");
            System.exit(1);
        }

        concentrationSiteHostName = args[11];
        try
        { 
            concentrationSitePortNumber = Integer.parseInt (args[12]);
        }
        catch (NumberFormatException e)
        { 
            System.out.println(args[12] + " is not a number!");
            System.exit(1);
        }
        if ((concentrationSitePortNumber < 4000) || (concentrationSitePortNumber >= 65536))
        { 
            System.out.println(args[12] + " is not a valid port number!");
            System.exit(1);
        }

        GeneralRepositoryStub generalRepository = new GeneralRepositoryStub(generalRepositoryHostName, generalRepositoryPortNumber);
        AssaultPartyStub[] assaultParties = {new AssaultPartyStub(0, assaultParty0HostName, assaultParty0PortNumber),
                                             new AssaultPartyStub(1, assaultParty1HostName, assaultParty1PortNumber)};
        MuseumStub museum = new MuseumStub(museumHostName, museumPortNumber);
        CollectionSiteStub collectionSite = new CollectionSiteStub(collectionSiteHostName, collectionSitePortNumber);
        ConcentrationSiteStub concentrationSite = new ConcentrationSiteStub(concentrationSiteHostName, concentrationSitePortNumber);
        Random random = new Random(System.currentTimeMillis());
        MasterThief masterThief = new MasterThief(collectionSite, concentrationSite, assaultParties);
        OrdinaryThief ordinaryThieves[] = new OrdinaryThief[Constants.NUM_THIEVES - 1];
        for(int i = 0; i < ordinaryThieves.length; i++) {
            ordinaryThieves[i] = new OrdinaryThief(i, museum, collectionSite, concentrationSite, assaultParties, 
                    random.nextInt(Constants.MAX_THIEF_DISPLACEMENT - Constants.MIN_THIEF_DISPLACEMENT + 1) + Constants.MIN_THIEF_DISPLACEMENT
            );
        }
        masterThief.start();
        for(OrdinaryThief ot: ordinaryThieves) {
            ot.start();
        }

        try {
            masterThief.join();
            for(OrdinaryThief ot: ordinaryThieves) {
                ot.join();
            }
        } catch (InterruptedException ignored) {}
        concentrationSite.shutdown();
        collectionSite.shutdown();
        museum.shutdown();
        for (AssaultPartyStub assaultParty: assaultParties) {
            assaultParty.shutdown();
        }
        generalRepository.shutdown();
    }
}
