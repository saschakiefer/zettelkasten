package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;

public class SlipNoteManagementAdapter {
    private CreateSlipNoteUseCase createSlipNoteUseCase;

    public SlipNoteManagementAdapter(CreateSlipNoteUseCase createSlipNoteUseCase) {
        this.createSlipNoteUseCase = createSlipNoteUseCase;
    }
}
