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
    public Museum(GeneralRepositoryInterface generalRepositoryStub, AssaultPartyInterface[] assaultPartyStubs) {
        this.generalRepositoryStub = generalRepositoryStub;
        this.assaultPartyStubs = assaultPartyStubs;
        this.rooms = new Room[Constants.NUM_ROOMS];
    }

    /**
     * Roll a canvas.
     * @param party the identification of the Assault Party
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return the updated state of the Ordinary Thief
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
     * Sets the rooms of the museum.
     * @param paintings an array with the paintings of each room, where the index is its identification
     * @param distances an array with the distances to each room, where the index is the identification of the room
     * @throws RemoteException if the execution of the remote code failed
     */
    @Override
    public void setRooms(int[] paintings, int[] distances) throws RemoteException {
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i] = new Room(i, distances[i], paintings[i]);
        }
        setInitialRoomStates(paintings, distances);
    }

    /**
     * Sends the shutdown signal to the Museum.
     */
    public synchronized void shutdown () {
        MuseumMain.shutdown();
    }

    /**
     * Getter for the number of paintings in a room.
     * @param id the identification of the room
     * @return the number of paintings in the room
     */
    public int getRoomDistance(int id) {
        return rooms[id].getDistance();
    }

    /**
     * Getter for the number of paintings in a room.
     * @param id the identification of the room
     * @return the number of paintings in the room
     */
    public int getRoomPaintings(int id) {
        return rooms[id].getPaintings();
    }

    /**
     * Calls the remote method setInitialRoomStates on the General Repository.
     * @param paintings an array with the number of paintings in each room, where the index of the value corresponds
     *                  to the identification of the room
     * @param distances an array with the distance to each room, where the index of the value corresponds to the
     *                  identification of the room
     */
    private void setInitialRoomStates(int[] paintings, int[] distances) {
        try {
            generalRepositoryStub.setInitialRoomStates(paintings, distances);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setInitialRoomStates: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Calls the remote method setOrdinaryThiefState on the General Repository.
     * @param ordinaryThief the identification of the Ordinary Thief
     */
    private void setOrdinaryThiefState(int ordinaryThief) {
        try {
            generalRepositoryStub.setOrdinaryThiefState(ordinaryThief, OrdinaryThief.AT_A_ROOM);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setOrdinaryThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Calls the remote method getRoom on the Assault Party.
     * @param assaultParty the identification of the Assault Party
     * @return the identification of the room
     */
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

    /**
     * Calls the remote method setBusyHands on the Assault Party.
     * @param assaultParty the identification of the Assault Party
     * @param ordinaryThief the identification of the Ordinary Thief
     */
    private void setBusyHands(int assaultParty, int ordinaryThief) {
        try {
            assaultPartyStubs[assaultParty].setBusyHands(ordinaryThief, true);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setBusyHands: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Calls the remote method setRoomState on the General Repository.
     * @param room the identification of the room
     * @param paintings the number of paintings in the room
     */
    private void setRoomState(int room, int paintings) {
        try {
            generalRepositoryStub.setRoomState(room, paintings);
        } catch (RemoteException e) {
            System.out.println("Remote exception on Museum.setRoomState: " + e.getMessage());
            System.exit(1);
        }
    }
}
