package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;

@Mock
@ApplicationScoped
public class TestSlipNoteManagementOutputPort implements SlipNoteManagementOutputPort {

    @Override
    public SlipNote retrieveSlipNote(SlipNoteId parentSlipNoteId) {
        return null;
    }

    @Override
    public SlipNoteId retrieveNextRootId() {
        return null;
    }

    @Override
    public void persistSlipNote(SlipNote slipNote) throws IOException {

    }
}
