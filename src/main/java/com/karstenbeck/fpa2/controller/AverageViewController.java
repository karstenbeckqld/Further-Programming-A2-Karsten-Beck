package com.karstenbeck.fpa2.controller;

import com.karstenbeck.fpa2.core.MyHealth;
import com.karstenbeck.fpa2.model.PatientRecord;
import com.karstenbeck.fpa2.model.RecordFinder;
import com.karstenbeck.fpa2.utilities.FXMLUtility;
import com.karstenbeck.fpa2.utilities.PatientRecordComparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * The AverageViewController class lets the display and filter the saved patient records.
 *
 * @author Karsten Beck
 * @version 1.0 (20/05/2023)
 */
public class AverageViewController extends Controller {

    @FXML
    private Button lastWeek, lastMonth, lastYear, bloodPressure, temp, weight, close;

    @FXML
    private LineChart<String, Float> lineChart;

    @FXML
    private DatePicker startDate, endDate;

    private String patientId;

    private final static String DATE_PATTERN = "dd/MM/yyyy";

    private int dateRange;

    private String categoryType;

    private LocalDate today;

    /**
     * The pattern field serves as a central template for the date format to display and to save to the database.
     */
    private final String pattern = "dd/MM/yyyy";
    private SimpleDateFormat dateFormat;

    private ObservableList<PatientRecord> records;

