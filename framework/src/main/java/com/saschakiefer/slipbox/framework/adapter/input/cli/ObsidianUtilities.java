package com.saschakiefer.slipbox.framework.adapter.input.cli;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.framework.adapter.output.file.SlipNoteFileFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.web.util.UriUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ObsidianUtilities {
    static void openNoteInObsidian(SlipNote newNote) {
        if (SystemUtils.IS_OS_MAC) {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|green Open Slip Note in Obsidian.|@"));

            String url = "open?path=" + UriUtils.encodePath(SlipNoteFileFactory.createById(newNote.getSlipNoteId()).getPath(), StandardCharsets.UTF_8.name());
            log.debug("Open note on obsidian://" + url);


            try {
                new ProcessBuilder("open", "obsidian://" + url)
                        .redirectErrorStream(false)
                        .start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(CommandLine.Help.Ansi.AUTO.string("@|red Sorry, I'm only working on Mac.|@"));
            System.exit(1);
        }
    }
}