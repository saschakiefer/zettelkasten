package com.saschakiefer.slipbox.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Template {
    public static final String PARENT_PREFIX = "_Vorgänger:_";
    public static final String TITLE_TOKEN = "<% title %>";
    public static final String FULL_TITLE_TOKEN = "<% full_title %>";
    public static final String PARENT_TITLE_TOKEN = "<% parent %>";


    private String template;

    public String process(SlipNote slipNote) {

        String parentString = "";
        if (slipNote.getParent() != null)
            parentString = slipNote.getParent().getFullTitle();

        return template
                .replace(TITLE_TOKEN, slipNote.getTitle())
                .replace(FULL_TITLE_TOKEN, slipNote.getFullTitle())
                .replace(PARENT_TITLE_TOKEN, parentString);
    }
}
