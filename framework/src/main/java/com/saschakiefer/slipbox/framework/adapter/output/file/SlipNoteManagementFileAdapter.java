package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;

public class SlipNoteManagementFileAdapter implements SlipNoteManagementOutputPort {
    // Root Note Patter: "# - Filename"
    String ROOT_NOTE_PATTERN = "\\d\\s-\\s.*";
    String FILE_EXTENSION = ".md";

    @Getter
    @Setter
    String slipBoxDir = "."; // ToDo add configuration from Quarkus

    @Override
    public SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId) {
        // ToDo Implement (Test with ' - ' in File name (not only delimiter)
        throw new UnsupportedOperationException();
    }

    @Override
    public SlipNoteId retrieveNextRootId() {
        TreeSet<String> idList = new TreeSet<>();

        try {
            Files.walkFileTree(Paths.get(slipBoxDir), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (!Files.isDirectory(file) && file.getFileName().toString().matches(ROOT_NOTE_PATTERN)) {
                        String[] components = file.getFileName().toString().split(SlipNote.DELIMITER);
                        idList.add(components[0]);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (idList.size() == 0) {
            return new SlipNoteId("1");
        } else {
            return new SlipNoteId(idList.last()).getNextPeerId();
        }
    }

    @Override
    public void persistSlipNote(SlipNote slipNote) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                slipBoxDir + File.separator + slipNote.getFullTitle() + FILE_EXTENSION
        ));

        writer.write(slipNote.getContent());

        writer.close();
    }
}
