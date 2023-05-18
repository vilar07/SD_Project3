package commInfra;

import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *   Internal structure of the exchanged messages.
 * <p>
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */
public class Message implements Serializable
{
  /**
   *  Serialization key.
   */
   private static final long serialVersionUID = 2023L;

  /**
   *  Message type.
   */
   private int msgType = -1;

    /**
     * Master Thief's state.
     */
   private int masterThiefState = -1;

    /**
     * Ordinary Thief's state.
     */
   private int ordinaryThiefState = -1;

    /**
     * Ordinary Thief's identification.
     */
   private int ordinaryThiefID = -1;

    /**
     * Ordinary Thief's maximum displacement.
     */
   private int ordinaryThiefMD = -1;

    /**
     * Ordinary Thief's situation.
     */
   private char ordinaryThiefSituation = 0;

    /**
     * Ordinary Thief's position.
     */
   private int ordinaryThiefPosition = -1;

    /**
     * Number of canvas the Ordinary Thief is holding.
     */
   private int ordinaryThiefCanvas = -1;

    /**
     * Assault Party's identification.
     */
   private int assaultParty = -1;

    /**
     * Operation of the Master Thief.
     */
   private char operation = 0;

    /**
     * Whether the Ordinary Thief is needed.
     */
   private boolean neededThief = false;

    /**
     * Room's identification.
     */
   private int room = -1;

    /**
     * Number of paintings.
     */
   private int paintings = -1;

    /**
     * Distance to a room.
     */
   private int distance = -1;

    /**
     * Initial number of paintings in each room.
     */
   private int[] initialPaintings = new int[0];

    /**
     * Distance to each room.
     */
   private int[] initialDistances = new int[0];

    /**
     * Whether the Assault Party is empty.
     */
   private boolean emptyAssaultParty = false;

    /**
     * Whether the Assault Party is in operation.
     */
   private boolean assaultPartyInOperation = false;

    /**
     * Whether the Ordinary Thief is a member of an Assault Party.
     */
   private boolean isMember = false;

    /**
     * The Ordinary Thieves of an Assault Party.
     */
   private int[] ordinaryThieves = new int[0];

  /**
   *  Message instantiation (form 1).
   *
   *     @param type message type.
   */
   public Message (int type)
   {
      msgType = type;
   }

   /**
    * Message instantiation (form 2).
    * @param type message type.
    * @param id the state of the Master Thief, the Assault Party ID in the case of the GET_NEXT_ASSAULT_PARTY_ID_DONE
    * or DISBAND_ASSAULT_PARTY messages;
    * the Room ID in GET_ROOM_DONE, GET_ROOM_DISTANCE_MUSEUM, GET_ROOM_PAINTINGS_MUSEUM,
    * GET_ROOM_DISTANCE_COLLECTION_SITE and GET_ROOM_PAINTINGS_COLLECTION_SITE;
    * the number of paintings in PRINT_TAIL, GET_ROOM_PAINTINGS_COLLECTION_SITE_DONE, GET_ROOM_PAINTINGS_MUSEUM_DONE,
    * GET_TOTAL_PAINTINGS_DONE;
    * the identification of the Ordinary Thief for HAS_BUSY_HANDS or REMOVE_MEMBER
    * or the room distance in GET_ROOM_DISTANCE_COLLECTION_SITE_DONE, GET_ROOM_DISTANCE_MUSEUM_DONE.
    */
   public Message(int type, int id) {
      msgType = type;
      if (type == MessageType.GET_NEXT_ASSAULT_PARTY_ID_DONE || type == MessageType.DISBAND_ASSAULT_PARTY) {
         this.assaultParty = id;
      } else if (type == MessageType.GET_ROOM_DONE || type == MessageType.GET_ROOM_DISTANCE_MUSEUM || type == MessageType.GET_ROOM_PAINTINGS_MUSEUM
            || type == MessageType.GET_ROOM_DISTANCE_COLLECTION_SITE || type == MessageType.GET_ROOM_PAINTINGS_COLLECTION_SITE || type == MessageType.GET_NEXT_ROOM_DONE) {
         this.room = id;
      } else if (type == MessageType.PRINT_TAIL || type == MessageType.GET_ROOM_PAINTINGS_COLLECTION_SITE_DONE || type == MessageType.GET_ROOM_PAINTINGS_MUSEUM_DONE
            || type == MessageType.GET_TOTAL_PAINTINGS_DONE) {
         this.paintings = id;
      } else if (type == MessageType.HAS_BUSY_HANDS || type == MessageType.REMOVE_MEMBER || type == MessageType.IS_MEMBER) {
         this.ordinaryThiefID = id;
      } else if (type == MessageType.GET_ROOM_DISTANCE_COLLECTION_SITE_DONE || type == MessageType.GET_ROOM_DISTANCE_MUSEUM_DONE) {
         this.distance = id;
      } else {
         this.masterThiefState = id;
      }
   }

