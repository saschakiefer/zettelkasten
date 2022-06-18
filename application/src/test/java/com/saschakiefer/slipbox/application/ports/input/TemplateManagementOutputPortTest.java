package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.TemplateManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.Template;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class TemplateManagementOutputPortTest implements TemplateManagementOutputPort {
    @Override
    public Template retrieveTemplate() {
        return null;
    }
}
