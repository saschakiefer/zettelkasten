package com.saschakiefer.slipbox.domain.vo;

import com.saschakiefer.slipbox.domain.exception.InvalidIdException;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class SlipNoteId implements Comparable<SlipNoteId> {
    public static String DELIMITER = ".";

    private final String id;

    public SlipNoteId(String id) {
        // Check for format #.#.#...
        if (!isValidId(id)) {
            throw new InvalidIdException(id + " is not a valid ID. The format needs to be #.#.#...");
        }

        this.id = id;
    }

    public static boolean isValidId(String id) {
        return id.matches("^\\d+(\\.(\\d+))*");
    }

    public SlipNoteId getFirstChildId() {
        return new SlipNoteId(id + ".1");
    }

    public SlipNoteId getNextPeerId() {
        int lastDelimiterPosition = id.lastIndexOf(DELIMITER);
        int nextNumber;
        String peerId;


        if (lastDelimiterPosition == -1) {
            nextNumber = Integer.parseInt(id) + 1;
            peerId = String.valueOf(nextNumber);
        } else {
            nextNumber = Integer.parseInt(id.substring(lastDelimiterPosition + 1)) + 1;
            peerId = id.substring(0, lastDelimiterPosition + 1) + nextNumber;
        }

        return new SlipNoteId(peerId);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    // see https://stackoverflow.com/questions/198431/how-do-you-compare-two-version-strings-in-java
    public int compareTo(SlipNoteId that) {
        if (that == null)
            return 1;

        String[] thisParts = this.id.split("\\.");
        String[] thatParts = that.id.split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);

        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;

            if (thisPart < thatPart)
                return -1;

            if (thisPart > thatPart)
                return 1;
        }
        return 0;
    }
}
