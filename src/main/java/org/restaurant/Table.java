package org.restaurant;

import static org.restaurant.ValidationUtil.isValidSize;

public class Table {
    private final int id;
    private final int size;
    private int emptySeats;

    public Table(int id, int size) {
        if (!isValidSize(size)) {
            throw new IllegalArgumentException("Table size must be between 2 and 6.");
        }
        int correctedSize = size;
        if (size < 2) {
            correctedSize = 2;
        } else if (size > 6) {
            correctedSize = 6;
        }
        this.id = id;
        this.size = correctedSize;
        this.emptySeats = size; //all seats are empty
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public void setEmptySeats(int emptySeats) {
        this.emptySeats = emptySeats;
    }
}
