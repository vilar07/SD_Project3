package serverSide.interfaces;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.AssaultPartyProxyAgent;
import serverSide.sharedRegions.AssaultParty;
import utils.Constants;

/**
 * Interface which provides communication to and from the Assault Party.
 */
public class AssaultPartyInterface {
    /**
     * The Assault Party shared region.
     */
    private final AssaultParty assaultParty;

    /**
     * AssaultPartyInterface constructor.
     * @param assaultParty the Assault Party.
     */
    public AssaultPartyInterface(AssaultParty assaultParty) {
        this.assaultParty = assaultParty;
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
            case MessageType.SEND_ASSAULT_PARTY:
                if (inMessage.getMasterThiefState() != MasterThief.ASSEMBLING_A_GROUP) {
                    throw new MessageException("Invalid Master Thief state - should be ASSEMBLING_A_GROUP!", inMessage);
                } else if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party identification!", inMessage);
                }
                ((AssaultPartyProxyAgent) Thread.currentThread()).setMasterThiefState(inMessage.getMasterThiefState());
                assaultParty.sendAssaultParty();
                outMessage = new Message(MessageType.SEND_ASSAULT_PARTY_DONE, ((AssaultPartyProxyAgent) Thread.currentThread()).getMasterThiefState());
                break;
            case MessageType.CRAWL_IN:
                if (inMessage.getOrdinaryThiefState() != OrdinaryThief.CONCENTRATION_SITE) {
                    throw new MessageException("Invalid Ordinary Thief state - should be CONCENTRATION_SITE!", inMessage);
                } else if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() >= Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() >= Constants.NUM_THIEVES) {
                    throw new MessageException("Invalid Ordinary Thief identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefMD() < Constants.MIN_THIEF_DISPLACEMENT || inMessage.getOrdinaryThiefMD() > Constants.MAX_THIEF_DISPLACEMENT) {
                    throw new MessageException("Invalid Ordinary Thief maximum displacement!", inMessage);
                }
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefID(inMessage.getOrdinaryThiefID());
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefMaxDisplacement(inMessage.getOrdinaryThiefMD());
                assaultParty.crawlIn();
                outMessage = new Message(MessageType.CRAWL_IN_DONE, inMessage.getOrdinaryThiefID(), ((AssaultPartyProxyAgent) Thread.currentThread()).getOrdinaryThiefState(), 
                                                ((AssaultPartyProxyAgent) Thread.currentThread()).getOrdinaryThiefMaxDisplacement());
                break;
            case MessageType.REVERSE_DIRECTION:
                if (inMessage.getOrdinaryThiefState() != OrdinaryThief.AT_A_ROOM) {
                    throw new MessageException("Invalid Ordinary Thief state - should be AT_A_ROOM!", inMessage);
                } else if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() >= Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() >= Constants.NUM_THIEVES) {
                    throw new MessageException("Invalid Ordinary Thief identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefMD() < Constants.MIN_THIEF_DISPLACEMENT || inMessage.getOrdinaryThiefMD() > Constants.MAX_THIEF_DISPLACEMENT) {
                    throw new MessageException("Invalid Ordinary Thief maximum displacement!", inMessage);
                }
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefID(inMessage.getOrdinaryThiefID());
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefMaxDisplacement(inMessage.getOrdinaryThiefMD());
                assaultParty.reverseDirection();
                outMessage = new Message(MessageType.REVERSE_DIRECTION_DONE);
                break;
            case MessageType.CRAWL_OUT:
                if (inMessage.getOrdinaryThiefState() != OrdinaryThief.AT_A_ROOM) {
                    throw new MessageException("Invalid Ordinary Thief state - should be AT_A_ROOM!", inMessage);
                } else if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() >= Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() >= Constants.NUM_THIEVES) {
                    throw new MessageException("Invalid Ordinary Thief identification!", inMessage);
                } else if (inMessage.getOrdinaryThiefMD() < Constants.MIN_THIEF_DISPLACEMENT || inMessage.getOrdinaryThiefMD() > Constants.MAX_THIEF_DISPLACEMENT) {
                    throw new MessageException("Invalid Ordinary Thief maximum displacement!", inMessage);
                }
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefID(inMessage.getOrdinaryThiefID());
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefState(inMessage.getOrdinaryThiefState());
                ((AssaultPartyProxyAgent) Thread.currentThread()).setOrdinaryThiefMaxDisplacement(inMessage.getOrdinaryThiefMD());
                assaultParty.crawlOut();
                outMessage = new Message(MessageType.CRAWL_OUT_DONE, inMessage.getOrdinaryThiefID(), ((AssaultPartyProxyAgent) Thread.currentThread()).getOrdinaryThiefState(), 
                                                ((AssaultPartyProxyAgent) Thread.currentThread()).getOrdinaryThiefMaxDisplacement());
                break;
            case MessageType.SHUTDOWN:
                assaultParty.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
            case MessageType.GET_ROOM:
                outMessage = new Message(MessageType.GET_ROOM_DONE, assaultParty.getRoom());
                break;
            case MessageType.SET_BUSY_HANDS:
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in SET_BUSY_HANDS!", inMessage);
                }
                assaultParty.setBusyHands(inMessage.getOrdinaryThiefID(), inMessage.getOrdinaryThiefCanvas() == 1);
                outMessage = new Message(MessageType.SET_BUSY_HANDS_DONE);
                break;
            case MessageType.HAS_BUSY_HANDS:
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in HAS_BUSY_HANDS!", inMessage);
                }
                outMessage = new Message(MessageType.HAS_BUSY_HANDS_DONE, assaultParty.hasBusyHands(inMessage.getOrdinaryThiefID()));
                break;
            case MessageType.REMOVE_MEMBER:
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in REMOVE_MEMBER!", inMessage);
                }
                assaultParty.removeMember(inMessage.getOrdinaryThiefID());
                outMessage = new Message(MessageType.REMOVE_MEMBER_DONE);
                break;
            case MessageType.IS_ASSAULT_PARTY_EMPTY:
                outMessage = new Message(MessageType.IS_ASSAULT_PARTY_EMPTY_DONE, assaultParty.isEmpty());
                break;
            case MessageType.SET_IN_OPERATION:
                assaultParty.setInOperation(inMessage.isAssaultPartyInOperation());
                outMessage = new Message(MessageType.SET_IN_OPERATION_DONE);
                break;
            case MessageType.SET_MEMBERS:
                if (inMessage.getOrdinaryThieves().length != Constants.ASSAULT_PARTY_SIZE) {
                    throw new MessageException("Invalid Ordinary Thieves length (" + inMessage.getOrdinaryThieves().length + ") in SET_MEMBERS!", inMessage);
                }
                assaultParty.setMembers(inMessage.getOrdinaryThieves());
                outMessage = new Message(MessageType.SET_MEMBERS_DONE);
                break;
            case MessageType.IS_ASSAULT_PARTY_IN_OPERATION:
                outMessage = new Message(MessageType.IS_ASSAULT_PARTY_IN_OPERATION_DONE, assaultParty.isInOperation());
                break;
            case MessageType.IS_MEMBER:
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in IS_MEMBER!", inMessage);
                }
                outMessage = new Message(MessageType.IS_MEMBER_DONE, assaultParty.isMember(inMessage.getOrdinaryThiefID()));
                break;
            case MessageType.SET_ROOM:
                if (inMessage.getRoom() < -1 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in HAS_BUSY_HANDS!", inMessage);
                }
                assaultParty.setRoom(inMessage.getRoom(), inMessage.getDistance(), inMessage.getPaintings());
                outMessage = new Message(MessageType.SET_ROOM_DONE);
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }
        return outMessage;
    }
}
