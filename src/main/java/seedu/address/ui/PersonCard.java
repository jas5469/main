package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String[] GROUP_COLOR_STYLES =
        {"teal", "red", "yellow", "blue", "orange", "brown", "green", "pink", "black", "grey"};

    /**
     * Define group colour styles
     */
    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane groups;

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        initGroups(person);
    }

    /**
     * Returns the color style for {@code name}'s label.
     */
    private String getGroupColorStyleFor(String groupName) {
        // use the hash code of the group name to generate a random color, so that the color remain consistent
        // between different runs of the program while still making it random enough between groups.
        return GROUP_COLOR_STYLES[Math.abs(groupName.hashCode()) % GROUP_COLOR_STYLES.length];
    }

    /**
     * Creates the group labels for {@code person}.
     */
    private void initGroups(Person person) {
        person.getGroups().forEach(group -> {
            Label groupLabel = new Label(group.name);
            if (group.color.equals("undefined")) {
                groupLabel.getStyleClass().add(getGroupColorStyleFor(group.name));
            } else {
                groupLabel.getStyleClass().add(group.color);
            }
            groups.getChildren().add(groupLabel);
        });
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
