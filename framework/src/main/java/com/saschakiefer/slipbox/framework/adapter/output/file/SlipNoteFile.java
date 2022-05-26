package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class SlipNoteFile extends File {
    public static final String FILE_EXTENSION = ".md";

    @Getter
    private final String slipNoteId;

    @Getter
    private final String title;

    public SlipNoteFile(String pathname) {
        super(pathname);

        if (isValidFileName(FilenameUtils.getName(pathname))) {
            String baseName = FilenameUtils.getBaseName(this.getName());
            slipNoteId = baseName.split(SlipNote.DELIMITER)[0];
            title = baseName.split(SlipNote.DELIMITER)[1];
        } else {
            throw new InvalidFilnameException();
        }

    }

    private boolean isValidFileName(String fileName) {
        return fileName.matches("^\\d+(\\.(\\d+))*(" + SlipNote.DELIMITER + ").*" + FILE_EXTENSION.replace(".", "\\."));
    }
}
