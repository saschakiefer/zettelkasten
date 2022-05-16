package com.saschakiefer.application.ports.input;

import com.saschakiefer.application.ports.output.CreateSlipNoteOutputPort;
import com.saschakiefer.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.domain.entity.SlipNote;
import com.saschakiefer.domain.vo.SlipNoteId;
import lombok.AllArgsConstructor;

// @NoArgsConstructor // ToDo: Reset maybe after Quarkus is introduced?
@AllArgsConstructor
public class CreateSlipNoteInputPort implements CreateSlipNoteUseCase {
    CreateSlipNoteOutputPort createSlipNoteOutputPort;

    @Override
    public SlipNote createSlipNote(String title, SlipNoteId parentSlipNoteId) {
        SlipNote parent = createSlipNoteOutputPort.retrieveSlipNote(parentSlipNoteId);

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
        SlipNoteId newId = createSlipNoteOutputPort.retrieveNextRootId();

        return SlipNote.builder()
                .slipNoteId(newId)
                .title(title)
                .build();
    }
}
