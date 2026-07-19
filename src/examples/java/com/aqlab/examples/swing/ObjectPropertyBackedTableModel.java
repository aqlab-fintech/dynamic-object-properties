package com.aqlab.dynamicobjectproperties.examples.swing;

import com.aqlab.dynamicobjectproperties.property.ObjectProperty;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * DEMONSTRATION ONLY. UNMAINTAINED CODE. USE AT YOUR OWN RISK.
 * <p>
 * A table model that is back by Dynamic Object Properties (DOP)
 *
 * @param <T> the row type of the table model
 */
public class ObjectPropertyBackedTableModel<T> extends AbstractTableModel {
    private final List<T> objectList;
    private final List<ObjectProperty<T>> properties;

    public ObjectPropertyBackedTableModel(final List<T> objectList, final List<ObjectProperty<T>> properties) {
        this.objectList = objectList;
        this.properties = properties;
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return properties.get(columnIndex).isWritable();
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        properties.get(columnIndex).set(objectList.get(rowIndex), aValue);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public int getRowCount() {
        return objectList.size();
    }

    @Override
    public int getColumnCount() {
        return properties.size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return properties.get(columnIndex).get(objectList.get(rowIndex));
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return properties.get(columnIndex).getValueType();
    }

    @Override
    public String getColumnName(final int column) {
        return properties.get(column).getUniqueIdentifier();
    }
}
