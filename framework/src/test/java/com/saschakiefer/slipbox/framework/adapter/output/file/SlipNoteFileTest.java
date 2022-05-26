package com.saschakiefer.slipbox.framework.adapter.output.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SlipNoteFileTest {

    @Test
    public void constructor_withValidFileName_returnsSlipNoteFile() {
        SlipNoteFile file = new SlipNoteFile("1 - Test File.md");

        assertEquals("1", file.getSlipNoteId());
        assertEquals("Test File", file.getTitle());
    }

    @Test
    public void constructor_withInvalidFileName_returnsSlipNoteFile() {
        assertThrows(InvalidFilnameException.class, () -> new SlipNoteFile("1. - Test File.md"));
        assertThrows(InvalidFilnameException.class, () -> new SlipNoteFile("1 - Test File.txt"));
        assertThrows(InvalidFilnameException.class, () -> new SlipNoteFile("1 Test File.md"));
        assertThrows(InvalidFilnameException.class, () -> new SlipNoteFile("1 Test - File.md"));
    }
}