package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlipNoteManagementFileAdapterTest {

    @Test
    void retrieveNextRootId_WithFilledSlipBox_ReturnsNextRootId() {
        SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();
        fileAdapter.setSlipBoxDir("./src/test/resources/slipbox/has-elements");

        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("3", nextId.toString());
    }

    @Test
    void retrieveNextRootId_WithEmptySlipBox_ReturnsNextRootId() {
        SlipNoteManagementFileAdapter fileAdapter = new SlipNoteManagementFileAdapter();
        fileAdapter.setSlipBoxDir("./src/test/resources/slipbox/empty");

        SlipNoteId nextId = fileAdapter.retrieveNextRootId();

        assertNotNull(nextId);
        assertEquals("1", nextId.toString());
    }
}