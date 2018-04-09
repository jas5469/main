//@@author jas5469
package seedu.address.logic.commands;

import seedu.address.commons.events.ui.SwitchThemeEvent;

/**
 * Switch Theme command to toggle between both themes (light and dark)
 */
public class SwitchTheme extends UndoableCommand {

    public static final String COMMAND_WORD = "theme";
    public static final String COMMAND_ALIAS = "t";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Toggles between bright and dark theme.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Theme switched!";

    @Override
    public CommandResult executeUndoableCommand() {
        new SwitchThemeEvent();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
