package com.pingpong.ui.view;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class IntegerField extends TextField {
    private Integer value;

    public Integer getIntValue() {
        return value;
    }

    public IntegerField(String label, Integer defaultValue, String placeHolder) {
        super(label);
        value = defaultValue;
        setPlaceholder(placeHolder);
        setValueChangeMode(ValueChangeMode.EAGER);
        addValueChangeListener(e -> textChange(e));
    }

    public void textChange(ComponentValueChangeEvent event) {
        try {
            if (event.getValue() == null || ((String) event.getValue()).isEmpty()) {
                value = 3;
            } else {
                value = Integer.parseInt((String)event.getValue());
            }
        } catch (NumberFormatException e) {
            value = Integer.parseInt((String)event.getOldValue());
            setValue((String)event.getOldValue());
        }
    }
}
