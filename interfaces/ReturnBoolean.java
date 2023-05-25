package interfaces;

import java.io.Serializable;

/**
 * Used for when a boolean value needs to be returned from a function where the state of one of the thieves is modified.
 * Contains the boolean result and the updated state.
 */
public class ReturnBoolean implements Serializable {
    /**
     * UID used for serialization.
     */
    public static final long serialVersionUID = 2023L;

    /**
     * The boolean value to be returned.
     */
    private final boolean value;

    /**
     * The updated state of the thief.
     */
    private final int state;

    /**
     * Single constructor.
     * @param value the value to be returned
     * @param state the updated state of the thief
     */
    public ReturnBoolean(boolean value, int state) {
        this.value = value;
        this.state = state;
    }

    /**
     * Getter for the return value.
     * @return the boolean value
     */
    public boolean getValue() {
        return value;
    }

    /**
     * Getter for the thief state.
     * @return the updated thief state
     */
    public int getState() {
        return state;
    }
}
