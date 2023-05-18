package interfaces;

import java.io.Serializable;

public class ReturnBoolean implements Serializable {
    public static final long serialVersionUID = 2023L;
    private final boolean value;
    private final int state;

    public ReturnBoolean(boolean value, int state) {
        this.value = value;
        this.state = state;
    }

    public boolean getValue() {
        return value;
    }

    public int getState() {
        return state;
    }
}
