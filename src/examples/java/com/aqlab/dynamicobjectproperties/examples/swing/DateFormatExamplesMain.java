package com.aqlab.dynamicobjectproperties.examples.swing;

import com.aqlab.dynamicobjectproperties.property.ChainedProperty;
import com.aqlab.dynamicobjectproperties.property.FunctionalProperty;
import com.aqlab.dynamicobjectproperties.property.FunctionalPropertyFactory;
import com.aqlab.dynamicobjectproperties.property.ObjectProperty;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * DEMONSTRATION ONLY. UNMAINTAINED CODE. USE AT YOUR OWN RISK.
 * <p>
 * This is an example using {@link FunctionalProperty}, {@link ChainedProperty} and {@link com.aqlab.dynamicobjectproperties.property.SelfProperty}.
 * <p>
 * This presents a JFrame. You may change the first column on each row. The rest of the cells will update accordingly.
 * <p>
 * TODO give columns proper names instead of using {@link ObjectProperty#getUniqueIdentifier()}
 */
public class DateFormatExamplesMain {
    public static void main(final String[] argv) {
        final List<AtomicReference<String>> dateTimeList = new ArrayList<>();
        dateTimeList.add(new AtomicReference<>(LocalDateTime.of(2002, 1, 7, 0, 0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        dateTimeList.add(new AtomicReference<>(LocalDateTime.of(2012, 3, 18, 4, 16).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        dateTimeList.add(new AtomicReference<>(LocalDateTime.of(2025, 12, 31, 5, 59).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        final ObjectProperty<AtomicReference<String>> parseProperty = FunctionalPropertyFactory.INSTANCE.<AtomicReference<String>>createFunctionalPropertyObjectValue(String.class, LocalDateTime.class, "!FUNC::parse",
                s -> {
                    try {
                        return LocalDateTime.parse(s.get(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    } catch (final Exception ex) {
                        return null;
                    }
                }, null);
        final List<ObjectProperty<AtomicReference<String>>> propertyList = new ArrayList<>();
        propertyList.add(FunctionalPropertyFactory.INSTANCE.<AtomicReference<String>>createFunctionalPropertyObjectValue(
                AtomicReference.class, String.class, "!FUNC::deref", ref -> ref.get(), (ref, s) -> ref.set((String) s)));
        propertyList.add(ChainedProperty.create(
                parseProperty,
                FunctionalPropertyFactory.INSTANCE.createFunctionalPropertyIntValue(
                        LocalDateTime.class, "!FUNC::year", LocalDateTime::getYear, null)
        ));
        propertyList.add(ChainedProperty.create(
                parseProperty,
                FunctionalPropertyFactory.INSTANCE.createFunctionalPropertyIntValue(
                        LocalDateTime.class, "!FUNC::month", LocalDateTime::getMonthValue, null)
        ));
        propertyList.add(ChainedProperty.create(
                parseProperty,
                FunctionalPropertyFactory.INSTANCE.createFunctionalPropertyIntValue(
                        LocalDateTime.class, "!FUNC::day", LocalDateTime::getDayOfMonth, null)
        ));
        propertyList.add(ChainedProperty.create(
                parseProperty,
                FunctionalPropertyFactory.INSTANCE.<LocalDateTime, DayOfWeek>createFunctionalPropertyObjectValue(
                        LocalDateTime.class, DayOfWeek.class, "!FUNC::dayOfWeek", LocalDateTime::getDayOfWeek, null)
        ));
        propertyList.add(ChainedProperty.create(
                parseProperty,
                FunctionalPropertyFactory.INSTANCE.<LocalDateTime>createFunctionalPropertyObjectValue(
                        LocalDateTime.class, String.class, "!FUNC::dateOnlyFormat", dt -> dt.format(DateTimeFormatter.ISO_LOCAL_DATE), null)
        ));
        propertyList.add(ChainedProperty.create(
                parseProperty,
                FunctionalPropertyFactory.INSTANCE.<LocalDateTime>createFunctionalPropertyObjectValue(
                        LocalDateTime.class, String.class, "!FUNC::timeOnlyFormat", dt -> dt.format(DateTimeFormatter.ISO_TIME), null)
        ));

        final JFrame frame = new JFrame("DateFormatExamplesMain");
        frame.setLayout(new BorderLayout());
        final JTable table = new JTable(new ObjectPropertyBackedTableModel<>(dateTimeList, propertyList));
        final TableCellRenderer originalRenderer = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, (table1, value, isSelected, hasFocus, row, column) -> {
            final Component c = originalRenderer.getTableCellRendererComponent(table1, value, isSelected, hasFocus, row, column);
            c.setBackground(table1.isCellEditable(row, column) ? Color.orange : null);

            return c;
        });
        frame.add(table.getTableHeader(), BorderLayout.NORTH);
        frame.add(table, BorderLayout.CENTER);
        final JLabel textArea = new JLabel("<html><p>The first column is editable.</p>" +
                "<p>The rest of the columns will change once a change is committed. Invalid format string blanks the remaining columns.</p>" +
                "<p>This table is backed by ObjectPropertyBackedTableModel which has no internal storage. Changes are reflected directly onto the dateTimeList in this class.</p>");
        frame.add(textArea, BorderLayout.SOUTH);
        frame.setPreferredSize(new Dimension(1200, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //frame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
        frame.toFront();
        frame.requestFocus();
    }
}
