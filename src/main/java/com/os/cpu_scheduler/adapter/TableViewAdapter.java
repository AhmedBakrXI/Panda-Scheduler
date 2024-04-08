package com.os.cpu_scheduler.adapter;

import com.os.cpu_scheduler.process.Process;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import java.util.stream.IntStream;

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

        remainingCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getRemainingTime) {{
            setAlignment(Pos.CENTER);
        }});
        remainingCol.setAlignment(Pos.CENTER);

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
        priorityCol.setRowCellFactory(process -> new MFXTableRowCell<>(Process::getPriority) {{
            setAlignment(Pos.CENTER);
        }});
        priorityCol.setAlignment(Pos.CENTER);

        tableView.getTableColumns().add(priorityCol);

    }

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
