package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupNotFoundException;


/**
 * Edits the details of an existing person in the address book.
 */
public class ChangeGroupColorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "changeGroupColor";
    public static final String COMMAND_ALIAS = "color";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the current color of the group specified by "
            + "name"
            + "\nParameters: GROUPNAME (must be an existing group) COLOR\n"
            + "Example: " + COMMAND_WORD + " CS2010 red\n"
            + "Available colors are: teal, red, yellow, blue, orange, brown, green, pink, black, grey";

    public static final String MESSAGE_EDIT_GROUP_SUCCESS = "Group %1$s's color changed to %2$s";
    public static final String MESSAGE_GROUP_NOT_IN_LIST = "The group specified is not associated with any person";

    private final String groupName;
    private final String groupColor;

    private Group groupToEdit;
    private Group editedGroup;

    /**
     * @param name of the group to edit
     * @param color to change the group into
     */
    public ChangeGroupColorCommand(String name, String color) {
        requireNonNull(name);
        requireNonNull(color);

        this.groupName = name;
        this.groupColor = color;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!Group.isValidGroupColor(groupColor)) {
            throw new AssertionError("Group color is not defined");
        }
        try {
            model.updateGroup(groupToEdit, editedGroup);
        } catch (GroupNotFoundException tnfe) {
            throw new CommandException(MESSAGE_GROUP_NOT_IN_LIST);
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_GROUP_SUCCESS, groupName, groupColor));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        editedGroup = new Group(groupName, groupColor);
        List<Group> allGroups = model.getAddressBook().getGroupList();
        for (Group group : allGroups) {
            if (group.name.equals(groupName)) {
                groupToEdit = group;
                return;
            }
        }
        throw new CommandException(MESSAGE_GROUP_NOT_IN_LIST);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChangeGroupColorCommand)) {
            return false;
        }

        // state check
        ChangeGroupColorCommand e = (ChangeGroupColorCommand) other;
        return groupName.equals(e.groupName)
                && groupColor.equals(e.groupColor);
    }
}
