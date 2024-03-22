package com.os.cpu_scheduler.adapter;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ComboBoxAdapter {
    private MFXComboBox<String> comboBox;
    private ObservableList<String> choicesList;

    public ComboBoxAdapter(MFXComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    public MFXComboBox<String> getComboBox() {
        return comboBox;
    }

    public void initComboBox(ArrayList<String> choices) {
        choicesList = FXCollections.observableList(choices);
        comboBox.setItems(choicesList);
    }

    public void addItem(String item) {
        choicesList.add(item);
    }

    public void removeItem(String item) {
        int index = -1;
        for (int i = 0; i < choicesList.size(); i++) {
            if (item.equalsIgnoreCase(choicesList.get(i))){
                index = i;
                break;
            }
        }
        if (index >= 0)
            choicesList.remove(index);
    }
}
