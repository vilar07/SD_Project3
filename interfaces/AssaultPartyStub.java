package interfaces;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import utils.Constants;

/**
 *  Stub to the Assault Party.
 * <p>
 *    It instantiates a remote reference to the Assault Party.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultPartyStub {
    /**
     * Name of the machine where the Assault Party is located.
     */
    private final String hostName;

    /**
     * Port number where the Assault Party is listening.
     */
    private final int portNumber;

    /**
     * Identification of the Assault Party.
     */
    private final int id;

    /**
     * AssaultPartyStub constructor 1, assumes hostName is localhost.
     * @param id the identification of the Assault Party.
     * @param portNumber the port number.
     */
    public AssaultPartyStub(int id, int portNumber) {
        this.id = id;
        this.portNumber = portNumber;
        this.hostName = "localhost";
    }

    /**
     * AssaultPartyStub constructor 2.
     * @param id the identification of the Assault Party.
     * @param hostName the host name.
     * @param portNumber the port number.
     */
    public AssaultPartyStub(int id, String hostName, int portNumber) {
        this.id = id;
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Getter for the identification of the Assault Party.
     * @return the identification of the Assault Party.
     */
    public int getID() {
        return this.id;
    }

    /**
     * Called by the Master Thief to send the Assault Party to the museum.
     * After that call, Assault Party can start crawling.
     */
    public void sendAssaultParty() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SEND_ASSAULT_PARTY, ((MasterThief) Thread.currentThread()).getMasterThiefState(), this.id);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SEND_ASSAULT_PARTY_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getMasterThiefState() != MasterThief.DECIDING_WHAT_TO_DO) {
            System.out.println("Invalid Master Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        ((MasterThief) Thread.currentThread()).setState(inMessage.getMasterThiefState());
    }

    /**
     * Called by the Ordinary Thief to crawl in.
     * @return false if they have finished the crawling.
     */
    public boolean crawlIn() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.CRAWL_IN, ((OrdinaryThief) Thread.currentThread()).getID(), 
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState(),
                ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement(),
                this.id);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.CRAWL_IN_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefID() != ((OrdinaryThief) Thread.currentThread()).getID()) {
            System.out.println("Invalid Ordinary Thief ID!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefState() != OrdinaryThief.CRAWLING_INWARDS) {
            System.out.println("Invalid Ordinary Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefMD() != ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement()) {
            System.out.println("Invalid Ordinary Thief maximum displacement!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        ((OrdinaryThief) Thread.currentThread()).setState(inMessage.getOrdinaryThiefState());
        return false;
    }

    /**
     * Called to awake the first member in the line of Assault Party, by the last party member that rolled a canvas,
     * so that the Assault Party can crawl out.
     * - Synchronization Point between members of the Assault Party.
     */
    public void reverseDirection() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.REVERSE_DIRECTION, ((OrdinaryThief) Thread.currentThread()).getID(), 
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState(),
                ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement(),
                this.id);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.REVERSE_DIRECTION_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Called by the Ordinary Thief to crawl out.
     * @return false if they have finished the crawling.
     */
    public boolean crawlOut() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.CRAWL_OUT, ((OrdinaryThief) Thread.currentThread()).getID(), 
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState(),
                ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement(),
                this.id);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.CRAWL_OUT_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefID() != ((OrdinaryThief) Thread.currentThread()).getID()) {
            System.out.println("Invalid Ordinary Thief ID!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefState() != OrdinaryThief.CRAWLING_OUTWARDS) {
            System.out.println("Invalid Ordinary Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefMD() != ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement()) {
            System.out.println("Invalid Ordinary Thief maximum displacement!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        ((OrdinaryThief) Thread.currentThread()).setState(inMessage.getOrdinaryThiefState());
        return false;
    }

    /**
     * Sends the shutdown signal to the Assault Party.
     */
    public void shutdown() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 1000);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SHUTDOWN);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SHUTDOWN_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }
 
    /**
     * Getter for the room destination.
     * @return the room identification.
     */
    public int getRoom() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.GET_ROOM);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GET_ROOM_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getRoom() < -1 || inMessage.getRoom() > Constants.NUM_ROOMS) {
            System.out.println("Invalid room xd!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getRoom();
    }

    /**
     * Setter for the busy hands attribute of a thief.
     * @param ordinaryThiefID the identification of the Ordinary Thief.
     * @param res true if they have a canvas, false otherwise.
     */
    public void setBusyHands(int ordinaryThiefID, boolean res) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_BUSY_HANDS, ordinaryThiefID, res);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_BUSY_HANDS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Getter for the busy hands attribute of a thief.
     * @param ordinaryThief the identification of the Ordinary Thief.
     * @return whether or not they have a canvas.
     */
    public boolean hasBusyHands(int ordinaryThief) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.HAS_BUSY_HANDS, ordinaryThief);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.HAS_BUSY_HANDS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefCanvas() < 0 || inMessage.getOrdinaryThiefCanvas() > 1) {
            System.out.println("Invalid Ordinary Thief number of canvas!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getOrdinaryThiefCanvas() == 1;
    }

    /**
     * Removes a member from the Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief.
     */
    public void removeMember(int ordinaryThief) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.REMOVE_MEMBER, ordinaryThief);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.REMOVE_MEMBER_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Returns whether the Assault Party has no members.
     * @return true if it is empty, false otherwise.
     */
    public boolean isEmpty() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.IS_ASSAULT_PARTY_EMPTY);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.IS_ASSAULT_PARTY_EMPTY_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.isAssaultPartyEmpty();
    }

    /**
     * Sets the Assault Party in operation.
     * @param inOperation true if the Assault Party is in operation, false otherwise.
     */
    public void setInOperation(boolean inOperation) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_IN_OPERATION, inOperation);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_IN_OPERATION_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the members of the Assault Party.
     * @param ordinaryThieves an array with the identification of the Ordinary Thieves.
     */
    public void setMembers(int[] ordinaryThieves) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_MEMBERS, ordinaryThieves);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_MEMBERS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Getter for if the Assault Party is in operation.
     * @return true if it is in operation, false otherwise.
     */
    public boolean isInOperation() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.IS_ASSAULT_PARTY_IN_OPERATION);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.IS_ASSAULT_PARTY_IN_OPERATION_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.isAssaultPartyInOperation();
    }

    /**
     * Checks if a given thief is a member of the Assault Party.
     * @param ordinaryThief the identification of the Ordinary Thief.
     * @return true if affirmative, false otherwise.
     */
    public boolean isMember(int ordinaryThief) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.IS_MEMBER, ordinaryThief);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.IS_MEMBER_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.isMember();
    }

    /**
     * Sets the room destination of the Assault Party.
     * @param room the room identification.
     * @param roomDistance the distance to the room.
     * @param roomPaintings the number of paintings in the room.
     */
    public void setRoom(int room, int roomDistance, int roomPaintings) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_ROOM, room, roomPaintings, roomDistance);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_ROOM_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }
}
