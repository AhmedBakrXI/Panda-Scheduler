package com.os.cpu_scheduler.adapter;

import com.os.cpu_scheduler.process.Process;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.ObservableList;

public class TableViewAdapter {
    private MFXTableView<Process> tableView;
    private ObservableList<Process> list;

    public TableViewAdapter(MFXTableView<Process> tableView, ObservableList<Process> list) {
        this.tableView = tableView;
        this.list = list;
    }

    public void setupTable(boolean priorityEnable) {
        MFXTableColumn<Process> nameCol = new MFXTableColumn<>("Name");
        MFXTableColumn<Process> arrivalCol = new MFXTableColumn<>("Arrival Time");
        MFXTableColumn<Process> burstCol = new MFXTableColumn<>("Burst Time");
        MFXTableColumn<Process> remainingCol = new MFXTableColumn<>("Remaining Time");


        nameCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getName));
        arrivalCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getArrivalTime));
        burstCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getBurstTime));
        remainingCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getRemainingTime));

        tableView.getTableColumns().addAll(nameCol, arrivalCol, burstCol);

        if (priorityEnable) {
            addPriorityColumn();
        }

        tableView.getTableColumns().add(remainingCol);

        tableView.autosizeColumnsOnInitialization();
        tableView.setItems(list);
    }

    public void clearTable() {
        list.clear();
    }

    public void addPriorityColumn() {
        MFXTableColumn<Process> priorityCol = new MFXTableColumn<>("Priority");
        priorityCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getPriority));
        tableView.getTableColumns().add(priorityCol);
    }

    public void removePriorityColumn() {
        tableView.getTableColumns().remove(3);
    }
}
