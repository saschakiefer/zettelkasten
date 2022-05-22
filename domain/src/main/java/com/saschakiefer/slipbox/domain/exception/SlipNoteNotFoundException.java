package com.saschakiefer.slipbox.domain.exception;

public class SlipNoteNotFoundException extends RuntimeException {
    public SlipNoteNotFoundException(String message) {
        super(message);
    }
}
