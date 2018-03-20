package seedu.address.model.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Group in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidGroupName(String)}
 */
public class Group {

    public static final String MESSAGE_GROUP_CONSTRAINTS = "Groups names should be alphanumeric";
    public static final String GROUP_VALIDATION_REGEX = "\\p{Alnum}+";

    public static final String MESSAGE_GROUP_COLOR_CONSTRAINTS = "Colors available are: "
            + "teal, red, yellow, blue, orange, brown, green, pink, black, grey";
    public static final String GROUP_COLOR_FILE_PATH = "data/groupColors.txt";
    private static final String[] AVAILABLE_COLORS = new String[] {"teal", "red", "yellow", "blue", "orange", "brown",
        "green", "pink", "black", "grey", "undefined"};

    public final String name;
    public final String color;

    /**
     * Constructs a {@code Group}.
     *
     * @param name A valid Group name.
     */
    public Group(String name) {
        requireNonNull(name);
        checkArgument(isValidGroupName(name), MESSAGE_GROUP_CONSTRAINTS);
        this.name = name;
        this.color = "undefined";
    }

    public Group(String name, String color) {
        requireNonNull(name);
        checkArgument(isValidGroupName(name), MESSAGE_GROUP_CONSTRAINTS);
        checkArgument(isValidGroupColor(color), MESSAGE_GROUP_COLOR_CONSTRAINTS);
        this.name = name;
        this.color = color;
    }

    /**
     * Returns true if a given string is a valid group name.
     */
    public static boolean isValidGroupName(String test) {
        return test.matches(GROUP_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a available group color
     */
    public static boolean isValidGroupColor(String color) {
        String trimmedColor = color.trim().toLowerCase();
        for (String s : AVAILABLE_COLORS) {
            if (s.equals(trimmedColor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Group // instanceof handles nulls
                && this.name.equals(((Group) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + name + ']';
    }

}
