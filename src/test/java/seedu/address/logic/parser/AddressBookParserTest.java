package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.commons.core.Messages.MESSAGE_DID_YOU_MEAN;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TODO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.AddGroupCommand;
import seedu.address.logic.commands.AddToDoCommand;
import seedu.address.logic.commands.ChangeTagColorCommand;
import seedu.address.logic.commands.CheckToDoCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteGroupCommand;
import seedu.address.logic.commands.DeleteToDoCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditToDoCommand;
import seedu.address.logic.commands.EditToDoCommand.EditToDoDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListTagMembersCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SwitchCommand;
import seedu.address.logic.commands.UnCheckToDoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.group.Group;
import seedu.address.model.group.Information;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainKeywordsPredicate;
import seedu.address.model.todo.ToDo;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.EditToDoDescriptorBuilder;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.EventUtil;
import seedu.address.testutil.GroupBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.ToDoBuilder;
import seedu.address.testutil.ToDoUtil;

public class AddressBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addAlias() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_ALIAS + " "
                + PersonUtil.getPersonDetails(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_addToDo() throws Exception {
        ToDo toDo = new ToDoBuilder().build();
        AddToDoCommand command = (AddToDoCommand) parser.parseCommand(ToDoUtil.getAddToDoCommand(toDo));
        assertEquals(new AddToDoCommand(toDo), command);
    }

    @Test
    public void parseCommand_addToDoAlias() throws Exception {
        ToDo toDo = new ToDoBuilder().build();
        AddToDoCommand command = (AddToDoCommand) parser.parseCommand(AddToDoCommand.COMMAND_ALIAS + " "
                + ToDoUtil.getToDoDetails(toDo));
        assertEquals(new AddToDoCommand(toDo), command);
    }

    @Test
    public void parseCommand_check() throws Exception {
        CheckToDoCommand command = (CheckToDoCommand) parser.parseCommand(
                CheckToDoCommand.COMMAND_WORD + " " + INDEX_FIRST_TODO.getOneBased());
        assertEquals(new CheckToDoCommand(INDEX_FIRST_TODO), command);
    }

    @Test
    public void parseCommand_uncheck() throws Exception {
        UnCheckToDoCommand command = (UnCheckToDoCommand) parser.parseCommand(
                UnCheckToDoCommand.COMMAND_WORD + " " + INDEX_FIRST_TODO.getOneBased());
        assertEquals(new UnCheckToDoCommand(INDEX_FIRST_TODO), command);
    }

    @Test
    public void parseCommand_editToDo() throws Exception {
        ToDo toDo = new ToDoBuilder().build();
        EditToDoDescriptor descriptor = new EditToDoDescriptorBuilder(toDo).build();
        EditToDoCommand command = (EditToDoCommand) parser.parseCommand(EditToDoCommand.COMMAND_WORD + " "
                + INDEX_FIRST_TODO.getOneBased() + " c/" + ToDoUtil.getToDoDetails(toDo));
        assertEquals(new EditToDoCommand(INDEX_FIRST_TODO, descriptor), command);
    }

    @Test
    public void parseCommand_editToDoAlias() throws Exception {
        ToDo toDo = new ToDoBuilder().build();
        EditToDoDescriptor descriptor = new EditToDoDescriptorBuilder(toDo).build();
        EditToDoCommand command = (EditToDoCommand) parser.parseCommand(EditToDoCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_TODO.getOneBased() + " c/" + ToDoUtil.getToDoDetails(toDo));
        assertEquals(new EditToDoCommand(INDEX_FIRST_TODO, descriptor), command);
    }

    @Test
    public void parseCommand_deleteToDo() throws Exception {
        DeleteToDoCommand command = (DeleteToDoCommand) parser.parseCommand(
                DeleteToDoCommand.COMMAND_WORD + " " + INDEX_FIRST_TODO.getOneBased());
        assertEquals(new DeleteToDoCommand(INDEX_FIRST_TODO), command);
    }

    @Test
    public void parseCommand_deleteToDoAlias() throws Exception {
        DeleteToDoCommand command = (DeleteToDoCommand) parser.parseCommand(
                DeleteToDoCommand.COMMAND_ALIAS + " " + INDEX_FIRST_TODO.getOneBased());
        assertEquals(new DeleteToDoCommand(INDEX_FIRST_TODO), command);
    }
    //@@author jas5469
    @Test
    public void parseCommand_addGroup() throws Exception {
        Group group = new GroupBuilder().build();
        AddGroupCommand command = (AddGroupCommand) parser.parseCommand(AddGroupCommand.COMMAND_WORD
                + " " + group.getInformation());
        assertEquals(new AddGroupCommand(group), command);
    }

    @Test
    public void parseCommand_addGroupAlias() throws Exception {
        Group group = new GroupBuilder().build();
        AddGroupCommand command = (AddGroupCommand) parser.parseCommand(AddGroupCommand.COMMAND_ALIAS
                + " " + group.getInformation());
        assertEquals(new AddGroupCommand(group), command);
    }

    @Test
    public void parseCommand_deleteGroup() throws Exception {
        Information information = new Information("Group A");
        DeleteGroupCommand command = (DeleteGroupCommand) parser.parseCommand(
                DeleteGroupCommand.COMMAND_WORD + " " + "Group A");
        assertEquals(new DeleteGroupCommand(information), command);
    }

    @Test
    public void parseCommand_deleteGroupAlias() throws Exception {
        Information information = new Information("Group A");
        DeleteGroupCommand command = (DeleteGroupCommand) parser.parseCommand(
                DeleteGroupCommand.COMMAND_ALIAS + " " + "Group A");
        assertEquals(new DeleteGroupCommand(information), command);
    }

    //@@author LeonidAgarth
    @Test
    public void parseCommand_addEvent() throws Exception {
        Event event = new EventBuilder().build();
        AddEventCommand command = (AddEventCommand) parser.parseCommand(EventUtil.getAddEventCommand(event));
        assertEquals(new AddEventCommand(event), command);
    }

    @Test
    public void parseCommand_addEventAlias() throws Exception {
        Event event = new EventBuilder().build();
        AddEventCommand command = (AddEventCommand) parser.parseCommand(AddEventCommand.COMMAND_ALIAS + " "
                + EventUtil.getEventDetails(event));
        assertEquals(new AddEventCommand(event), command);
    }

    @Test
    public void parseCommand_changeTagColor() throws Exception {
        String tagName = "friends";
        String tagColor = "red";
        ChangeTagColorCommand command = (ChangeTagColorCommand) parser.parseCommand(ChangeTagColorCommand.COMMAND_WORD
                + " " + tagName + " " + tagColor);
        assertEquals(new ChangeTagColorCommand(tagName, tagColor), command);
    }

    @Test
    public void parseCommand_changeTagColorAlias() throws Exception {
        String tagName = "friends";
        String tagColor = "red";
        ChangeTagColorCommand command = (ChangeTagColorCommand) parser.parseCommand(ChangeTagColorCommand.COMMAND_ALIAS
                + " " + tagName + " " + tagColor);
        assertEquals(new ChangeTagColorCommand(tagName, tagColor), command);
    }

    @Test
    public void parseCommand_switch() throws Exception {
        assertTrue(parser.parseCommand(SwitchCommand.COMMAND_WORD) instanceof SwitchCommand);
        assertTrue(parser.parseCommand(SwitchCommand.COMMAND_WORD + " view") instanceof SwitchCommand);
    }

    @Test
    public void parseCommand_switchAlias() throws Exception {
        assertTrue(parser.parseCommand(SwitchCommand.COMMAND_ALIAS) instanceof SwitchCommand);
        assertTrue(parser.parseCommand(SwitchCommand.COMMAND_ALIAS + " view") instanceof SwitchCommand);
    }

    //@@author
    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearAlias() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_editAlias() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND + MESSAGE_DID_YOU_MEAN + HistoryCommand.COMMAND_WORD,
                    pe.getMessage());
        }
    }

    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listAlias() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " 3") instanceof ListCommand);
    }
    //@@author jas5469
    @Test
    public void parseCommand_listTagMembers() throws Exception {
        List<String> keywords = Arrays.asList("friends", "CS3230");
        ListTagMembersCommand command = (ListTagMembersCommand) parser.parseCommand(
                ListTagMembersCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new ListTagMembersCommand(new TagContainKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_listTagGMembersAlias() throws Exception {
        List<String> keywords = Arrays.asList("friends", "CS3230");
        ListTagMembersCommand command = (ListTagMembersCommand) parser.parseCommand(
                ListTagMembersCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new ListTagMembersCommand(new TagContainKeywordsPredicate(keywords)), command);
    }
    //@@author
    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS + " 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS + " 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
