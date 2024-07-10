package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class SettingsView extends VerticalLayout {


    ComboBox<String> listDigits = new ComboBox<>();
    Checkbox allScoringSounds = new Checkbox();
    PageGame pageGame;

    public SettingsView(PageGame pageGame) {

        this.pageGame = pageGame;

        listDigits.setItems(Utils.getFolderAvalaible());
        listDigits.setLabel("Chiffres pour le pointage :");
        listDigits.setValue("Normal");
        listDigits.setId("listDigits");

        listDigits.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Utils.setupDigitsWithNewConfig(event.getValue());
            }
        });

        allScoringSounds.setLabel("All scoring sound");
        allScoringSounds.setValue(false);
        allScoringSounds.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Utils.setAllScoringSound(event.getValue());
                pageGame.refreshSoundList();
            }
        });

        setAlignItems(FlexComponent.Alignment.CENTER);
        add(listDigits, allScoringSounds);

        setVisible(false);



    }

}
