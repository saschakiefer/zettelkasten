package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.exception.SlipNoteNotFoundException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(SipNoteFactoryTest.TestProfile.class)
class SipNoteFactoryTest {

    @Test
    void createFromFileWithId_returnsSlipNote() {
        System.out.println("Entering createFromFileWithId_returnsSlipNote");
        SlipNote testNote = SlipNoteFactory.creteFromFileById(
                new SlipNoteId("1")
        );

        assertNotNull(testNote);
        assertEquals("1", testNote.getSlipNoteId().toString());
        assertEquals("1 - Test Root 1", testNote.getFullTitle());
        assertEquals("_Vorgänger:_ [[]]", testNote.getContent());
        assertNull(testNote.getParent());
        assertEquals(2, testNote.getChildren().size());

        SlipNoteId key = new SlipNoteId("1.2");
        assertNotNull(testNote.getChildren().get(key));
        assertEquals(1, testNote.getChildren().get(key).getChildren().size());
    }

    @Test
    void createFromFileWithId_noteWithParent_returnsSlipNoteWithParent() {
        System.out.println("Entering createFromFileWithId_noteWithParent_returnsSlipNoteWithParent");
        SlipNote testNote = SlipNoteFactory.creteFromFileById(
                new SlipNoteId("1.2.1")
        );

        assertNotNull(testNote);
        assertNotNull(testNote.getParent());

        assertEquals("1.2.1", testNote.getSlipNoteId().toString());
        assertEquals("1.2", testNote.getParent().getSlipNoteId().toString());
    }

    @Test
    void createFromFileWithId_withNoneExistingId_thrwsExceptio() {
        System.out.println("Entering createFromFileWithId_withNoneExistingId_thrwsExceptio");
        assertThrows(SlipNoteNotFoundException.class, () -> SlipNoteFactory.creteFromFileById(
                new SlipNoteId("999")
        ));
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("slipbox.path", "./src/test/resources/slipbox/has-elements");
        }
    }
}