    /**
     *  Message instantiation (form 3).
     *
     *     @param type message type.
     * @param res whether the Assault Party is in operation in SET_IN_OPERATION, IS_ASSAULT_PARTY_IN_OPERATION_DONE;
     *            whether the Ordinary Thief holds a canvas in HAS_BUSY_HANDS_DONE;
     *            whether the Ordinary Thief is a member of an Assault Party in IS_MEMBER_DONE;
     *            whether the Assault Party is empty in the other cases.
     */
   public Message(int type, boolean res) {
      msgType = type;
      if (type == MessageType.SET_IN_OPERATION || type == MessageType.IS_ASSAULT_PARTY_IN_OPERATION_DONE) {
         this.assaultPartyInOperation = res;
      } else if (type == MessageType.HAS_BUSY_HANDS_DONE) {
         this.ordinaryThiefCanvas = (res ? 1: 0);
      } else if (type == MessageType.IS_MEMBER_DONE) {
         this.isMember = res;
      } else {
         this.emptyAssaultParty = res;
      }
   }

    /**
     *  Message instantiation (form 4).
     *
     *     @param type message type.
     * @param operation the operation of the Master Thief.
     */
   public Message(int type, char operation) {
      msgType = type;
      this.operation = operation;
   }

    /**
     *  Message instantiation (form 5).
     *
     *     @param type message type.
     * @param ordinaryThieves the identifications of the Ordinary Thieves.
     */
   public Message(int type, int[] ordinaryThieves) {
      msgType = type;
      this.ordinaryThieves = ordinaryThieves;
   }

    /**
     *  Message instantiation (form 6).
     *
     *     @param type message type.
     * @param id the identification of the room in SET_ASSAULT_PARTY_ROOM, SET_ROOM_STATE_LESS_ARGS;
     *           the identification of the Ordinary Thief in REMOVE_ASSAULT_PARTY_MEMBER;
     *           the state of the Master Thief, every other case.
     * @param assaultPartyOrPaintings the number of paintings in SET_ROOM_STATE_LESS_ARGS;
     *                                the identification of the Assault Party in the other situations.
     */
   public Message(int type, int id, int assaultPartyOrPaintings) {
      msgType = type;
      if (type == MessageType.SET_ASSAULT_PARTY_ROOM || type == MessageType.SET_ROOM_STATE_LESS_ARGS) {
         this.room = id;
      } else if (type == MessageType.REMOVE_ASSAULT_PARTY_MEMBER) {
         this.ordinaryThiefID = id;
      } else {
         this.masterThiefState = id;
      }
      if (type == MessageType.SET_ROOM_STATE_LESS_ARGS) {
         this.paintings = assaultPartyOrPaintings;
      } else {
         this.assaultParty = assaultPartyOrPaintings;
      }
   }

    /**
     *  Message instantiation (form 7).
     *
     *     @param type message type.
     * @param ordinaryThiefOrRoomID the identification of the room in SET_ROOM_STATE, SET_ROOM;
     *                              the identification of the Ordinary Thief every other case.
     * @param ordinaryThiefStateOrRoomPaintings the number of paintings in SET_ROOM_STATE, SET_ROOM;
     *                                          the state of the Ordinary Thief every other case.
     * @param ordinaryThiefMDOrRoomDistance the distance to the room in SET_ROOM_STATE, SET_ROOM;
     *                                      the maximum displacement of the Ordinary Thief every other case.
     */
   public Message(int type, int ordinaryThiefOrRoomID, int ordinaryThiefStateOrRoomPaintings, int ordinaryThiefMDOrRoomDistance) {
      msgType = type;
      if (type == MessageType.SET_ROOM_STATE || type == MessageType.SET_ROOM) {
         this.room = ordinaryThiefOrRoomID;
         this.paintings = ordinaryThiefStateOrRoomPaintings;
         this.distance = ordinaryThiefMDOrRoomDistance;
      } else {
         this.ordinaryThiefID = ordinaryThiefOrRoomID;
         this.ordinaryThiefState = ordinaryThiefStateOrRoomPaintings;
         this.ordinaryThiefMD = ordinaryThiefMDOrRoomDistance;
      }
   }

