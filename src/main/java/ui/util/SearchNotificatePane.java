package ui.util;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by island on 2017/4/8.
 */
public class SearchNotificatePane {
    private TextField stockTextField;
    private Pane searchNotificationPane;
    private Label nullLabel;
    private ScrollPane searchScrollPane;

    private List<String> stocks;

    public SearchNotificatePane(TextField stockTextField, Pane searchNotificationPane, Label nullLabel, ScrollPane searchScrollPane){
        StockBLService stockBLService = new StockBL();
        stocks = stockBLService.getAllStockCodeAndName();
        this.stockTextField = stockTextField;
        this.searchNotificationPane = searchNotificationPane;
        this.nullLabel = nullLabel;
        this.searchScrollPane = searchScrollPane;
    }

    public void setTextField(double x, double y, double h){
        stockTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchNotificationPane.getChildren().clear();
                String s = stockTextField.getText().toUpperCase();
                searchScrollPane.setPrefHeight(h);
                //判断当前输入框中有无输入
                if(!s.equals("")) {
                    List<String> possibleStocks = new ArrayList<>();
                    String stockID = "";
                    String stockName = "";
                    for (int i = 0; i < stocks.size(); i++) {
                        String b = stocks.get(i);
                        stockID = b.split(";")[1];
                        stockName = b.split(";")[0];
                        if (stockID.startsWith(s)) {
                            possibleStocks.add(stockID);
                        }
                        if (stockName.contains(s)) {
                            possibleStocks.add(stockName);
                        }
                    }
                    //判断当前输入有无可能结果
                    if (possibleStocks.size() == 0) {
                        searchNotificationPane.getChildren().add(nullLabel);
                        nullLabel.setVisible(true);
                    } else {
                        nullLabel.setVisible(false);
                        searchNotificationPane.setPrefHeight(possibleStocks.size() * y);
                        for (int i = 0; i < possibleStocks.size(); i++) {
                            Button stockButton = new Button(possibleStocks.get(i));
                            stockButton.setLayoutY(i * y);
                            stockButton.setPrefHeight(y);
                            stockButton.setPrefWidth(x);
                            stockButton.setStyle("-fx-background-color: #ffffff00; -fx-text-fill: #ffffff; -fx-font-size: 10px");
                            searchNotificationPane.getChildren().add(stockButton);
                            stockButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                                stockButton.setStyle("-fx-background-color: #689cc4; -fx-text-fill: #ffffff; -fx-font-size: 10px");
                            });
                            stockButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                                stockButton.setStyle("-fx-background-color: #ffffff00; -fx-text-fill: #ffffff; -fx-font-size: 10px");
                            });
                            stockButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                                stockTextField.setText(stockButton.getText());
                                searchScrollPane.setVisible(false);
                            });
                        }
                    }
                    searchScrollPane.setVisible(true);
                }
                else{
                    searchScrollPane.setVisible(false);
                }
            }
        });
    }
}
