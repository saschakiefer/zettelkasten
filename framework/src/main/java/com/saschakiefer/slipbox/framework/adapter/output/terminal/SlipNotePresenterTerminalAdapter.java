package com.saschakiefer.slipbox.framework.adapter.output.terminal;

import com.saschakiefer.slipbox.application.ports.output.SlipNotePresenterOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import java.util.Iterator;
import java.util.Objects;

@ApplicationScoped
public class SlipNotePresenterTerminalAdapter implements SlipNotePresenterOutputPort {
    private static String formatId(SlipNote note) {

        if (Objects.equals(note.getSlipNoteId().toString(), "0"))
            return "";
        else
            return "@|faint,white (" + note.getSlipNoteId().toString() + ")|@";
    }

    private static String formatTitle(SlipNote note) {
        if (note.getSlipNoteId().isRoot())
            return "@|bold " + note.getTitle() + "|@ ";
        else
            return note.getTitle() + " ";
    }

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
        buffer.append("@|faint,white ").append(prefix).append("|@ ");
        buffer.append(formatTitle(note));
        buffer.append(formatId(note));
        buffer.append("\n");

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
