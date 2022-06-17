package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.usecase.CreateSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.exception.SlipNoteNotFoundException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import com.saschakiefer.slipbox.framework.adapter.output.file.InvalidFilnameException;
import com.saschakiefer.slipbox.framework.adapter.output.file.SlipNoteFile;
import com.saschakiefer.slipbox.framework.adapter.output.file.SlipNoteFileFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@NoArgsConstructor
@Slf4j
@CommandLine.Command(name = "create", mixinStandardHelpOptions = true, description = "Create a new slip note")
public class CreateNewSlipNoteCommand implements Runnable {
    @Inject
    CreateSlipNoteUseCase createSlipNoteUseCase;

    @CommandLine.Parameters(index = "0", description = "Slip note title")
    String title;

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "0..1")
    ParentGroup parentGroup;

    @Override
    public void run() {

        SlipNote newNote;
        if (parentGroup == null) {
            newNote = createSlipNoteUseCase.createSlipNote(title);
        } else {
            SlipNoteId slipNoteId;
            SlipNoteFile parentFile = null;

            try {
                if (parentGroup.parentFile == null) {
                    // Check if file exists by deviating using the factory -> Throws exception if not
                    slipNoteId = new SlipNoteId(parentGroup.parentId);
                } else {
                    // Check if the expected file format -> Throws exception if not
                    // We use the SlipNoteFile Object for conveniently extract the ID from the file name
                    slipNoteId = new SlipNoteFile(parentGroup.parentFile).getSlipNoteId();
                }

                // Check if file exists and has the expected file format by deviating using the factory -> Throws exception if not
                parentFile = SlipNoteFileFactory.createById(slipNoteId);
            } catch (InvalidFilnameException | SlipNoteNotFoundException e) {
                System.out.println(CommandLine.Help.Ansi.AUTO.string("@|red " + e.getMessage() + "|@"));
                System.exit(1);
            }

            newNote = createSlipNoteUseCase.createSlipNote(title, parentFile.getSlipNoteId());
        }

        System.out.println(CommandLine.Help.Ansi.AUTO.string(
                String.format("@|green Slip note '%s' successfully created.|@", newNote.getFullTitle())));
    }

    static class ParentGroup {
        @CommandLine.Option(names = {"-pf", "--parent-file"}, description = "Parent slip note (full file name)", required = true)
        String parentFile;

        @CommandLine.Option(names = {"-p", "--parent"}, description = "Parent slip note ID", required = true)
        String parentId;
    }
}
