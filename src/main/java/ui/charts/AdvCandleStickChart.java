package ui.charts;

/*
 * Part of Ensemble code
 * related to AdvCandleStickChart
 *
 * extracted by Nicolas
 * July 20th, 2016
 *
 * Exemple shown at the bottom of
 * http://docs.oracle.com/javafx/2/charts/chart-overview.htm
 *
 * Ensemble launch page:
 * http://download.oracle.com/otndocs/products/javafx/2/samples/Ensemble/index.html
 *
 * Initial source code:
 * http://grepcode.com/file/repo1.maven.org/maven2/org.jbundle.javafx.example/org.jbundle.javafx.example.ensemble/0.9.0/ensemble/samples/charts/custom/AdvCandleStickChartSample.java?av=f
 *
 */

 /*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.layout.Pane;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * A custom candlestick chart.
 *
 * @see javafx.scene.chart.Axis
 * @see javafx.scene.chart.Chart
 * @see javafx.scene.chart.NumberAxis
 * @see javafx.scene.chart.XYChart
 */
public class AdvCandleStickChart extends Pane {

    //  OPEN, CLOSE, HIGH, LOW, DAY, AVERAGE
    //private static double[][] data ;

    /**
     * 构造方法
     * @param data 包含每日的open／close／high／low
     * @param dates 包含所有日期
     * @param average 包含每日的 ma5/ ma10/ ma30/ ma60/ ma120/ ma240
     */
    public AdvCandleStickChart(double[][] data, String[] dates, double[][] average) {
        // x-axis:
        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("");


        // y-axis:
        final NumberAxis yAxis = new NumberAxis();
//        yAxis.setLabel("Price");
        yAxis.setForceZeroInRange(false);
//        yAxis.setForceZeroInRange(true);

        // chart:
        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
        bc.setTitle("日K线图");

        // add starting data
//        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> series4 = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> series5 = new XYChart.Series<String, Number>();
        XYChart.Series<String, Number> series6 = new XYChart.Series<String, Number>();

        for (int i = 0; i < data.length; i++) {
            double[] day = data[i];
            series1.getData().add(
                    new XYChart.Data<String, Number>(dates[i].substring(2), day[1], new CandleStickExtraValues(day[2], day[3], day[4], average[i][0]))
            );
            series2.getData().add(
                    new XYChart.Data<String, Number>(dates[i].substring(2), day[1], new CandleStickExtraValues(day[2], day[3], day[4], average[i][1]))
            );
            series3.getData().add(
                    new XYChart.Data<String, Number>(dates[i].substring(2), day[1], new CandleStickExtraValues(day[2], day[3], day[4], average[i][2]))
            );
            series4.getData().add(
                    new XYChart.Data<String, Number>(dates[i].substring(2), day[1], new CandleStickExtraValues(day[2], day[3], day[4], average[i][3]))
            );
            series5.getData().add(
                    new XYChart.Data<String, Number>(dates[i].substring(2), day[1], new CandleStickExtraValues(day[2], day[3], day[4], average[i][4]))
            );
            series6.getData().add(
                    new XYChart.Data<String, Number>(dates[i].substring(2), day[1], new CandleStickExtraValues(day[2], day[3], day[4], average[i][5]))
            );
        }
        ObservableList<XYChart.Series<String, Number>> data1 = bc.getData();
        if (data1 == null) {
            data1 = FXCollections.observableArrayList(series1);
//            System.out.println("line148 : " + data.size());
            bc.setData(data1);
        } else {
            bc.getData().add(series1);
        }
        bc.getData().add(series2);
        bc.getData().add(series3);
        bc.getData().add(series4);
        bc.getData().add(series5);
        bc.getData().add(series6);


        //设置图表属性
        bc.setStyle("-fx-background-color: #fffff00");
        bc.setPrefHeight(450);
        bc.setPrefWidth(1180);

        //增加图表中移动的线
        Line line = new Line();
        line.setVisible(false);
        line.setStyle("-fx-stroke: #689cc4");
        line.setStartY(35);
        line.setEndY(360);
        line.setStartX(-1000);
        line.setEndX(-1000);
        //增加移动显示当日相关信息面板
        TooltipContent tooltipContent = createCursorGraphCoordsMonitorLabel(bc, dates, data, average, line);
        getChildren().addAll(bc, tooltipContent, line);
        tooltipContent.toFront();
    }

