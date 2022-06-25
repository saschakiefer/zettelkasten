package com.saschakiefer.slipbox.domain.vo;

import com.saschakiefer.slipbox.domain.exception.InvalidIdException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlipNoteIdTest {

    @Test
    void getFirstChild_FromRootZettel_ReturnsChild() {
        SlipNoteId testSlipNoteId = new SlipNoteId("1");

        assertEquals("1.1", testSlipNoteId.getFirstChildId().toString());
    }

    @Test
    void getFirstChild_FromSubZettel_ReturnsChild() {
        SlipNoteId testSlipNoteId = new SlipNoteId("1.1");

        assertEquals("1.1.1", testSlipNoteId.getFirstChildId().toString());
    }

    @Test
    void getNextPeer_FromRootZettel_ReturnsPeer() {
        SlipNoteId testSlipNoteId = new SlipNoteId("1");

        assertEquals("2", testSlipNoteId.getNextPeerId().toString());
    }

    @Test
    void getNextPeer_FromSubZettel_ReturnsPeer() {
        SlipNoteId testSlipNoteId = new SlipNoteId("1.3");

        assertEquals("1.4", testSlipNoteId.getNextPeerId().toString());
    }

    @Test
    void getNextPeer_FromSubSubZettel_ReturnsPeer() {
        SlipNoteId testSlipNoteId = new SlipNoteId("245.23.17");

        assertEquals("245.23.18", testSlipNoteId.getNextPeerId().toString());
    }

    @Test
    void createId_withInvalidString_ThrowsException() {
        assertThrows(InvalidIdException.class, () -> new SlipNoteId("1.1..1"));
        assertThrows(InvalidIdException.class, () -> new SlipNoteId("1.a.1"));
        assertThrows(InvalidIdException.class, () -> new SlipNoteId("value"));
        assertThrows(InvalidIdException.class, () -> new SlipNoteId("1.-1.1"));
    }

    @Test
    void isRoot() {
        assertTrue(new SlipNoteId("1").isRoot());
        assertFalse(new SlipNoteId("1.1").isRoot());
    }

    @Test
    void getLevel() {
        assertEquals(0, new SlipNoteId("1").getLevel());
        assertEquals(1, new SlipNoteId("1.2").getLevel());
        assertEquals(2, new SlipNoteId("1.2.1").getLevel());
    }
}