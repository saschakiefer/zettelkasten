package com.saschakiefer.slipbox.domain.entity;

import com.saschakiefer.slipbox.domain.vo.SlipNoteId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Builder
@Getter
public class Template {
    public static final String PARENT_PREFIX = "_Vorgänger:_";
    public static final String TITLE_TOKEN = "<% title %>";
    public static final String FULL_TITLE_TOKEN = "<% fullTitle %>";
    public static final String ID_TOKEN = "<% id %>";
    public static final String PARENT_TITLE_TOKEN = "<% parentTitle %>";
    public static final String PARENT_FULL_TITLE_TOKEN = "<% parentFullTitle %>";
    public static final String PARENT_ID_TOKEN = "<% parentId %>";

    private String template;

    public static Optional<SlipNoteId> getParentIdFromContent(String content) {
        final String regex = PARENT_PREFIX + "\\s*\\[\\[(.*?)" + SlipNote.DELIMITER + ".*\\]\\]";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return Optional.of(new SlipNoteId(matcher.group(1)));
        }

        return Optional.empty();
    }

    public String process(SlipNote slipNote) {

        String parentFullTitle = "";
        String parentTitle = "";
        String parentId = "";
        if (slipNote.getParent() != null) {
            parentFullTitle = slipNote.getParent().getFullTitle();
            parentTitle = slipNote.getParent().getTitle();
            parentId = slipNote.getParent().getSlipNoteId().toString();
        }

        return template
                .replace(TITLE_TOKEN, slipNote.getTitle())
                .replace(FULL_TITLE_TOKEN, slipNote.getFullTitle())
                .replace(TITLE_TOKEN, slipNote.getTitle())
                .replace(ID_TOKEN, slipNote.getSlipNoteId().toString())
                .replace(FULL_TITLE_TOKEN, slipNote.getFullTitle())
                .replace(PARENT_TITLE_TOKEN, parentTitle)
                .replace(PARENT_FULL_TITLE_TOKEN, parentFullTitle)
                .replace(PARENT_ID_TOKEN, parentId);
    }
}
