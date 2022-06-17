package com.saschakiefer.slipbox.domain.entity;

import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.specification.NotAChildSpec;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.TreeMap;

@AllArgsConstructor
@Builder
@Getter
public class SlipNote {
    public static String DELIMITER = " - ";

    private SlipNoteId slipNoteId;

    private String title;

    @Setter
    private SlipNote parent;

    @Builder.Default
    @Setter
    private String content = "";

    private TreeMap<SlipNoteId, SlipNote> children;

    public void addChild(SlipNote slipNote) throws GenericSpecificationException {
        NotAChildSpec notAChildSpec = new NotAChildSpec(this);

        notAChildSpec.check(slipNote);

        slipNote.setParent(this);
        children.put(slipNote.getSlipNoteId(), slipNote);
    }

    public String getFullTitle() {
        return slipNoteId + DELIMITER + title;
    }
}
