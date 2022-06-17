package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.ports.output.TemplateManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.entity.Template;
import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class CreateSlipNoteInputPortTest {

    @Inject
    CreateSlipNoteUseCase createSlipNoteUseCase;

    @BeforeEach
    void setup() {
        // Mock Framework Template Manager -> empty template
        TemplateManagementOutputPort templateManagement = mock(TestTemplateManagementOutputPort.class);
        when(templateManagement.retrieveTemplate()).thenReturn(new Template(""));
        QuarkusMock.installMockForType(templateManagement, TemplateManagementOutputPort.class);
    }

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

        // Mock Framework - Slip Note
        SlipNoteManagementOutputPort slipNoteManagement = mock(TestSlipNoteManagementOutputPort.class);
        when(slipNoteManagement.retrieveSlipNote(testNote.getSlipNoteId())).thenReturn(testNote);
        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        SlipNote newNote = createSlipNoteUseCase.createSlipNote("Test", testNote.getSlipNoteId());

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

        // Mock Framework - Slip Note
        SlipNoteManagementOutputPort slipNoteManagement = mock(TestSlipNoteManagementOutputPort.class);
        when(slipNoteManagement.retrieveSlipNote(testNote.getSlipNoteId())).thenReturn(testNote);
        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        SlipNote newNote = createSlipNoteUseCase.createSlipNote("Test", testNote.getSlipNoteId());

        assertEquals("Test", newNote.getTitle());
        assertEquals("1.1", newNote.getSlipNoteId().toString());
        assertEquals(testNote, newNote.getParent());
    }

    @Test
    void createSlipNote_WithoutParent_ReturnsNewSlipNote() {
        // Mock Framework - Slip Note
        SlipNoteManagementOutputPort slipNoteManagement = mock(TestSlipNoteManagementOutputPort.class);
        when(slipNoteManagement.retrieveNextRootId()).thenReturn(new SlipNoteId("3"));
        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        SlipNote newNote = createSlipNoteUseCase.createSlipNote("Test");

        assertEquals("Test", newNote.getTitle());
        assertEquals("3", newNote.getSlipNoteId().toString());
        assertNull(newNote.getParent());
    }

}

