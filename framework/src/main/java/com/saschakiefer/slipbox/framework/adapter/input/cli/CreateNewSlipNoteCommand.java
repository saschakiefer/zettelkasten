package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import lombok.NoArgsConstructor;
import picocli.CommandLine;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@NoArgsConstructor
@CommandLine.Command(name = "create", mixinStandardHelpOptions = true, description = "Create a new slip note")
public class CreateNewSlipNoteCommand implements Runnable {
    @Inject
    CreateSlipNoteUseCase createSlipNoteUseCase;

    @Inject
    SlipNoteManagementOutputPort test;

    @CommandLine.Option(names = {"-p", "--parent"}, description = "Parent slip note (full file name)")
    String parent;

    @CommandLine.Option(names = {"-t", "--title"}, description = "Slip note title", required = true)
    String title;

    @Override
    public void run() {
        if (parent == null) {
            SlipNote newNote = createSlipNoteUseCase.createSlipNote(title);
        }
        System.out.println(String.format("Create Slip note '%s' with parent '%s'", title, parent));
    }
}
