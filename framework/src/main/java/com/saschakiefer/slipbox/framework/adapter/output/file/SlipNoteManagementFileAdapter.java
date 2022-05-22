package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;


public class SlipNoteManagementFileAdapter implements SlipNoteManagementOutputPort {
    public static String FILE_EXTENSION = ".md";

    @Getter
    @Setter
    String slipBoxPath = "."; // ToDo add configuration from Quarkus

    @Override
    public SlipNote retrieveSlipNote(SlipNoteId slipNoteId) {
        return SlipNoteFactory.creteFromFileWithId(slipNoteId, slipBoxPath);
    }

    @Override
    public SlipNoteId retrieveNextRootId() {
        // Root Note Pattern: "# - Filename"
        String ROOT_NOTE_PATTERN = "\\d\\s-\\s.*";

        TreeSet<String> idList = new TreeSet<>();

        try {
            Files.walkFileTree(Paths.get(slipBoxPath), new SimpleFileVisitor<>() {
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
        File file = new File(slipBoxPath + File.separator + slipNote.getFullTitle() + FILE_EXTENSION);
        FileUtils.writeStringToFile(file, slipNote.getContent(), StandardCharsets.UTF_8.name());
    }
}
