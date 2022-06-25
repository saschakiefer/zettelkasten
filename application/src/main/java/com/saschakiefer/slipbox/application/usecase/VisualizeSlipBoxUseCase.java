package com.saschakiefer.slipbox.application.usecase;

import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;

import java.util.TreeMap;

public interface VisualizeSlipBoxUseCase {
    TreeMap<SlipNoteId, SlipNote> retrieveAllNotesAsTree();

    TreeMap<SlipNoteId, SlipNote> retrieveNoteAsTree(SlipNoteId slipNoteId);
}
