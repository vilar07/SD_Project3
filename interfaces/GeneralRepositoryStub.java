package interfaces;

import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;

/**
 * The General Repository, where all information is stored and logged.
 */
public class GeneralRepositoryStub {
    /**
     * Name of the machine where the General Repository resides.
     */
    private final String hostName;

    /**
     * Port number where the General Repository is listening.
     */
    private final int portNumber;

    /**
     * GeneralRepositoryStub constructor 1, hostName is localhost.
     * @param portNumber the port number.
     */
    public GeneralRepositoryStub(int portNumber) {
        this.portNumber = portNumber;
        this.hostName = "localhost";
    }

    /**
     * GeneralRepositoryStub constructor 2.
     * @param hostName the host name.
     * @param portNumber the port number.
     */
    public GeneralRepositoryStub(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Prints the state of the simulation to the logging file.
     */
    public void printState() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.PRINT_STATE);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.PRINT_STATE_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Prints the tail of the logging file.
     * @param total number of paintings acquired.
     */
    public void printTail(int total) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.PRINT_TAIL, total);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.PRINT_TAIL_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the Master Thief state.
     * @param state the state code to change to.
     */
    public void setMasterThiefState(int state) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_MASTER_STATE, state);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_MASTER_STATE_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the Ordinary Thief state.
     * @param id the identification of the thief.
     * @param state the state code to change to.
     * @param situation the situation of the thief.
     * @param maxDisplacement the maximum displacement of the thief.
     */
    public void setOrdinaryThiefState(int id, int state, char situation, int maxDisplacement) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_ORDINARY_STATE, id, state, situation, maxDisplacement);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_ORDINARY_STATE_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the Assault Party room target.
     * @param party the party number.
     * @param room the room identification.
     */
    public void setAssaultPartyRoom(int party, int room) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_ASSAULT_PARTY_ROOM, room, party);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_ASSAULT_PARTY_ROOM_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets an Assault Party member.
     * @param party the party number.
     * @param thief the identification of the thief.
     * @param pos the present position of the thief.
     * @param cv 1 if the thief is carrying a canvas, 0 otherwise.
     */
    public void setAssaultPartyMember(int party, int thief, int pos, int cv) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_ASSAULT_PARTY_MEMBER, thief, pos, cv, party);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_ASSAULT_PARTY_MEMBER_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Removes an Assault Party member.
     * @param party the identification of the Assault Party.
     * @param thief the identification of the Ordinary Thief.
     */
    public void removeAssaultPartyMember(int party, int thief) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.REMOVE_ASSAULT_PARTY_MEMBER, thief, party);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.REMOVE_ASSAULT_PARTY_MEMBER_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Resets the Assault Party logging details.
     * @param party the party number.
     */
    public void disbandAssaultParty(int party) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.DISBAND_ASSAULT_PARTY, party);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.DISBAND_ASSAULT_PARTY_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the room state.
     * @param id the room identification.
     * @param paintings the number of paintings.
     * @param distance the distance to the outside gathering site.
     */
    public void setRoomState(int id, int paintings, int distance) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_ROOM_STATE, id, paintings, distance);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_ROOM_STATE_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the room state.
     * @param id the room identification.
     * @param paintings the number of paintings.
     */
    public void setRoomState(int id, int paintings) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_ROOM_STATE_LESS_ARGS, id, paintings);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_ROOM_STATE_LESS_ARGS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sets the initial room states.
     * @param paintings an array with the number of paintings of each room.
     * @param distances an array with the distance to each room.
     */
    public void setInitialRoomStates(int[] paintings, int[] distances) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SET_INITIAL_ROOM_STATES, paintings, distances);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SET_INITIAL_ROOM_STATES_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     * Sends the shutdown signal to the General Repository.
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
}
