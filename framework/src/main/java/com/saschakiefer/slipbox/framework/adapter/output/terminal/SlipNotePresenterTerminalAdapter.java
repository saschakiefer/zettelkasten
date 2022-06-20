package com.saschakiefer.slipbox.framework.adapter.output.terminal;

import com.saschakiefer.slipbox.application.ports.output.SlipNotePresenterOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import java.util.Iterator;

@ApplicationScoped
public class SlipNotePresenterTerminalAdapter implements SlipNotePresenterOutputPort {
    @Override
    public void present(SlipNote note) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string(noteToString(note)));
    }

    private String noteToString(SlipNote note) {
        StringBuilder buffer = new StringBuilder(100);
        print(note, buffer, "", "");
        return buffer.toString();
    }

    private void print(SlipNote note, StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append("@|faint,white ");
        buffer.append(prefix);
        // ToDo: Make only root Elements bold
        // ToDo: Make only root Elements bold
        buffer.append("|@@|bold ");
        buffer.append(note.getTitle());
        buffer.append("|@ @|faint,white (");
        buffer.append(note.getSlipNoteId().toString());
        buffer.append(")|@\n");

        for (Iterator<SlipNote> it = note.getChildren().values().iterator(); it.hasNext(); ) {
            SlipNote next = it.next();
            if (it.hasNext()) {
                print(next, buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                print(next, buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
