package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;
<<<<<<< HEAD
=======

>>>>>>> d95a571d6d62d9dea303c596ade42209f4d6e3a5
import seedu.address.model.todo.ToDo;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the groups list.
     * This list will not contain any duplicate groups.
     */
    ObservableList<Group> getGroupList();

    /**
     * Returns an unmodifiable view of the todos list.
     * This list will not contain any duplicate todos.
     */
    ObservableList<ToDo> getToDoList();

}
