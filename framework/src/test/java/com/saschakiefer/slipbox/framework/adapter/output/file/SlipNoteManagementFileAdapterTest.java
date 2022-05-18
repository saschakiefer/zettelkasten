package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SlipNoteManagementFileAdapterTest {

    @AfterAll
    static void cleanUp() {
        new File("./src/test/resources/slipbox/empty/1 - Persisted Test Note.md").delete();
    }

    @Test
    @Order(1)
    void retrieveNextRootId_WithFilledSlipBox_ReturnsNextRootId() {
        SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();
        fileAdapter.setSlipBoxDir("./src/test/resources/slipbox/has-elements");

        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("3", nextId.toString());
    }

    @Test
    @Order(2)
    void retrieveSlipNote_WithChildren_ReturnSlipNote() {
    }

    @Test
    @Order(3)
    void retrieveNextRootId_WithEmptySlipBox_ReturnsNextRootId() {
        SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();
        fileAdapter.setSlipBoxDir("./src/test/resources/slipbox/empty");

        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("1", nextId.toString());
    }

    @Test
    @Order(4)
    void persistSlipNote_returnTrue() throws IOException {
        SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();
        fileAdapter.setSlipBoxDir("./src/test/resources/slipbox/empty");

        SlipNote testSlipNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Persisted Test Note")
                .build();

        fileAdapter.persistSlipNote(testSlipNote);

        File f = new File("./src/test/resources/slipbox/empty/1 - Persisted Test Note.md");
        assertTrue(f.exists());
        assertFalse(f.isDirectory());
    }
}