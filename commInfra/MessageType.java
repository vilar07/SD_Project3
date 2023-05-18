package commInfra;

/**
 *   Type of the exchanged messages.
 *the
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType
{
  /**
   * Type of the collectionSite.startOperations request.
   */
  public static final int START_OPERATIONS = 0;

  /**
   * Type of the collectionSite.appraiseSit request.
   */
  public static final int APPRAISE_SIT = 1;

  /**
   * Type of the collectionSite.getNextAssaultPartyID request.
   */
  public static final int GET_NEXT_ASSAULT_PARTY_ID = 2;

  /**
   * Type of the concentrationSite.prepareAssaultParty request.
   */
  public static final int PREPARE_ASSAULT_PARTY = 3;

  /**
   * Type of the assaultParty.sendAssaultParty request.
   */
  public static final int SEND_ASSAULT_PARTY = 4;

  /**
   * Type of the collectionSite.takeARest request.
   */
  public static final int TAKE_A_REST = 5;

  /**
   * Type of the collectionSite.collectACanvas request.
   */
  public static final int COLLECT_A_CANVAS = 6;

  /**
   * Type of the concentrationSite.sumUpResults request.
   */
  public static final int SUM_UP_RESULTS = 7;

  /**
   * Type of the concentrationSite.amINeeded request.
   */
  public static final int AM_I_NEEDED = 8;

  /**
   * Type of the concentrationSite.prepareExcursion request.
   */
  public static final int PREPARE_EXCURSION = 9;

  /**
   * Type of the assaultParty.crawlIn request.
   */
  public static final int CRAWL_IN = 10;

  /**
   * Type of the museum.rollACanvas request.
   */
  public static final int ROLL_A_CANVAS = 11;

  /**
   * Type of the assaultParty.reverseDirection request.
   */
  public static final int REVERSE_DIRECTION = 12;

  /**
   * Type of the assaultParty.crawlOut request.
   */
  public static final int CRAWL_OUT = 13;

  /**
   * Type of the collectionSite.handACanvas request.
   */
  public static final int HAND_A_CANVAS = 14;

  /**
   * Type of the startOperations reply.
   */
  public static final int START_OPERATIONS_DONE = 15;

  /**
   * Type of the collectionSite.appraiseSit reply.
   */
  public static final int APPRAISE_SIT_DONE = 16;

  /**
   * Type of the collectionSite.getNextAssaultPartyID reply.
   */
  public static final int GET_NEXT_ASSAULT_PARTY_ID_DONE = 17;

  /**
   * Type of the concentrationSite.prepareAssaultParty reply.
   */
  public static final int PREPARE_ASSAULT_PARTY_DONE = 18;

  /**
   * Type of the assaultParty.sendAssaultParty reply.
   */
  public static final int SEND_ASSAULT_PARTY_DONE = 19;

  /**
   * Type of the collectionSite.takeARest reply.
   */
  public static final int TAKE_A_REST_DONE = 20;

  /**
   * Type of the collectionSite.collectACanvas reply.
   */
  public static final int COLLECT_A_CANVAS_DONE = 21;

  /**
   * Type of the concentrationSite.sumUpResults reply.
   */
  public static final int SUM_UP_RESULTS_DONE = 22;

  /**
   * Type of the concentrationSite.amINeeded reply.
   */
  public static final int AM_I_NEEDED_DONE = 23;

  /**
   * Type of the concentrationSite.prepareExcursion reply.
   */
  public static final int PREPARE_EXCURSION_DONE = 24;

  /**
   * Type of the assaultParty.crawlIn reply.
   */
  public static final int CRAWL_IN_DONE = 25;

  /**
   * Type of the museum.rollACanvas reply.
   */
  public static final int ROLL_A_CANVAS_DONE = 26;

  /**
   * Type of the assaultParty.reverseDirection reply.
   */
  public static final int REVERSE_DIRECTION_DONE = 27;

  /**
   * Type of the assaultParty.crawlOut reply.
   */
  public static final int CRAWL_OUT_DONE = 28;

  /**
   * Type of the collectionSite.handACanvas reply.
   */
  public static final int HAND_A_CANVAS_DONE = 29;

  /**
   * Type of the server shutdown request.
   */
  public static final int SHUTDOWN = 30;

  /**
   * Type of the server shutdown reply.
   */
  public static final int SHUTDOWN_DONE = 31;

  /**
   * Type of the generalRepository.printState request.
   */
  public static final int PRINT_STATE = 32;

  /**
   * Type of the generalRepository.printState reply.
   */
  public static final int PRINT_STATE_DONE = 33;

  /**
   * Type of the generalRepository.printTail request.
   */
  public static final int PRINT_TAIL = 34;

  /**
   * Type of the generalRepository.printTail reply.
   */
  public static final int PRINT_TAIL_DONE = 35;

  /**
   * Type of the generalRepository.setMasterThiefState request.
   */
  public static final int SET_MASTER_STATE = 36;

  /**
   * Type of the generalRepository.setMasterThiefState reply.
   */
  public static final int SET_MASTER_STATE_DONE = 37;

  /**
   * Type of the generalRepository.setOrdinaryThiefState request.
   */
  public static final int SET_ORDINARY_STATE = 38;

  /**
   * Type of the generalRepository.setOrdinaryThiefState reply.
   */
  public static final int SET_ORDINARY_STATE_DONE = 39;

  /**
   * Type of the generalRepository.setAssaultPartyRoom request.
   */
  public static final int SET_ASSAULT_PARTY_ROOM = 40;

  /**
   * Type of the generalRepository.setAssaultPartyRoom reply.
   */
  public static final int SET_ASSAULT_PARTY_ROOM_DONE = 41;

  /**
   * Type of the generalRepository.setAssaultPartyMember request.
   */
  public static final int SET_ASSAULT_PARTY_MEMBER = 42;

  /**
   * Type of the generalRepository.setAssaultPartyMember reply.
   */
  public static final int SET_ASSAULT_PARTY_MEMBER_DONE = 43;

  /**
   * Type of the generalRepository.removeAssaultPartyMember request.
   */
  public static final int REMOVE_ASSAULT_PARTY_MEMBER = 44;

  /**
   * Type of the generalRepository.removeAssaultPartyMember reply.
   */
  public static final int REMOVE_ASSAULT_PARTY_MEMBER_DONE = 45;

  /**
   * Type of the generalRepository.disbandAssaultParty request.
   */
  public static final int DISBAND_ASSAULT_PARTY = 46;

  /**
   * Type of the generalRepository.disbandAssaultParty reply.
   */
  public static final int DISBAND_ASSAULT_PARTY_DONE = 47;

  /**
   * Type of the generalRepository.setRoomState request.
   */
  public static final int SET_ROOM_STATE = 48;

  /**
   * Type of the generalRepository.setRoomState reply.
   */
  public static final int SET_ROOM_STATE_DONE = 49;

  /**
   * Type of the generalRepository.setRoomState request.
   */
  public static final int SET_ROOM_STATE_LESS_ARGS = 50;

  /**
   * Type of the generalRepository.setRoomState reply.
   */
  public static final int SET_ROOM_STATE_LESS_ARGS_DONE = 51;

  /**
   * Type of the generalRepository.setInitialRoomStates request.
   */
  public static final int SET_INITIAL_ROOM_STATES = 52;

  /**
   * Type of the generalRepository.setInitialRoomStates reply.
   */
  public static final int SET_INITIAL_ROOM_STATES_DONE = 53;

  /**
   * Type of the assaultParty.setBusyHands request.
   */
  public static final int SET_BUSY_HANDS = 54;

  /**
   * Type of the assaultParty.setBusyHands reply.
   */
  public static final int SET_BUSY_HANDS_DONE = 55;

  /**
   * Type of the assaultParty.getRoom request.
   */
  public static final int GET_ROOM = 56;

  /**
   * Type of the assaultParty.getRoom reply.
   */
  public static final int GET_ROOM_DONE = 57;

  /**
   * Type of the collectionSite.getNextRoom request.
   */
  public static final int GET_NEXT_ROOM = 58;

  /**
   * Type of the collectionSite.getNextRoom reply.
   */
  public static final int GET_NEXT_ROOM_DONE = 59;

  /**
   * Type of the museum.getRoomDistance request.
   */
  public static final int GET_ROOM_DISTANCE_MUSEUM = 60;

  /**
   * Type of the museum.getRoomDistance reply.
   */
  public static final int GET_ROOM_DISTANCE_MUSEUM_DONE = 61;

  /**
   * Type of the museum.getRoomPaintings request.
   */
  public static final int GET_ROOM_PAINTINGS_MUSEUM = 62;

  /**
   * Type of the museum.getRoomPaintings reply.
   */
  public static final int GET_ROOM_PAINTINGS_MUSEUM_DONE = 63;

  /**
   * Type of the collectionSite.getRoomPaintings request.
   */
  public static final int GET_ROOM_PAINTINGS_COLLECTION_SITE = 64;

  /**
   * Type of the collectionSite.getRoomPaintings reply.
   */
  public static final int GET_ROOM_PAINTINGS_COLLECTION_SITE_DONE = 65;

  /**
   * Type of the collectionSite.getRoomDistance request.
   */
  public static final int GET_ROOM_DISTANCE_COLLECTION_SITE = 66;

  /**
   * Type of the collectionSite.getRoomDistance reply.
   */
  public static final int GET_ROOM_DISTANCE_COLLECTION_SITE_DONE = 67;

  /**
   * Type of the collectionSite.getPaintings request.
   */
  public static final int GET_TOTAL_PAINTINGS = 68;

  /**
   * Type of the collectionSite.getPaintings reply.
   */
  public static final int GET_TOTAL_PAINTINGS_DONE = 69;

  /**
   * Type of the assaultParty.hasBusyHands request.
   */
  public static final int HAS_BUSY_HANDS = 70;

  /**
   * Type of the assaultParty.hasBusyHands reply.
   */
  public static final int HAS_BUSY_HANDS_DONE = 71;

  /**
   * Type of the assaultParty.removeMember request.
   */
  public static final int REMOVE_MEMBER = 72;

  /**
   * Type of the assaultParty.removeMember reply.
   */
  public static final int REMOVE_MEMBER_DONE = 73;

  /**
   * Type of the assaultParty.isEmpty request.
   */
  public static final int IS_ASSAULT_PARTY_EMPTY = 74;

  /**
   * Type of the assaultParty.isEmpty reply.
   */
  public static final int IS_ASSAULT_PARTY_EMPTY_DONE = 75;

  /**
   * Type of the assaultParty.setInOperation request.
   */
  public static final int SET_IN_OPERATION = 76;

  /**
   * Type of the assaultParty.setInOperation reply.
   */
  public static final int SET_IN_OPERATION_DONE = 77;

  /**
   * Type of the assaultParty.setMembers request.
   */
  public static final int SET_MEMBERS = 78;

  /**
   * Type of the assaultParty.setMembers reply.
   */
  public static final int SET_MEMBERS_DONE = 79;

  /**
   * Type of the assaultParty.isInOperation request.
   */
  public static final int IS_ASSAULT_PARTY_IN_OPERATION = 80;

  /**
   * Type of the assaultParty.isInOperation reply.
   */
  public static final int IS_ASSAULT_PARTY_IN_OPERATION_DONE = 81;

  /**
   * Type of the assaultParty.isMember request.
   */
  public static final int IS_MEMBER = 82;

  /**
   * Type of the assaultParty.isMember reply.
   */
  public static final int IS_MEMBER_DONE = 83;

  /**
   * Type of the assaultParty.setRoom request.
   */
  public static final int SET_ROOM = 84;

  /**
   * Type of the assaultParty.setRoom reply.
   */
  public static final int SET_ROOM_DONE = 85;
}
