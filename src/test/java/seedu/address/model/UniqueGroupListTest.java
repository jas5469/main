package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_COLOR_ORANGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_CS2010;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.group.Group;
import seedu.address.model.group.UniqueGroupList;

public class UniqueGroupListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueGroupList.asObservableList().remove(0);
    }

    @Test
    public void equals_sameList_true() throws Exception {
        UniqueGroupList uniqueGroupList1 = new UniqueGroupList();
        UniqueGroupList uniqueGroupList2 = new UniqueGroupList();
        assertEquals(uniqueGroupList1, uniqueGroupList2);

        uniqueGroupList1.add(new Group(VALID_GROUP_COLOR_ORANGE));
        uniqueGroupList2.add(new Group(VALID_GROUP_COLOR_ORANGE));
        assertEquals(uniqueGroupList1, uniqueGroupList2);

        uniqueGroupList1.add(new Group(VALID_GROUP_CS1010));
        uniqueGroupList1.add(new Group(VALID_GROUP_CS2010));
        uniqueGroupList2.add(new Group(VALID_GROUP_CS1010));
        uniqueGroupList2.add(new Group(VALID_GROUP_CS2010));
        assertEquals(uniqueGroupList1, uniqueGroupList2);
    }

    @Test
    public void equals_differentList_false() throws Exception {
        UniqueGroupList uniqueGroupList1 = new UniqueGroupList();
        UniqueGroupList uniqueGroupList2 = new UniqueGroupList();
        uniqueGroupList2.add(new Group(VALID_GROUP_COLOR_ORANGE));
        assertNotEquals(uniqueGroupList1, uniqueGroupList2);

        uniqueGroupList1.add(new Group(VALID_GROUP_CS1010));
        uniqueGroupList1.add(new Group(VALID_GROUP_COLOR_ORANGE));
        uniqueGroupList2.add(new Group(VALID_GROUP_CS1010));
        assertNotEquals(uniqueGroupList1, uniqueGroupList2);
    }

    @Test
    public void hashCode_sameList_sameResult() throws Exception {
        UniqueGroupList uniqueGroupList1 = new UniqueGroupList();
        UniqueGroupList uniqueGroupList2 = new UniqueGroupList();
        assertEquals(uniqueGroupList1.hashCode(), uniqueGroupList2.hashCode());

        uniqueGroupList1.add(new Group(VALID_GROUP_COLOR_ORANGE));
        uniqueGroupList2.add(new Group(VALID_GROUP_COLOR_ORANGE));
        assertEquals(uniqueGroupList1, uniqueGroupList2);

        uniqueGroupList1.add(new Group(VALID_GROUP_CS1010));
        uniqueGroupList1.add(new Group(VALID_GROUP_CS2010));
        uniqueGroupList2.add(new Group(VALID_GROUP_CS1010));
        uniqueGroupList2.add(new Group(VALID_GROUP_CS2010));
        assertEquals(uniqueGroupList1, uniqueGroupList2);
    }

    @Test
    public void hashCode_differentList_differentResult() throws Exception {
        UniqueGroupList uniqueGroupList1 = new UniqueGroupList();
        UniqueGroupList uniqueGroupList2 = new UniqueGroupList();
        uniqueGroupList2.add(new Group(VALID_GROUP_COLOR_ORANGE));
        assertNotEquals(uniqueGroupList1.hashCode(), uniqueGroupList2.hashCode());

        uniqueGroupList1.add(new Group(VALID_GROUP_CS1010));
        uniqueGroupList1.add(new Group(VALID_GROUP_COLOR_ORANGE));
        uniqueGroupList2.add(new Group(VALID_GROUP_CS1010));
        assertNotEquals(uniqueGroupList1.hashCode(), uniqueGroupList2.hashCode());
    }

    @Test
    public void duplicateGroup() throws Exception {
        UniqueGroupList uniqueGroupList = new UniqueGroupList();
        uniqueGroupList.add(new Group(VALID_GROUP_CS1010));
        thrown.expect(UniqueGroupList.DuplicateGroupException.class);
        uniqueGroupList.add(new Group(VALID_GROUP_CS1010));
    }
}
