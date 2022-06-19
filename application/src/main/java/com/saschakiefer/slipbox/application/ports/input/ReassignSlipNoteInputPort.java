package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.ReassignSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@NoArgsConstructor
@ApplicationScoped
public class ReassignSlipNoteInputPort implements ReassignSlipNoteUseCase {
    @Inject
    SlipNoteManagementOutputPort slipNoteManagement;

    @Override
    public SlipNote reassign(SlipNoteId assignee, SlipNoteId to) {
        SlipNote toNote = slipNoteManagement.retrieveSlipNote(to);
        SlipNote assigneeNote = slipNoteManagement.retrieveSlipNote(assignee);

        assigneeNote.reassignToParen(toNote);

        try {
            slipNoteManagement.persistSlipNote(assigneeNote);
//            TODO: Continue Here
//            slipNoteManagement.deleteSlipNote(assignee);
            return assigneeNote;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SlipNote makeRootNote(SlipNoteId assignee) {
        return null;
    }
}