    /**
     * 当日相关信息面板
     * @param lineChart
     * @param dateList
     * @param data
     * @param average
     * @param line
     * @return
     */
    private TooltipContent createCursorGraphCoordsMonitorLabel(CandleStickChart lineChart, String[] dateList, double[][] data, double[][] average, Line line) {
        final Axis<String> xAxis = lineChart.getXAxis();
        final Axis<Number> yAxis = lineChart.getYAxis();

        TooltipContent tooltipContent = new TooltipContent();
        tooltipContent.setVisible(false);
        tooltipContent.setStyle("-fx-background-color: #4d4d4d; -fx-opacity: 0.8;");

        //获取图表面板
        final Node chartBackground = lineChart.lookup(".chart-plot-background");
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


                double h = tooltipContent.getHeight();
                double w = tooltipContent.getWidth();
                double x = mouseEvent.getX() + xAxis.getLayoutX() + 7;
                double y = mouseEvent.getY() + lineChart.getLayoutY() + 50;

                //防止面包超过主屏幕大小
                if(mouseEvent.getSceneX() + w <= 1270 && mouseEvent.getSceneY() + h <= 720){
                    tooltipContent.setLayoutX(x + 3);
                    tooltipContent.setLayoutY(y);
                }
                else if(mouseEvent.getSceneX() + w > 1270 && mouseEvent.getSceneY() + h <= 720) {
                    tooltipContent.setLayoutX(x - 10 - w);
                    tooltipContent.setLayoutY(y);
                }
                else if(mouseEvent.getSceneX() + w <= 1270 && mouseEvent.getSceneY() + h > 720){
                    tooltipContent.setLayoutX(x + 3);
                    tooltipContent.setLayoutY(y - 40 - h);
                }else{
                    tooltipContent.setLayoutX(x - 10 - w);
                    tooltipContent.setLayoutY(y - 40 - h);
                }



                int index = (int) ((mouseEvent.getX() / xAxis.getWidth()) * dateList.length) ;


                // 更新信息面板和线的设置
                tooltipContent.update(dateList[index], data[index][1], data[index][2], data[index][3], data[index][4]
                        , average[index][0], average[index][1], average[index][2], average[index][3], average[index][4], average[index][5]);

                line.setStartX(x);
                line.setEndX(x);

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
                int index = (int) ((mouseEvent.getX() / xAxis.getWidth()) * dateList.length);
                tooltipContent.showXAxis(dateList[index]);
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
                line.setVisible(true);
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
                line.setStartX(x);
                line.setEndX(x);
            }
        });

        //鼠标离开y轴
        yAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                tooltipContent.setVisible(false);
                line.setVisible(false);
            }
        });


        return tooltipContent;
    }


    /**
     * A candlestick chart is a style of bar-chart used primarily to describe
     * price movements of a security, derivative, or currency over time.
     *
     * The Data Y value is used for the opening price and then the close, high
     * and low values are stored in the Data's extra value property using a
     * CandleStickExtraValues object.
     */
    private class CandleStickChart extends XYChart<String, Number> {

        // -------------- CONSTRUCTORS ----------------------------------------------
        /**
         * Construct a new CandleStickChart with the given axis.
         *
         * @param xAxis The x axis to use
         * @param yAxis The y axis to use
         */
        public CandleStickChart(Axis<String> xAxis, Axis<Number> yAxis) {
            super(xAxis, yAxis);
            setAnimated(false);
            xAxis.setAnimated(false);
            yAxis.setAnimated(false);
        }

        /**
         * Construct a new CandleStickChart with the given axis and data.
         *
         * @param xAxis The x axis to use
         * @param yAxis The y axis to use
         * @param data The data to use, this is the actual list used so any
         * changes to it will be reflected in the chart
         */
        public CandleStickChart(Axis<String> xAxis, Axis<Number> yAxis, ObservableList<Series<String, Number>> data) {
            this(xAxis, yAxis);
            setData(data);
        }

        // -------------- METHODS ------------------------------------------------------------------------------------------
        /**
         * Called to update and layout the content for the plot
         */
        @Override
        protected void layoutPlotChildren() {
            // we have nothing to layout if no data is present
            if (getData() == null) {
//                System.out.println("line206: nothing to layout!!!");
                return;
            }
//            System.out.println("line209: layout!!!");
            // update candle positions

            for (int seriesIndex = 0; seriesIndex < 1; seriesIndex++) {
                Series<String, Number> series = getData().get(seriesIndex);

                Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);

                while (iter.hasNext()) {
                    Data<String, Number> item = iter.next();
                    double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                    double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));
                    Node itemNode = item.getNode();
                    CandleStickExtraValues extra = (CandleStickExtraValues) item.getExtraValue();
                    if (itemNode instanceof Candle && extra != null) {
                        Candle candle = (Candle) itemNode;
                        double close = getYAxis().getDisplayPosition(extra.getClose());
                        double high = getYAxis().getDisplayPosition(extra.getHigh());
                        double low = getYAxis().getDisplayPosition(extra.getLow());
                        // calculate candle width
                        double candleWidth = -1;
                        if (getXAxis() instanceof CategoryAxis) {
                            CategoryAxis xa = (CategoryAxis) getXAxis();
                            candleWidth = (xa.getCategorySpacing() * 0.7); // use 90% width between ticks

                            // update candle
                            candle.update(close - y, high - y, low - y, candleWidth);
                            double[] ave = new double[6];
                            DecimalFormat df = new DecimalFormat("#.00");
                            for (int i = 0; i < 6; i++) {
                                Series<String, Number> tempSeries = getData().get(i);
                                Iterator<Data<String, Number>> tempIter = getDisplayedDataIterator(tempSeries);
                                Data<String, Number> tempItem = tempIter.next();
                                                              CandleStickExtraValues tempExtra = (CandleStickExtraValues) tempItem.getExtraValue();
                                ave[i] = Double.parseDouble(df.format(tempExtra.getAverage()));
                            }
                            // position the candle
                            candle.setLayoutX(x);
                            candle.setLayoutY(y);
                        }
                    }

                }
            }
            int count = 0;
            Path[] seriesPath = new Path[6];
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                Series<String, Number> series = getData().get(seriesIndex);
                Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);
                if (series.getNode() instanceof Path) {
                    seriesPath[seriesIndex] = (Path) series.getNode();
                }

                while (iter.hasNext()) {
                    Data<String, Number> item = iter.next();
                    double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                    CandleStickExtraValues extra = (CandleStickExtraValues) item.getExtraValue();

                    if (seriesPath[seriesIndex] != null) {
                        double ave = extra.getAverage();
                        if (ave != 0) {
                            if (count == 0) {
                                seriesPath[seriesIndex].getElements().add(new MoveTo(x, getYAxis().getDisplayPosition(ave)));
                                count++;
                            } else {
                                seriesPath[seriesIndex].getElements().add(new LineTo(x, getYAxis().getDisplayPosition(ave)));
                                count++;
                            }
                        }
                    }
                }
                count = 0;
            }
            seriesPath[0].setStroke(Paint.valueOf("#565656"));
            seriesPath[1].setStroke(Paint.valueOf("#beae69"));
            seriesPath[2].setStroke(Paint.valueOf("#9e6789"));
            seriesPath[3].setStroke(Paint.valueOf("#729d63"));
            seriesPath[4].setStroke(Paint.valueOf("#5f828f"));
            seriesPath[5].setStroke(Paint.valueOf("#f3622d"));


