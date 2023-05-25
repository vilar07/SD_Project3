package serverSide.objects;

import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import interfaces.AssaultPartyInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.ReturnBoolean;
import serverSide.main.AssaultParty0Main;
import serverSide.main.AssaultParty1Main;
import utils.Constants;
import utils.Room;

/**
 * Assault Party is constituted by Ordinary Thieves that are going to attack the museum.
 */
public class AssaultParty implements AssaultPartyInterface {
    /**
     * List with the identifications of the thieves in the party.
     */
    private final List<Integer> thieves;

    /**
     * Identification number of the Assault Party.
     */
    private final int id;

    /**
     * Room target of the Assault Party, contains identification of the room and the distance to it.
     */
    private Room room;

    /**
     * Boolean value which is true if the Assault Party is operating or false if it is not.
     */
    private boolean inOperation;

    /**
     * Number of Ordinary Thieves ready to crawl out/reverse direction.
     */
    private int thievesReadyToReverse;

    /**
     * Identification of the next Ordinary Thief in the line to crawl.
     */
    private int nextThiefToCrawl;

    /**
     * Map that holds the positions of the Ordinary Thieves.
     * The key is the identification of the thief, the value is their position in the line to the room/exit.
     */
    private final Map<Integer, Integer> thiefPositions;

    /**
     * Map that holds whether the Ordinary Thieves have a canvas.
     * The key is the identification of the thief, the value is true if they have a canvas, false if not.
     */
    private final Map<Integer, Boolean> thiefCanvas;

    /**
     * The General Repository, where logging happens.
     */
    private final GeneralRepositoryInterface generalRepositoryStub;

    /**
     * Enumerate for the situation of the Ordinary Thief in the line, can be either front, mid or back.
     */
    private enum Situation {
        FRONT,
        MID,
        BACK
    }

    /**
     * Public constructor for the Assault Party shared region.
     * @param generalRepositoryStub the General Repository.
     */
    public AssaultParty(int id, GeneralRepositoryInterface generalRepositoryStub) {
        this.id = id;
        this.generalRepositoryStub = generalRepositoryStub;
        thieves = new LinkedList<>();
        room = null;
        inOperation = false;
        thievesReadyToReverse = 0;
        nextThiefToCrawl = -1;
        thiefPositions = new HashMap<>();
        thiefCanvas = new HashMap<>();
    }

