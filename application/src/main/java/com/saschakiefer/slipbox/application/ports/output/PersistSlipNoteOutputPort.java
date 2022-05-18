package com.saschakiefer.slipbox.application.ports.output;

import com.saschakiefer.slipbox.domain.entity.SlipNote;

public interface PersistSlipNoteOutputPort {
    boolean persistSlipNote(SlipNote slipNote);
}
