package serverSide.objects;

import clientSide.entities.OrdinaryThief;
import interfaces.AssaultPartyInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.MuseumInterface;
import serverSide.main.MuseumMain;
import utils.Constants;
import utils.Room;

import java.rmi.RemoteException;

/**
 * The Museum has rooms inside it. Those rooms have paintings that can be stolen
 * by the Ordinary Thieves of the Assault Party.
 */
public class Museum implements MuseumInterface {
    /**
     * Rooms inside the museum.
     */
    private final Room[] rooms;

    /**
     * The General Repository where logging occurs.
     */
    private final GeneralRepositoryInterface generalRepositoryStub;

    /**
     * The Assault Parties shared region.
     */
    private final AssaultPartyInterface[] assaultPartyStubs;

    /**
     * Museum constructor, initializes rooms.
     * @param generalRepositoryStub the General Repository.
     */
    public Museum(GeneralRepositoryInterface generalRepositoryStub, AssaultPartyInterface[] assaultPartyStubs, int[] paintings, int[] distances) {
        this.generalRepositoryStub = generalRepositoryStub;
        this.assaultPartyStubs = assaultPartyStubs;
        this.rooms = new Room[Constants.NUM_ROOMS];
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i] = new Room(i, distances[i], paintings[i]);
        }
        setInitialRoomStates(paintings, distances);
    }

    /**
     * The Ordinary Thief tries to roll a canvas.
     * @param party the party identification.
     */
    public int rollACanvas(int party, int ordinaryThief) {
        setOrdinaryThiefState(ordinaryThief);
        boolean res = false;
        int room = getAssaultPartyRoom(party);
        synchronized (this) {
            res = rooms[room].rollACanvas();
            if (res) {
                setBusyHands(party, ordinaryThief);
                setRoomState(room, rooms[room].getPaintings());
            }
        }
        return OrdinaryThief.AT_A_ROOM;
    }

    /**
     * Shuts down the Museum server.
     */
    public synchronized void shutdown () {
        MuseumMain.shutdown();
    }

    /**
     * Getter for the distance to a specific room of the Museum.
     * @param id the room identification.
     * @return the distance to the room.
     */
    public int getRoomDistance(int id) {
        return rooms[id].getDistance();
    }

    /**
     * Getter for the number of paintings in a specific room of the Museum.
     * @param id the room identification.
     * @return the number of paintings in the room.
     */
    public int getRoomPaintings(int id) {
        return rooms[id].getPaintings();
    }

    private void setInitialRoomStates(int[] paintings, int[] distances) {
        try {
            generalRepositoryStub.setInitialRoomStates(paintings, distances);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setInitialRoomStates: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setOrdinaryThiefState(int ordinaryThief) {
        try {
            generalRepositoryStub.setOrdinaryThiefState(ordinaryThief, OrdinaryThief.AT_A_ROOM);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setOrdinaryThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private int getAssaultPartyRoom(int assaultParty) {
        int ret = 0;
        try {
            ret = assaultPartyStubs[assaultParty].getRoom();
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.getAssaultPartyRoom: " + e.getMessage());
            System.exit(1);
        }
        return ret;
    }

    private void setBusyHands(int assaultParty, int ordinaryThief) {
        try {
            assaultPartyStubs[assaultParty].setBusyHands(ordinaryThief, true);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setBusyHands: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setRoomState(int room, int paintings) {
        try {
            generalRepositoryStub.setRoomState(room, paintings);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setRoomState: " + e.getMessage());
            System.exit(1);
        }
    }
}
