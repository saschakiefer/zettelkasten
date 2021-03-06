package com.saschakiefer.slipbox.application.usecase;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;

public interface CreateSlipNoteUseCase {
    SlipNote createSlipNote(String title, SlipNoteId parentSlipNoteId);

    SlipNote createSlipNote(String title);
}
