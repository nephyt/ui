package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class SettingsView extends VerticalLayout {


    ComboBox<String> listDigits = new ComboBox<>();
    Checkbox allScoringSounds = new Checkbox();
    Checkbox useVictorySongForMatchPoint = new Checkbox();
    ComboBox<Integer> listTimeToPlayVictorySong = new ComboBox<>();
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

        List<Integer> listValues = new ArrayList<>();
        for (int i = 3; i<100; ++i) {
            listValues.add(i);
        }
        listTimeToPlayVictorySong.setItems(listValues);
        listTimeToPlayVictorySong.setLabel("Temps du match point :");
        listTimeToPlayVictorySong.setValue(5);
        listTimeToPlayVictorySong.setId("timeMatchPoint");

        listTimeToPlayVictorySong.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Utils.setTimeVictorySongForMatchPoint(event.getValue());
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

        useVictorySongForMatchPoint.setLabel("Use VictorySong for Match Point");
        useVictorySongForMatchPoint.setValue(true);
        useVictorySongForMatchPoint.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Utils.setUseVictorySongForMatchPoint(event.getValue());
            }
        });

        setAlignItems(FlexComponent.Alignment.CENTER);
        add(listDigits, useVictorySongForMatchPoint, listTimeToPlayVictorySong, allScoringSounds);

        setVisible(false);



    }

}
