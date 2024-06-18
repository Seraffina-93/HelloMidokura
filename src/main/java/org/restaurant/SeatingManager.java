package org.restaurant;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;

public class SeatingManager {
    private final TreeMap<Integer, PriorityQueue<Table>> tableMap = new TreeMap<>();
    private final Queue<CustomerGroup> waitingList = new LinkedList<>();
    private final Map<Integer, Table> groupToTableMap = new HashMap<>();

    public SeatingManager(List<Table> tables) {
        // Initialize the TreeMap with PriorityQueue for each table size
        for (Table table : tables) {
            addTable(table);
        }
    }

    public TreeMap<Integer, PriorityQueue<Table>> getTableMap() { return tableMap; }

    public Queue<CustomerGroup> getWaitingList() { return waitingList; }

    public Map<Integer, Table> getGroupToTableMap() { return groupToTableMap; }

    public void arrives(CustomerGroup group) {
        boolean seated = seatGroup(group);

        if (!seated) {
            // If group couldn't be seated, add to waiting list
            System.out.println("No available table for group of " + group.getSize());
            waitingList.add(group);
        }
    }

    public void leaves(CustomerGroup group) {
        if (waitingList.remove(group)) {
            System.out.println("Group of " + group.getSize() + " left from waiting list.");
        } else if (removeGroupFromTable(group)) {
            // If group was seated, try to seat another group from waiting list
            seatWaitingGroups();
        } else {
            System.out.println("Group with ID " + group.getId() + " not found or already left.");
        }
    }

    private boolean removeGroupFromTable(CustomerGroup group) {
        Table table = groupToTableMap.get(group.getId());
        if (table != null) {
            int emptySeats = table.getEmptySeats();
            PriorityQueue<Table> tables = tableMap.get(emptySeats);
            table.setEmptySeats(table.getEmptySeats() + group.getSize());
            tables.remove(table);
            System.out.println("Group " + group.getId() + " has left table " + table.getId());
            addTable(table);
            groupToTableMap.remove(group.getId());

            return true;
        }
        return false;
    }

    public Table locate(CustomerGroup group) {
        Table table = groupToTableMap.get(group.getId());
        if (table != null) {
            System.out.println("Group " + group.getId() + " is seated at table " + table.getId());
            return table;
        }
        System.out.println("Group with ID " + group.getId() + " is not currently seated.");
        return null;
    }

    // Method to seat groups waiting in the waiting list
    private void seatWaitingGroups() {
        Iterator<CustomerGroup> iterator = waitingList.iterator();
        while (iterator.hasNext()) {
            CustomerGroup waitingGroup = iterator.next();
            // Attempt to seat the waiting group
            boolean seated = seatGroup(waitingGroup);
            if (seated) {
                iterator.remove(); // Remove from waiting list if seated
            }
            // If not seated, continue to the next waiting group
        }
    }

    // Method to try to seat groups, returns 1 if seated and 0 otherwise
    private boolean seatGroup(CustomerGroup group) {
        int requiredEmptySeats = group.getSize();
        for (Map.Entry<Integer, PriorityQueue<Table>> entry : tableMap.tailMap(requiredEmptySeats).entrySet()) {
            PriorityQueue<Table> tables = entry.getValue();
            Table table = tables.poll();
            if (table != null) {
                table.setEmptySeats(table.getEmptySeats() - group.getSize());
                groupToTableMap.put(group.getId(), table);

                System.out.println("Seated group of " + group.getSize() + " at table " + table.getId());
                addTable(table);

                return true; // Group seated successfully
            }
        }
        // If no suitable table found, group remains in waiting list
        return false;
    }


    // Method to add a table to the tableMap updating the key for available seats
    private void addTable(Table table) {
        int emptySeats = table.getEmptySeats();
        if (!tableMap.containsKey(emptySeats)) {
            tableMap.put(emptySeats, new PriorityQueue<>(Comparator.comparingInt(Table::getSize)));
        }
        tableMap.get(emptySeats).add(table);
    }

}
