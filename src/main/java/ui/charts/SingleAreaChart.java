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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by island on 2017/3/22.
 */
public class SingleAreaChart extends Pane {

    public SingleAreaChart(List<Double> value, List<Integer> date,  String title, String type){
        double maxLimit = Collections.max(value);
        double minLimit = Collections.min(value);
        double tickUnit = (maxLimit - minLimit) / 10;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // x轴
        CategoryAxis xAxis = new CategoryAxis();
        //y轴
        NumberAxis yAxis = new NumberAxis(minLimit - tickUnit, maxLimit + tickUnit, tickUnit);
        if(title.equals("策略胜率% - 不同计算周期") && Collections.max(value) == 1) {
                yAxis = new NumberAxis(minLimit - tickUnit, 1.000000001, tickUnit);
        }

        yAxis.setMinorTickCount(0);
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

        //创建areaChart
        final AreaChart<String,Number> areaChart = new AreaChart<>(xAxis,yAxis);
        areaChart.setTitle(title);
        areaChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();
        if(title.equals("超额收益 vs 全样本-不同计算周期")){
            series.setName("超额收益 vs 全样本");
        }
        if(title.equals("策略胜率% - 不同计算周期")){
            series.setName("策略胜率%");
        }

        // 增加数据
        for (int i = 0; i < date.size(); i++) {
            series.getData().add(new XYChart.Data(date.get(i) + "", value.get(i)));
        }

        areaChart.getData().addAll(series);

        areaChart.setPrefSize(1060, 590);
        areaChart.getStylesheets().add("ui/MyChart.css");

        Line line = new Line();
        line.setStyle("-fx-stroke: #689cc4");
        line.setStartY(35);
        line.setEndY(560);
        line.setVisible(false);

        TooltipContent tooltipContent = createCursorGraphCoordsMonitorLabel(areaChart, date, value, line, type, title);
        this.getChildren().addAll(areaChart, tooltipContent, line);
    }

    /**
     * 当日相关信息面板
     * @param areaChart
     * @param dateList
     * @param value
     * @param line
     * @return
     */
    private TooltipContent createCursorGraphCoordsMonitorLabel(AreaChart<String, Number> areaChart, List<Integer> dateList, List<Double> value, Line line, String type, String title) {
        final Axis<String> xAxis = areaChart.getXAxis();
        final Axis<Number> yAxis = areaChart.getYAxis();
        String period = "";
        if(type.equals("形成期"))
            period = "持有期";
        else
            period = "形成期";
        String name = "";
        if(title.equals("超额收益 vs 全样本-不同计算周期")){
            name = "超额收益 vs 全样本";
        }else{
            name = "策略胜率%";
        }

        TooltipContent tooltipContent = new TooltipContent(name, period, name, "", "backtest");
        tooltipContent.setVisible(false);
        tooltipContent.setStyle("-fx-background-color: black; -fx-opacity: 0.8;");


        //获取图表面板
        final Node chartBackground = areaChart.lookup(".chart-plot-background");
        for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
            if (n != chartBackground && n != xAxis && n != yAxis) {
                n.setMouseTransparent(true);
            }
        }

        //鼠标进入图表
        chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(true);
                line.setVisible(true);
            }
        });

        //鼠标在图表中移动
        chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX() + xAxis.getLayoutX() + 8;
                double y = mouseEvent.getY() + areaChart.getLayoutY() + 50;

                tooltipContent.setLayoutX(x);
                tooltipContent.setLayoutY(y);

                int index = (int) ((mouseEvent.getX() / xAxis.getWidth()) * dateList.size()) ;

                if(index < value.size()) {
                    tooltipContent.upadteArea(dateList.get(index), value.get(index));
                    line.setStartX(x);
                    line.setEndX(x);
                }

            }
        });

        //鼠标离开图表
        chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
                line.setVisible(false);
            }
        });

        //鼠标进入x轴
        xAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(true);
            }
        });

        //鼠标在x轴移动
        xAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {

                double x = mouseEvent.getX() + xAxis.getLayoutX() + 7;
                double y = mouseEvent.getY() + yAxis.getHeight() + 50;

                tooltipContent.setLayoutX(x);
                tooltipContent.setLayoutY(y);
                int index = (int) ((mouseEvent.getX() / xAxis.getWidth()) * dateList.size());
                if(index < dateList.size()) {
                    tooltipContent.showXAxis(dateList.get(index) + "");
                }

            }
        });

        //鼠标离开x轴
        xAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
            }
        });

        //鼠标进入y轴
        yAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(true);
            }
        });

        //鼠标在y轴移动
        yAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                double x = mouseEvent.getX() + 7;
                double y = mouseEvent.getY() + 50;

                tooltipContent.setLayoutX(x);
                tooltipContent.setLayoutY(y);
                tooltipContent.showYAxis(yAxis.getValueForDisplay(mouseEvent.getY()).doubleValue());

            }
        });

        //鼠标离开y轴
        yAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
            }
        });


        return tooltipContent;
    }

}

