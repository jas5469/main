package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_COLOR_RED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS2010;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.group.Group;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for ChangeGroupColorCommand.
 */
public class ChangeGroupColorCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_correctFields_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        ChangeGroupColorCommand command = prepareCommand(VALID_GROUP_CS1010, VALID_GROUP_COLOR_RED);

        String expectedMessage =
                String.format(ChangeGroupColorCommand.MESSAGE_EDIT_GROUP_SUCCESS, VALID_GROUP_CS1010, VALID_GROUP_COLOR_RED);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Group oldGroup = new Group(VALID_GROUP_CS1010);
        Group newGroup = new Group(VALID_GROUP_CS1010, VALID_GROUP_COLOR_RED);
        expectedModel.updateGroup(oldGroup, newGroup);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_groupNameNotInList_failure() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        ChangeGroupColorCommand command = prepareCommand(VALID_GROUP_CS2010, VALID_GROUP_COLOR_RED);

        assertCommandFailure(command, model, ChangeGroupColorCommand.MESSAGE_GROUP_NOT_IN_LIST);
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private ChangeGroupColorCommand prepareCommand(String name, String color) {
        ChangeGroupColorCommand command = new ChangeGroupColorCommand(name, color);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
