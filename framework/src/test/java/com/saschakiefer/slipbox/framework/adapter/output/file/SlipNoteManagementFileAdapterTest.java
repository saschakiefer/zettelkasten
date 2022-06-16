package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@TestProfile(SlipNoteManagementFileAdapterTest.TestProfile.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SlipNoteManagementFileAdapterTest {
    SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();

    @Test
    @Order(1)
    void retrieveNextRootId_WithFilledSlipBox_ReturnsNextRootId() {
        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("3", nextId.toString());
    }

    @Test
    @Order(2)
    void retrieveSlipNote_WithChildren_ReturnSlipNote() {
        SlipNote slipNote = fileAdapter.retrieveSlipNote(new SlipNoteId("1"));

        assertEquals("1", slipNote.getSlipNoteId().toString());
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("slipbox.path", "./src/test/resources/slipbox/has-elements");
        }
    }
}