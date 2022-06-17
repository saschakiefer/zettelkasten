package com.saschakiefer.slipbox.application.ports.output;

import com.saschakiefer.slipbox.domain.entity.Template;

public interface TemplateManagementOutputPort {
    Template retrieveTemplate();
}
