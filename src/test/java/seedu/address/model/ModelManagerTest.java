package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.*;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.group.Group;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredPersonList().remove(0);
    }

    @Test
    public void deleteGroup_nonExistent_modelUnchanged() throws Exception {
        AddressBook addressBook = new AddressBookBuilder().withPerson(BOB).withPerson(AMY).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(addressBook, userPrefs);
        modelManager.removeGroup(new Group(VALID_GROUP_UNUSED));
        assertEquals(new ModelManager(addressBook, userPrefs), modelManager);
    }

    @Test
    public void deleteGroup_groupUsedByMultiplePersons_groupRemoved() throws Exception {
        AddressBook addressBook = new AddressBookBuilder().withPerson(BOB).withPerson(AMY).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(addressBook, userPrefs);
        modelManager.removeGroup(new Group(VALID_GROUP_CS1010));

        Person expectedAmy = new PersonBuilder(AMY).withGroups().build();
        Person expectedBob = new PersonBuilder(BOB).withGroups(VALID_GROUP_CS2010).build();
        AddressBook expecetedAddressBook = new AddressBookBuilder()
                .withPerson(expectedBob).withPerson(expectedAmy).build();

        ModelManager expectedModelManager = new ModelManager(expecetedAddressBook, userPrefs);
        assertEquals(expectedModelManager, modelManager);

    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookName("differentName");
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
