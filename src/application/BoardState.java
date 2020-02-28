package application;

import java.util.Optional;

public class BoardState {
    private State state;

    /**
     * get state
     *
     * @return State state
     */
    public State getState() {
        return state;
    }

    /**
     * Set state
     *
     * @param state state
     */
    public void setState(State state) {
        this.state = state;
    }
}
