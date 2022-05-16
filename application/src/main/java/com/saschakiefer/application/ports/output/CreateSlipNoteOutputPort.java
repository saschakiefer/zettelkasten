package com.saschakiefer.application.ports.output;

import com.saschakiefer.domain.entity.SlipNote;
import com.saschakiefer.domain.vo.SlipNoteId;

public interface CreateSlipNoteOutputPort {
    SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId);
}
