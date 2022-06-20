package com.saschakiefer.slipbox.application.ports.input;

import com.saschakiefer.slipbox.application.ports.output.SlipNoteManagementOutputPort;
import com.saschakiefer.slipbox.domain.entity.SlipNote;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.TreeMap;

@Mock
@ApplicationScoped
public class SlipNoteManagementOutputPortTest implements SlipNoteManagementOutputPort {

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

    @Override
    public void deleteSlipNote(SlipNoteId slipNoteId) {

    }

    @Override
    public TreeMap<SlipNoteId, SlipNote> retrieveAllRootNotes() {
        return null;
    }
}
