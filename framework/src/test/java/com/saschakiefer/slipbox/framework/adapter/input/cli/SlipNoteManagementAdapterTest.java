package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import org.junit.jupiter.api.BeforeAll;

import java.util.ServiceLoader;

class SlipNoteManagementAdapterTest {
    SlipNoteManagementAdapter slipNoteManagementAdapter;

    @BeforeAll
    protected void loadPortsAndUseCases() {
        // Load Implementation
        CreateSlipNoteUseCase createSlipNoteUseCase;

        ServiceLoader<CreateSlipNoteUseCase> loaderUseCase = ServiceLoader.load(CreateSlipNoteUseCase.class);
        createSlipNoteUseCase = loaderUseCase.findFirst().get();

        ServiceLoader<SlipNoteManagementOutputPort> loaderOutputPort = ServiceLoader.load(SlipNoteManagementOutputPort.class);
        createSlipNoteUseCase.setOutputPort(loaderOutputPort.findFirst().get());

        slipNoteManagementAdapter = new SlipNoteManagementAdapter(createSlipNoteUseCase);
    }
}