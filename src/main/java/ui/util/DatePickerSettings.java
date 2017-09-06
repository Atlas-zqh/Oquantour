package ui.util;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import sun.plugin2.jvm.RemoteJVMLauncher;

import javax.xml.stream.events.EndDocument;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by st on 2017/3/8.
 */
public class DatePickerSettings {

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 设置开始时间选择器格式，最早不早于2005-3-1，最晚不晚于2014-3-25
     * @param startDate
     * @return
     */
    public Callback<DatePicker, DateCell> getStartSettings(DatePicker startDate) {
        Date minDate = null;
        Date maxDate = null;
        try {
            minDate = sdf.parse("2005-2-1");
            maxDate = sdf.parse("2014-4-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Instant minInstant = minDate.toInstant();
        Instant maxInstant = maxDate.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDate minLocal = LocalDateTime.ofInstant(minInstant, zone).toLocalDate();
        LocalDate maxLocal = LocalDateTime.ofInstant(maxInstant, zone).toLocalDate();
        startDate.setValue(minLocal);
//        endDate.setValue(maxLocal);
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(minLocal) || item.isAfter(maxLocal)
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #d8e7ef;");
                                }
                            }
                        };
                    }
                };
        startDate.setDayCellFactory(dayCellFactory);
//        endDate.setDayCellFactory(dayCellFactory);
        return dayCellFactory;
    }

    /**
     * 设置结束时间选择器格式，最早不早于开始时间的后两个月，最晚不晚于开始时间的后两年，也不晚于2014-3-25
     * @param startDate
     * @param endDate
     * @return
     */
    public Callback<DatePicker, DateCell> getEndSettings(DatePicker startDate, DatePicker endDate) {
        Date maxDate = null;
        try {
            maxDate = sdf.parse("2014-4-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Instant maxInstant = maxDate.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDate maxLocal = LocalDateTime.ofInstant(maxInstant, zone).toLocalDate();
        endDate.setValue(startDate.getValue().plusYears(1));
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(startDate.getValue().plusMonths(2)) ||
                                        item.isAfter(startDate.getValue().plusYears(1)) ||
                                        item.isAfter(maxLocal)
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #d8e7ef;");
                                }
                            }
                        };
                    }
                };
        endDate.setDayCellFactory(dayCellFactory);
//        endDate.setDayCellFactory(dayCellFactory);
        return dayCellFactory;
    }

    /**
     * 设置结束时间选择器格式，最早不早于开始时间的后两个月，最晚不晚于2014-3-25
     * 该方法供回测时使用
     * @param startDate
     * @param endDate
     * @return
     */
    public Callback<DatePicker, DateCell> getBackTestEndSettings(DatePicker startDate, DatePicker endDate) {
        Date maxDate = null;
        try {
            maxDate = sdf.parse("2014-4-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Instant maxInstant = maxDate.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDate maxLocal = LocalDateTime.ofInstant(maxInstant, zone).toLocalDate();

        endDate.setValue(startDate.getValue().plusMonths(2));



        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(startDate.getValue().plusMonths(2)) ||
                                        item.isAfter(maxLocal)
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #d8e7ef;");
                                }
                            }
                        };
                    }
                };
        endDate.setDayCellFactory(dayCellFactory);
//        endDate.setDayCellFactory(dayCellFactory);
        return dayCellFactory;
    }
    public Date getDate(DatePicker datePicker) {
        LocalDate localDate = datePicker.getValue();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }
}
