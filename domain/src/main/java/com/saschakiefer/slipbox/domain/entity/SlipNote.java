package com.saschakiefer.slipbox.domain.entity;

import com.saschakiefer.slipbox.domain.exception.GenericSpecificationException;
import com.saschakiefer.slipbox.domain.specification.NotAChildSpec;
import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class SlipNote {
    public static String DELIMITER = " - ";

    private SlipNoteId slipNoteId;

    private String title;
    private SlipNote parent;
    @Builder.Default
    private String content = "";
    @Builder.Default
    private TreeMap<SlipNoteId, SlipNote> children = new TreeMap<>();

    public void reassignToParen(SlipNote parent) {
        assert parent != null;

        SlipNote original = (SlipNote) clone();

        // Update Parent Reference in Content
        if (this.parent != null) {
            // ToDo this should also consider the Template Prefix for parent using Regex
            setContent(getContent().replaceFirst("\\[\\[" + this.parent.getFullTitle() + "]]",
                    "\\[\\[" + parent.getFullTitle() + "]]"));
        } else {
            setContent(getContent().replaceFirst("\\[\\[]]", String.format("[[%s]]", parent.getFullTitle())));
        }
        this.parent = parent;
        this.slipNoteId = parent.getNextChildId();

        children = new TreeMap<>();
        for (Map.Entry<SlipNoteId, SlipNote> entry : original.children.entrySet()) {
            SlipNote v = entry.getValue();
            v.reassignToParen(this);

            try {
                addChild(v);
            } catch (GenericSpecificationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addChild(SlipNote slipNote) throws GenericSpecificationException {
        NotAChildSpec notAChildSpec = new NotAChildSpec(this);

        notAChildSpec.check(slipNote);

        slipNote.reassignToParen(this);
        children.put(slipNote.getSlipNoteId(), slipNote);
    }

    public String getFullTitle() {
        return slipNoteId + DELIMITER + title;
    }

    public SlipNoteId getNextChildId() {
        return getChildren().isEmpty()
                ? slipNoteId.getFirstChildId()
                : children.lastEntry().getValue().getSlipNoteId().getNextPeerId();
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            SlipNote slipNote = SlipNote.builder()
                    .slipNoteId(new SlipNoteId(slipNoteId.toString()))
                    .parent(parent)
                    .title(title)
                    .content(content)
                    .build();

            children.forEach((k, v) -> {
                try {
                    slipNote.addChild((SlipNote) v.clone());
                } catch (GenericSpecificationException ex) {
                    throw new RuntimeException(ex);
                }

            });
            return slipNote;
        }

    }
}