    public void initialize() throws ParseException {

        /* First, we obtain the patientId for the currently logged in patient. */
        this.patientId = MyHealth.getMyHealthInstance().getCurrentPatient().getPatientId();

        /* Then we define a new date format. */
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        /* We set the today variable to the current day. */
        this.today = LocalDate.now();

        /* Now we get the records for the currently registered patient from the database. */
        this.records = new RecordFinder().where("patientId", patientId).getData("records");

        /* We set the data range value to 1 on default as this selects one day less than the current date.  */
        this.dateRange = 1;

        /* We set the data range value to 'weight' on default, so that we can load a dataset when the class gets
        called. */
        this.categoryType = "weight";

        /* BUTTON ACTIONS ------------------------------------------------------------------------------------------- */

        /* The close button will call the close() method when clicked. */
        this.close.setOnAction(event -> {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* The weight button will call the displayChart() method when clicked and set the category to "weight". */
        this.weight.setOnAction(actionEvent -> {
            this.categoryType = "weight";
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        /* The temp button will call the displayChart() method when clicked and set the category to "temperature". */
        this.temp.setOnAction(actionEvent -> {
            this.categoryType = "temperature";
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        /* The bloodPressure button will call the displayChart() method when clicked and set the category to "disBp". */
        this.bloodPressure.setOnAction(actionEvent -> {
            this.categoryType = "sysBp";
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        /* The lastWeek, lastMonth and lastYear buttons all trigger the selectRange() method that returns an instance of
         * this class and sets the dateRange variable to a value matching the range the button suggests.
         */
        this.lastWeek.setOnAction(event -> {
            selectRange("week");
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        this.lastMonth.setOnAction(event -> {
            selectRange("month");
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        this.lastYear.setOnAction(event -> {
            selectRange("year");
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        /* When the user enters a date via the datepicker, we trigger an update of the chart when that happens. This way,
         * the user doesn't have to press the 'weight', 'temperature' or 'blood pressure' button again after selecting a
         * date.
         */
        this.startDate.setOnAction(actionEvent -> {
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        this.endDate.setOnAction(actionEvent -> {
            try {
                displayChart(this.records, this.categoryType, this.dateRange);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        /* As we've set the type and range, we call the displayChart() method here to load the screen with a chart and
         * not empty.
         */
        displayChart(this.records, this.categoryType, this.dateRange);
    }

    /**
     * The selectRange() method allows the user to select a predefined range of dates (e.g., a week, a month or a
     * year back). It returns the AverageViewController class and sets the dateRange value to whatever the user chooses.
     *
     * @param range The range to display as String
     * @return The AverageViewController class instance.
     */
    private AverageViewController selectRange(String range) {

        /* According to the string passed into this method, we set the dateRange instance variable to a defined value
         * that can get used by the getSelectedDateRange to filter for the appropriate time intervals. */
        if (range.equals("week")) {
            this.dateRange = 8;
        } else if (range.equals("month")) {
            this.dateRange = 30;
        } else if (range.equals("year")) {
            this.dateRange = 366;
        } else {
            this.dateRange = 1;
        }

        /* Here we return the instance of the AverageViewController class with an updated value for the dateRange. */
        return this;
    }

    /**
     * The displayChart() method displays a line chart with values from a list of patient records.
     *
     * @param data The list of records for a given patient.
     * @param type The type of record to get displayed, options are weight, temperature and sysBp (also displays diaBp)
     * @param dayAdjustment The value to adjust the date range, according to what a user chooses to display.
     * @throws ParseException As we parse the stored values from PatientRecord from String to Float, we might encounter
     * a ParseException.
     */
    private void displayChart(ObservableList<PatientRecord> data, String type, int dayAdjustment) throws ParseException {

        /* We perform a lot of filtering for the display of the data, why we define two new ObservableList<PatientRecord>
         * here to receive the filter results. */
        ObservableList<PatientRecord> dateRange, filteredRange;

        /* Here we set the value to be looked up in the patient records to the value passed into this method. */
        for (PatientRecord record : data) {
            record.setValue(type);
        }

        /* Now we clear the old chart data and define the series to get displayed. We define two series as the blood
        pressure values come in two sets (systolic and diastolic) */
        this.lineChart.getData().clear();
        XYChart.Series<String, Float> series1 = new XYChart.Series<>();
        this.lineChart.setLegendVisible(true);

        /* If we find dates picked in the date picker, we can filter the records to match the start and end dates. */
        if (this.startDate.getValue() != null && this.endDate.getValue() != null) {

            /* As the date range is two defined dates, we can set the adjustment just to one day, which will subtract
             * one day from the start date in the getSelectedDateRange() method.
             */
            dateRange = getSelectedDateRange(data, 1);

        } else if (dayAdjustment > 1) {

            /* If the dayAdjustment variable is set to anything greater than one, it means the user pressed one of the
             * predefined range buttons. Then we use whatever adjustment is stored in dayAdjustment. As this value is
             * preset to one for the end date, we only have to adjust the start date.
             */
            dateRange = getSelectedDateRange(data, dayAdjustment);

        } else {

            /* If there is no selection in terms of date at all, we display all data. */
            dateRange = data;

        }

        /* As lists in Java Collections are not necessarily sorted, we take the filtered by date result set and use the
         * sortDates() method to sort the content according to date. */
        filteredRange = sortDates(dateRange);

        /* We have three data fields to display. Weight, Temperature and systolic as well as diastolic blood pressure.
         * The latter is usually displayed together, why we add both as series to the chart when the value 'sysBp' gets
         * received. We also set the legend accordingly.
         */
        if (type.equals("sysBp")) {

            /* As ea have defined series1 already at the top of the method, we define a second series here (also that not
             * always a second series gets created, even when the values are not requested).
             */
            XYChart.Series<String, Float> series2 = new XYChart.Series<>();

            /* Now we add all relevant values to the series while we parse the String values from the PatientRecord
             * class to floats.
             */
            for (PatientRecord record : filteredRange) {
                series1.getData().add(new XYChart.Data<>(record.getDateAsString(), Float.valueOf(record.getSysBp())));
                series1.setName("SYSTOLIC BP");
                series2.getData().add(new XYChart.Data<>(record.getDateAsString(), Float.valueOf(record.getDiaBp())));
                series2.setName("DIASTOLIC BP");
            }

            /* Now we add the series to the chart. */
            this.lineChart.getData().addAll(series1, series2);

            /* If the type passed in is not 'sysBp', we can call the getValue() method on each record to obtain the
             * relevant data. */
        } else {
            for (PatientRecord record : filteredRange) {

                /* Here we can use the series defined at the top of the method and add the relevant records to it. */
                series1.getData().add(new XYChart.Data<>(record.getDateAsString(), Float.valueOf((String) record.getValue())));

                /* We now set the legend to the type value passed into this method. As this comes in lowercase, we added
                 * the step of converting it to uppercase before adding it to the legend.  */
                String category = this.categoryType.toUpperCase();
                series1.setName(category);
            }

            /* Now we can display the chart. */
            this.lineChart.getData().add(series1);
        }
    }

    /**
     * The close() method closes the scene in the view and lets the dashboard return to its initial display.
     *
     * @throws IOException As we use the FXMLLoader class, the method might throw an IOException/
     */
    private void close() throws IOException {

        /* First we need to get the root of the close button and cast it to an AnchorPane. Because the root scene
        (Dashboard) is an AnchorPane too, that's possible. */
        AnchorPane mainWindow = (AnchorPane) this.close.getScene().getRoot();

        /* We then get the first child of this root, which happens to be the main window. */
        AnchorPane contentContainer = (AnchorPane) mainWindow.getChildren().get(0);

        /* Firstly, we clear the content not to print the new content on top of the old one. */
        contentContainer.getChildren().clear();

        /* Now we load the new scene. */
        FXMLLoader tableViewLoader = new FXMLLoader(FXMLUtility.recordListing);

        /* Assign the new scene to a pane. */
        AnchorPane averageView = tableViewLoader.load();

        /* Now we establish a reference to the root scene's first child. */
        AnchorPane main = (AnchorPane) mainWindow.getChildren().get(0);

        /* Here we add the new view to the main AnchorPane. */
        main.getChildren().addAll(averageView.getChildren());

        /* Now we load the corresponding controller. */
        TableViewController tableViewController = tableViewLoader.getController();
    }

    /**
     * The getSelectedDateRange() method filters patient records according to a start and end date
     * entered by the user, as well as according to set date intervals as defined in the FXML file's buttons.
     *
     * @param data The list of patient records as ObservableList&lt;PatientRecord&gt;
     * @return A List&lt;PatientRecord&gt; of filtered patient records
     */
    private ObservableList<PatientRecord> getSelectedDateRange(ObservableList<PatientRecord> data, int dayAdjustment) {

        /* To have the start and end dates in scope, they get defined here. */
        LocalDate start, end;

        /* When the user defines a date range via the date pickers, we set the start date to the picked start date and
         * the end date to the picked end date.
         * Because the filter only displays what is between the two dates and to include the entered end date in the
         * query, we add a day to the end date, as we'll never display values from the future. We also have to do this
         * to the start date, however as this varies, we pass in the dayAdjustment variable which will determine how
         * many days the start date must be back.
         *
         * This value is det to one by default, as this is what we need to add when the user picks a date from the date
         * picker. For the predefined ranges (week, month, year), the respective values (8, 30, 366) will be used.
         */
        if (this.startDate.getValue() != null && this.endDate.getValue() != null) {

            /* Here we use the dates entered by the user through the date picker. The value for dayAdjustment will be 1. */
            start = this.startDate.getValue().minusDays(dayAdjustment);
            end = this.endDate.getValue().plusDays(1);

        } else {

            /* We can then set the start date to today's date minus the required adjustment. If no predefined range gets
             * selected. it'll be one, for week it'll be 8, for month it'll be 30 and for year it'll be 366. The end
             * date is then today's date plus one day.
             */
            start = this.today.minusDays(dayAdjustment);
            end = this.today.plusDays(1);
        }

        /* We now use a stream to filter the patient records according to the date range entered and return the result. */
        return data.stream()
                .filter(p -> (p.getDateAsDate().isAfter(start) && p.getDateAsDate().isBefore(end)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * The filterDates() method filters the patient records by date ascending.
     *
     * @param data The ObservableList&lt;PatientRecord&gt; list containing the patient's records.
     * @return The filtered ObservableList&lt;PatientRecord&gt;
     */
    private ObservableList<PatientRecord> sortDates(ObservableList<PatientRecord> data) {

        /* Because ObservableLists are part of the Java Collections, we can use Collections.sort to sort the records in
         * the data variable according to their date. For this purpose, we have created the PatientRecordComparator class
         * that compares the date of one record to the one of another one.
         * The Collections class consists of static methods that operate on or return collections and return a new
         * collection backed by a specified collection.
         * This way, we can filter the final record list with this method before we display the data.
         */
        Collections.sort(data, new PatientRecordComparator());

        /* Here we do the actual sorting by comparing the two dates of two records. */
        data.sort(Comparator.comparing(PatientRecord::getDateAsDate));

        /* Now we return the sorted ObservableList. */
        return data;
    }

}
