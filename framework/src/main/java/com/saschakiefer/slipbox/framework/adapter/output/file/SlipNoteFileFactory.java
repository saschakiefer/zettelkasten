package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.exception.SlipNoteInconcistencyException;
import com.saschakiefer.slipbox.domain.exception.SlipNoteNotFoundException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.File;
import java.io.FileFilter;

@Slf4j
public class SlipNoteFileFactory {
    public static SlipNoteFile createById(SlipNoteId slipNoteId) {
        String slipBoxPath = ConfigProvider.getConfig().getValue("slipbox.path", String.class);
        log.debug("Working in {}", slipBoxPath);

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