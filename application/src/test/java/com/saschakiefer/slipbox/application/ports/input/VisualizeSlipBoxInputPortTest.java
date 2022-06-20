package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.VisualizeSlipBoxUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class VisualizeSlipBoxInputPortTest {

    @Inject
    VisualizeSlipBoxUseCase visualizeSlipBoxUseCase;

    @Test
    void getAllNotesAsFlatList_returnsFlatSortedListOfNotes() {
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

        TreeMap<SlipNoteId, SlipNote> rootNotes = new TreeMap<>();
        rootNotes.put(n8.getSlipNoteId(), n8);
        rootNotes.put(n9.getSlipNoteId(), n9);
        rootNotes.put(n10.getSlipNoteId(), n10);
        when(slipNoteManagement.retrieveAllRootNotes()).thenReturn(rootNotes);

        QuarkusMock.installMockForType(slipNoteManagement, SlipNoteManagementOutputPort.class);

        // ACT
        TreeMap<SlipNoteId, SlipNote> notes = visualizeSlipBoxUseCase.retrieveAllNotesAsTree();

        // ASSERT
        assertNotNull(notes);
        assertEquals(3, notes.size());
    }
}