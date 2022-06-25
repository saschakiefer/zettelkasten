package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.VisualizeSlipBoxUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.TreeMap;

@NoArgsConstructor
@ApplicationScoped
public class VisualizeSlipBoxInputPort implements VisualizeSlipBoxUseCase {
    @Inject
    SlipNoteManagementOutputPort slipNoteManagement;

    @Override
    public TreeMap<SlipNoteId, SlipNote> retrieveAllNotesAsTree() {
        return slipNoteManagement.retrieveAllRootNotes();
    }

    @Override
    public TreeMap<SlipNoteId, SlipNote> retrieveNoteAsTree(SlipNoteId slipNoteId) {
        SlipNote slipNote = slipNoteManagement.retrieveSlipNote(slipNoteId);

        TreeMap<SlipNoteId, SlipNote> noteMap = new TreeMap<>();
        noteMap.put(slipNote.getSlipNoteId(), slipNote);
        return noteMap;
    }
}
