package com.saschakiefer.domain.entity;

import com.saschakiefer.domain.exception.GenericSpecificationException;
import com.saschakiefer.domain.vo.SlipNoteId;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SlipNoteTest {

    @Test
    void addChild_WithValidSlipNote_AddsAndReturnsSlipNote() throws GenericSpecificationException {
        SlipNote parent = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Parent")
                .children(new TreeMap<>())
                .build();

        SlipNote child = SlipNote.builder()
                .slipNoteId(parent.getSlipNoteId().getFirstChildId())
                .title("Test Child")
                .content("Test ref to [[1]]")
                .build();

        parent.addChild(child);

        assertEquals(parent, child.getParent());
        assertEquals(1, parent.getChildren().size());
        assertEquals(child, parent.getChildren().get(child.getSlipNoteId()));
    }

    @Test
    void addChild_WithInvalidSlipNote_ThrowsException() {
        SlipNote parent = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Parent")
                .children(new TreeMap<>())
                .build();

        SlipNote child = SlipNote.builder()
                .slipNoteId(parent.getSlipNoteId().getFirstChildId())
                .title("Test Child")
                .build();

        Exception exception = assertThrows(GenericSpecificationException.class, () -> parent.addChild(child));
        assertEquals("Zettel 1.1 is not a child of 1. There is no reference to the parent found in the content", exception.getMessage());
    }

    @Test
    void getFullTitle_ReturnsFullTitle() {
        SlipNote parent = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Parent")
                .children(new TreeMap<>())
                .build();

        assertEquals("1 - Test Parent", parent.getFullTitle());
    }
}