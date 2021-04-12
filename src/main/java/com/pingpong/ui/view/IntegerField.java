package com.pingpong.ui.view;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class IntegerField extends TextField {
    private Integer value;
    private Integer defaultValue;

    public Integer getIntValue() {
        return value;
    }

    public IntegerField(String label, Integer defaultValue, String placeHolder) {
        super(label);
        value = defaultValue;
        this.defaultValue = defaultValue;
        setPlaceholder(placeHolder);
        setValueChangeMode(ValueChangeMode.EAGER);
        addValueChangeListener(e -> textChange(e));
    }

    public void textChange(ComponentValueChangeEvent event) {
        try {
            if (event.getValue() == null || ((String) event.getValue()).isEmpty()) {
                value = defaultValue;
                setValue("");
            } else {
                value = Integer.parseInt((String)event.getValue());
            }
        } catch (NumberFormatException e) {
            if (((String)event.getOldValue()).isEmpty()) {
                value = defaultValue;
            } else {
                value = Integer.parseInt((String) event.getOldValue());
            }
            setValue((String)event.getOldValue());
        }
    }
}
