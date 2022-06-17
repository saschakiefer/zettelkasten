package com.saschakiefer.slipbox.domain.entity;

import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateTest {

    Template template;

    @BeforeEach
    public void setup() {
        template = new Template("<% title %>,<% full_title %>,<% parent %>");
    }

    @Test
    void process_withoutParent_returnsCorrectContent() {
        SlipNote testNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Test Note")
                .build();

        assertEquals("Test Note,1 - Test Note,", template.process(testNote));
    }

    @Test
    void process_withParent_returnsCorrectContent() {
        SlipNote parentNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1"))
                .title("Parent")
                .build();

        SlipNote testNote = SlipNote.builder()
                .slipNoteId(new SlipNoteId("1.1"))
                .title("Test Note")
                .parent(parentNote)
                .build();

        assertEquals("Test Note,1.1 - Test Note,1 - Parent", template.process(testNote));
    }
}