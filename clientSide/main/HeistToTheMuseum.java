package clientSide.main;

import interfaces.*;
import utils.Constants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;

/**
 * Distributed version of the Heist To The Museum.
 */
public class HeistToTheMuseum
{
    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - name of the platform where is located the RMI registering service
     *        args[1] - port number where the registering service is listening to service requests
     */
    public static void main(String[] args) {
        String rmiRegistryHostName;       // name of the platform where is located the RMI registering service
        int rmiRegistryPortNumber = -1;   // port number where the registering service is listening to service requests

        /* getting problem runtime parameters */
        if (args.length != 2) {
            System.out.println("Wrong number of parameters!");
            System.exit(1);
        }
        rmiRegistryHostName = args[0];
        try {
            rmiRegistryPortNumber = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("args[1] is not a number!");
            System.exit(1);
        }
        if ((rmiRegistryPortNumber < 4000) || (rmiRegistryPortNumber >= 65536)) {
            System.out.println("args[1] is not a valid port number!");
            System.exit (1);
        }

        /* problem initialization */
        String nameEntryGeneralRepository = "GeneralRepository"; // public name of the general repository object
        GeneralRepositoryInterface generalRepositoryStub = null; // remote reference to the general repository object

        String nameEntryAssaultParty0 = "AssaultParty0"; // public name of the assault party 0 object
        String nameEntryAssaultParty1 = "AssaultParty1"; // public name of the assault party 1 object
        AssaultPartyInterface[] assaultPartyStubs = {null, null}; // remote reference to the assault party objects

        String nameEntryMuseum = "Museum"; // public name of the museum object
        MuseumInterface museumStub = null; // remote reference to the museum object

        String nameEntryCollectionSite = "CollectionSite"; // public name of the collection site object
        CollectionSiteInterface collectionSiteStub = null; // remote reference to the collection site object

        String nameEntryConcentrationSite = "ConcentrationSite"; // public name of the concentration site object
        ConcentrationSiteInterface concentrationSiteStub = null; // remote reference to the concentration site object

        Registry registry = null; // remote reference for registration in the RMI registry service
        try {
            registry = LocateRegistry.getRegistry(rmiRegistryHostName, rmiRegistryPortNumber);
        } catch (RemoteException e) {
            System.out.println("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

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

        try {
            museumStub = (MuseumInterface) registry.lookup(nameEntryMuseum);
        } catch (RemoteException e) {
            System.out.println("Museum lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Museum not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            collectionSiteStub = (CollectionSiteInterface) registry.lookup(nameEntryCollectionSite);
        } catch (RemoteException e) {
            System.out.println("Collection Site lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Collection Site not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            concentrationSiteStub = (ConcentrationSiteInterface) registry.lookup(nameEntryConcentrationSite);
        } catch (RemoteException e) {
            System.out.println("Concentration Site lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("Concentration Site not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        Random random = new Random(System.currentTimeMillis());

        int[] paintings = new int[Constants.NUM_ROOMS];
        int[] distances = new int[Constants.NUM_ROOMS];
        for (int i = 0; i < paintings.length; i++) {
            paintings[i] = Constants.MIN_PAINTINGS +
                    random.nextInt(Constants.MAX_PAINTINGS - Constants.MIN_PAINTINGS + 1);
            distances[i] = Constants.MIN_ROOM_DISTANCE +
                    random.nextInt(Constants.MAX_ROOM_DISTANCE - Constants.MIN_ROOM_DISTANCE + 1);
        }
        try {
            museumStub.setRooms(paintings, distances);
        } catch (RemoteException e) {
            System.out.println("Remote exception on setRooms: " + e.getMessage());
            System.exit (1);
        }

        MasterThief masterThief = new MasterThief(collectionSiteStub, concentrationSiteStub, assaultPartyStubs);
        OrdinaryThief[] ordinaryThieves = new OrdinaryThief[Constants.NUM_THIEVES - 1];
        int[] maxDisplacements = new int[Constants.NUM_THIEVES - 1];
        for(int i = 0; i < ordinaryThieves.length; i++) {
            maxDisplacements[i] = random.nextInt(Constants.MAX_THIEF_DISPLACEMENT -
                    Constants.MIN_THIEF_DISPLACEMENT + 1) + Constants.MIN_THIEF_DISPLACEMENT;
            ordinaryThieves[i] = new OrdinaryThief(i, museumStub, collectionSiteStub, concentrationSiteStub,
                    assaultPartyStubs, maxDisplacements[i]);
        }
        try {
            generalRepositoryStub.setAttributesOfOrdinaryThieves(maxDisplacements);
        } catch (RemoteException e) {
            System.out.println("Remote exception on setAttributesOfOrdinaryThieves: " + e.getMessage());
            System.exit (1);
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

        try {
            concentrationSiteStub.shutdown();
        } catch (RemoteException e) {
            System.out.println("Remote exception on Concentration Site shutdown: " + e.getMessage());
            System.exit(1);
        }
        try {
            collectionSiteStub.shutdown();
        } catch (RemoteException e) {
            System.out.println("Remote exception on Collection Site shutdown: " + e.getMessage());
            System.exit(1);
        }
        try {
            museumStub.shutdown();
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum shutdown: " + e.getMessage());
            System.exit(1);
        }
        try {
            assaultPartyStubs[0].shutdown();
        } catch (RemoteException e) {
            System.out.println("Remote exception on Assault Party 0 shutdown: " + e.getMessage());
            System.exit(1);
        }
        try {
            assaultPartyStubs[1].shutdown();
        } catch (RemoteException e) {
            System.out.println("Remote exception on Assault Party 1 shutdown: " + e.getMessage());
            System.exit(1);
        }
        try {
            generalRepositoryStub.shutdown();
        } catch (RemoteException e) {
            System.out.println("Remote exception on General Repository shutdown: " + e.getMessage());
            System.exit(1);
        }
    }
}
