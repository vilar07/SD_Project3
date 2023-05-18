package serverSide.interfaces;

import clientSide.entities.OrdinaryThief;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.MuseumProxyAgent;
import serverSide.sharedRegions.Museum;
import utils.Constants;

/**
 * Interface which provides communication to and from the Museum.
 */
public class MuseumInterface {
    /**
     * The Museum shared region.
     */
    private final Museum museum;

    /**
     * MuseumInterface constructor.
     * @param museum the Museum.
     */
    public MuseumInterface(Museum museum) {
        this.museum = museum;
    }

    /**
     * Processes incoming messages and replies to the sender or throws an exception if an error occurred or the message
     * parameters were invalid.
     * @param inMessage the incoming message.
     * @return reply message.
     * @throws MessageException exception containing a short explanation and the incoming message.
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;
        switch (inMessage.getMsgType()) {
            case MessageType.ROLL_A_CANVAS:
                if (inMessage.getOrdinaryThiefState() != OrdinaryThief.CRAWLING_INWARDS) {
                    throw new MessageException("Invalid Ordinary Thief state - should be CRAWLING_INWARDS!", inMessage);
                } else if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() >= Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() >= Constants.NUM_THIEVES) {
                    throw new MessageException("Invalid Ordinary Thief identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefMD() < Constants.MIN_THIEF_DISPLACEMENT || inMessage.getOrdinaryThiefMD() > Constants.MAX_THIEF_DISPLACEMENT) {
                    throw new MessageException("Invalid Ordinary Thief maximum displacement!", inMessage);
                }
                ((MuseumProxyAgent) Thread.currentThread()).setOrdinaryThiefID(inMessage.getOrdinaryThiefID());
                ((MuseumProxyAgent) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
                ((MuseumProxyAgent) Thread.currentThread()).setOrdinaryThiefMaxDisplacement(inMessage.getOrdinaryThiefMD());
                museum.rollACanvas(inMessage.getAssaultParty());
                outMessage = new Message(MessageType.ROLL_A_CANVAS_DONE, inMessage.getOrdinaryThiefID(), ((MuseumProxyAgent) Thread.currentThread()).getOrdinaryThiefState(),
                        ((MuseumProxyAgent) Thread.currentThread()).getOrdinaryThiefMaxDisplacement());
                break;
            case MessageType.SHUTDOWN:
                museum.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
            case MessageType.GET_ROOM_DISTANCE_MUSEUM:
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in GET_ROOM_DISTANCE_MUSEUM!", inMessage);
                }
                outMessage = new Message(MessageType.GET_ROOM_DISTANCE_MUSEUM_DONE, museum.getRoomDistance(inMessage.getRoom()));
                break;
            case MessageType.GET_ROOM_PAINTINGS_MUSEUM:
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in GET_ROOM_DISTANCE_MUSEUM!", inMessage);
                }
                outMessage = new Message(MessageType.GET_ROOM_PAINTINGS_MUSEUM_DONE, museum.getRoomPaintings(inMessage.getRoom()));
                break;
        }
        return outMessage;
    }
}
