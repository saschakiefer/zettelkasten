package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.usecase.ReassignSlipNoteUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@NoArgsConstructor
@Slf4j
@CommandLine.Command(name = "reassign", aliases = {"r"}, mixinStandardHelpOptions = true, description = "Reassign Slip Note to new Parent Note")
public class ReassignSlipNoteCommand implements Runnable {
    @Inject
    ReassignSlipNoteUseCase reassignSlipNoteUseCase;

    @CommandLine.Parameters(index = "0", description = "Slip Note ID to be moved")
    private String assignee;

    @CommandLine.Parameters(index = "1", description = "Slip Note ID of new parent")
    private String to;

    @CommandLine.Option(names = "--open", description = "Open document after creation in Obsidian (default: ${DEFAULT-VALUE}))", negatable = true)
    private boolean openFileAfterCreation = false;

    @Override
    public void run() {

        try {
            SlipNote newNote = reassignSlipNoteUseCase.reassign(new SlipNoteId(assignee), new SlipNoteId(to));

            System.out.println(CommandLine.Help.Ansi.AUTO.string(
                    String.format("@|green Slip Note '%s' successfully reassigned.|@", newNote.getFullTitle())));

            if (openFileAfterCreation) {
                ObsidianUtilities.openNoteInObsidian(newNote);
            }
        } catch (RuntimeException e) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|red " + e.getMessage() + "|@"));
            System.exit(1);
        }
    }
}
