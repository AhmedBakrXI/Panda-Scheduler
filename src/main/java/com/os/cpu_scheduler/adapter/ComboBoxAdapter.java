package com.os.cpu_scheduler.adapter;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


/**
 * This class provides an adapter for a MaterialFX ComboBox. It allows for the
 * initialization of the ComboBox with a list of choices, the clearing of the
 * selection, the addition of new items, and the removal of existing items.
 */

public class ComboBoxAdapter {
    private final MFXComboBox<String> comboBox;
    private ObservableList<String> choicesList;


    /**
     * Constructs a new ComboBoxAdapter with the specified MaterialFX ComboBox.
     *
     * @param comboBox the MaterialFX ComboBox to adapt
     */
    public ComboBoxAdapter(MFXComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    /**
     * Retrieves the MaterialFX ComboBox associated with this adapter.
     *
     * @return the MaterialFX ComboBox associated with this adapter
     */
    public MFXComboBox<String> getComboBox() {
        return comboBox;
    }


    /**
     * Initializes the MaterialFX ComboBox with the specified list of choices.
     *
     * @param choices the list of choices to initialize the MaterialFX ComboBox with
     */
    public void initComboBox(ArrayList<String> choices) {
        choicesList = FXCollections.observableList(choices);
        comboBox.setItems(choicesList);
    }

    /**
     * Clears the selection of the MaterialFX ComboBox.
     */
    public void clear() {
        comboBox.getSelectionModel().clearSelection();
    }

    /**
     * Adds the specified item to the MaterialFX ComboBox.
     *
     * @param item the item to add to the MaterialFX ComboBox
     */
    public void addItem(String item) {
        choicesList.add(item);
    }

    /**
     * Removes the specified item from the MaterialFX ComboBox.
     *
     * @param item the item to remove from the MaterialFX ComboBox
     */
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
