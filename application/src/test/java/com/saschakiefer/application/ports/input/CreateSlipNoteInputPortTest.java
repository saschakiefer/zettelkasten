package com.saschakiefer.application.ports.input;

import com.saschakiefer.application.ports.output.CreateSlipNoteOutputPort;
import com.saschakiefer.domain.entity.SlipNote;
import com.saschakiefer.domain.exception.GenericSpecificationException;
import com.saschakiefer.domain.vo.SlipNoteId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateSlipNoteInputPortTest {
    SlipNote testNote;

    @BeforeEach
    void setUp() {
        testNote = SlipNote.builder()
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
    }

    @Test
    void createSlipNote_WithParent_ReturnsNewSlipNote() {
        CreateSlipNoteOutputPort mockedOutputPort = mock(CreateSlipNoteOutputPort.class);
        when(mockedOutputPort.retrieveSlipNote(testNote.getSlipNoteId())).thenReturn(testNote);

        CreateSlipNoteInputPort inputPort = new CreateSlipNoteInputPort(mockedOutputPort);
        SlipNote newNote = inputPort.createSlipNote(testNote.getSlipNoteId(), "Test");

        assertEquals("Test", newNote.getTitle());
        assertEquals("1.4", newNote.getSlipNoteId().toString());
        assertEquals(testNote, newNote.getParent());
    }
}