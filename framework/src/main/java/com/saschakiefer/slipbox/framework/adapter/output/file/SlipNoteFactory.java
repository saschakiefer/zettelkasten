package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.entity.Template;
import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.exception.SlipNoteInconcistencyException;
import com.saschakiefer.slipbox.domain.exception.SlipNoteNotFoundException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
public class SlipNoteFactory {
    public static SlipNote createFromFile(SlipNoteFile file) {
        return null;
    }

    public static SlipNote creteFromFileWithId(SlipNoteId slipNoteId, String slipBoxPath) {
        SlipNoteFile file = getSlipNoteFile(slipNoteId, slipBoxPath);

        String content;
        try {
            content = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SlipNote newNote = SlipNote.builder()
                .slipNoteId(slipNoteId)
                .title(file.getTitle())
                .content(content)
                .children(new TreeMap<>())
                .build();

        for (SlipNoteId id : findChildren(slipNoteId, slipBoxPath)) {
            try {
                newNote.addChild(creteFromFileWithId(id, slipBoxPath));
            } catch (GenericSpecificationException e) {
                // The tree should be consistent, since it's automatically loaded based on the consistent patterns
                // If not a generic exception is OK
                throw new RuntimeException(e);
            }
        }

        return newNote;
    }

    private static List<SlipNoteId> findChildren(SlipNoteId slipNoteId, String slipBoxPath) {
        final String parentRegex = "'"
                + Template.PARENT_PREFIX
                + "\\s*\\[\\["
                + slipNoteId
                + SlipNote.DELIMITER
                + "'";

        List<SlipNoteId> foundIds = new ArrayList<>();

        if (SystemUtils.IS_OS_MAC) {
            ProcessBuilder builder = new ProcessBuilder("sh", "-c", "grep -Eilr " + parentRegex)
                    .redirectErrorStream(true)
                    .directory(new File(slipBoxPath));

            Process process;
            try {
                process = builder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    SlipNoteFile f = new SlipNoteFile(line);
                    foundIds.add(f.getSlipNoteId());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Sorry, I'm only working on Mac.");
        }

        return foundIds;
    }

    private static SlipNoteFile getSlipNoteFile(SlipNoteId slipNoteId, String slipBoxPath) {
        File dir = new File(slipBoxPath);
        FileFilter fileFilter = new WildcardFileFilter(
                slipNoteId.toString() + SlipNote.DELIMITER + "*" + SlipNoteFile.FILE_EXTENSION
        );
        File[] files = dir.listFiles(fileFilter);

        assert files != null;
        if (files.length < 1) {
            throw new SlipNoteNotFoundException(String.format("Slip note with ID '%s' not found", slipNoteId));
        } else if (files.length > 1) {
            throw new SlipNoteInconcistencyException(String.format("More than one slip note with id '%s' found", slipNoteId));
        }

        return new SlipNoteFile(files[0].getPath());
    }
}
