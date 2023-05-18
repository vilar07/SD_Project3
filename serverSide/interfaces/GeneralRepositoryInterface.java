package serverSide.interfaces;

import clientSide.entities.MasterThief;
import clientSide.entities.OrdinaryThief;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.sharedRegions.GeneralRepository;
import utils.Constants;

/**
 * Interface which provides communication to and from the General Repository.
 */
public class GeneralRepositoryInterface {
    /**
     * The General Repository shared region.
     */
    private final GeneralRepository generalRepository;

    /**
     * GeneralRepositoryInterface constructor.
     * @param generalRepository the General Repository.
     */
    public GeneralRepositoryInterface(GeneralRepository generalRepository) {
        this.generalRepository = generalRepository;
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
            case MessageType.PRINT_STATE:
                generalRepository.printState();
                outMessage = new Message(MessageType.PRINT_STATE_DONE);
                break;
            case MessageType.PRINT_TAIL:
                if (inMessage.getPaintings() < 0) {
                    throw new MessageException("Invalid total number of paintings in PRINT_TAIL!", inMessage);
                }
                generalRepository.printTail(inMessage.getPaintings());
                outMessage = new Message(MessageType.PRINT_TAIL_DONE);
                break;
            case MessageType.SET_MASTER_STATE:
                if (inMessage.getMasterThiefState() < MasterThief.PLANNING_THE_HEIST || inMessage.getMasterThiefState() > MasterThief.PRESENTING_THE_REPORT) {
                    throw new MessageException("Invalid Master Thief state in SET_MASTER_STATE!", inMessage);
                }
                generalRepository.setMasterThiefState(inMessage.getMasterThiefState());
                outMessage = new Message(MessageType.SET_MASTER_STATE_DONE);
                break;
            case MessageType.SET_ORDINARY_STATE:
                if (inMessage.getOrdinaryThiefState() < OrdinaryThief.CONCENTRATION_SITE || inMessage.getOrdinaryThiefState() > OrdinaryThief.CRAWLING_OUTWARDS) {
                    throw new MessageException("Invalid Ordinary Thief state in SET_ORDINARY_STATE!", inMessage);
                }
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in SET_ORDINARY_STATE!", inMessage);
                }
                if (inMessage.getOrdinaryThiefSituation() != 'W' && inMessage.getOrdinaryThiefSituation() != 'P') {
                    throw new MessageException("Invalid Ordinary Thief situation in SET_ORDINARY_STATE!", inMessage);
                }
                if (inMessage.getOrdinaryThiefMD() < Constants.MIN_THIEF_DISPLACEMENT || inMessage.getOrdinaryThiefMD() > Constants.MAX_THIEF_DISPLACEMENT) {
                    throw new MessageException("Invalid Ordinary Thief maximum displacement in SET_ORDINARY_STATE!", inMessage);
                }
                generalRepository.setOrdinaryThiefState(inMessage.getOrdinaryThiefID(), inMessage.getOrdinaryThiefState(), inMessage.getOrdinaryThiefSituation(), inMessage.getOrdinaryThiefMD());
                outMessage = new Message(MessageType.SET_ORDINARY_STATE_DONE);
                break;
            case MessageType.SET_ASSAULT_PARTY_ROOM:
                if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party in SET_ASSAULT_PARTY_ROOM!", inMessage);
                }
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in SET_ASSAULT_PARTY_ROOM!", inMessage);
                }
                generalRepository.setAssaultPartyRoom(inMessage.getAssaultParty(), inMessage.getRoom());
                outMessage = new Message(MessageType.SET_ASSAULT_PARTY_ROOM_DONE);
                break;
            case MessageType.SET_ASSAULT_PARTY_MEMBER:
                if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party in SET_ASSAULT_PARTY_MEMBER!", inMessage);
                }
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in SET_ASSAULT_PARTY_MEMBER!", inMessage);
                }
                if (inMessage.getOrdinaryThiefPosition() < 0) {
                    throw new MessageException("Invalid Ordinary Thief position in SET_ASSAULT_PARTY_MEMBER!", inMessage);
                }
                if (inMessage.getOrdinaryThiefCanvas() < 0 || inMessage.getOrdinaryThiefCanvas() > 1) {
                    throw new MessageException("Invalid Ordinary Thief number of canvas in SET_ASSAULT_PARTY_MEMBER!", inMessage);
                }
                generalRepository.setAssaultPartyMember(inMessage.getAssaultParty(), inMessage.getOrdinaryThiefID(), inMessage.getOrdinaryThiefPosition(), inMessage.getOrdinaryThiefCanvas());
                outMessage = new Message(MessageType.SET_ASSAULT_PARTY_MEMBER_DONE);
                break;
            case MessageType.REMOVE_ASSAULT_PARTY_MEMBER:
                if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party in REMOVE_ASSAULT_PARTY_MEMBER!", inMessage);
                }
                if (inMessage.getOrdinaryThiefID() < 0 || inMessage.getOrdinaryThiefID() > Constants.NUM_THIEVES - 1) {
                    throw new MessageException("Invalid Ordinary Thief identification in REMOVE_ASSAULT_PARTY_MEMBER!", inMessage);
                }
                generalRepository.removeAssaultPartyMember(inMessage.getAssaultParty(), inMessage.getOrdinaryThiefID());
                outMessage = new Message(MessageType.REMOVE_ASSAULT_PARTY_MEMBER_DONE);
                break;
            case MessageType.DISBAND_ASSAULT_PARTY:
                if (inMessage.getAssaultParty() < 0 || inMessage.getAssaultParty() > Constants.NUM_ASSAULT_PARTIES) {
                    throw new MessageException("Invalid Assault Party in DISBAND_ASSAULT_PARTY!", inMessage);
                }
                generalRepository.disbandAssaultParty(inMessage.getAssaultParty());
                outMessage = new Message(MessageType.DISBAND_ASSAULT_PARTY_DONE);
                break;
            case MessageType.SET_ROOM_STATE:
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in SET_ROOM_STATE!", inMessage);
                }
                if (inMessage.getPaintings() < 0 || inMessage.getPaintings() > Constants.MAX_PAINTINGS) {
                    throw new MessageException("Invalid room number of paintings in SET_ROOM_STATE!", inMessage);
                }
                if (inMessage.getDistance() < Constants.MIN_ROOM_DISTANCE || inMessage.getDistance() > Constants.MAX_ROOM_DISTANCE) {
                    throw new MessageException("Invalid room distance in SET_ROOM_STATE!", inMessage);
                }
                generalRepository.setRoomState(inMessage.getRoom(), inMessage.getPaintings(), inMessage.getDistance());
                outMessage = new Message(MessageType.SET_ROOM_STATE_DONE);
                break;
            case MessageType.SET_ROOM_STATE_LESS_ARGS:
                if (inMessage.getRoom() < 0 || inMessage.getRoom() > Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid room identification in SET_ROOM_STATE_LESS_ARGS!", inMessage);
                }
                if (inMessage.getPaintings() < 0 || inMessage.getPaintings() > Constants.MAX_PAINTINGS) {
                    throw new MessageException("Invalid room number of paintings in SET_ROOM_STATE_LESS_ARGS!", inMessage);
                }
                generalRepository.setRoomState(inMessage.getRoom(), inMessage.getPaintings());
                outMessage = new Message(MessageType.SET_ROOM_STATE_LESS_ARGS_DONE);
                break;
            case MessageType.SET_INITIAL_ROOM_STATES:
                if (inMessage.getInitialPaintings().length < Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid initial room paintings (length is " + inMessage.getInitialPaintings().length + ") in SET_INITIAL_ROOM_STATES!", inMessage);
                }
                if (inMessage.getInitialDistances().length < Constants.NUM_ROOMS) {
                    throw new MessageException("Invalid initial room distances (length is " + inMessage.getInitialDistances().length + ") in SET_INITIAL_ROOM_STATES!", inMessage);
                }
                generalRepository.setInitialRoomStates(inMessage.getInitialPaintings(), inMessage.getInitialDistances());
                outMessage = new Message(MessageType.SET_INITIAL_ROOM_STATES_DONE);
                break;
            case MessageType.SHUTDOWN:
                generalRepository.shutdown();
                outMessage = new Message(MessageType.SHUTDOWN_DONE);
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }
        return outMessage;
    }
}
