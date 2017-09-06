package ui.charts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by island on 2017/3/22.
 */
public class SingleBarChart extends Pane {

    final BarChart<String, Number> bc;

    private double width;

    public SingleBarChart(List<Double> value) {
        //stage.setScene(new Scene(new SingleBarChart(value),1060, 600));

        List<Integer> intvalue = new ArrayList<>();
        int intnum = 0;
        for (int i = 0; i < value.size(); i++) {
            intnum = (int) (value.get(i).doubleValue() * 100);
            intvalue.add(intnum);
        }

        int maxLimit = Collections.max(intvalue);
        int minLimit = (-1) * Collections.min(intvalue);
        if(maxLimit + minLimit < 40){
            minLimit += (40 - maxLimit - minLimit) / 2;
            maxLimit += (40 - maxLimit - minLimit) / 2;
        }


        int[] positive = new int[maxLimit + 1];
        int[] negative = new int[minLimit + 1];

        for (int i = 0; i <= maxLimit; i++) {
            positive[i] = 0;
        }
        for (int i = 0; i <= minLimit; i++) {
            negative[i] = 0;
        }
        int num;
        for (int i = 0; i < value.size(); i++) {
            num = Math.abs(intvalue.get(i));
            if (value.get(i) >= 0) {
                positive[num]++;
            } else {
                negative[num]++;
            }
        }
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        series1.setName("正收益周期");
        series2.setName("负收益周期");
        for (int i = negative.length - 1; i > 0; i--) {
            final XYChart.Data<String, Number> data = new XYChart.Data((-1 * i) + "%", negative[i]);
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                    if (node != null) {
                        displayLabelForData(data);
                        node.setStyle("-fx-bar-fill: #7dbbfb;");
                    }
                }
            });
            series1.getData().add(data);
        }

        for (int i = 0; i < positive.length; i++) {
            final XYChart.Data<String, Number> data = new XYChart.Data(i + "%", positive[i]);
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
                    if (node != null) {
                        displayLabelForData(data);
                        node.setStyle("-fx-bar-fill: #fba71b;");
                    }
                }
            });
            series1.getData().add(data);
        }
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                if (object.intValue() != object.doubleValue())
                    return "";
                return "" + (object.intValue());
            }

            @Override
            public Number fromString(String string) {
                return 0;
            }
        });

        bc = new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("收益率分布直方图");
        bc.setPrefSize(1060, 590);
        width = -1;
        bc.setBarGap(0);
        bc.setCategoryGap(width);
        bc.getData().addAll(series1);
        bc.getStylesheets().add("ui/MyChart.css");

        this.getChildren().addAll(bc);
        //bc.getData().addAll(series2);

    }

    private void displayLabelForData(XYChart.Data<String, Number> data) {
        Node node = data.getNode();
        if (data.getYValue().intValue() != 0) {
            Text dataText = new Text(data.getYValue() + "");
            dataText.setStyle("-fx-text-fill: #838383; -fx-stroke: #838383;");
            node.parentProperty().addListener(new ChangeListener<Parent>() {
                @Override
                public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                    Group parentGroup = (Group) parent;
                    parentGroup.getChildren().add(dataText);
                }
            });

            node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
                @Override
                public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                    dataText.setLayoutX(
                            Math.round(
                                    bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                            )
                    );
                    dataText.setLayoutY(
                            Math.round(
                                    bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                            )
                    );
                    width = bounds.getWidth();
                }
            });
        }
    }
}