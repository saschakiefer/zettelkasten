package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class SlipNoteFile extends File {
    public static final String FILE_EXTENSION = ".md";
    @Getter
    private final SlipNoteId slipNoteId;
    @Getter
    private final String title;

    public SlipNoteFile(String pathname) {
        super(pathname);

        if (isSlipNoteValidFileName()) {
            String baseName = FilenameUtils.getBaseName(this.getName());
            slipNoteId = new SlipNoteId(baseName.split(SlipNote.DELIMITER)[0]);
            title = baseName.split(SlipNote.DELIMITER)[1];
        } else {
            throw new InvalidFilnameException(String.format("File name '%s' does not match the expected pattern", pathname));
        }

    }

    public boolean isSlipNoteValidFileName() {
        return FilenameUtils.getName(this.getPath())
                .matches("^\\d+(\\.(\\d+))*(" + SlipNote.DELIMITER + ").*" + FILE_EXTENSION.replace(".", "\\."));
    }

    public boolean isRootSlipNote() {
        return slipNoteId.isRoot();
    }
}