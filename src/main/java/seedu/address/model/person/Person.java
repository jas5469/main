package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.group.Group;
import seedu.address.model.group.UniqueGroupList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;
    private final TimeTableLink link;
    private final Detail detail;

    private final UniqueTagList tags;
    private final UniqueGroupList groups;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, TimeTableLink link, Detail detail,
                  Set<Tag> tags , Set<Group> groups) {
        requireAllNonNull(name, phone, email, address, link, detail, tags , groups);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.link = link;
        this.detail = detail;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.groups = new UniqueGroupList(groups);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public  TimeTableLink getTimeTableLink() {
        return link;
    }

    public  Detail getDetail() {
        return detail;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress())
                && otherPerson.getTimeTableLink().equals(this.getTimeTableLink())
                && otherPerson.getDetail().equals(this.getDetail());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, link, detail, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Timetable link: ")
                .append(getTimeTableLink())
                .append(" Detail: ")
                .append(getDetail())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append(" Groups:");
        getGroups().forEach(builder::append);
        return builder.toString();
    }

}
