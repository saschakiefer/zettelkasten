package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import com.saschakiefer.slipbox.framework.adapter.output.file.SlipNoteFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import javax.enterprise.context.Dependent;

@Dependent
@NoArgsConstructor
@Slf4j
@CommandLine.Command(name = "open", aliases = {"o"}, mixinStandardHelpOptions = true, description = "Opens a slip note in Obsidian")
public class OpenSlipNoteCommand implements Runnable {
    @CommandLine.Parameters(index = "0", description = "Note to be opened")
    String note;

    @Override
    public void run() {
        ObsidianUtilities.openNoteInObsidian(SlipNoteFactory.creteFromFileById(new SlipNoteId(note)));
    }

}