/*
            Task<Void> task1 = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int seriesIndex = 0; seriesIndex < 1; seriesIndex++) {
                                Series<String, Number> series = getData().get(seriesIndex);

                                Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);

                                while (iter.hasNext()) {
                                    Data<String, Number> item = iter.next();
                                    double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                                    double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));
                                    Node itemNode = item.getNode();
                                    CandleStickExtraValues extra = (CandleStickExtraValues) item.getExtraValue();
                                    if (itemNode instanceof Candle && extra != null) {
                                        Candle candle = (Candle) itemNode;

                                        double close = getYAxis().getDisplayPosition(extra.getClose());
                                        double high = getYAxis().getDisplayPosition(extra.getHigh());
                                        double low = getYAxis().getDisplayPosition(extra.getLow());
                                        // calculate candle width
                                        double candleWidth = -1;
                                        if (getXAxis() instanceof CategoryAxis) {
                                            CategoryAxis xa = (CategoryAxis) getXAxis();
                                            candleWidth = (xa.getCategorySpacing() * 0.7); // use 90% width between ticks

                                        }
                                        // update candle
                                        candle.update(close - y, high - y, low - y, candleWidth);
                                        double[] ave = new double[6];
                                        DecimalFormat df = new DecimalFormat("#.00");
                                        for (int i = 0; i < 6; i++) {
                                            Series<String, Number> tempSeries = getData().get(i);
                                            Iterator<Data<String, Number>> tempIter = getDisplayedDataIterator(tempSeries);
                                            Data<String, Number> tempItem = tempIter.next();
                                            CandleStickExtraValues tempExtra = (CandleStickExtraValues) tempItem.getExtraValue();
                                            ave[i] = Double.parseDouble(df.format(tempExtra.getAverage()));
                                        }

                                        // position the candle
                                        candle.setLayoutX(x);
                                        candle.setLayoutY(y);
                                    }
                                }
                            }
                            int count = 0;
                            Path[] seriesPath = new Path[6];
                            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {

                                Series<String, Number> series = getData().get(seriesIndex);
                                Iterator<Data<String, Number>> iter = getDisplayedDataIterator(series);

                                if (series.getNode() instanceof Path) {
                                    seriesPath[seriesIndex] = (Path) series.getNode();
                                }

                                while (iter.hasNext()) {
                                    Data<String, Number> item = iter.next();
                                    double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                                    CandleStickExtraValues extra = (CandleStickExtraValues) item.getExtraValue();

                                    if (seriesPath[seriesIndex] != null) {
                                        double ave = extra.getAverage();
                                        if (ave != 0) {
                                            if (count == 0) {
                                                seriesPath[seriesIndex].getElements().add(new MoveTo(x, getYAxis().getDisplayPosition(ave)));
                                                count++;
                                            } else {
                                                seriesPath[seriesIndex].getElements().add(new LineTo(x, getYAxis().getDisplayPosition(ave)));
                                                count++;
                                            }
                                        }
                                    }
                                }


                                count = 0;
                            }
                            seriesPath[0].setStroke(Paint.valueOf("#FFFFFF"));
                            seriesPath[1].setStroke(Paint.valueOf("#beae69"));
                            seriesPath[2].setStroke(Paint.valueOf("#9e6789"));
                            seriesPath[3].setStroke(Paint.valueOf("#729d63"));
                            seriesPath[4].setStroke(Paint.valueOf("#5f828f"));
                            seriesPath[5].setStroke(Paint.valueOf("#f3622d"));

                        }
                    });
                    return null;

                }
            };
            Thread thread1 = new Thread(task1);
            thread1.start();

*/
        }

        @Override
        protected void dataItemChanged(Data<String, Number> item) {
        }

        @Override
        protected void dataItemAdded(Series<String, Number> series, int itemIndex, Data<String, Number> item) {
            Node candle = createCandle(getData().indexOf(series), item, itemIndex);
            if (shouldAnimate()) {
                candle.setOpacity(0);
                getPlotChildren().add(candle);
                // fade in new candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(1);
                ft.play();
            } else {
                getPlotChildren().add(candle);
            }
            // always draw average line on top
            if (series.getNode() != null) {
                series.getNode().toFront();
            }
        }

        @Override
        protected void dataItemRemoved(Data<String, Number> item, Series<String, Number> series) {
            final Node candle = item.getNode();
            if (shouldAnimate()) {
                // fade out old candle
                FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(0);
                ft.setOnFinished(new EventHandler<ActionEvent>() {


                    public void handle(ActionEvent actionEvent) {
                        getPlotChildren().remove(candle);
                    }
                });
                ft.play();
            } else {
                getPlotChildren().remove(candle);
            }
        }

        @Override
        protected void seriesAdded(Series<String, Number> series, int seriesIndex) {
            // handle any data already in series
            for (int j = 0; j < series.getData().size(); j++) {
                Data item = series.getData().get(j);
                Node candle = createCandle(seriesIndex, item, j);
                if (shouldAnimate()) {
                    candle.setOpacity(0);
                    getPlotChildren().add(candle);
                    // fade in new candle
                    FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                    ft.setToValue(1);
                    ft.play();
                } else {
                    getPlotChildren().add(candle);
                }
            }
            // create series path
            Path[] seriesPath = new Path[6];
            for(int i = 0; i < 6; i++) {
                seriesPath[i] = new Path();
                seriesPath[i].getStyleClass().setAll("candlestick-average-line1", "series" + seriesIndex);
                series.setNode(seriesPath[i]);
                getPlotChildren().add(seriesPath[i]);
            }
            seriesPath[1].getStyleClass().setAll("candlestick-average-line2", "series" + seriesIndex);

        }

        @Override
        protected void seriesRemoved(Series<String, Number> series) {
            // remove all candle nodes
            for (XYChart.Data<String, Number> d : series.getData()) {
                final Node candle = d.getNode();
                if (shouldAnimate()) {
                    // fade out old candle
                    FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                    ft.setToValue(0);
                    ft.setOnFinished(new EventHandler<ActionEvent>() {


                        public void handle(ActionEvent actionEvent) {
                            getPlotChildren().remove(candle);
                        }
                    });
                    ft.play();
                } else {
                    getPlotChildren().remove(candle);
                }
            }
        }

        /**
         * Create a new Candle node to represent a single data item
         *
         * @param seriesIndex The index of the series the data item is in
         * @param item The data item to create node for
         * @param itemIndex The index of the data item in the series
         * @return New candle node to represent the give data item
         */
        private Node createCandle(int seriesIndex, final Data item, int itemIndex) {
            Node candle = item.getNode();
            // check if candle has already been created
            if (candle instanceof Candle) {
                ((Candle) candle).setSeriesAndDataStyleClasses("series" + seriesIndex, "data" + itemIndex);
            } else {
                candle = new Candle("series" + seriesIndex, "data" + itemIndex);
                item.setNode(candle);
            }
            return candle;
        }

        /**
         * This is called when the range has been invalidated and we need to
         * update it. If the axis are auto ranging then we compile a list of all
         * data that the given axis has to plot and call invalidateRange() on
         * the axis passing it that data.
         */
        @Override
        protected void updateAxisRange() {
            // For candle stick chart we need to override this method as we need to let the axis know that they need to be able
            // to cover the whole area occupied by the high to low range not just its center data value
            final Axis<String> xa = getXAxis();
            final Axis<Number> ya = getYAxis();
            List<String> xData = null;
            List<Number> yData = null;
            if (xa.isAutoRanging()) {
                xData = new ArrayList<String>();
            }
            if (ya.isAutoRanging()) {
                yData = new ArrayList<Number>();
            }
            if (xData != null || yData != null) {
                for (Series<String, Number> series : getData()) {
                    for (Data<String, Number> data : series.getData()) {
                        if (xData != null) {
                            xData.add(data.getXValue());
                        }
                        if (yData != null) {
                            CandleStickExtraValues extras = (CandleStickExtraValues) data.getExtraValue();
                            if (extras != null) {
                                yData.add(extras.getHigh());
                                yData.add(extras.getLow());
                                for(int i = 0; i < 6; i++) {
                                    if(extras.getAverage() != 0)
                                        yData.add(extras.getAverage());
                                }
                            } else {
                                yData.add(data.getYValue());
                            }
                        }
                    }
                }
                if (xData != null) {
                    xa.invalidateRange(xData);
                }
                if (yData != null) {
                    ya.invalidateRange(yData);
                }
            }
        }
    }

    /**
     * Data extra values for storing close, high and low.
     */
    private class CandleStickExtraValues {

        private double close;
        private double high;
        private double low;
        private double average;

        public CandleStickExtraValues(double close, double high, double low, double average) {
            this.close = close;
            this.high = high;
            this.low = low;
            this.average = average;
        }

        public double getClose() {
            return close;
        }

        public double getHigh() {
            return high;
        }

        public double getLow() {
            return low;
        }

        public double getAverage() {
            return average;
        }
    }

    /**
     * Candle node used for drawing a candle
     */
    private class Candle extends Group {

        private Line highLowLine = new Line();
        private Region bar = new Region();
        private String seriesStyleClass;
        private String dataStyleClass;
        private boolean openAboveClose = true;
        private Tooltip tooltip = new Tooltip();

        private Candle(String seriesStyleClass, String dataStyleClass) {
            setAutoSizeChildren(false);
            getChildren().addAll(highLowLine, bar);
            this.seriesStyleClass = seriesStyleClass;
            this.dataStyleClass = dataStyleClass;
            updateStyleClasses();
            tooltip.setGraphic(new TooltipContent());
            Tooltip.install(bar, tooltip);
        }

        public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass) {
            this.seriesStyleClass = seriesStyleClass;
            this.dataStyleClass = dataStyleClass;
            updateStyleClasses();
        }

        public void update(double closeOffset, double highOffset, double lowOffset, double candleWidth) {
            openAboveClose = closeOffset > 0;
            updateStyleClasses();
            highLowLine.setStartY(highOffset);
            highLowLine.setEndY(lowOffset);
            highLowLine.setStrokeWidth(candleWidth/5);
            if (candleWidth == -1) {
                candleWidth = bar.prefWidth(-1);
            }
            if (openAboveClose) {
                bar.resizeRelocate(-candleWidth / 2, 0, candleWidth, closeOffset);
            } else {
                bar.resizeRelocate(-candleWidth / 2, closeOffset, candleWidth, closeOffset * -1);
            }
        }


        private void updateStyleClasses() {
            getStyleClass().setAll("candlestick-candle", seriesStyleClass, dataStyleClass);
            highLowLine.getStyleClass().setAll("candlestick-line", seriesStyleClass, dataStyleClass,
                    openAboveClose ? "open-above-close" : "close-above-open");
            bar.getStyleClass().setAll("candlestick-bar", seriesStyleClass, dataStyleClass,
                    openAboveClose ? "open-above-close" : "close-above-open");
        }
    }

    private class TooltipContent extends GridPane {
        private Label yValue = new Label();
        private Label dateValue = new Label();
        private Label openValue = new Label();
        private Label closeValue = new Label();
        private Label highValue = new Label();
        private Label lowValue = new Label();
        private Label ma5Value = new Label();
        private Label ma10Value = new Label();
        private Label ma30Value = new Label();
        private Label ma60Value = new Label();
        private Label ma120Value = new Label();
        private Label ma240Value = new Label();

        private Label y = new Label("y:");
        private Label date = new Label("DATE:");
        private Label open = new Label("OPEN:");
        private Label close = new Label("CLOSE:");
        private Label high = new Label("HIGH:");
        private Label low = new Label("LOW:");
        private Label ma5 = new Label("MA5:");
        private Label ma10 = new Label("MA10:");
        private Label ma30 = new Label("MA30:");
        private Label ma60 = new Label("MA60:");
        private Label ma120 = new Label("MA120:");
        private Label ma240 = new Label("MA240:");

        private TooltipContent() {

            y.getStyleClass().add("candlestick-tooltip-label1");
            date.getStyleClass().add("candlestick-tooltip-label1");
            open.getStyleClass().add("candlestick-tooltip-label");
            close.getStyleClass().add("candlestick-tooltip-label");
            high.getStyleClass().add("candlestick-tooltip-label");
            low.getStyleClass().add("candlestick-tooltip-label");
            ma5.getStyleClass().add("candlestick-tooltip-label1");
            ma10.getStyleClass().add("candlestick-tooltip-label2");
            ma30.getStyleClass().add("candlestick-tooltip-label3");
            ma60.getStyleClass().add("candlestick-tooltip-label4");
            ma120.getStyleClass().add("candlestick-tooltip-label5");
            ma240.getStyleClass().add("candlestick-tooltip-label6");

            yValue.getStyleClass().add("candlestick-tooltip-label1");
            dateValue.getStyleClass().add("candlestick-tooltip-label1");
            openValue.getStyleClass().add("candlestick-tooltip-label");
            closeValue.getStyleClass().add("candlestick-tooltip-label");
            highValue.getStyleClass().add("candlestick-tooltip-label");
            lowValue.getStyleClass().add("candlestick-tooltip-label");
            ma5Value.getStyleClass().add("candlestick-tooltip-label1");
            ma10Value.getStyleClass().add("candlestick-tooltip-label2");
            ma30Value.getStyleClass().add("candlestick-tooltip-label3");
            ma60Value.getStyleClass().add("candlestick-tooltip-label4");
            ma120Value.getStyleClass().add("candlestick-tooltip-label5");
            ma240Value.getStyleClass().add("candlestick-tooltip-label6");

        }

        private void setNormal(){
            getChildren().clear();

            setConstraints(date, 0, 0);
            setConstraints(dateValue, 1, 0);
            setConstraints(open, 0, 1);
            setConstraints(openValue, 1, 1);
            setConstraints(close, 0, 2);
            setConstraints(closeValue, 1, 2);
            setConstraints(high, 0, 3);
            setConstraints(highValue, 1, 3);
            setConstraints(low, 0, 4);
            setConstraints(lowValue, 1, 4);
            setConstraints(ma5, 0, 5);
            setConstraints(ma5Value, 1, 5);
            setConstraints(ma10, 0, 6);
            setConstraints(ma10Value, 1, 6);
            setConstraints(ma30, 0, 7);
            setConstraints(ma30Value, 1, 7);
            setConstraints(ma60, 0, 8);
            setConstraints(ma60Value, 1, 8);
            setConstraints(ma120, 0, 9);
            setConstraints(ma120Value, 1, 9);
            setConstraints(ma240, 0, 10);
            setConstraints(ma240Value, 1, 10);

            getChildren().addAll(date, dateValue, open, openValue, close, closeValue, high, highValue, low, lowValue,
                    ma5, ma5Value, ma10, ma10Value, ma30, ma30Value, ma60, ma60Value, ma120, ma120Value, ma240, ma240Value);
        }

        public void update(String date, double open, double close, double high, double low, double ma5, double ma10, double ma30, double ma60, double ma120, double ma240) {
            setNormal();

            dateValue.setText(date);
            openValue.setText(Double.toString(open));
            closeValue.setText(Double.toString(close));
            highValue.setText(Double.toString(high));
            lowValue.setText(Double.toString(low));
            ma5Value.setText("暂无");
            ma10Value.setText("暂无");
            ma30Value.setText("暂无");
            ma60Value.setText("暂无");
            ma120Value.setText("暂无");
            ma240Value.setText("暂无");
            if(ma5 != 0)
                ma5Value.setText(Double.toString(ma5));
            if(ma10 != 0)
                ma10Value.setText(Double.toString(ma10));
            if(ma30 != 0)
                ma30Value.setText(Double.toString(ma30));
            if(ma60 != 0)
                ma60Value.setText(Double.toString(ma60));
            if(ma120 != 0)
                ma120Value.setText(Double.toString(ma120));
            if(ma240 != 0)
                ma240Value.setText(Double.toString(ma240));
        }



        public void showXAxis(String dateVa){
            getChildren().clear();
            setConstraints(date, 0, 0);
            setConstraints(dateValue, 1, 0);
            getChildren().addAll(date, dateValue);
            dateValue.setText(dateVa);
        }

        public void showYAxis(double yVa){
            getChildren().clear();
            setConstraints(y, 0, 0);
            setConstraints(yValue, 1, 0);
            getChildren().addAll(y, yValue);
            yValue.setText(yVa + "");
        }
    }
}