    /**
     *  Message instantiation (form 8).
     *
     *     @param type message type.
     * @param ordinaryThiefID the identification of the Ordinary Thief.
     * @param ordinaryThiefState the state of the Ordinary Thief.
     * @param ordinaryThiefSituation the situation of the Ordinary Thief.
     * @param ordinaryThiefMD the maximum displacement of the Ordinary Thief.
     */
   public Message(int type, int ordinaryThiefID, int ordinaryThiefState, char ordinaryThiefSituation, int ordinaryThiefMD) {
      msgType = type;
      this.ordinaryThiefID = ordinaryThiefID;
      this.ordinaryThiefState = ordinaryThiefState;
      this.ordinaryThiefSituation = ordinaryThiefSituation;
      this.ordinaryThiefMD = ordinaryThiefMD;
   }

    /**
     *  Message instantiation (form 9).
     *
     *     @param type message type.
     * @param ordinaryThiefID the identification of the Ordinary Thief.
     * @param ordinaryThiefStateOrPos the position of the Ordinary Thief in SET_ASSAULT_PARTY_MEMBER;
     *                                the state of the Ordinary Thief in all other cases.
     * @param ordinaryThiefMDOrCanvas the number of canvas the Ordinary Thief holds in SET_ASSAULT_PARTY_MEMBER;
     *                                the maximum displacement of the Ordinary Thief in all other cases.
     * @param assaultParty the identification of the Assault Party.
     */
   public Message(int type, int ordinaryThiefID, int ordinaryThiefStateOrPos, int ordinaryThiefMDOrCanvas, int assaultParty) {
      msgType = type;
      this.ordinaryThiefID = ordinaryThiefID;
      if (type == MessageType.SET_ASSAULT_PARTY_MEMBER) {
         this.ordinaryThiefPosition = ordinaryThiefStateOrPos;
         this.ordinaryThiefCanvas = ordinaryThiefMDOrCanvas;
      } else {
         this.ordinaryThiefState = ordinaryThiefStateOrPos;
         this.ordinaryThiefMD = ordinaryThiefMDOrCanvas;
      }
      this.assaultParty = assaultParty;
   }

    /**
     *  Message instantiation (form 10).
     *
     *     @param type message type.
     * @param ordinaryThiefID the identification of the Ordinary Thief.
     * @param ordinaryThiefState the state of the Ordinary Thief.
     * @param ordinaryThiefMD the maximum displacement of the Ordinary Thief.
     * @param neededThief whether the Ordinary Thief is needed.
     */
   public Message(int type, int ordinaryThiefID, int ordinaryThiefState, int ordinaryThiefMD, boolean neededThief) {
      msgType = type;
      this.ordinaryThiefID = ordinaryThiefID;
      this.ordinaryThiefState = ordinaryThiefState;
      this.ordinaryThiefMD = ordinaryThiefMD;
      this.neededThief = neededThief;
   }

    /**
     *  Message instantiation (form 11).
     *
     *     @param type message type.
     * @param paintings the initial number of paintings in each room.
     * @param distances the distance to each room.
     */
   public Message(int type, int[] paintings, int[] distances) {
      msgType = type;
      this.initialPaintings = paintings;
      this.initialDistances = distances;
   }

    /**
     *  Message instantiation (form 12).
     *
     *     @param type message type.
     * @param ordinaryThiefID the identification of the Ordinary Thief.
     * @param canvas whether the Ordinary Thief holds a canvas.
     */
   public Message(int type, int ordinaryThiefID, boolean canvas) {
      msgType = type;
      this.ordinaryThiefID = ordinaryThiefID;
      this.ordinaryThiefCanvas = (canvas ? 1 : 0);
   }

   /**
   *  Getting message type.
   *
   *     @return message type.
   */
   public int getMsgType ()
   {
      return (msgType);
   }

    /**
     * Getter for the state of the Master Thief.
     * @return the state of the Master Thief.
     */
   public int getMasterThiefState() {
      return this.masterThiefState;
   }

    /**
     * Getter for the state of the Ordinary Thief.
     * @return the state of the Ordinary Thief.
     */
   public int getOrdinaryThiefState() {
      return this.ordinaryThiefState;
   }

    /**
     * Getter for the identification of the Assault Party.
     * @return the identification of the Assault Party.
     */
   public int getAssaultParty() {
      return this.assaultParty;
   }

    /**
     * Getter for the identification of the Ordinary Thief.
     * @return the identification of the Ordinary Thief.
     */
   public int getOrdinaryThiefID() {
      return this.ordinaryThiefID;
   }

