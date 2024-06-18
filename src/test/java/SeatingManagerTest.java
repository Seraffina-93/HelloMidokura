import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.restaurant.CustomerGroup;
import org.restaurant.SeatingManager;
import org.restaurant.Table;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeatingManagerTest {

    private SeatingManager seatingManager;
    private Table table1;
    private Table table2;
    private Table table3;
    private CustomerGroup group1;
    private CustomerGroup group2;
    private CustomerGroup group3;

    @BeforeEach
    void setUp() {
        table1 = new Table(1, 4); // Table ID 1, capacity 4
        table2 = new Table(2, 5); // Table ID 2, capacity 6
        table3 = new Table(3, 6); // Table ID 3, capacity 6

        List<Table> tables = new ArrayList<>();
        tables.add(table1);
        tables.add(table2);
        tables.add(table3);

        seatingManager = new SeatingManager(tables);

        group1 = new CustomerGroup(1, 2); // Group ID 1, size 2
        group2 = new CustomerGroup(2, 4); // Group ID 2, size 4
        group3 = new CustomerGroup(3, 6); // Group ID 3, size 6
    }

    @Test
    void testArrivesSeated() {
        seatingManager.arrives(group1);
        assertTrue(seatingManager.getGroupToTableMap().containsKey(group1.getId()));
        assertEquals(table1, seatingManager.getGroupToTableMap().get(group1.getId()));
        assertEquals(2, table1.getEmptySeats());
    }

    @Test
    void testArrivesWaitlist() {
        CustomerGroup largeGroup = new CustomerGroup(4, 6); // Group ID 4, size 6
        seatingManager.arrives(group3);
        seatingManager.arrives(largeGroup);
        assertFalse(seatingManager.getGroupToTableMap().containsKey(largeGroup.getId()));
        assertTrue(seatingManager.getWaitingList().contains(largeGroup));
    }

    @Test
    void testArrivesMultipleGroups() {
        seatingManager.arrives(group1);
        seatingManager.arrives(group2);
        seatingManager.arrives(group3);

        assertTrue(seatingManager.getGroupToTableMap().containsKey(group1.getId()));
        assertTrue(seatingManager.getGroupToTableMap().containsKey(group2.getId()));
        assertTrue(seatingManager.getGroupToTableMap().containsKey(group3.getId()));

        assertEquals(table1, seatingManager.getGroupToTableMap().get(group1.getId()));
        assertEquals(table2, seatingManager.getGroupToTableMap().get(group2.getId()));
        assertEquals(table3, seatingManager.getGroupToTableMap().get(group3.getId()));

        assertEquals(2, table1.getEmptySeats());
        assertEquals(1, table2.getEmptySeats());
        assertEquals(0, table3.getEmptySeats());
    }

    @Test
    void testLeavesSeated() {
        seatingManager.arrives(group1);

        assertTrue(seatingManager.getGroupToTableMap().containsKey(group1.getId()));

        seatingManager.leaves(group1);

        assertFalse(seatingManager.getGroupToTableMap().containsKey(group1.getId()));
    }

    @Test
    void testLeavesWaitingList() {
        CustomerGroup largeGroup = new CustomerGroup(4, 6); // Group ID 4, size 6
        seatingManager.arrives(group3);
        seatingManager.arrives(largeGroup);

        assertTrue(seatingManager.getWaitingList().contains(largeGroup));

        seatingManager.leaves(largeGroup);

        assertFalse(seatingManager.getWaitingList().contains(largeGroup));

    }

    @Test
    void testLocateGroupInTable() {
        seatingManager.arrives(group1);
        seatingManager.arrives(group2);
        seatingManager.arrives(group3);

        assertEquals(table1, seatingManager.locate(group1));
        assertEquals(table2, seatingManager.locate(group2));
        assertEquals(table3, seatingManager.locate(group3));
    }

    @Test
    void testLocateGroupInWaitingList() {
        CustomerGroup largeGroup = new CustomerGroup(4, 6); // Group ID 4, size 6
        seatingManager.arrives(group3);
        seatingManager.arrives(largeGroup);

        assertNull(seatingManager.locate(largeGroup));
    }

    @Test
    void testSeatAfterGroupLeaves() {
        CustomerGroup largeGroup = new CustomerGroup(4, 6); // Group ID 4, size 6
        seatingManager.arrives(group3);
        seatingManager.arrives(largeGroup);

        assertTrue(seatingManager.getWaitingList().contains(largeGroup));

        seatingManager.leaves(group3);

        assertTrue(seatingManager.getGroupToTableMap().containsKey(largeGroup.getId()));
        assertTrue(seatingManager.getWaitingList().isEmpty());
    }


}
