package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS2010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_UNUSED;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalToDos.TODO_A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;
import seedu.address.model.todo.ToDo;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook();
    private final AddressBook amyNBobAddressBook = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getGroupList());
        assertEquals(Collections.emptyList(), addressBook.getToDoList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ALICE twice
        List<Person> newPersons = Arrays.asList(ALICE, ALICE);
        List<Group> newGroups = new ArrayList<>(ALICE.getGroups());
        List<ToDo> newToDos = Arrays.asList(TODO_A);
        AddressBookStub newData = new AddressBookStub(newPersons, newGroups, newToDos);

        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }

    @Test
    public void resetData_withDuplicateToDos_throwsAssertionError() {
        // Repeat TODO_A twice
        List<Person> newPersons = Arrays.asList(ALICE);
        List<Group> newGroups = new ArrayList<>(ALICE.getGroups());
        List<ToDo> newToDos = Arrays.asList(TODO_A, TODO_A);
        AddressBookStub newData = new AddressBookStub(newPersons, newGroups, newToDos);
        thrown.expect(AssertionError.class);
        addressBook.resetData(newData);
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getPersonList().remove(0);
    }

    @Test
    public void getGroupList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getGroupList().remove(0);
    }

    @Test
    public void getToDoList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getToDoList().remove(0);
    }

    @Test
    public void removeGroup_removeNonexistentGroup_addressBookUnchanged() throws Exception {
        amyNBobAddressBook.removeGroup(new Group(VALID_GROUP_UNUSED));


        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();

        assertEquals(expectedAddressBook, amyNBobAddressBook);
    }

    @Test
    public void removeGroup_groupUsedByMultiplePersons_groupRemoved() throws Exception {
        amyNBobAddressBook.removeGroup(new Group(VALID_GROUP_CS1010));

        Person expectedAmy = new PersonBuilder(AMY).withGroups().build();
        Person expectedBob = new PersonBuilder(BOB).withGroups(VALID_GROUP_CS2010).build();
        AddressBook expectedAddressBook = new AddressBookBuilder()
                .withPerson(expectedAmy).withPerson(expectedBob).build();

        assertEquals(expectedAddressBook, amyNBobAddressBook);
    }

    /**
     * A stub ReadOnlyAddressBook whose persons and groups lists can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Group> groups = FXCollections.observableArrayList();
        private final ObservableList<ToDo> todos = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons, Collection<? extends Group> groups, Collection<ToDo> todos) {
            this.persons.setAll(persons);
            this.groups.setAll(groups);
            this.todos.setAll(todos);

        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Group> getGroupList() {
            return groups;
        }

        @Override
        public ObservableList<ToDo> getToDoList() {
            return todos;
        }
    }

}
