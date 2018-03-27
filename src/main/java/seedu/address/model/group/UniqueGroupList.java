package seedu.address.model.group;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.group.exceptions.DuplicateGroupException;

/**
 * A list of to-dos that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Group#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueGroupList implements Iterable<Group> {

    private final ObservableList<Group> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent to-do as the given argument.
     */
    public boolean contains(Group toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a to-do to the list.
     *
     * @throws DuplicateGroupException if the to-do to add is a duplicate of an existing to-do in the list.
     */
    public void add(Group toAdd) throws DuplicateGroupException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateGroupException();
        }
        internalList.add(toAdd);
    }

    public void setGroups(UniqueGroupList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setGroups(List<Group> groups) throws DuplicateGroupException {
        requireAllNonNull(groups);
        final UniqueGroupList replacement = new UniqueGroupList();
        for (final Group group : groups) {
            replacement.add(group);
        }
        setGroups(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Group> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Group> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueGroupList // instanceof handles nulls
                && this.internalList.equals(((UniqueGroupList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
