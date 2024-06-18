package org.restaurant;

import static org.restaurant.ValidationUtil.isValidSize;

public class CustomerGroup {
    private final int id;
    private final int size;

    public CustomerGroup(int id, int size) {
        if (!isValidSize(size)) {
            throw new IllegalArgumentException("Table size must be between 2 and 6.");
        }
        int correctedSize = size;
        if (size < 1) {
            correctedSize = 1;
        } else if (size > 6) {
            correctedSize = 6;
        }
        this.id = id;
        this.size = correctedSize;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }
}
