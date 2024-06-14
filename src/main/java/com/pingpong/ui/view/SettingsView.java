package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SettingsView extends VerticalLayout {


    ComboBox<String> listDigits = new ComboBox<>();

    public SettingsView() {

        listDigits.setItems(Utils.getFolderAvalaible());
        listDigits.setLabel("Chiffres pour le pointage :");
        listDigits.setValue("Normal");
        listDigits.setId("listDigits");

        listDigits.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Utils.setupDigitsWithNewConfig(event.getValue());
            }
        });
        setAlignItems(FlexComponent.Alignment.CENTER);
        add(listDigits);

        setVisible(false);
    }

}
