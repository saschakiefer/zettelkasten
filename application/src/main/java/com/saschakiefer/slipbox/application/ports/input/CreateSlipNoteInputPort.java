package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.RetrieveSlipNoteOutputPort;
import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.AllArgsConstructor;

// @NoArgsConstructor // ToDo: Reset maybe after Quarkus is introduced?
@AllArgsConstructor
public class CreateSlipNoteInputPort implements CreateSlipNoteUseCase {
    RetrieveSlipNoteOutputPort retrieveSlipNoteOutputPort;

    @Override
    public SlipNote createSlipNote(String title, SlipNoteId parentSlipNoteId) {
        SlipNote parent = retrieveSlipNoteOutputPort.retrieveSlipNote(parentSlipNoteId);

        SlipNoteId newId;

        if (parent.getChildren().isEmpty()) {
            newId = parent.getSlipNoteId().getFirstChildId();
        } else {
            newId = parent.getChildren().lastEntry().getValue().getSlipNoteId().getNextPeerId();
        }

        return SlipNote.builder()
                .slipNoteId(newId)
                .title(title)
                .parent(parent)
                .build();
    }

    @Override
    public SlipNote createSlipNote(String title) {
        SlipNoteId newId = retrieveSlipNoteOutputPort.retrieveNextRootId();

        return SlipNote.builder()
                .slipNoteId(newId)
                .title(title)
                .build();
    }
}
