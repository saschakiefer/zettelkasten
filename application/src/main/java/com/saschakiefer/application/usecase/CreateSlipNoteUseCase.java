package com.saschakiefer.application.usecase;

import com.saschakiefer.domain.entity.SlipNote;
import com.saschakiefer.domain.vo.SlipNoteId;

public interface CreateSlipNoteUseCase {
    SlipNote createSlipNote(String title, SlipNoteId parentSlipNoteId);

    SlipNote createSlipNote(String title);
}