    /**
     * Getter for the maximum displacement of the Ordinary Thief.
     * @return the maximum displacement of the Ordinary Thief.
     */
   public int getOrdinaryThiefMD() {
      return this.ordinaryThiefMD;
   }

    /**
     * Getter for the situation of the Ordinary Thief.
     * @return the situation of the Ordinary Thief.
     */
   public char getOrdinaryThiefSituation() {
      return this.ordinaryThiefSituation;
   }

    /**
     * Getter for the position of the Ordinary Thief.
     * @return the position of the Ordinary Thief.
     */
   public int getOrdinaryThiefPosition() {
      return this.ordinaryThiefPosition;
   }

    /**
     * Getter for the number of canvas held by the Ordinary Thief.
     * @return the number of canvas.
     */
   public int getOrdinaryThiefCanvas() {
      return this.ordinaryThiefCanvas;
   }

    /**
     * Getter for the operation of the Master Thief.
     * @return the operation.
     */
   public char getOperation() {
      return this.operation;
   }

    /**
     * Getter for whether the Ordinary Thief is needed.
     * @return true if affirmative, false otherwise.
     */
   public boolean neededThief() {
      return this.neededThief;
   }

    /**
     * Getter for the identification of the room.
     * @return the identification of the room.
     */
   public int getRoom() {
      return this.room;
   }

    /**
     * Getter for the number of paintings.
     * @return the number of paintings.
     */
   public int getPaintings() {
      return this.paintings;
   }

    /**
     * Getter for the distance to the room.
     * @return the distance.
     */
   public int getDistance() {
      return this.distance;
   }

    /**
     * Getter for the initial number of paintings in each room.
     * @return the initial number of paintings in each room.
     */
   public int[] getInitialPaintings() {
      return this.initialPaintings;
   }

    /**
     * Getter for the distances to each room.
     * @return the distances.
     */
   public int[] getInitialDistances() {
      return this.initialDistances;
   }

    /**
     * Getter for whether the Assault Party is empty.
     * @return true if affirmative, false otherwise.
     */
   public boolean isAssaultPartyEmpty() {
      return this.emptyAssaultParty;
   }

    /**
     * Getter for whether the Assault Party is in operation.
     * @return true if affirmative, false otherwise.
     */
   public boolean isAssaultPartyInOperation() {
      return this.assaultPartyInOperation;
   }

    /**
     * Getter for whether the Ordinary Thief is a member of an Assault Party.
     * @return true if affirmative, false otherwise.
     */
   public boolean isMember() {
      return this.isMember;
   }

    /**
     * Getter for the identifications of the Ordinary Thieves belonging to an Assault Party.
     * @return the identifications of the Ordinary Thieves.
     */
   public int[] getOrdinaryThieves() {
      return this.ordinaryThieves;
   }

    /**
     * Returns a string representation of the message.
     * @return a string representation of the message.
     */
   @Override
   public String toString ()
   {
      return ("Message type = " + msgType +
              "\nMaster Thief state = " + masterThiefState +
              "\nOrdinary Thief state = " + ordinaryThiefState +
              "\nOrdinary Thief ID = " + ordinaryThiefID +
              "\nOrdinary Thief MD = " + ordinaryThiefMD +
              "\nOrdinary Thief Situation = " + ordinaryThiefSituation +
              "\nOrdinary Thief Position = " + ordinaryThiefPosition +
              "\nOrdinary Thief Canvas = " + ordinaryThiefCanvas +
              "\nThief is " + (neededThief ? "needed": "not needed") +
              "\nAssault Party ID = " + assaultParty +
              "\nOperation = " + operation + 
              "\nRoom = " + room +
              "\nPaintings = " + paintings +
              "\nDistance = " + distance +
              "\nInitial Room Paintings = " + (Stream.of(initialPaintings).map(Object::toString)).collect(Collectors.joining(", ")).toString() +
              "\nInitial Room Distances = " + (Stream.of(initialDistances).map(Object::toString)).collect(Collectors.joining(", ")).toString() +
              "\nAssault Party is " + (emptyAssaultParty ? "empty" : "not empty") +
              "\nAssault Party is " + (assaultPartyInOperation ? "in operation": "not in operation") +
              "\nOrdinary Thief is " + (isMember ? "member": "not member") +
              "\nOrdinary Thieves = " + (Stream.of(ordinaryThieves).map(Object::toString)).collect(Collectors.joining(", ")).toString());
   }
}
