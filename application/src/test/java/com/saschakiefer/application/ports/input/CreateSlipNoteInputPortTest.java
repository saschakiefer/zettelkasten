package com.saschakiefer.application.ports.input;

import com.saschakiefer.application.ports.output.CreateSlipNoteOutputPort;
import com.saschakiefer.domain.entity.SlipNote;
import com.saschakiefer.domain.exception.GenericSpecificationException;
import com.saschakiefer.domain.vo.SlipNoteId;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateSlipNoteInputPortTest {

    @Test
    void createSlipNote_WithParentHavingChildren_ReturnsNewSlipNote() {
        SlipNote testNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Parent")
                .children(new TreeMap<>())
                .build();

        try {
            testNote.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.1")).content("Parent: [[1]]").build());
            testNote.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.3")).content("Parent: [[1]]").build());
            testNote.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2")).content("Parent: [[1]]").build());
        } catch (GenericSpecificationException e) {
            throw new RuntimeException(e);
        }

        CreateSlipNoteOutputPort mockedOutputPort = mock(CreateSlipNoteOutputPort.class);
        when(mockedOutputPort.retrieveSlipNote(testNote.getSlipNoteId())).thenReturn(testNote);

        CreateSlipNoteInputPort inputPort = new CreateSlipNoteInputPort(mockedOutputPort);
        SlipNote newNote = inputPort.createSlipNote("Test", testNote.getSlipNoteId());

        assertEquals("Test", newNote.getTitle());
        assertEquals("1.4", newNote.getSlipNoteId().toString());
        assertEquals(testNote, newNote.getParent());
    }

    @Test
    void createSlipNote_WithParentHavingNoChildren_ReturnsNewSlipNote() {
        SlipNote testNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Parent")
                .children(new TreeMap<>())
                .build();

        CreateSlipNoteOutputPort mockedOutputPort = mock(CreateSlipNoteOutputPort.class);
        when(mockedOutputPort.retrieveSlipNote(testNote.getSlipNoteId())).thenReturn(testNote);

        CreateSlipNoteInputPort inputPort = new CreateSlipNoteInputPort(mockedOutputPort);
        SlipNote newNote = inputPort.createSlipNote("Test", testNote.getSlipNoteId());

        assertEquals("Test", newNote.getTitle());
        assertEquals("1.1", newNote.getSlipNoteId().toString());
        assertEquals(testNote, newNote.getParent());
    }

    @Test
    void createSlipNote_WithoutParent_ReturnsNewSlipNote() {
        CreateSlipNoteOutputPort mockedOutputPort = mock(CreateSlipNoteOutputPort.class);
        when(mockedOutputPort.retrieveNextRootId()).thenReturn(new SlipNoteId("3"));

        CreateSlipNoteInputPort inputPort = new CreateSlipNoteInputPort(mockedOutputPort);
        SlipNote newNote = inputPort.createSlipNote("Test");

        assertEquals("Test", newNote.getTitle());
        assertEquals("3", newNote.getSlipNoteId().toString());
        assertNull(newNote.getParent());
    }


}