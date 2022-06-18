package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.ports.output.TemplateManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.NoArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@NoArgsConstructor
@ApplicationScoped
public class CreateSlipNoteInputPort implements CreateSlipNoteUseCase {
    @Inject
    SlipNoteManagementOutputPort slipNoteManagement;

    @Inject
    TemplateManagementOutputPort templateManagement;

    @Override
    public SlipNote createSlipNote(String title, SlipNoteId parentSlipNoteId) {
        SlipNote parent = slipNoteManagement.retrieveSlipNote(parentSlipNoteId);

        SlipNote newSlipNote = SlipNote.builder()
                .slipNoteId(parent.getNextChildId())
                .title(title)
                .parent(parent)
                .build();

        newSlipNote.setContent(templateManagement.retrieveTemplate().process(newSlipNote));

        try {
            slipNoteManagement.persistSlipNote(newSlipNote);
            return newSlipNote;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public SlipNote createSlipNote(String title) {
        SlipNoteId newId = slipNoteManagement.retrieveNextRootId();

        SlipNote newSlipNote = SlipNote.builder()
                .slipNoteId(newId)
                .title(title)
                .build();

        newSlipNote.setContent(templateManagement.retrieveTemplate().process(newSlipNote));

        try {
            slipNoteManagement.persistSlipNote(newSlipNote);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newSlipNote;
    }
}
