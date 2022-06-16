package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(SlipNoteManagementFileAdapterWithEmptySlipBoxTest.TestProfile.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SlipNoteManagementFileAdapterWithEmptySlipBoxTest {
    SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();

    @AfterAll
    static void tearDown() {
        new File("./src/test/resources/slipbox/empty/1 - Persisted Test Note.md").delete();
    }

    @Test
    @Order(1)
    void retrieveNextRootId_WithEmptySlipBox_ReturnsNextRootId() {
        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("1", nextId.toString());
    }

    @Test
    @Order(2)
    void persistSlipNote_returnTrue() throws IOException {
        SlipNote testSlipNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Persisted Test Note")
                .build();

        fileAdapter.persistSlipNote(testSlipNote);

        File f = new File("./src/test/resources/slipbox/empty/1 - Persisted Test Note.md");
        assertTrue(f.exists());
        assertFalse(f.isDirectory());
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("slipbox.path", "./src/test/resources/slipbox/empty");
        }
    }
}