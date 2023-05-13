package com.karstenbeck.fpa2.services;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class CustomCheckBoxTableCell<S, T> extends TableCell<S, T> {
    private final CheckBox checkBox;
    private S rowData;

    public CustomCheckBoxTableCell() {
        checkBox = new CheckBox();
        checkBox.setOnAction(event -> {
            S rowData = getTableView().getItems().get(getIndex());
            this.rowData = rowData;
            TableColumn<S, T> column = getTableColumn();
            T cellData = column.getCellData(rowData);
            commitEdit((T) cellData);
        });
        setGraphic(checkBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setEditable(true);
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            checkBox.setSelected((Boolean) item);
            setGraphic(checkBox);
        }
    }

    public S returnRowData(){
        return null;
    }
}