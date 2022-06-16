package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.exception.SlipNoteInconcistencyException;
import com.saschakiefer.slipbox.domain.exception.SlipNoteNotFoundException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SipNoteFactoryTest {

    @Test
    void creteFromFileWithId_returnsSLipNote() {
        SlipNote testNote = SlipNoteFactory.creteFromFileById(
                new SlipNoteId("1"),
                "./src/test/resources/slipbox/has-elements"
        );

        assertNotNull(testNote);
        assertEquals("1", testNote.getSlipNoteId().toString());
        assertEquals("1 - Test Root 1", testNote.getFullTitle());
        assertEquals("_Vorgänger:_", testNote.getContent());
        assertEquals(2, testNote.getChildren().size());

        SlipNoteId key = new SlipNoteId("1.2");
        assertNotNull(testNote.getChildren().get(key));
        assertEquals(1, testNote.getChildren().get(key).getChildren().size());
    }

    @Test
    void creteFromFileWithId_withNoneExistingId_thrwsExceptio() {
        Exception exception = assertThrows(SlipNoteNotFoundException.class, () -> {
            SlipNote testNote = SlipNoteFactory.creteFromFileById(
                    new SlipNoteId("999"),
                    "./src/test/resources/slipbox/has-elements");
        });
    }

    @Test
    void creteFromFileWithId_withDublicatId_thrwsExceptio() {
        Exception exception = assertThrows(SlipNoteInconcistencyException.class, () -> {
            SlipNote testNote = SlipNoteFactory.creteFromFileById(
                    new SlipNoteId("1"),
                    "./src/test/resources/slipbox/inconsistent");
        });
    }
}