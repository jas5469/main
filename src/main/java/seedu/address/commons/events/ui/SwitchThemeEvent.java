//@@author jas5469
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Handles switch Theme Event
 */
public class SwitchThemeEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}