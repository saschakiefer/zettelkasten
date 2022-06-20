package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.ports.output.SlipNotePresenterOutputPort;
import com.saschakiefer.slipbox.application.usecase.VisualizeSlipBoxUseCase;
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
@CommandLine.Command(name = "print", mixinStandardHelpOptions = true, description = "Print the slip box as tree.")
public class PrintSlipBoxCommand implements Runnable {
    @Inject
    VisualizeSlipBoxUseCase visualizeSlipBoxUseCase;

    @Inject
    SlipNotePresenterOutputPort slipNotePresenter;

    @CommandLine.Option(names = {"-r", "--root-only"}, description = "Only print root notes", required = false)
    String parentFile;

    @Override
    public void run() {
        // TODO: Implement -r flag
        SlipNote slipBox = SlipNote.builder()
                .title("Slip Box")
                .slipNoteId(new SlipNoteId("0"))
                .children(visualizeSlipBoxUseCase.retrieveAllNotesAsTree())
                .build();

        slipNotePresenter.present(slipBox);
    }
}
