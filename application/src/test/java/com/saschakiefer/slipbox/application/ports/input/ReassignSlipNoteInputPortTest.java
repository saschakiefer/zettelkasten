package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.ports.output.TemplateManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.ReassignSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.entity.Template;
import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class ReassignSlipNoteInputPortTest {

    @Inject
    ReassignSlipNoteUseCase reassignSlipNoteUseCase;

    @BeforeEach
    void setUp() {
        // Mock Framework Template Manager -> empty template
        TemplateManagementOutputPort templateManagement = mock(TemplateManagementOutputPortTest.class);
        when(templateManagement.retrieveTemplate()).thenReturn(new Template(""));
        QuarkusMock.installMockForType(templateManagement, TemplateManagementOutputPort.class);
    }

    @Test
    void reassign_toOtherSLipNote_ReturnsReassignedSlipNoteWithCorrectSubTree() {

        // ARRANGE
        SlipNote target = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.3.2"))
                .title("Test Target")
                .build();

        SlipNote assignee = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.2"))
                .title("Test Parent")
                .build();

        try {
            assignee.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2.1")).content("_Vorgänger:_ [[1.2 - Test Parent 1]]").build());

            SlipNote child = SlipNote.builder().slipNoteId(new SlipNoteId("1.2.2")).content("_Vorgänger:_ [[1.2 - Test Parent 2]]").build();
            child.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2.2.1")).content("_Vorgänger:_ [[1.2.2 - Test Parent 2.1]]").build());

            assignee.addChild(child);

            assignee.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2.3")).content("_Vorgänger:_ [[1.2 - Test Parent 3]]").build());
        } catch (GenericSpecificationException e) {
            throw new RuntimeException(e);
        }

        // Mock Framework - Slip Note
        SlipNoteManagementOutputPort slipNoteManagement = mock(SlipNoteManagementOutputPortTest.class);
        when(slipNoteManagement.retrieveSlipNote(assignee.getSlipNoteId())).thenReturn(assignee);
        when(slipNoteManagement.retrieveSlipNote(target.getSlipNoteId())).thenReturn(target);
        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        // ACT
        SlipNote newNote = reassignSlipNoteUseCase.reassign(assignee.getSlipNoteId(), target.getSlipNoteId());

        // ASSERT
        assertNotNull(newNote);
        assertEquals(target, newNote.getParent());
        assertEquals("1.3.2.1", newNote.getSlipNoteId().toString());
        assertTrue(assignee.getChildren().containsKey(new SlipNoteId("1.3.2.1.1")));

        assertTrue(assignee.getChildren().containsKey(new SlipNoteId("1.3.2.1.2")));
        assertTrue(assignee.getChildren().get(new SlipNoteId("1.3.2.1.2")).getChildren().containsKey(new SlipNoteId("1.3.2.1.2.1")));

        assertTrue(assignee.getChildren().containsKey(new SlipNoteId("1.3.2.1.3")));
    }
}