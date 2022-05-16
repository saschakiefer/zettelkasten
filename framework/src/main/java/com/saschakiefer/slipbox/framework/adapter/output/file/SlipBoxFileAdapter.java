package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.application.ports.output.CreateSlipNoteOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;

public class SlipBoxFileAdapter implements CreateSlipNoteOutputPort {

    @Getter
    @Setter
    String slipBoxDir = "."; // ToDo add configuration from Quarkus

    @Override
    public SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId) {
        return null;
    }

    @Override
    public SlipNoteId retrieveNextRootId() {
        TreeSet<String> idList = new TreeSet<>();

        try {
            Files.walkFileTree(Paths.get(slipBoxDir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!Files.isDirectory(file)) {
                        String[] components = file.getFileName().toString().split(SlipNote.DELIMITER);
                        if (components.length > 1) {
                            idList.add(components[0]);
                        }
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
}
