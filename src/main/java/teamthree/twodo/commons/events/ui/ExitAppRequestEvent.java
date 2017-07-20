package teamthree.twodo.commons.events.ui;

import teamthree.twodo.commons.events.BaseEvent;

/**
 * Indicates a request for 2Do termination
 */
public class ExitAppRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
