package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.Template;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestProfile(TemplateManagementFileAdapterTest.TestProfile.class)
class TemplateManagementFileAdapterTest {

    @Test
    public void retrieveTemplate_WithInvalidFileName_ReturnsEmptyTemplate() {
        Template out = new TemplateManagementFileAdapter().retrieveTemplate();

        assertEquals("TestTemplate", out.getTemplate());
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("slipbox.template", "./src/test/resources/slipbox/template/Zettel.md");
        }
    }
}