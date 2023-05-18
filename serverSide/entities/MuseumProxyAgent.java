package serverSide.entities;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import serverSide.interfaces.MasterThiefClone;
import serverSide.interfaces.MuseumInterface;
import serverSide.interfaces.OrdinaryThiefClone;

/**
 * Proxy Agent for the Museum, inherits the attributes and methods of both Master Thief and Ordinary Thief.
 */
public class MuseumProxyAgent extends Thread implements MasterThiefClone, OrdinaryThiefClone {
     /**
    *  Communication channel.
    */
    private final ServerCom serverCom;

    /**
     * State of the Ordinary Thief.
     */
    private int ordinaryThiefState;

    /**
     * State of the Master Thief.
     */
    private int masterThiefState;

    /**
     * Identification of the Ordinary Thief.
     */
    private int ordinaryThiefID;

    /**
     * Maximum displacement of the Ordinary Thief.
     */
    private int ordinaryThiefMaxDisplacement;

    /**
     * Interface to the Museum.
     */
    private final MuseumInterface museumInterface;

    /**
     * MuseumProxyAgent constructor.
     * @param serverCom the server communication channel.
     * @param museumInterface the interface to the Museum.
     */
    public MuseumProxyAgent(ServerCom serverCom, MuseumInterface museumInterface) {
        this.serverCom = serverCom;
        this.museumInterface = museumInterface;
    }

    /**
     * Getter for the identification of the Ordinary Thief.
     * @return the identification of the thief.
     */
    @Override
    public int getOrdinaryThiefID() {
        return ordinaryThiefID;
    }

    /**
     * Getter for the maximum displacement of the Ordinary Thief.
     * @return the maximum displacement of the thief.
     */
    @Override
    public int getOrdinaryThiefMaxDisplacement() {
        return ordinaryThiefMaxDisplacement;
    }

    /**
     * Getter for the state of the Ordinary Thief.
     * @return the state of the thief.
     */
    public int getOrdinaryThiefState() {
        return this.ordinaryThiefState;
    }

    /**
     * Getter for the state of the Master Thief.
     * @return the state of the thief.
     */
    public int getMasterThiefState() {
        return this.masterThiefState;
    }

    /**
     * Returns the situation of the Ordinary Thief.
     * @return 'W' if waiting or 'P' if in a party.
     */
    public char getOrdinaryThiefSituation() {
        return (ordinaryThiefState == CONCENTRATION_SITE || ordinaryThiefState == COLLECTION_SITE) ? 'W' : 'P';
    }

    /**
     * Setter for the identification of the Ordinary Thief.
     * @param id the identification of the thief.
     */
    public void setOrdinaryThiefID(int id) {
        this.ordinaryThiefID = id;
    }

    /**
     * Setter for the state of the Ordinary Thief.
     * @param state the state of the thief.
     */
    @Override
    public void setOrdinaryThiefState(int state) {
        this.ordinaryThiefState = state;
    }

    /**
     * Setter for the maximum displacement of the Ordinary Thief.
     * @param maxDisplacement the maximum displacement of the thief.
     */
    public void setOrdinaryThiefMaxDisplacement(int maxDisplacement) {
        this.ordinaryThiefMaxDisplacement = maxDisplacement;
    }

    /**
     * Setter for the state of the Master Thief.
     * @param state the state of the thief.
     */
    @Override
    public void setMasterThiefState(int state) {
        this.masterThiefState = state;
    }

    /**
     * Lifecycle of the MuseumProxyAgent.
     */
    @Override
    public void run() {
        Message inMessage = null, outMessage = null;
        inMessage = (Message) serverCom.readObject();
        try {
            outMessage = museumInterface.processAndReply(inMessage);
        } catch (MessageException e) {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit (1);
        }
        serverCom.writeObject(outMessage);
        serverCom.close();
    }
}
