package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.application.ports.output.SlipNotePresenterOutputPort;
import com.saschakiefer.slipbox.application.usecase.VisualizeSlipBoxUseCase;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import com.saschakiefer.slipbox.framework.adapter.output.file.SlipNoteFactory;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.TreeMap;

@Dependent
@NoArgsConstructor
@Slf4j
@CommandLine.Command(name = "print", mixinStandardHelpOptions = true, description = "Print the slip box as tree.")
public class PrintSlipBoxCommand implements Runnable {
    @Inject
    VisualizeSlipBoxUseCase visualizeSlipBoxUseCase;

    @Inject
    SlipNotePresenterOutputPort slipNotePresenter;

    @CommandLine.Parameters(index = "0", description = "Start note (default: 0 = all notes)", defaultValue = "0")
    String startNote;

    @CommandLine.Option(names = {"-r", "--root-only"}, description = "Only print root notes")
    boolean rootOnly;

    @Override
    public void run() {
        SlipNote slipBox;

        if (startNote == null || "0".equals(startNote))
            slipBox = SlipNote.builder()
                    .title("Slip Box")
                    .slipNoteId(new SlipNoteId("0"))
                    .children(visualizeSlipBoxUseCase.retrieveAllNotesAsTree())
                    .build();
        else {
            SlipNote startNoteObject = SlipNoteFactory.creteFromFileById(new SlipNoteId(startNote));

            TreeMap<SlipNoteId, SlipNote> startNoteAsMap = new TreeMap<>();
            startNoteAsMap.put(startNoteObject.getSlipNoteId(), startNoteObject);

            slipBox = SlipNote.builder()
                    .title("Slip Box")
                    .slipNoteId(new SlipNoteId("0"))
                    .children(startNoteAsMap)
                    .build();
        }

        slipNotePresenter.present(slipBox, rootOnly);
    }
}
