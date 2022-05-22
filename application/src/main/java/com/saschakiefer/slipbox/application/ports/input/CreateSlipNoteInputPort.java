package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class CreateSlipNoteInputPort implements CreateSlipNoteUseCase {
    SlipNoteManagementOutputPort slipNoteManagement;

    public CreateSlipNoteInputPort(SlipNoteManagementOutputPort slipNoteManagement) {
        this.slipNoteManagement = slipNoteManagement;
    }

    @Override
    public SlipNote createSlipNote(String title, SlipNoteId parentSlipNoteId) {
        SlipNote parent = slipNoteManagement.retrieveSlipNote(parentSlipNoteId);

        SlipNoteId newId;

        if (parent.getChildren().isEmpty()) {
            newId = parent.getSlipNoteId().getFirstChildId();
        } else {
            newId = parent.getChildren().lastEntry().getValue().getSlipNoteId().getNextPeerId();
        }

        SlipNote newSlipNote = SlipNote.builder()
                .slipNoteId(newId)
                .title(title)
                .parent(parent)
                .build();

        try {
            slipNoteManagement.persistSlipNote(newSlipNote);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newSlipNote;
    }

    @Override
    public SlipNote createSlipNote(String title) {
        SlipNoteId newId = slipNoteManagement.retrieveNextRootId();

        SlipNote newSlipNote = SlipNote.builder()
                .slipNoteId(newId)
                .title(title)
                .build();

        try {
            slipNoteManagement.persistSlipNote(newSlipNote);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newSlipNote;
    }

    @Override
    public void setOutputPort(SlipNoteManagementOutputPort slipNoteManagementOutputPort) {
        this.slipNoteManagement = slipNoteManagementOutputPort;
    }
}
