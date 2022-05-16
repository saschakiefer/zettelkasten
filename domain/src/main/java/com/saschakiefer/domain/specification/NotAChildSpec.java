package com.saschakiefer.domain.specification;

import com.saschakiefer.domain.entity.SlipNote;
import com.saschakiefer.domain.exception.GenericSpecificationException;
import com.saschakiefer.domain.specification.shared.AbstractSpecification;

public class NotAChildSpec extends AbstractSpecification<SlipNote> {

    private final SlipNote parent;

    public NotAChildSpec(SlipNote parent) {
        this.parent = parent;
    }

    @Override
    public boolean isSatisfiedBy(SlipNote slipNote) {
        if (slipNote.getContent() == null)
            return false;

        return slipNote.getContent().contains(parent.getSlipNoteId().toString());
    }

    @Override
    public void check(SlipNote slipNote) throws GenericSpecificationException {
        if (!isSatisfiedBy(slipNote))
            throw new GenericSpecificationException("Zettel " + slipNote.getSlipNoteId()
                    + " is not a child of " + parent.getSlipNoteId()
                    + ". There is no reference to the parent found in the content");
    }
}
