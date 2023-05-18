package serverSide.sharedRegions;

import clientSide.stubs.AssaultPartyStub;
import clientSide.stubs.GeneralRepositoryStub;
import serverSide.entities.MuseumProxyAgent;
import serverSide.main.MuseumMain;
import utils.Constants;
import utils.Room;

/**
 * The Museum has rooms inside it. Those rooms have paintings that can be stolen
 * by the Ordinary Thieves of the Assault Party.
 */
public class Museum {
    /**
     * Rooms inside the museum.
     */
    private final Room[] rooms;

    /**
     * The General Repository where logging occurs.
     */
    private final GeneralRepositoryStub generalRepository;

    /**
     * The Assault Parties shared region.
     */
    private final AssaultPartyStub[] assaultParties;

    /**
     * Museum constructor, initializes rooms.
     * @param generalRepository the General Repository.
     */
    public Museum(GeneralRepositoryStub generalRepository, AssaultPartyStub[] assaultParties, int[] paintings, int[] distances) {
        this.generalRepository = generalRepository;
        this.assaultParties = assaultParties;
        this.rooms = new Room[Constants.NUM_ROOMS];
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i] = new Room(i, distances[i], paintings[i]);
        }
        generalRepository.setInitialRoomStates(paintings, distances);
    }

    /**
     * The Ordinary Thief tries to roll a canvas.
     * @param party the party identification.
     */
    public void rollACanvas(int party) {
        MuseumProxyAgent thief = (MuseumProxyAgent) Thread.currentThread();
        thief.setOrdinaryThiefState(MuseumProxyAgent.AT_A_ROOM);
        generalRepository.setOrdinaryThiefState(thief.getOrdinaryThiefID(), thief.getOrdinaryThiefState(), thief.getOrdinaryThiefSituation(), thief.getOrdinaryThiefMaxDisplacement());
        boolean res = false;
        int room = assaultParties[party].getRoom();
        synchronized (this) {
            res = rooms[room].rollACanvas();
            if (res) {
                assaultParties[party].setBusyHands(thief.getOrdinaryThiefID(), true);
                generalRepository.setRoomState(room, rooms[room].getPaintings());
            }
        }
    }

    /**
     * Shuts down the Museum server.
     */
    public synchronized void shutdown () {
        MuseumMain.waitConnection = false;
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
}
