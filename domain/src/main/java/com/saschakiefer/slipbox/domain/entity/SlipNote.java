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
    private SlipNote parent;
    @Builder.Default
    @Setter
    private String content = "";
    @Builder.Default
    private TreeMap<SlipNoteId, SlipNote> children = new TreeMap<>();

    public SlipNote getParent() {
        return parent;
    }

    public void setParent(SlipNote parent) {

        TreeMap<SlipNoteId, SlipNote> childrenBackup = children;
        SlipNoteId slipNoteIdBackup = slipNoteId;

        this.parent = parent;
        this.slipNoteId = parent.getNextChildId();

        children = new TreeMap<>();
        childrenBackup.forEach((k, v) -> {
            v.setParent(this);
            // ToDo this should also consider the Template Prefix for parent using Regex
            v.setContent(v.getContent().replaceFirst(slipNoteIdBackup.toString(), slipNoteId.toString()));

            try {
                addChild(v);
            } catch (GenericSpecificationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void addChild(SlipNote slipNote) throws GenericSpecificationException {
        NotAChildSpec notAChildSpec = new NotAChildSpec(this);

        notAChildSpec.check(slipNote);

        slipNote.setParent(this);
        children.put(slipNote.getSlipNoteId(), slipNote);
    }

    public String getFullTitle() {
        return slipNoteId + DELIMITER + title;
    }

    public SlipNoteId getNextChildId() {

        return getChildren().isEmpty() ? slipNoteId.getFirstChildId() : children.lastEntry().getValue().getSlipNoteId().getNextPeerId();
    }
}
