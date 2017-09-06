package ui.charts;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.text.DecimalFormat;

/**
 * Created by island on 2017/3/22.
 */
public class TooltipContent extends GridPane {
    private String type;
    private Label yValue = new Label();
    private Label dateValue = new Label();
    private Label stock1Value = new Label();
    private Label stock2Value = new Label();

    private Label y = new Label("y:");
    private Label date = new Label("Date:");
    private Label stock1 = new Label("Stock1:");
    private Label stock2 = new Label("Stock2:");

    public TooltipContent(String yname, String datename, String name1, String name2, String type) {
        y.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-padding: 2 5 2 0;");
        yValue.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-padding: 2 5 2 0;");
        date.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-padding: 2 5 2 0;");
        dateValue.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-padding: 2 5 2 0;");
        stock1.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #69a9fd; -fx-padding: 2 5 2 0;");
        stock1Value.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #69a9fd; -fx-padding: 2 5 2 0;");
        stock2.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #fa9800; -fx-padding: 2 5 2 0;");
        stock2Value.setStyle("-fx-font-size: 15px; -fx-font-family: Helvetica; -fx-font-weight: bold; -fx-text-fill: #f98600; -fx-padding: 2 5 2 0;");

        setConstraints(date, 0, 0);
        setConstraints(dateValue, 1, 0);
        setConstraints(stock1, 0, 1);
        setConstraints(stock1Value, 1, 1);
        setConstraints(stock2, 0, 2);
        setConstraints(stock2Value, 1, 2);

        y.setText(yname);
        date.setText(datename);
        stock1.setText(name1);
        stock2.setText(name2);
        this.type = type;
    }

    public void update(String dateVa, double stock1Va, double stock2Va) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        getChildren().clear();
        dateValue.setText(dateVa);
        if(type.equals("backtest")){
            stock1Value.setText(decimalFormat.format(stock1Va * 100) + "%");
            stock2Value.setText(decimalFormat.format(stock2Va * 100) + "%");
        }
        else {
            stock1Value.setText(decimalFormat.format(stock1Va) + "");
            stock2Value.setText(decimalFormat.format(stock2Va) + "");
        }

        getChildren().addAll(date, dateValue, stock1, stock1Value, stock2, stock2Value);
    }

    public void upadteArea(int dateVa, double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        getChildren().clear();
        dateValue.setText(dateVa + "");
        stock1Value.setText(decimalFormat.format(value * 100) + "%");
        getChildren().addAll(date, dateValue, stock1, stock1Value);
    }

    public void showXAxis(String dateVa){
            getChildren().clear();
            setConstraints(date, 0, 0);
            setConstraints(dateValue, 1, 0);
            getChildren().addAll(date, dateValue);
            dateValue.setText(dateVa);
    }

    public void showYAxis(double yVa){
            DecimalFormat decimalFormat = new DecimalFormat("#.###");

            getChildren().clear();
            setConstraints(y, 0, 0);
            setConstraints(yValue, 1, 0);
            getChildren().addAll(y, yValue);
            if(type.equals("backtest")) {
                yValue.setText(decimalFormat.format(yVa * 100) + "%" );
            }
            else {
                yValue.setText(yVa + "");
            }
    }

}


