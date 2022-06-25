package com.saschakiefer.slipbox.application.ports.output;

import com.saschakiefer.slipbox.domain.entity.SlipNote;

public interface SlipNotePresenterOutputPort {
    void present(SlipNote note, boolean rootNoteOnly);
}
