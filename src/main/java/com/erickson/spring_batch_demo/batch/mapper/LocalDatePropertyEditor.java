package com.erickson.spring_batch_demo.batch.mapper;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDatePropertyEditor extends PropertyEditorSupport {

    private final DateTimeFormatter formatter;

    public LocalDatePropertyEditor(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public String getAsText() {
        LocalDate value = (LocalDate) getValue();
        return (value != null) ? value.format(formatter) : "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.isEmpty()) {
            setValue(LocalDate.parse(text, formatter));
        }
        else {
            setValue(null);
        }
    }
}