    /**
     * Called by the Master Thief to send the Assault Party to the museum.
     * After that call, Assault Party can start crawling.
     * @return the state of the Master Thief after this operation.
     */
    public synchronized int sendAssaultParty() {
        while (thieves.size() < Constants.ASSAULT_PARTY_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException ignored) {}
        }
        inOperation = true;
        thievesReadyToReverse = 0;
        notifyAll();
        setMasterThiefState();
        return MasterThief.DECIDING_WHAT_TO_DO;
    }

    /**
     * Called by the Ordinary Thief to crawl in.
     * @return false if they have finished the crawling.
     */
    public synchronized ReturnBoolean crawlIn(int ordinaryThief, int maxDisplacement) {
        setOrdinaryThiefState(ordinaryThief, OrdinaryThief.CRAWLING_INWARDS, maxDisplacement);
        int roomDistance = room.getDistance();
        Situation situation;
        do {
            situation = whereAmI(ordinaryThief);
            int movement = 0;
            switch (situation) {
                case FRONT:
                movement = crawlFront(ordinaryThief, maxDisplacement);
                break;
                case MID:
                movement = crawlMid(ordinaryThief, maxDisplacement, true, roomDistance);
                break;
                case BACK:
                movement = crawlBack(ordinaryThief, maxDisplacement, true, roomDistance);
                break;
                default:
                break;
            }
            if (movement > 0) {
                thiefPositions.put(ordinaryThief, Math.min(thiefPositions.get(ordinaryThief) + movement, roomDistance));
                setAssaultPartyMember(ordinaryThief, thiefPositions.get(ordinaryThief),
                        thiefCanvas.get(ordinaryThief) ? 1 : 0);
                updateLineIn();
            } else {
                nextThiefToCrawl = getNextInLine(situation);
                notifyAll();
                while (nextThiefToCrawl != ordinaryThief) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {}
                }
            }
        } while (thiefPositions.get(ordinaryThief) < roomDistance);
        nextThiefToCrawl = getNextInLine(whereAmI(ordinaryThief));
        notifyAll();
        return new ReturnBoolean(false, OrdinaryThief.CRAWLING_INWARDS);
    }

    /**
     * Called to awake the first member in the line of Assault Party, by the last party member that rolled a canvas,
     * so that the Assault Party can crawl out
     * - Synchronization Point between members of the Assault Party.
     */
    public synchronized void reverseDirection() {
        thievesReadyToReverse++;
        while (thievesReadyToReverse < Constants.ASSAULT_PARTY_SIZE) {
            try {
                wait();
            } catch (InterruptedException ignored) {

            }
        }
        notifyAll();
    }

    /**
     * Called by the Ordinary Thief to crawl out.
     * @return false if they have finished the crawling.
     */
    public synchronized ReturnBoolean crawlOut(int ordinaryThief, int maxDisplacement) {
        setOrdinaryThiefState(ordinaryThief, OrdinaryThief.CRAWLING_OUTWARDS, maxDisplacement);
        Situation situation;
        do {
            situation = whereAmI(ordinaryThief);
            int movement = 0;
            switch (situation) {
                case FRONT:
                movement = crawlFront(ordinaryThief, maxDisplacement);
                break;
                case MID:
                movement = crawlMid(ordinaryThief, maxDisplacement, false, 0);
                break;
                case BACK:
                movement = crawlBack(ordinaryThief, maxDisplacement, false, 0);
                break;
                default:
                break;
            }
            if (movement > 0) {
                thiefPositions.put(ordinaryThief, Math.max(thiefPositions.get(ordinaryThief) - movement, 0));
                setAssaultPartyMember(ordinaryThief, thiefPositions.get(ordinaryThief),
                                                        thiefCanvas.get(ordinaryThief) ? 1 : 0);
                updateLineOut();
            } else {
                updateLineOut();
                nextThiefToCrawl = getNextInLine(situation);
                notifyAll();
                while (ordinaryThief != nextThiefToCrawl) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {}
                }
            }
        } while (thiefPositions.get(ordinaryThief) > 0);
        nextThiefToCrawl = getNextInLine(whereAmI(ordinaryThief));
        notifyAll();
        return new ReturnBoolean(false, OrdinaryThief.CRAWLING_OUTWARDS);
    }

    /**
     * Shuts down the Assault Party server.
     */
    public synchronized void shutdown () {
        if (id == 0) {
            AssaultParty0Main.shutdown();
        } else {
            AssaultParty1Main.shutdown();
        }
    }

    /**
     * Getter for the room destination.
     * @return the room identification.
     */
    public int getRoom() {
        if (room == null) {
            return -1;
        }
        return room.getID();
    }

    /**
     * Getter for the assault party identification.
     * @return the assault party number.
     */
    public int getID() {
        return id;
    }

    /**
     * Getter for the inOperation attribute.
     * @return true if Assault Party is operating, false otherwise.
     */
    public boolean isInOperation() {
        return inOperation;
    }

    /**
     * Setter for the inOperation attribute.
     * @param inOperation true if Assault Party is operating, false if not.
     */
    public void setInOperation(boolean inOperation) {
        this.inOperation = inOperation;
    }

    /**
     * Setter for the room destination.
     * @param room the room identification.
     * @param paintings the number of paintings in the room.
     * @param distance the distance to the room.
     */
    public void setRoom(int room, int paintings, int distance) {
        this.room = new Room(room, distance, paintings);
        setAssaultPartyRoom(room);
    }

    /**
     * Sets the members of the Assault Party.
     * @param thieves array with the Ordinary Thieves' identifications.
     */
    public void setMembers(int[] thieves) {
        this.thieves.clear();
        this.thiefPositions.clear();
        this.thiefCanvas.clear();
        this.inOperation = false;
        this.thievesReadyToReverse = 0;
        for (int thief: thieves) {
            this.thieves.add(thief);
            thiefPositions.put(thief, 0);
            thiefCanvas.put(thief, false);
            setAssaultPartyMember(thief, 0, 0);
        }
    }

    /**
     * Checks if given thief is in the Assault Party.
     * @param thief the Ordinary Thief identification.
     * @return true if they are part of the Assault Party, false otherwise.
     */
    public boolean isMember(int thief) {
        return thieves.contains(thief);
    }

    /**
     * Removes an Ordinary Thief from the Assault Party, if they are a member of it.
     * @param thief the Ordinary Thief.
     */
    public void removeMember(int thief) {
        if (this.thieves.contains(thief)) {
            this.thieves.remove((Integer) thief);
            removeAssaultPartyMember(thief);
        }
    }

    /**
     * Returns whether the Assault Party is empty, or still has Ordinary Thieves in action.
     * @return true if it is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.thieves.isEmpty();
    }

    /**
     * Sets if an Ordinary Thief has a canvas.
     * @param thief the identification of the Ordinary Thief.
     * @param canvas true if the thief has a canvas in its possession, false otherwise.
     */
    public void setBusyHands(int thief, boolean canvas) {
        this.thiefCanvas.put(thief, canvas);
    }

    /**
     * Returns whether an Ordinary Thief has a canvas.
     * @param thief the identification of the Ordinary Thief.
     * @return true if the thief has a canvas in its possession, false otherwise.
     */
    public boolean hasBusyHands(int thief) {
        return this.thiefCanvas.get(thief);
    }

    /**
     * Updates the order of the line when crawling in.
     */
    private void updateLineOut() {
        this.thieves.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer t1, Integer t2) {
                return thiefPositions.get(t1).compareTo(thiefPositions.get(t2));
            }
        });
    }

    /**
     * Updates the order of the line when crawling out.
     */
    private void updateLineIn() {
        this.thieves.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer t1, Integer t2) {
                return thiefPositions.get(t2).compareTo(thiefPositions.get(t1));
            }
        });
    }

    /**
     * Returns the maximum possible movement for the Ordinary Thief in the front.
     * @param ordinaryThief the Ordinary Thief in the front.
     * @return the maximum possible movement.
     */
    private int crawlFront(int ordinaryThief, int maxDisplacement) {
        int nextThief = getNextInLine(Situation.FRONT);
        int thiefSeparation = Math.abs(thiefPositions.get(ordinaryThief) - thiefPositions.get(nextThief));
        if (thiefSeparation > Constants.MAX_THIEF_SEPARATION) {
            return 0;
        }
        return Math.min(maxDisplacement, Constants.MAX_THIEF_SEPARATION - thiefSeparation);
    }

    /**
     * Returns the maximum possible movement for the Ordinary Thief in the middle.
     * @param ordinaryThief the Ordinary Thief in the middle.
     * @param in true if crawling in, false if crawling out.
     * @return the maximum possible movement.
     */
    private int crawlMid(int ordinaryThief, int maxDisplacement, boolean in, int goalPosition) {
        int frontThief = getPreviousInLine(Situation.MID);
        int backThief = getNextInLine(Situation.MID);
        int nextPosition;
        int position = thiefPositions.get(ordinaryThief);
        int frontPosition = thiefPositions.get(frontThief);
        int backPosition = thiefPositions.get(backThief);
        for (int displacement = maxDisplacement; displacement > 0; displacement--) {
            if (in) {
                nextPosition = position + displacement;
                if (Math.min(nextPosition - backPosition, frontPosition - backPosition) <= Constants.MAX_THIEF_SEPARATION
                        && nextPosition - frontPosition <= Constants.MAX_THIEF_SEPARATION
                        && (nextPosition != frontPosition || nextPosition == goalPosition)) {
                    return displacement;
                }
            } else {
                nextPosition = position - displacement;
                if (Math.min(backPosition - nextPosition, backPosition - frontPosition) <= Constants.MAX_THIEF_SEPARATION
                        && frontPosition - nextPosition <= Constants.MAX_THIEF_SEPARATION
                        && (nextPosition != frontPosition || nextPosition == goalPosition)) {
                    return displacement;
                }
            }
        }
        return 0;
    }

    /**
     * Returns the maximum possible movement for the Ordinary Thief in the back.
     * @param ordinaryThief the Ordinary Thief in the back.
     * @param in true if crawling in, false if crawling out.
     * @return the maximum possible movement.
     */
    private int crawlBack(int ordinaryThief, int maxDisplacement, boolean in, int goalPosition) {
        int frontThief = getNextInLine(Situation.BACK);
        int movement, nextPosition, frontThiefPosition = thiefPositions.get(frontThief), position = thiefPositions.get(ordinaryThief);
        int maxMovement = Constants.MAX_THIEF_SEPARATION + Math.abs(frontThiefPosition - position);
        if (thiefPositions.get(frontThief) != goalPosition) {
            int midThief = getPreviousInLine(Situation.BACK);
            for (int displacement = maxDisplacement; displacement > 0; displacement--) {
                movement = Math.min(displacement, maxMovement);
                if (in) {
                    nextPosition = position + movement;
                } else {
                    nextPosition = position - movement;
                }
                if (nextPosition != thiefPositions.get(frontThief) && 
                        (nextPosition != thiefPositions.get(midThief) || nextPosition == goalPosition)) {
                    return movement;
                }
            }
        } else {
            for (int displacement = maxDisplacement; displacement > 0; displacement--) {
                movement = Math.min(displacement, maxMovement);
                if (in) {
                    nextPosition = position + movement;
                } else {
                    nextPosition = position - movement;
                }
                if (nextPosition == goalPosition || nextPosition != frontThiefPosition) {
                    return movement;
                }
            }
        }
        return 0;
    }

    /**
     * Returns the next Ordinary Thief in line to crawl.
     * @param situation the situation of the current Ordinary Thief.
     * @return the identification of the next Ordinary Thief.
     */
    private int getPreviousInLine(Situation situation) {
        switch (situation) {
            case FRONT:
            return this.thieves.get(this.thieves.size() - 1);
            case MID:
            return this.thieves.get(0);
            case BACK:
            if (this.thieves.size() == 2) {
                return this.thieves.get(0);
            }
            return this.thieves.get(1);
            default:
            return -1;
        }
    }

    /**
     * Returns the next Ordinary Thief in line to crawl.
     * @param situation the situation of the current Ordinary Thief.
     * @return the identification of the next Ordinary Thief.
     */
    private int getNextInLine(Situation situation) {
        switch (situation) {
            case FRONT:
            if (this.thieves.size() == 1) {
                return this.thieves.get(0);
            }
            return this.thieves.get(1);
            case MID:
            return this.thieves.get(2);
            case BACK:
            return this.thieves.get(0);
            default:
            return -1;
        }
    }

    /**
     * Returns the situation of the Ordinary Thief in the line of the crawling. The thief can be either in front, mid or back.
     * @param currentThief the identification of the Ordinary Thief.
     * @return the situation (FRONT, MID or BACK).
     */
    private Situation whereAmI(int currentThief) {
        if (currentThief == this.thieves.get(0)) {
            return Situation.FRONT;
        }
        if (currentThief == this.thieves.get(this.thieves.size() - 1)) {
            return Situation.BACK;
        }
        return Situation.MID;
    }
    
    private void setMasterThiefState() {
        try {
            generalRepositoryStub.setMasterThiefState(MasterThief.DECIDING_WHAT_TO_DO);
        } catch (RemoteException e) {
            System.out.println("Remote exception on AssaultParty.setMasterThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setOrdinaryThiefState(int ordinaryThief, int state, int maxDisplacement) {
        try {
            generalRepositoryStub.setOrdinaryThiefState(ordinaryThief, state, maxDisplacement);
        } catch (RemoteException e) {
            System.out.println("Remote exception on AssaultParty.setOrdinaryThiefState: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setAssaultPartyMember(int ordinaryThief, int position, int canvas) {
        try {
            generalRepositoryStub.setAssaultPartyMember(this.id, ordinaryThief, position, canvas);
        } catch (RemoteException e) {
            System.out.println("Remote exception on AssaultParty.setAssaultPartyMember: " + e.getMessage());
            System.exit(1);
        }
    }

    private void setAssaultPartyRoom(int room) {
        try {
            generalRepositoryStub.setAssaultPartyRoom(this.id, room);
        } catch (RemoteException e) {
            System.out.println("Remote exception on AssaultParty.setAssaultPartyRoom: " + e.getMessage());
            System.exit(1);
        }
    }

    private void removeAssaultPartyMember(int ordinaryThief) {
        try {
            generalRepositoryStub.removeAssaultPartyMember(this.id, ordinaryThief);
        } catch (RemoteException e) {
            System.out.println("Remote exception on AssaultParty.removeAssaultPartyMember: " + e.getMessage());
            System.exit(1);
        }
    }
}
