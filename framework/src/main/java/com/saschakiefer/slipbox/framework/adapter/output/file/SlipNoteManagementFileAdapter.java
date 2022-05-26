package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;

@NoArgsConstructor
@ApplicationScoped
public class SlipNoteManagementFileAdapter implements SlipNoteManagementOutputPort {

    @Getter
    @Setter
    String slipBoxPath = "."; // ToDo add configuration from Quarkus

    @Override
    public SlipNote retrieveSlipNote(SlipNoteId slipNoteId) {
        return SlipNoteFactory.creteFromFileWithId(slipNoteId, slipBoxPath);
    }

    @Override
    public SlipNoteId retrieveNextRootId() {


        TreeSet<SlipNoteId> idList = new TreeSet<>();

        try {
            Files.walkFileTree(Paths.get(slipBoxPath), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    SlipNoteFile slipNoteFile;

                    // Files that don't match the expected pattern get ignored
                    try {
                        slipNoteFile = new SlipNoteFile(file.getFileName().toString());
                    } catch (InvalidFilnameException e) {
                        return FileVisitResult.CONTINUE;
                    }

                    if (!Files.isDirectory(file) && slipNoteFile.isRootSlipNote()) {
                        idList.add(slipNoteFile.getSlipNoteId());
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
            return idList.last().getNextPeerId();
        }
    }

    @Override
    public void persistSlipNote(SlipNote slipNote) throws IOException {
        File file = new File(slipBoxPath + File.separator + slipNote.getFullTitle() + SlipNoteFile.FILE_EXTENSION);
        FileUtils.writeStringToFile(file, slipNote.getContent(), StandardCharsets.UTF_8.name());
    }
}
