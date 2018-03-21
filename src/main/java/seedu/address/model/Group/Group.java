package seedu.address.model.Group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Group in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidGroupName(String)}
 */
public class Group {

    public static final String MESSAGE_GROUP_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String GROUP_VALIDATION_REGEX = "\\p{Alnum}+";

    public final String name;

    /**
     * Constructs a {@code Group}.
     *
     * @param name A valid group name.
     */
    public Group(String name) {
        requireNonNull(name);
        checkArgument(isValidGroupName(name), MESSAGE_GROUP_CONSTRAINTS);
        this.name = name;
    }
    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidGroupName(String test) {
        return test.matches(GROUP_VALIDATION_REGEX);
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
