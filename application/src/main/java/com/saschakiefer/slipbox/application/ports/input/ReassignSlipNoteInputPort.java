package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.ReassignSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

@NoArgsConstructor
@ApplicationScoped
public class ReassignSlipNoteInputPort implements ReassignSlipNoteUseCase {
    @Inject
    SlipNoteManagementOutputPort slipNoteManagement;

    @Override
    public SlipNote reassign(SlipNoteId assignee, SlipNoteId to) {
        SlipNote toNote = slipNoteManagement.retrieveSlipNote(to);
        SlipNote assigneeNote = slipNoteManagement.retrieveSlipNote(assignee);

        Set<SlipNoteId> keysToDelete = getAllChildKeys(assigneeNote);

        assigneeNote.reassignToParen(toNote);

        try {
            slipNoteManagement.persistSlipNote(assigneeNote);
            keysToDelete.forEach(slipNoteId -> slipNoteManagement.deleteSlipNote(slipNoteId));
            return assigneeNote;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<SlipNoteId> getAllChildKeys(SlipNote slipNote) {
        Set<SlipNoteId> keys = new TreeSet<>();
        keys.add(slipNote.getSlipNoteId());

        slipNote.getChildren().values().forEach(note -> keys.addAll(getAllChildKeys(note)));

        return keys;
    }

    @Override
    public SlipNote makeRootNote(SlipNoteId assignee) {
        return null;
    }
}
