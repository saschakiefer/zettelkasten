package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SlipNoteManagementFileAdapterTest {
    SlipNoteManagementFileAdapter fileAdapter;

    @AfterAll
    static void tearDown() {
        new File("./src/test/resources/slipbox/empty/1 - Persisted Test Note.md").delete();
    }

    @BeforeEach
    public void init() {
        fileAdapter = new SlipNoteManagementFileAdapter();
        fileAdapter.setSlipBoxPath("./src/test/resources/slipbox/has-elements");
    }

    @Test
    @Order(1)
    void retrieveNextRootId_WithFilledSlipBox_ReturnsNextRootId() {
        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("3", nextId.toString());
    }

    @Test
    @Order(2)
    void retrieveSlipNote_WithChildren_ReturnSlipNote() {
        SlipNote slipNote = fileAdapter.retrieveSlipNote(new SlipNoteId("1"));

        assertEquals("1", slipNote.getSlipNoteId().toString());
    }

    @Test
    @Order(3)
    void retrieveNextRootId_WithEmptySlipBox_ReturnsNextRootId() {
        fileAdapter.setSlipBoxPath("./src/test/resources/slipbox/empty");
        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("1", nextId.toString());
    }

    @Test
    @Order(4)
    void persistSlipNote_returnTrue() throws IOException {
        fileAdapter.setSlipBoxPath("./src/test/resources/slipbox/empty");
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