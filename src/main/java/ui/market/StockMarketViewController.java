package ui.market;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import exception.NotTransactionDayException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import ui.ControlledStage;
import ui.charts.SingleTableView;
import ui.StageController;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import vo.MarketTemperatureVO;
import vo.SearchVO;
import vo.StockVO;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by st on 2017/3/9.
 */
public class StockMarketViewController implements ControlledStage {

    private StageController stageController;
    private StockBLService stockBLService;

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @FXML
    DatePicker searchDate;
    @FXML
    Label totalTradeLabel;
    @FXML
    Label limitUpLabel;
    @FXML
    Label limitDownLabel;
    @FXML
    Label up5perLabel;
    @FXML
    Label down5perLabel;
    @FXML
    Label openUp5perLabel;
    @FXML
    Label openDown5perLabel;
    @FXML
    AnchorPane upListPane;
    @FXML
    AnchorPane downListPane;
    @FXML
    Pane searchPane;
    @FXML
    Rectangle temperatureRec;
    @FXML
    Label temperatureLabel;
    @FXML
    Button temButton;
    @FXML
    Label noDataLabel1;
    @FXML
    Label noDataLabel2;
    @FXML
    ChoiceBox plateNameChoiceBox;
    @FXML
    Button confirmButton;


    /**
     * 初始化方法，设置日期选择器范围
     *
     * @param searchVO
     */
    public void initial(SearchVO searchVO) {

        Date initDate = searchVO.startDate;
        Instant dateInstant = initDate.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        LocalDate localDate = LocalDateTime.ofInstant(dateInstant, zone).toLocalDate();

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        searchDate.setDayCellFactory(datePickerSettings.getStartSettings(searchDate));

        searchDate.setValue(localDate);

        ObservableList<String> types = FXCollections.observableArrayList("市场", "深市主板", "创业板", "中小板");
        plateNameChoiceBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #689cc4");
        plateNameChoiceBox.setItems(types);
        plateNameChoiceBox.setValue("市场");

        MarketTemperatureVO marketTemperatureVO = null;
        try {
            stockBLService = new StockBL();
            marketTemperatureVO = stockBLService.getMarketInfo(searchVO);
            updateInfo(marketTemperatureVO);
        } catch (NotTransactionDayException e) {
            // 若不是交易日，处理异常
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("该日不是交易日");
        }


        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchInfo();
            }
        });
    }


    private void updateInfo(MarketTemperatureVO marketTemperatureVO) {
        // 设置温度计初始值
        temperatureRec.setX(0);
        temperatureRec.setY(0);
        temperatureRec.setWidth(14);
        temperatureRec.setHeight(56);
        temperatureLabel.setText("0.0 ℃");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // 获得相关数据
                try {
                    int limitUpNum = marketTemperatureVO.limitUpStock.size();
                    int limitDownNum = marketTemperatureVO.limitDownStock.size();
                    int totalNum = marketTemperatureVO.totalNum;
                    int up5perNum = marketTemperatureVO.up5perStock.size();
                    int down5perNum = marketTemperatureVO.down5perStock.size();
                    int openUp5perNum = marketTemperatureVO.openUp5perStock.size();
                    int openDown5perNum = marketTemperatureVO.openDown5perStock.size();
                    totalTradeLabel.setText(String.valueOf(marketTemperatureVO.totalVolume));
                    limitUpLabel.setText(String.valueOf(limitUpNum));
                    limitDownLabel.setText(String.valueOf(limitDownNum));
                    up5perLabel.setText(String.valueOf(up5perNum));
                    down5perLabel.setText(String.valueOf(down5perNum));
                    openUp5perLabel.setText(String.valueOf(openUp5perNum));
                    openDown5perLabel.setText(String.valueOf(openDown5perNum));
                    List<StockVO> upList = marketTemperatureVO.upList;
                    List<StockVO> downList = marketTemperatureVO.downList;
                    double temperature = stockBLService.getTemperature(marketTemperatureVO);
                    double length = temperature / 2;
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(500);
                                for (int i = 0; i < 10; i++) {
                                    if (temperatureRec.getY() <= -68) {
                                        break;
                                    }
                                    sleep(500);
                                    temperatureRec.setHeight(temperatureRec.getHeight() + length / 10);
                                    temperatureRec.setY(temperatureRec.getY() - length / 10);
                                    final double temp = (i + 1) * temperature / 10;
                                    DecimalFormat decimalFormat = new DecimalFormat("#0.0");
                                    Platform.runLater(() -> {
                                        temperatureLabel.setText("" + decimalFormat.format(temp) + " ℃");
                                    });
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    };

                    thread.start();

                    upListPane.getChildren().clear();
                    if (upList.isEmpty() || upList.size() == 0) {
                        upListPane.getChildren().add(noDataLabel1);
                    } else {
                        upListPane.getChildren().add(new SingleTableView(upList, true));
                    }

                    downListPane.getChildren().clear();
                    if (downList.isEmpty() || downList.size() == 0) {
                        downListPane.getChildren().add(noDataLabel2);
                    } else {
                        downListPane.getChildren().add(new SingleTableView(downList, false));
                    }

                } catch (NotTransactionDayException e) {
                    // 若不是交易日，处理异常
                    clearPane();
                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                    autoCloseStage.showErrorBox("当日无该板块数据");
                }
            }
        });
    }

    /**
     * 搜索按钮结果，发起搜索
     */
    @FXML
    private void searchInfo() {
        MarketTemperatureVO marketTemperatureVO = null;
        try {
            marketTemperatureVO = stockBLService.getMarketInfoInPlate((String) plateNameChoiceBox.getValue(), new DatePickerSettings().getDate(searchDate));
        } catch (NotTransactionDayException e) {
            clearPane();
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            if ((plateNameChoiceBox.getValue()).equals("市场")) {
                autoCloseStage.showErrorBox("该日不是交易日");
            } else {
                autoCloseStage.showErrorBox("当日无该板块数据");
            }


        }
        updateInfo(marketTemperatureVO);
    }

    /**
     * 当日市场温度按钮结果，显示市场温度计算方法
     */
    @FXML
    private void temperatureInfo() {
        AutoCloseStage autoCloseStage = new AutoCloseStage();
        autoCloseStage.showErrorBox("\"市场温度计\"是Octopus团队自主研发的，记录当日市场股票状况的方法。标准温度为0，具体温度根据涨跌停股票数计算。");
    }

    /**
     * 鼠标位置监听，按钮颜色变化
     */
    @FXML
    private void turnPink() {
        temButton.setStyle("-fx-text-fill: #cda79c; -fx-background-color: transparent;");
    }

    @FXML
    private void turnWhite() {
        temButton.setStyle("-fx-text-fill: #838383; -fx-background-color: transparent;");
    }

    /**
     * 按钮响应设置
     */
    @FXML
    private void conTurnRed() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void conTurnWhite() {
        confirmButton.setStyle("-fx-text-fill: #689cc4;  -fx-background-color: transparent; -fx-border-color: #689cc4; -fx-border-width: 1");
    }

    /**
     * 把界面上数据置零的方法
     */
    private void clearPane() {
        totalTradeLabel.setText("--");
        limitUpLabel.setText("--");
        limitDownLabel.setText("--");
        up5perLabel.setText("--");
        down5perLabel.setText("--");
        openUp5perLabel.setText("--");
        openDown5perLabel.setText("--");
        upListPane.getChildren().clear();
        upListPane.getChildren().add(noDataLabel1);
        downListPane.getChildren().clear();
        downListPane.getChildren().add(noDataLabel2);
    }


}
