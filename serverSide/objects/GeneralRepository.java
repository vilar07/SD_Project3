package serverSide.objects;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import interfaces.GeneralRepositoryInterface;
import serverSide.main.GeneralRepositoryMain;
import utils.AssaultPartyElemLogging;
import utils.AssaultPartyLogging;
import utils.Constants;
import utils.Logger;
import utils.OrdinaryThiefLogging;
import utils.RoomLogging;

import java.rmi.RemoteException;

/**
 * General Repository where logging occurs.
 */
public class GeneralRepository implements GeneralRepositoryInterface {
    /**
     * Logger that handles the writing of the internal state of the simulation to the logging file.
     */
    private final Logger logger;

    /**
     * State of the Master Thief.
     */
    private String masterThiefState;

    /**
     * Information of the Ordinary Thieves.
     */
    private final OrdinaryThiefLogging[] ordinaryThieves;

    /**
     * Information of the Assault Parties.
     */
    private final AssaultPartyLogging[] assaultParties;

    /**
     * Information of the rooms in the Museum.
     */
    private final RoomLogging[] rooms;

    /**
     * Constructor for the General Repository.
     */
    public GeneralRepository() {
        logger = new Logger();
        masterThiefState = "----";
        ordinaryThieves = new OrdinaryThiefLogging[Constants.NUM_THIEVES - 1];
        for (int i = 0; i < ordinaryThieves.length; i++) {
            ordinaryThieves[i] = new OrdinaryThiefLogging("----", '-', '-');
        }
        assaultParties = new AssaultPartyLogging[Constants.NUM_ASSAULT_PARTIES];
        for (int i = 0; i < assaultParties.length; i++) {
            AssaultPartyElemLogging[] elems = new AssaultPartyElemLogging[Constants.ASSAULT_PARTY_SIZE];
            for (int j = 0; j < elems.length; j++) {
                elems[j] = new AssaultPartyElemLogging('-', "--", '-');
            }
            assaultParties[i] = new AssaultPartyLogging('-', elems);
        }
        rooms = new RoomLogging[Constants.NUM_ROOMS];
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new RoomLogging(0, 0);
        }
        printHead();
    }
    
    /**
     * Prints the head of the logging file.
     */
    public void printHead() {
        StringBuilder stringBuilder = new StringBuilder(
                         "                              Heist to the Museum - Description of the internal state\n\n"
                ).append("MstT    Thief 1      Thief 2      Thief 3      Thief 4      Thief 5      Thief 6\n")
                 .append("Stat   Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD    Stat S MD\n")
                 .append("                    Assault party 1                       Assault party 2                       Museum\n")
                 .append("            Elem 1     Elem 2     Elem 3          Elem 1     Elem 2     Elem 3   Room 1  Room 2  Room 3  Room 4  Room 5\n")
                 .append("     RId  Id Pos Cv  Id Pos Cv  Id Pos Cv  RId  Id Pos Cv  Id Pos Cv  Id Pos Cv   NP DT   NP DT   NP DT   NP DT   NP DT\n");
        logger.print(stringBuilder.toString());
    }

    /**
     * Prints the state of the simulation to the logging file.
     */
    public synchronized void printState() {
        StringBuilder stringBuilder = new StringBuilder(
                 String.format("%4s   %4s %c  %c    %4s %c  %c    %4s %c  %c    %4s %c  %c    %4s %c  %c    %4s %c  %c\n",
                        masterThiefState, 
                        ordinaryThieves[0].getState(), ordinaryThieves[0].getSituation(), ordinaryThieves[0].getMaxDisplacement(),
                        ordinaryThieves[1].getState(), ordinaryThieves[1].getSituation(), ordinaryThieves[1].getMaxDisplacement(),
                        ordinaryThieves[2].getState(), ordinaryThieves[2].getSituation(), ordinaryThieves[2].getMaxDisplacement(),
                        ordinaryThieves[3].getState(), ordinaryThieves[3].getSituation(), ordinaryThieves[3].getMaxDisplacement(),
                        ordinaryThieves[4].getState(), ordinaryThieves[4].getSituation(), ordinaryThieves[4].getMaxDisplacement(),
                        ordinaryThieves[5].getState(), ordinaryThieves[5].getSituation(), ordinaryThieves[5].getMaxDisplacement()
                )
        ).append(String.format("      %c    %c  %2s  %c   %c  %2s  %c   %c  %2s  %c   %c   %c   %2s  %c   %c  %2s  %c   %c  %2s  %c   %2d %2d   %2d %2d   %2d %2d   %2d %2d   %2d %2d\n",
                        assaultParties[0].getRoom(), 
                        assaultParties[0].getElems()[0].getID(), assaultParties[0].getElems()[0].getPos(), assaultParties[0].getElems()[0].getCv(),
                        assaultParties[0].getElems()[1].getID(), assaultParties[0].getElems()[1].getPos(), assaultParties[0].getElems()[1].getCv(),
                        assaultParties[0].getElems()[2].getID(), assaultParties[0].getElems()[2].getPos(), assaultParties[0].getElems()[2].getCv(),
                        assaultParties[1].getRoom(), 
                        assaultParties[1].getElems()[0].getID(), assaultParties[1].getElems()[0].getPos(), assaultParties[1].getElems()[0].getCv(),
                        assaultParties[1].getElems()[1].getID(), assaultParties[1].getElems()[1].getPos(), assaultParties[1].getElems()[1].getCv(),
                        assaultParties[1].getElems()[2].getID(), assaultParties[1].getElems()[2].getPos(), assaultParties[1].getElems()[2].getCv(),
                        rooms[0].getPaintings(), rooms[0].getDistance(),
                        rooms[1].getPaintings(), rooms[1].getDistance(),
                        rooms[2].getPaintings(), rooms[2].getDistance(),
                        rooms[3].getPaintings(), rooms[3].getDistance(),
                        rooms[4].getPaintings(), rooms[4].getDistance()
                )
        );
        logger.print(stringBuilder.toString());
    }

    /**
     * Prints the tail of the logging file.
     * @param total number of paintings acquired
     */
    public void printTail(int total) {
        logger.print(String.format("My friends, tonight's effort produced %2d priceless paintings!\n", total));
        logger.close();
    }

    /**
     * Sets the Master Thief state.
     * @param state the state code to change to
     */
    public void setMasterThiefState(int state) {
        switch (state) {
            case MasterThief.PLANNING_THE_HEIST:
            masterThiefState = "PLAN";
            break;
            case MasterThief.DECIDING_WHAT_TO_DO:
            masterThiefState = "DECI";
            break;
            case MasterThief.ASSEMBLING_A_GROUP:
            masterThiefState = "AGRP";
            break;
            case MasterThief.WAITING_FOR_ARRIVAL:
            masterThiefState = "WAIT";
            break;
            case MasterThief.PRESENTING_THE_REPORT:
            masterThiefState = "PRES";
            break;
            default:
            break;
        }
        printState();
    }

    /**
     * Sets the Ordinary Thief state.
     * @param id the identification of the thief
     * @param state the state code to change to
     * @param maxDisplacement the maximum displacement of the thief
     */
    public void setOrdinaryThiefState(int id, int state, int maxDisplacement) {
        switch (state) {
            case OrdinaryThief.CONCENTRATION_SITE:
            ordinaryThieves[id].setState("CONC");
            break;
            case OrdinaryThief.COLLECTION_SITE:
            ordinaryThieves[id].setState("COLL");
            break;
            case OrdinaryThief.CRAWLING_INWARDS:
            ordinaryThieves[id].setState("CRIN");
            break;
            case OrdinaryThief.AT_A_ROOM:
            ordinaryThieves[id].setState("ROOM");
            break;
            case OrdinaryThief.CRAWLING_OUTWARDS:
            ordinaryThieves[id].setState("COUT");
            break;
        }
        ordinaryThieves[id].setSituation(getSituation(id));
        ordinaryThieves[id].setMaxDisplacement((char) (maxDisplacement + '0'));
        printState();
    }

    /**
     * Sets the Ordinary Thief state (version 2).
     * @param id the identification of the thief
     * @param state the state code to change to
     */
    public void setOrdinaryThiefState(int id, int state) {
        switch (state) {
            case OrdinaryThief.CONCENTRATION_SITE:
                ordinaryThieves[id].setState("CONC");
                break;
            case OrdinaryThief.COLLECTION_SITE:
                ordinaryThieves[id].setState("COLL");
                break;
            case OrdinaryThief.CRAWLING_INWARDS:
                ordinaryThieves[id].setState("CRIN");
                break;
            case OrdinaryThief.AT_A_ROOM:
                ordinaryThieves[id].setState("ROOM");
                break;
            case OrdinaryThief.CRAWLING_OUTWARDS:
                ordinaryThieves[id].setState("COUT");
                break;
        }
        ordinaryThieves[id].setSituation(getSituation(id));
        printState();
    }

    /**
     * Sets the Assault Party room target.
     * @param party the party number
     * @param room the room identification
     */
    public void setAssaultPartyRoom(int party, int room) {
        assaultParties[party].setRoom((char) (room + 1 + '0'));
        printState();
    }

    /**
     * Sets an Assault Party member.
     * @param party the party number
     * @param thief the identification of the thief
     * @param pos the present position of the thief
     * @param cv 1 if the thief is carrying a canvas, 0 otherwise
     */
    public void setAssaultPartyMember(int party, int thief, int pos, int cv) {
        AssaultPartyElemLogging[] elems = assaultParties[party].getElems();
        int idx = 0;
        for (int i = elems.length - 1; i >= 0; i--) {
            if (elems[i].getID() == (char) (thief + 1 + '0')) {
                elems[i].setPos(Integer.toString(pos));
                elems[i].setCv((char) (cv + '0'));
                printState();
                return;
            }
            if (elems[i].getID() == '-') {
                idx = i;
            }
        }
        elems[idx].setID((char) ((thief + 1) + '0'));
        elems[idx].setPos(Integer.toString(pos));
        elems[idx].setCv((char) (cv + '0'));
        printState();
    }

    /**
     * Removes an Assault Party member.
     * @param party the identification of the Assault Party
     * @param thief the identification of the Ordinary Thief
     */
    public void removeAssaultPartyMember(int party, int thief) {
        AssaultPartyElemLogging[] elems = assaultParties[party].getElems();
        for (AssaultPartyElemLogging elem : elems) {
            if (elem.getID() == (char) (thief + 1 + '0')) {
                elem.setID('-');
                elem.setPos("--");
                elem.setCv('-');
                printState();
                return;
            }
        }
    }

    /**
     * Resets the Assault Party logging details.
     * @param party the party number
     */
    public void disbandAssaultParty(int party) {
        assaultParties[party].setRoom('-');
        AssaultPartyElemLogging[] elems = assaultParties[party].getElems();
        for (AssaultPartyElemLogging elem : elems) {
            elem.setID('-');
            elem.setPos("--");
            elem.setCv('-');
        }
        printState();
    }

    /**
     * Sets the room state.
     * @param id the room identification
     * @param paintings the number of paintings
     * @param distance the distance to the outside gathering site
     */
    public void setRoomState(int id, int paintings, int distance) {
        rooms[id].setPaintings(paintings);
        rooms[id].setDistance(distance);
        printState();
    }

    /**
     * Sets the room state.
     * @param id the room identification
     * @param paintings the number of paintings
     */
    public void setRoomState(int id, int paintings) {
        setRoomState(id, paintings, rooms[id].getDistance());
    }

    /**
     * Sets the initial room states.
     * @param paintings an array with the number of paintings of each room
     * @param distances an array with the distance to each room
     */
    public void setInitialRoomStates(int[] paintings, int[] distances) {
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i].setDistance(distances[i]);
            this.rooms[i].setPaintings(paintings[i]);
        }
        printState();
    }

    /**
     * Sets the initial attributes of the thieves.
     * @param maxDisplacements an array with the maximum displacements of the Ordinary Thieves, where the index of each
     *                         value is the identification of the thief
     * @throws RemoteException if the execution of the remote code failed
     */
    @Override
    public void setInitialThieves(int[] maxDisplacements) throws RemoteException {
        this.masterThiefState = "PLAN";
        for (int i = 0; i < maxDisplacements.length; i++) {
            this.ordinaryThieves[i].setState("CONC");
            this.ordinaryThieves[i].setSituation('W');
            this.ordinaryThieves[i].setMaxDisplacement((char) (maxDisplacements[i] + '0'));
        }
        printState();
    }

    /**
     * Sends the shutdown signal to the General Repository.
     */
    public synchronized void shutdown() {
        GeneralRepositoryMain.shutdown();
    }

    /**
     * Returns the situation of a given Ordinary Thief
     * @param ordinaryThief the identification of the Ordinary Thief
     * @return 'P' if the Ordinary Thief is in a party, 'W' otherwise
     */
    private char getSituation(int ordinaryThief) {
        String ordinaryThiefState = ordinaryThieves[ordinaryThief].getState();
        return (ordinaryThiefState.equals("CONC") || ordinaryThiefState.equals("COLL")) ? 'W' : 'P';
    }
}
