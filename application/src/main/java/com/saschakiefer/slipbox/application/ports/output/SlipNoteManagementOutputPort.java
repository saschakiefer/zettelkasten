package com.saschakiefer.slipbox.application.ports.output;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;

public interface SlipNoteManagementOutputPort {
    SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId);

    SlipNoteId retrieveNextRootId();

    boolean persistSlipNote(SlipNote slipNote);
}
