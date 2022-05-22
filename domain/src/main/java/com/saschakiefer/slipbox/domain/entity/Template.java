package com.saschakiefer.slipbox.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Template {
    // ToDO make configurable
    public static final String PARENT_PREFIX = "_Vorgänger:_";

    private String template;
}
