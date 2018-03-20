package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.group.UniqueGroupList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueGroupList groups;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        groups = new UniqueGroupList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Persons and Groups in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setGroups(Set<Group> groups) {
        this.groups.setGroups(groups);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setGroups(new HashSet<>(newData.getGroupList()));
        List<Person> syncedPersonList = newData.getPersonList().stream()
                .map(this::syncWithMasterGroupList)
                .collect(Collectors.toList());

        try {
            setPersons(syncedPersonList);
        } catch (DuplicatePersonException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }
    }

    //// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's groupss and updates {@link #groups} with any new Groups found,
     * and updates the Group objects in the person to point to those in {@link #groups}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person p) throws DuplicatePersonException {
        Person person = syncWithMasterGroupList(p);
        // TODO: the Groups master list will be updated even though the below line fails.
        // This can cause the Groups master list to have additional Groups that are not grouped to any person
        // in the person list.
        persons.add(person);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code AddressBook}'s group list will be updated with the Groups of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *                                  another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     * @see #syncWithMasterGroupList(Person)
     */
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        Person syncedEditedPerson = syncWithMasterGroupList(editedPerson);
        // TODO: the Groups master list will be updated even though the below line fails.
        // This can cause the Groups master list to have additional Groups that are not Grouped to any person
        // in the person list.
        persons.setPerson(target, syncedEditedPerson);
    }

    /**
     * Updates the master group list to include Groups in {@code person} that are not in the list.
     *
     * @return a copy of this {@code person} such that every group in this person points to a Group object in the master
     * list.
     */
    private Person syncWithMasterGroupList(Person person) {
        final UniqueGroupList personGroups = new UniqueGroupList(person.getGroups());
        groups.mergeFrom(personGroups);

        // Create map with values = group object references in the master list
        // used for checking person group references
        final Map<Group, Group> masterGroupObjects = new HashMap<>();
        groups.forEach(Group -> masterGroupObjects.put(Group, Group));

        // Rebuild the list of person Groups to point to the relevant Groups in the master group list.
        final Set<Group> correctGroupReferences = new HashSet<>();
        personGroups.forEach(Group -> correctGroupReferences.add(masterGroupObjects.get(Group)));
        return new Person(
                person.getName(), person.getPhone(), person.getEmail(), person.getAddress(), person.getTimeTableLink(),
                person.getDetail(), correctGroupReferences);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //// group-level operations

    public void addGroup(Group t) throws UniqueGroupList.DuplicateGroupException {
        groups.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + groups.asObservableList().size() + " Groups";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Group> getGroupList() {
        return groups.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.groups.equalsOrderInsensitive(((AddressBook) other).groups));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, groups);
    }

    /**
     * Removes {@code group} from all {@code persons} in the {@code AddressBook} and from the {@code AddressBook}.
     */
    public void removeGroup(Group group) {
        try {
            for (Person person : persons) {
                removeGroupFromPerson(group, person);
            }
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Impossible: original person is not found from the address book.");
        }

        removeGroupFromAddressBook(group);

    }

    /**
     * Removes {@code group} from the {@code AddressBook}.
     */
    private void removeGroupFromAddressBook(Group group) {
        Set<Group> editedGroupList = groups.toSet();
        if (editedGroupList.contains(group)) {
            editedGroupList.remove(group);
            groups.setGroups(editedGroupList);
        }
    }

    /**
     * Replaces the old {@code target} group with the new {@code editedGroup}
     */
    public void editGroups(Group target, Group editedGroup) throws GroupNotFoundException {
        Set<Group> editedGroupList = groups.toSet();
        if (editedGroupList.contains(target)) {
            editedGroupList.remove(target);
            editedGroupList.add(editedGroup);
            groups.setGroups(editedGroupList);
        } else {
            throw new GroupNotFoundException();
        }
        for (Person p : persons) {
            replaceGroupInPerson(target, editedGroup, p);
        }
    }

    /**
     * Replaces the old {@code target} group of a {@code person} with the new {@code editedGroup}
     */
    private void replaceGroupInPerson(Group target, Group editedGroup, Person person) {
        Set<Group> groupList = new HashSet<>(person.getGroups());

        //Terminate if group is not is groupList
        if (!groupList.remove(target)) {
            return;
        }
        groupList.add(editedGroup);
        Person updatedPerson = new Person(person.getName(), person.getPhone(),
                person.getEmail(), person.getAddress(), person.getTimeTableLink(), person.getDetail(), groupList);

        try {
            updatePerson(person, updatedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("Modifying a person's groups only should not result in a duplicate. "
                    + "See Person#equals(Object).");
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Modifying a person's groups only should not result in "
                    + "a PersonNotFoundException. See Person#equals(Object).");
        }
    }

    /**
     * Removes {@code group} from all {@code persons} in the {@code AddressBook}.
     */
    private void removeGroupFromPerson(Group group, Person person) throws PersonNotFoundException {
        Set<Group> groupList = new HashSet<>(person.getGroups());

        //Terminate if group is not is groupList
        if (!groupList.remove(group)) {
            return;
        }
        Person updatedPerson = new Person(person.getName(), person.getPhone(),
                person.getEmail(), person.getAddress(), person.getTimeTableLink(), person.getDetail(), groupList);

        try {
            updatePerson(person, updatedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("Modifying a person's groups only should not result in a duplicate. "
                    + "See Person#equals(Object).");
        }
    }

    /**
     * Add all the user-specified colors from saved file to the Groups in the address book
     */
    public void addColorsToGroup() {
        HashMap<String, String> groupColors = readGroupColorFile();
        HashSet<Group> coloredGroups = new HashSet<Group>();
        for (Group group : groups) {
            if (groupColors.containsKey(group.name)) {
                coloredGroups.add(new Group(group.name, groupColors.get(group.name)));
            } else {
                coloredGroups.add(new Group(group.name));
            }
        }
        groups.setGroups(coloredGroups);
    }

    /**
     * Read the saved file to map the Groups to the color that the user specified
     */
    private HashMap<String, String> readGroupColorFile() {
        String groupColorsFilePath = Group.GROUP_COLOR_FILE_PATH;
        HashMap<String, String> groupColors = new HashMap<String, String>();
        try {
            Scanner scan = new Scanner(new File(groupColorsFilePath));
            while (scan.hasNextLine()) {
                String[] t = scan.nextLine().split(":");
                groupColors.put(t[0], t[1]);
            }
            return groupColors;
        } catch (FileNotFoundException fnfe) {
            throw new AssertionError("Group color file not found. Using default settings.");
        }
    }
}
