package com.saschakiefer.slipbox.framework.adapter.output.file;

import com.saschakiefer.slipbox.domain.exception.SlipNoteInconcistencyException;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@TestProfile(SipNoteFactoryInconcistencyTest.TestProfile.class)
class SipNoteFactoryInconcistencyTest {

    @Test
    void creteFromFileWithId_withDublicatId_thrwsExceptio() {
        assertThrows(SlipNoteInconcistencyException.class, () -> SlipNoteFactory.creteFromFileById(
                new SlipNoteId("1")
        ));
    }

    public static class TestProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("slipbox.path", "./src/test/resources/slipbox/inconsistent");
        }
    }
}