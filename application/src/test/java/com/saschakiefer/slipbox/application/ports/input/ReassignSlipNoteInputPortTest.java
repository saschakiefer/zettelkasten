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
    void reassign_NoteWithoutParentToOtherSLipNote_ReturnsReassignedSlipNoteWithCorrectSubTree() {
        // ARRANGE
        SlipNote target = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.3.2"))
                .title("Test Target")
                .build();

        SlipNote assignee = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.2"))
                .title("Test Parent")
                .content("_Vorgänger:_ [[]]")
                .build();

        try {
            assignee.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2.1")).content("_Vorgänger:_ [[1.2 - Test Parent]] - 1.2.1").build());

            SlipNote child = SlipNote.builder().slipNoteId(new SlipNoteId("1.2.2")).content("_Vorgänger:_ [[1.2 - Test Parent]] - 1.2.2").title("Mid").build();
            child.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2.2.1")).content("_Vorgänger:_ [[1.2.2 - Mid]] - 1.2.2.1").build());

            assignee.addChild(child);

            assignee.addChild(SlipNote.builder().slipNoteId(new SlipNoteId("1.2.3")).content("_Vorgänger:_ [[1.2 - Test Parent]] - 1.2.3").build());
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

    @Test
    void reassign_NoteWithParentToOtherSLipNote_ReturnsReassignedSlipNoteWithCorrectSubTree() {
        // ARRANGE
        SlipNote target = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.3.2"))
                .title("Test Target")
                .build();

        SlipNote parent = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Parent")
                .build();

        SlipNote assignee = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.2"))
                .title("Test Assignee")
                .content("Aliases: [\"1.2\"];_Vorgänger:_ [[1 - Test Parent]]")
                .parent(parent)
                .build();

        // Mock Framework - Slip Note
        SlipNoteManagementOutputPort slipNoteManagement = mock(SlipNoteManagementOutputPortTest.class);
        when(slipNoteManagement.retrieveSlipNote(assignee.getSlipNoteId())).thenReturn(assignee);
        when(slipNoteManagement.retrieveSlipNote(target.getSlipNoteId())).thenReturn(target);
        when(slipNoteManagement.retrieveSlipNote(parent.getSlipNoteId())).thenReturn(parent);
        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        // ACT
        SlipNote newNote = reassignSlipNoteUseCase.reassign(assignee.getSlipNoteId(), target.getSlipNoteId());

        // ASSERT
        assertNotNull(newNote);
        assertEquals(target, newNote.getParent());
        assertEquals("1.3.2.1", newNote.getSlipNoteId().toString());
        assertEquals("Aliases: [\"1.2\"];_Vorgänger:_ [[1.3.2 - Test Target]]", newNote.getContent());
    }

    @Test
    void reassign_ComplexSetup_ReturnsReassignedSlipNoteWithCorrectSubTree() {
        // ARRANGE
        SlipNote n8 = SlipNote.builder().slipNoteId(new SlipNoteId("8")).title("Test Title 1").content("Aliases: [\"Test Title 1\"],_Vorgänger:_ [[]]").build();
        SlipNote n81 = SlipNote.builder().slipNoteId(new SlipNoteId("8.1")).title("Test Title 2").content("Aliases: [\"Test Title 2\"],_Vorgänger:_ [[8 - Test Title 1]]").build();
        SlipNote n811 = SlipNote.builder().slipNoteId(new SlipNoteId("8.1.1")).title("Test Title 5").content("Aliases: [\"Test Title 5\"],_Vorgänger:_ [[8.1 - Test Title 2]]").build();
        SlipNote n8111 = SlipNote.builder().slipNoteId(new SlipNoteId("8.1.1.1")).title("Test Title 7").content("Aliases: [\"Test Title 7\"],_Vorgänger:_ [[8.1.1 - Test Title 5]]").build();
        SlipNote n812 = SlipNote.builder().slipNoteId(new SlipNoteId("8.1.2")).title("Test Title 6").content("Aliases: [\"Test Title 6\"],_Vorgänger:_ [[8.2 - Test Title 3]]").build();

        SlipNote n82 = SlipNote.builder().slipNoteId(new SlipNoteId("8.2")).title("Test Title 3").content("Aliases: [\"Test Title 3\"],_Vorgänger:_ [[8 - Test Title 1]]").build();

        SlipNote n83 = SlipNote.builder().slipNoteId(new SlipNoteId("8.3")).title("Test Title 4").content("Aliases: [\"Test Title 4\"],_Vorgänger:_ [[8 - Test Title 1]]").build();

        SlipNote n9 = SlipNote.builder().slipNoteId(new SlipNoteId("9")).title("Test Title 8").content("Aliases: [\"Test Title 8\"],_Vorgänger:_ [[]]").build();
        SlipNote n91 = SlipNote.builder().slipNoteId(new SlipNoteId("9.1")).title("Test Title 9").content("Aliases: [\"Test Title 9\"],_Vorgänger:_ [[9 - Test Title 8]]").build();

        SlipNote n10 = SlipNote.builder().slipNoteId(new SlipNoteId("10")).title("Test Title 10").content("Aliases: [\"Test Title 10\"],_Vorgänger:_ [[]]").build();

        try {
            n8.addChild(n81);
            n81.addChild(n811);
            n811.addChild(n8111);
            n8.addChild(n82);
            n82.addChild(n812);
            n8.addChild(n83);
            n9.addChild(n91);
        } catch (GenericSpecificationException e) {
            throw new RuntimeException(e);
        }

        // Mock Framework - Slip Note
        SlipNoteManagementOutputPort slipNoteManagement = mock(SlipNoteManagementOutputPortTest.class);
        when(slipNoteManagement.retrieveSlipNote(n8.getSlipNoteId())).thenReturn(n8);
        when(slipNoteManagement.retrieveSlipNote(n81.getSlipNoteId())).thenReturn(n81);
        when(slipNoteManagement.retrieveSlipNote(n811.getSlipNoteId())).thenReturn(n811);
        when(slipNoteManagement.retrieveSlipNote(n8111.getSlipNoteId())).thenReturn(n8111);
        when(slipNoteManagement.retrieveSlipNote(n812.getSlipNoteId())).thenReturn(n812);
        when(slipNoteManagement.retrieveSlipNote(n82.getSlipNoteId())).thenReturn(n82);
        when(slipNoteManagement.retrieveSlipNote(n83.getSlipNoteId())).thenReturn(n83);
        when(slipNoteManagement.retrieveSlipNote(n9.getSlipNoteId())).thenReturn(n9);
        when(slipNoteManagement.retrieveSlipNote(n91.getSlipNoteId())).thenReturn(n91);
        when(slipNoteManagement.retrieveSlipNote(n10.getSlipNoteId())).thenReturn(n10);
        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        // ACT
        SlipNote newNote = reassignSlipNoteUseCase.reassign(n81.getSlipNoteId(), n9.getSlipNoteId());

        // ASSERT
        assertNotNull(newNote);
    }
}