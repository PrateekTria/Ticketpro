package com.ticketpro.model;

public interface RepeatOffenderHandler {
    public void successRepeatOffender(Violation violation, RepeatOffender repeatOffender);
    public void failRepeatOffender(String fail);
}
