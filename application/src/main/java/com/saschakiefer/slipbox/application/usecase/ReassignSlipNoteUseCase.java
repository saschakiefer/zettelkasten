package com.saschakiefer.slipbox.application.usecase;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;

public interface ReassignSlipNoteUseCase {
    SlipNote reassign(SlipNoteId assignee, SlipNoteId to);

    SlipNote makeRootNote(SlipNoteId assignee);
}
