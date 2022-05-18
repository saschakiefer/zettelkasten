package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
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

        SlipNoteManagementOutputPort mockedOutputPort = mock(SlipNoteManagementOutputPort.class);
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

        SlipNoteManagementOutputPort mockedOutputPort = mock(SlipNoteManagementOutputPort.class);
        when(mockedOutputPort.retrieveSlipNote(testNote.getSlipNoteId())).thenReturn(testNote);

        CreateSlipNoteInputPort inputPort = new CreateSlipNoteInputPort(mockedOutputPort);
        SlipNote newNote = inputPort.createSlipNote("Test", testNote.getSlipNoteId());

        assertEquals("Test", newNote.getTitle());
        assertEquals("1.1", newNote.getSlipNoteId().toString());
        assertEquals(testNote, newNote.getParent());
    }

    @Test
    void createSlipNote_WithoutParent_ReturnsNewSlipNote() {
        SlipNoteManagementOutputPort mockedOutputPort = mock(SlipNoteManagementOutputPort.class);
        when(mockedOutputPort.retrieveNextRootId()).thenReturn(new SlipNoteId("3"));

        CreateSlipNoteInputPort inputPort = new CreateSlipNoteInputPort(mockedOutputPort);
        SlipNote newNote = inputPort.createSlipNote("Test");

        assertEquals("Test", newNote.getTitle());
        assertEquals("3", newNote.getSlipNoteId().toString());
        assertNull(newNote.getParent());
    }


}