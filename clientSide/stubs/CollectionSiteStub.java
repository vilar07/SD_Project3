package clientSide.stubs;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import utils.Constants;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;

/**
 * Collection Site where Master Thief intelligence and paintings are stored.
 */
public class CollectionSiteStub {
    /**
     * Name of the machine where the Collection Site resides.
     */
    private final String hostName;

    /**
     * Port number where the Collection Site is listening.
     */
    private final int portNumber;

    /**
     * CollectionSiteStub constructor 1, hostName is localhost.
     * @param portNumber the port number.
     */
    public CollectionSiteStub(int portNumber) {
        this.portNumber = portNumber;
        this.hostName = "localhost";
    }

    /**
     * CollectionSiteStub constructor 2.
     * @param hostName the host name.
     * @param portNumber the port number.
     */
    public CollectionSiteStub(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Called by Master Thief to initiate operations.
     */
    public void startOperations() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.START_OPERATIONS, ((MasterThief) Thread.currentThread()).getMasterThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.START_OPERATIONS_DONE) {
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
     * Called by Master Thief to appraise situation: either to take a rest, prepare assault party or
     * sum up results.
     * @return next situation.
     */
    public char appraiseSit() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.APPRAISE_SIT, ((MasterThief) Thread.currentThread()).getMasterThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.APPRAISE_SIT_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOperation() != 'E' && inMessage.getOperation() != 'R' && inMessage.getOperation() != 'P') {
            System.out.println("Invalid operation!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getOperation();
    }

    /**
     * Master Thief waits while there are still Assault Parties in operation.
     */
    public void takeARest() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.TAKE_A_REST, ((MasterThief) Thread.currentThread()).getMasterThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.TAKE_A_REST_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getMasterThiefState() != MasterThief.WAITING_FOR_ARRIVAL) {
            System.out.println("Invalid Master Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        ((MasterThief) Thread.currentThread()).setState(inMessage.getMasterThiefState());
    }

    /**
     * Called by Master Thief to collect all available canvas
     * - Synchronization point between Master Thief and each individual Ordinary Thief with a canvas.
     */
    public void collectACanvas() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.COLLECT_A_CANVAS, ((MasterThief) Thread.currentThread()).getMasterThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.COLLECT_A_CANVAS_DONE) {
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
     * Called by the Ordinary Thief to hand a canvas to the Master Thief if they have any
     * - Synchronization point between each Ordinary Thief and the Master Thief.
     * @param party the identification of the Assault Party the thief belongs to.
     */
    public void handACanvas(int party) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.HAND_A_CANVAS, 
                ((OrdinaryThief) Thread.currentThread()).getID(),
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState(),
                ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement(),
                party);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.HAND_A_CANVAS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefState() != OrdinaryThief.COLLECTION_SITE) {
            System.out.println("Invalid Ordinary Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefID() != ((OrdinaryThief) Thread.currentThread()).getID()) {
            System.out.println("Invalid Ordinary Thief ID!");
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
    }

    /**
     * Get the number of the next Assault Party and remove it from the queue.
     * @return the Assault Party identification.
     */
    public int getNextAssaultPartyID() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.GET_NEXT_ASSAULT_PARTY_ID, ((MasterThief) Thread.currentThread()).getMasterThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GET_NEXT_ASSAULT_PARTY_ID_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
            System.out.println("Invalid Assault Party ID!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getAssaultParty();
    }

    /**
     * Sends the shutdown signal to the Collection Site.
     */
    public void shutdown() {
        // use Socket setSoTimeout() to raise a SocketTimeoutException somehow
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
     * Gets the next room which is not empty.
     * @return the identification of the next room.
     */
    public int getNextRoom() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.GET_NEXT_ROOM);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GET_NEXT_ROOM_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getRoom() < -1 || inMessage.getRoom() > Constants.NUM_ROOMS) {
            System.out.println("Invalid room!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getRoom();
    }

    /**
     * Gets the number of paintings for a given room.
     * @param room the room identification.
     * @return the number of paintings.
     */
    public int getRoomPaintings(int room) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.GET_ROOM_PAINTINGS_COLLECTION_SITE, room);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GET_ROOM_PAINTINGS_COLLECTION_SITE_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getPaintings() > Constants.MAX_PAINTINGS) {
            System.out.println("Invalid number of paintings!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getPaintings();
    }

    /**
     * Gets the distance to a room.
     * @param room the room identification.
     * @return the distance to the room.
     */
    public int getRoomDistance(int room) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.GET_ROOM_DISTANCE_COLLECTION_SITE, room);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GET_ROOM_DISTANCE_COLLECTION_SITE_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getDistance() < Constants.MIN_ROOM_DISTANCE || inMessage.getDistance() > Constants.MAX_ROOM_DISTANCE) {
            System.out.println("Invalid room distance!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getDistance();
    }

    /**
     * Getter for the total number of paintings acquired.
     * @return the total number of paintings.
     */
    public int getPaintings() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.GET_TOTAL_PAINTINGS);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.GET_TOTAL_PAINTINGS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getPaintings() < 0) {
            System.out.println("Invalid total paintings!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getPaintings();
    }
}
