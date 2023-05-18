package serverSide.interfaces;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.CollectionSiteProxyAgent;
import serverSide.sharedRegions.CollectionSite;
import utils.Constants;

/**
 * Interface which provides communication to and from the Collection Site.
 */
public class CollectionSiteInterface {
    /**
     * The Collection Site shared region.
     */
    private final CollectionSite collectionSite;

    /**
     * CollectionSiteInterface constructor.
     * @param collectionSite the Collection Site.
     */
    public CollectionSiteInterface(CollectionSite collectionSite) {
        this.collectionSite = collectionSite;
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
            case MessageType.START_OPERATIONS:
                if (inMessage.getMasterThiefState() != MasterThief.PLANNING_THE_HEIST) {
                    throw new MessageException("Invalid Master Thief state - should be PLANNING_THE_HEIST!", inMessage);
                }
                ((CollectionSiteProxyAgent) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
                collectionSite.startOperations();
                outMessage = new Message(MessageType.START_OPERATIONS_DONE,
                        ((CollectionSiteProxyAgent) Thread.currentThread()).getMasterThiefState());
                break;
            case MessageType.APPRAISE_SIT:
                if (inMessage.getMasterThiefState() != MasterThief.DECIDING_WHAT_TO_DO) {
                    throw new MessageException("Invalid Master Thief state - should be DECIDING_WHAT_TO_DO!", inMessage);
                }
                ((CollectionSiteProxyAgent) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
                outMessage = new Message(MessageType.APPRAISE_SIT_DONE, collectionSite.appraiseSit());
                break;
            case MessageType.TAKE_A_REST:
                if (inMessage.getMasterThiefState() != MasterThief.DECIDING_WHAT_TO_DO) {
                    throw new MessageException("Invalid Master Thief state - should be ASSEMBLING_A_GROUP!", inMessage);
                }
                ((CollectionSiteProxyAgent) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
                collectionSite.takeARest();
                outMessage = new Message(MessageType.TAKE_A_REST_DONE,
                        ((CollectionSiteProxyAgent) Thread.currentThread()).getMasterThiefState());
                break;
            case MessageType.COLLECT_A_CANVAS:
                if (inMessage.getMasterThiefState() != MasterThief.WAITING_FOR_ARRIVAL) {
                    throw new MessageException("Invalid Master Thief state - should be WAITING_FOR_ARRIVAL!", inMessage);
                }
                ((CollectionSiteProxyAgent) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
                collectionSite.collectACanvas();
                outMessage = new Message(MessageType.COLLECT_A_CANVAS_DONE,
                        ((CollectionSiteProxyAgent) Thread.currentThread()).getMasterThiefState());
                break;
            case MessageType.HAND_A_CANVAS:
                if (inMessage.getOrdinaryThiefState() != OrdinaryThief.CRAWLING_OUTWARDS) {
                    throw new MessageException("Invalid Ordinary Thief state - should be CRAWLING_OUTWARDS!", inMessage);
                } else if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() >= Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() >= Constants.NUM_THIEVES) {
                    throw new MessageException("Invalid Ordinary Thief identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefMD() < Constants.MIN_THIEF_DISPLACEMENT || inMessage.getOrdinaryThiefMD() > Constants.MAX_THIEF_DISPLACEMENT) {
                    throw new MessageException("Invalid Ordinary Thief maximum displacement!", inMessage);
                }
                ((CollectionSiteProxyAgent) Thread.currentThread()).setOrdinaryThiefID(inMessage.getOrdinaryThiefID());
                ((CollectionSiteProxyAgent) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
                ((CollectionSiteProxyAgent) Thread.currentThread()).setOrdinaryThiefMaxDisplacement(inMessage.getOrdinaryThiefMD());
                collectionSite.handACanvas(inMessage.getAssaultParty());
                outMessage = new Message(MessageType.HAND_A_CANVAS_DONE,
                        ((CollectionSiteProxyAgent) Thread.currentThread()).getOrdinaryThiefID(),
                        ((CollectionSiteProxyAgent) Thread.currentThread()).getOrdinaryThiefState(),
                        ((CollectionSiteProxyAgent) Thread.currentThread()).getOrdinaryThiefMaxDisplacement());
                break;
            case MessageType.GET_NEXT_ASSAULT_PARTY_ID:
                if (inMessage.getMasterThiefState() != MasterThief.DECIDING_WHAT_TO_DO) {
                    throw new MessageException("Invalid Master Thief state - should be DECIDING_WHAT_TO_DO!", inMessage);
                }
                ((CollectionSiteProxyAgent) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
                outMessage = new Message(MessageType.GET_NEXT_ASSAULT_PARTY_ID_DONE, collectionSite.getNextAssaultPartyID());
                break;
            case MessageType.SHUTDOWN:
                collectionSite.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
            case MessageType.GET_NEXT_ROOM:
                outMessage = new Message(MessageType.GET_NEXT_ROOM_DONE, collectionSite.getNextRoom());
                break;
            case MessageType.GET_ROOM_PAINTINGS_COLLECTION_SITE:
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in GET_ROOM_PAINTINGS_COLLECTION_SITE!", inMessage);
                }
                outMessage = new Message(MessageType.GET_ROOM_PAINTINGS_COLLECTION_SITE_DONE, collectionSite.getRoomPaintings(inMessage.getRoom()));
                break;
            case MessageType.GET_ROOM_DISTANCE_COLLECTION_SITE:
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in GET_ROOM_DISTANCE_COLLECTION_SITE!", inMessage);
                }
                outMessage = new Message(MessageType.GET_ROOM_DISTANCE_COLLECTION_SITE_DONE, collectionSite.getRoomDistance(inMessage.getRoom()));
                break;
            case MessageType.GET_TOTAL_PAINTINGS:
                outMessage = new Message(MessageType.GET_TOTAL_PAINTINGS_DONE, collectionSite.getPaintings());
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }
        return outMessage;
    }
}
