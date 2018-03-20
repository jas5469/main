package seedu.address.ui.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import seedu.address.model.person.Person;
import seedu.address.ui.PersonCard;

/**
 * A set of assertion methods useful for writing GUI tests.
 */
public class GuiTestAssert {
    private static final String LABEL_DEFAULT_STYLE = "label";

    /**
     * Asserts that {@code actualCard} displays the same values as {@code expectedCard}.
     */
    public static void assertCardEquals(PersonCardHandle expectedCard, PersonCardHandle actualCard) {
        assertEquals(expectedCard.getId(), actualCard.getId());
        assertEquals(expectedCard.getAddress(), actualCard.getAddress());
        assertEquals(expectedCard.getEmail(), actualCard.getEmail());
        assertEquals(expectedCard.getName(), actualCard.getName());
        assertEquals(expectedCard.getPhone(), actualCard.getPhone());
        assertEquals(expectedCard.getGroups(), actualCard.getGroups());

        expectedCard.getGroups().forEach(group ->
                assertEquals(expectedCard.getGroupStyleClasses(group), actualCard.getGroupStyleClasses(group)));
    }

    /**
     * Asserts that {@code actualCard} displays the details of {@code expectedPerson}.
     */
    public static void assertCardDisplaysPerson(Person expectedPerson, PersonCardHandle actualCard) {
        assertEquals(expectedPerson.getName().fullName, actualCard.getName());
        assertEquals(expectedPerson.getPhone().value, actualCard.getPhone());
        assertEquals(expectedPerson.getEmail().value, actualCard.getEmail());
        assertEquals(expectedPerson.getAddress().value, actualCard.getAddress());
        assertGroupsEqual(expectedPerson, actualCard);
    }

    /**
     * Returns the color style for {@code name}'s label. The group's color is determined by looking up the color
     * in {@code PersonCard#GROUP_COLOR_STYLES}, using an index generated by the hash code of the group's content.
     *
     * @see PersonCard#getGroupColorStyleFor(String)
     */
    private static String getGroupColorStyleFor(String groupName) {
        switch (groupName) {
        case "CS3230":
            return "teal";

        case "CS1010":
        case "GEQ1000":
            return "orange";

        case "CS2010":
        case "GER1000":
            return "brown";


        default:
            fail(groupName + " does not have a color assigned.");
            return "";
        }
    }

    /**
     * Asserts that the groups in {@code actualCard} matches all the groups in {@code expectedPerson} with the correct
     * color.
     */
    private static void assertGroupsEqual(Person expectedPerson, PersonCardHandle actualCard) {
        List<String> expectedGroups = expectedPerson.getGroups().stream()
                .map(group -> group.name).collect(Collectors.toList());
        assertEquals(expectedGroups, actualCard.getGroups());
        expectedGroups.forEach(group ->
                assertEquals(Arrays.asList(LABEL_DEFAULT_STYLE, getGroupColorStyleFor(group)),
                        actualCard.getGroupStyleClasses(group)));
    }

    /**
     * Asserts that the list in {@code personListPanelHandle} displays the details of {@code persons} correctly and
     * in the correct order.
     */
    public static void assertListMatching(PersonListPanelHandle personListPanelHandle, Person... persons) {
        for (int i = 0; i < persons.length; i++) {
            assertCardDisplaysPerson(persons[i], personListPanelHandle.getPersonCardHandle(i));
        }
    }

    /**
     * Asserts that the list in {@code personListPanelHandle} displays the details of {@code persons} correctly and
     * in the correct order.
     */
    public static void assertListMatching(PersonListPanelHandle personListPanelHandle, List<Person> persons) {
        assertListMatching(personListPanelHandle, persons.toArray(new Person[0]));
    }

    /**
     * Asserts the size of the list in {@code personListPanelHandle} equals to {@code size}.
     */
    public static void assertListSize(PersonListPanelHandle personListPanelHandle, int size) {
        int numberOfPeople = personListPanelHandle.getListSize();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in {@code resultDisplayHandle} equals to {@code expected}.
     */
    public static void assertResultMessage(ResultDisplayHandle resultDisplayHandle, String expected) {
        assertEquals(expected, resultDisplayHandle.getText());
    }
}
