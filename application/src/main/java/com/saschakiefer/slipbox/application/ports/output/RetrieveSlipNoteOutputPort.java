package com.saschakiefer.slipbox.application.ports.output;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;

public interface RetrieveSlipNoteOutputPort {
    SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId);

    SlipNoteId retrieveNextRootId();
}
