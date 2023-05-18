package interfaces;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import utils.Constants;

/**
 * Concentration Site where ordinary thieves wait for orders.
 */
public class ConcentrationSiteInterface {
    /**
     * Name of the machine where the Concentration Site resides.
     */
    private final String hostName;

    /**
     * Port number where the Concentration Site is listening.
     */
    private final int portNumber;

    /**
     * ConcentrationSiteStub constructor 1, hostName is localhost.
     * @param portNumber the port number.
     */
    public ConcentrationSiteInterface(int portNumber) {
        this.portNumber = portNumber;
        this.hostName = "localhost";
    }

    /**
     * ConcentrationSiteStub constructor 2.
     * @param hostName the host name.
     * @param portNumber the port number.
     */
    public ConcentrationSiteInterface(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Called by the master thief, when enough ordinary thieves are available and there is still a
     * room with paintings
     * - Synchronization point between Master Thief and every Ordinary Thief constituting the Assault Party.
     * @param assaultParty the Assault Party identification.
     */
    public void prepareAssaultParty(int assaultParty) {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.PREPARE_ASSAULT_PARTY, ((MasterThief) Thread.currentThread()).getMasterThiefState(),
                assaultParty);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.PREPARE_ASSAULT_PARTY_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getMasterThiefState() != MasterThief.ASSEMBLING_A_GROUP) {
            System.out.println("Invalid Master Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        ((MasterThief) Thread.currentThread()).setState(inMessage.getMasterThiefState());
    }

    /**
     * The Master Thief announces the end of operations
     * and shares the number of paintings acquired in the heist.
     */
    public void sumUpResults() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.SUM_UP_RESULTS, ((MasterThief) Thread.currentThread()).getMasterThiefState());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.SUM_UP_RESULTS_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getMasterThiefState() != MasterThief.PRESENTING_THE_REPORT) {
            System.out.println("Invalid Master Thief state!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        ((MasterThief) Thread.currentThread()).setState(inMessage.getMasterThiefState());
    }

    /**
     * Called by an ordinary thief to wait for orders.
     * @return true if needed, false otherwise.
     */
    public boolean amINeeded() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.AM_I_NEEDED, ((OrdinaryThief) Thread.currentThread()).getID(),
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState(),
                ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.AM_I_NEEDED_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefState() != OrdinaryThief.CONCENTRATION_SITE) {
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
        return inMessage.neededThief();
    }

    /**
     * Ordinary Thief waits for the Master Thief to dispatch the designed Assault Party.
     * @return the Assault Party identification.
     */
    public int prepareExcursion() {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(hostName, portNumber);
        while (!com.open()) {
            try {
                Thread.sleep((long) 10);
            } catch (InterruptedException ignored) {}
        }
        outMessage = new Message(MessageType.PREPARE_EXCURSION, ((OrdinaryThief) Thread.currentThread()).getID(),
                ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState(),
                ((OrdinaryThief) Thread.currentThread()).getMaxDisplacement());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.PREPARE_EXCURSION_DONE) {
            System.out.println("Invalid message type!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryThiefState() != ((OrdinaryThief) Thread.currentThread()).getOrdinaryThiefState()) {
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
        if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
            System.out.println("Invalid Assault Party!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        com.close();
        return inMessage.getAssaultParty();
    }

    /**
     * Sends the signal to the Concentration Site.
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
