package ui.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;

/**
 * Created by st on 2017/3/8.
 */
public class SinglePieChart extends Pane {

    public SinglePieChart(int numOfUp, int numOfDown, int numOfOthers, String type1, String type2, String title) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data(type1, numOfUp),
                        new PieChart.Data(type2, numOfDown),
                        new PieChart.Data("其他", numOfOthers));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle(title);
        chart.getStylesheets().add("ui/MyChart.css");
        getChildren().add(chart);
    }

}
