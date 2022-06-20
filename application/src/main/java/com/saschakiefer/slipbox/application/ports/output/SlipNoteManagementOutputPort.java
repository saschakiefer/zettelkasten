package com.saschakiefer.slipbox.application.ports.output;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;

import java.io.IOException;
import java.util.TreeMap;

public interface SlipNoteManagementOutputPort {
    SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId);

    SlipNoteId retrieveNextRootId();

    void persistSlipNote(SlipNote slipNote) throws IOException;

    void deleteSlipNote(SlipNoteId slipNoteId);

    TreeMap<SlipNoteId, SlipNote> retrieveAllRootNotes();
}
