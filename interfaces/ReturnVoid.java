package interfaces;

import java.io.Serializable;

public class ReturnVoid implements Serializable {
    public static final long serialVersionUID = 2023L;
    private final int state;

    public ReturnVoid(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
