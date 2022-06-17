package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.application.ports.output.TemplateManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@ApplicationScoped
public class TemplateManagementFileAdapter implements TemplateManagementOutputPort {

    private final Template template;

    public TemplateManagementFileAdapter() {
        String slipNoteTemplate = ConfigProvider.getConfig().getValue("slipbox.template", String.class);
        log.debug("Reading template from {}", slipNoteTemplate);

        String content = "";

        try {
            content = FileUtils.readFileToString(new File(slipNoteTemplate), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            log.warn("Template could not be read");
        }

        template = new Template(content);

    }

    @Override
    public Template retrieveTemplate() {
        return template;
    }
}
