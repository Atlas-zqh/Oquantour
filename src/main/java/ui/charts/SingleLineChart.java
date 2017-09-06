package ui.charts;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by st on 2017/3/7.
 */
public class SingleLineChart extends Pane {

    /**
     * 利用传入信息构造两只股票的对比折线图
     *
     * @param value1
     * @param value2
     * @param dateList1
     * @param dateList2
     * @param title
     */
    public SingleLineChart(List<Double> value1, List<Double> value2, List<Date> dateList1,
                           List<Date> dateList2, String stock1, String stock2, String title) {

        double maxLimit = Math.max(Collections.max(value1), Collections.max(value2));
        double minLimit = Math.min(Collections.min(value1), Collections.min(value2));
        double tickUnit = (maxLimit - minLimit) / 10;

        // x轴
        final CategoryAxis xAxis = new CategoryAxis();
//        xAxis.setTickLabelsVisible(false);

        //y轴
//        final NumberAxis yAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(minLimit - tickUnit, maxLimit + tickUnit, tickUnit);
//        yAxis.setAutoRanging(true);

//        yAxis.setLowerBound(minLimit);
//        yAxis.setUpperBound(maxLimit);
//        System.out.println("45: " + maxLimit + " " + minLimit);
        yAxis.setMinorTickCount(0);


        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setCreateSymbols(false);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName(stock1);
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(stock2);

        // 增加数据
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");

        for (int i = 0; i < dateList1.size(); i++) {
            series1.getData().add(new XYChart.Data(sdf.format(dateList1.get(i)), value1.get(i)));
        }

        for (int i = 0; i < dateList2.size(); i++) {
            series2.getData().add(new XYChart.Data(sdf.format(dateList2.get(i)), value2.get(i)));
        }

        lineChart.getData().addAll(series1, series2);

        lineChart.setPrefSize(1060, 590);
        lineChart.setStyle("-fx-background-color: #ffffff00");
        lineChart.getStylesheets().add("ui/MyChart.css");

        Line line = new Line();
        line.setStyle("-fx-stroke: #689cc4");
        line.setStartY(35);
        line.setEndY(560);
        line.setVisible(false);

        String type = "";
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if(title.equals("策略和基准的累计收益率比较图")) {
            type = "backtest";
            yAxis.setTickLabelFormatter(new StringConverter<Number>() {
                @Override
                public String toString(Number object) {
                    return decimalFormat.format(object.doubleValue() * 100) + "%";
                }

                @Override
                public Number fromString(String string) {
                    return 0;
                }
            });
        }
        TooltipContent tooltipContent = createCursorGraphCoordsMonitorLabel(lineChart, dateList1, value1, value2, line, type, stock1, stock2);
        tooltipContent.setVisible(false);


//        Scene scene  = new Scene(lineChart,1060,500);
        this.getChildren().addAll(lineChart, tooltipContent, line);
    }

    /**
     * 当日相关信息面板
     *
     * @param lineChart
     * @param dateList
     * @param value1
     * @param value2
     * @param line
     * @return
     */
    private TooltipContent createCursorGraphCoordsMonitorLabel(LineChart<String, Number> lineChart, List<Date> dateList, List<Double> value1, List<Double> value2, Line line, String type, String stock1, String stock2) {
        final Axis<String> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();
        TooltipContent tooltipContent;
        if(type.equals("backtest"))
            tooltipContent = new TooltipContent("y:", "Date:", "策略", "基准", type);
        else
            tooltipContent = new TooltipContent("y:", "Date:", stock1, stock2, type);
        tooltipContent.setVisible(false);
        tooltipContent.setStyle("-fx-background-color: #4d4d4d; -fx-opacity: 0.8;");


        //获取图表面板
        final Node chartBackground = lineChart.lookup(".chart-plot-background");
        for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }

        //鼠标进入图表
        chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(true);
                line.setVisible(true);
            }
        });

        //鼠标在图表中移动
        chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double h = tooltipContent.getHeight();
                double w = tooltipContent.getWidth();
                double x = mouseEvent.getX() + xAxis.getLayoutX() + 7;
                double y = mouseEvent.getY() + lineChart.getLayoutY() + 50;


                //防止面包超过主屏幕大小
                if (mouseEvent.getSceneX() + w <= 1260 && mouseEvent.getSceneY() + h <= 720) {
                    tooltipContent.setLayoutX(x + 5);
                    tooltipContent.setLayoutY(y);
                } else if (mouseEvent.getSceneX() + w > 1260 && mouseEvent.getSceneY() + h <= 720) {
                    tooltipContent.setLayoutX(x - 10 - w);
                    tooltipContent.setLayoutY(y);
                } else if (mouseEvent.getSceneX() + w <= 1260 && mouseEvent.getSceneY() + h > 720) {
                    tooltipContent.setLayoutX(x + 5);
                    tooltipContent.setLayoutY(y - 40 - h);
                } else {
                    tooltipContent.setLayoutX(x - 10 - w);
                    tooltipContent.setLayoutY(y - 40 - h);
                }

                int index = (int) ((mouseEvent.getX() / xAxis.getWidth()) * dateList.size());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(dateList.get((index)));
                tooltipContent.update(day, value1.get(index), value2.get(index));
                line.setStartX(x);
                line.setEndX(x);

            }
        });

        //鼠标离开图表
        chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
                line.setVisible(false);
            }
        });

        //鼠标进入x轴
        xAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(true);
            }
        });

        //鼠标在x轴移动
        xAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX() + xAxis.getLayoutX() + 10;
                double y = mouseEvent.getY() + yAxis.getHeight() + 50;

                int index = (int) ((mouseEvent.getX() / xAxis.getWidth()) * dateList.size());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String day = sdf.format(dateList.get((index)));
                tooltipContent.showXAxis(day);
            }

        });

        //鼠标离开x轴
        xAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
            }
        });

        //鼠标进入y轴
        yAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(true);
            }
        });

        //鼠标在y轴移动
        yAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX() + 7;
                double y = mouseEvent.getY() + 50;

                tooltipContent.setLayoutX(x);
                tooltipContent.setLayoutY(y);
                tooltipContent.showYAxis(yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue());
            }
        });

        //鼠标离开y轴
        yAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
            }
        });


        return tooltipContent;
    }
}