package com.os.cpu_scheduler.adapter;

import com.os.cpu_scheduler.process.Process;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import java.util.stream.IntStream;

/**
 * This class is an adapter that is used to connect the JavaFX TableView to the Process data model.
 * It provides a convenient way to display and manipulate the data in the table.
 */
public class TableViewAdapter {
    private final MFXTableView<Process> tableView;
    private final ObservableList<Process> list;

    /**
     * Constructs a new TableViewAdapter instance.
     *
     * @param tableView the JavaFX TableView to be connected to the Process data model
     * @param list      the ObservableList containing the data to be displayed in the TableView
     */
    public TableViewAdapter(MFXTableView<Process> tableView, ObservableList<Process> list) {
        this.tableView = tableView;
        this.list = list;
    }


    /**
     * Sets up the TableView to display the data in the Process data model.
     *
     * @param priorityEnable whether to enable the priority column in the TableView
     */
    public void setupTable(boolean priorityEnable) {
        MFXTableColumn<Process> nameCol = new MFXTableColumn<>("Name");
        MFXTableColumn<Process> arrivalCol = new MFXTableColumn<>("Arrival Time");
        MFXTableColumn<Process> burstCol = new MFXTableColumn<>("Burst Time");
        MFXTableColumn<Process> remainingCol = new MFXTableColumn<>("Remaining Time");

        nameCol.setRowCellFactory(person -> new MFXTableRowCell<>(Process::getName) {{
            setAlignment(Pos.CENTER);
        }});
        nameCol.setAlignment(Pos.CENTER);

        arrivalCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getArrivalTime) {{
            setAlignment(Pos.CENTER);
        }});
        arrivalCol.setAlignment(Pos.CENTER);

        burstCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getBurstTime) {{
            setAlignment(Pos.CENTER);
        }});
        burstCol.setAlignment(Pos.CENTER);

        remainingCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getRemainingTime) {
            {
                setAlignment(Pos.CENTER);
            }
        });
        remainingCol.setAlignment(Pos.CENTER);

        tableView.getTableColumns().addAll(nameCol, arrivalCol, burstCol);

        if (priorityEnable) {
            addPriorityColumn();
        }

        tableView.getTableColumns().add(remainingCol);

        tableView.autosizeColumnsOnInitialization();
        tableView.setItems(list);
    }

    /**
     * Clears the table.
     */
    public void clearTable() {
        list.clear();
    }


    /**
     * Adds priority column to the table.
     */
    public void addPriorityColumn() {
        MFXTableColumn<Process> priorityCol = new MFXTableColumn<>("Priority");
        priorityCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getPriority) {{
            setAlignment(Pos.CENTER);
        }});
        priorityCol.setAlignment(Pos.CENTER);

        tableView.getTableColumns().add(priorityCol);

    }

    /**
     * Removes the priority column from the table
     */
    public void removePriorityColumn() {
        int index = IntStream.range(0, tableView.getTableColumns().size())
                .filter(i -> tableView.getTableColumns()
                        .get(i)
                        .getText()
                        .equals("Priority"))
                .findFirst()
                .orElse(-1);
        if (index != -1) {
            tableView
                    .getTableColumns()
                    .remove(index);
        }
    }
}
