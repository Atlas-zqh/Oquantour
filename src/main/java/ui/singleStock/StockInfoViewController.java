package ui.singleStock;


import bl.stock.StockBL;
import bl.stock.StockBLService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ui.*;
import ui.charts.AdvCandleStickChart;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import ui.util.SearchNotificatePane;
import vo.DailyAverageVO;
import vo.KLineVO;
import vo.SearchVO;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by st on 2017/3/4.
 */
public class StockInfoViewController implements ControlledStage {
    private StageController stageController = new StageController();

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private List<KLineVO> kLineVOS;

    private List<List<DailyAverageVO>> average;

    @FXML
    private Button confirmButton;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField stockID;

    @FXML
    private Label stockIDLabel;

    @FXML
    private Label stockNameLabel;

    @FXML
    private Pane kLinePane;

    @FXML
    private Pane searchPane;

    @FXML
    private Pane loadingPane;

    @FXML
    private Pane searchNotificationPane;

    @FXML
    private ScrollPane searchScrollPane;

    @FXML
    private Label nullLabel;


    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    /**
     * 确认按钮方法
     * 点击后进行 搜索
     */
    @FXML
    private void confirm() {
        if (stockID.getText().equals("")) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("请输入股票代码或名称");
        } else {
            StockBLService stockBL = new StockBL();
            try {
                kLineVOS = stockBL.getKLineInfo(getSearchVO());
                average = stockBL.getDailyAverageInfo(getSearchVO());
                setStockInfo();

            } catch (Exception e) {
                //无股票信息
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("找不到数据");
            }
        }
        searchScrollPane.setVisible(false);
    }

    /**
     * 股票信息界面初始化方法
     * 设置搜索界面传来的参数
     *
     * @param searchVO
     */
    public void init(SearchVO searchVO, List<KLineVO> kLines, List<List<DailyAverageVO>> ave) {
        ZoneId zone = ZoneId.systemDefault();

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        startDate.setDayCellFactory(datePickerSettings.getStartSettings(startDate));
        endDate.setDayCellFactory(datePickerSettings.getEndSettings(startDate, endDate));

        //设置上一界面所选日期信息及股票信息
        startDate.setValue(searchVO.startDate.toInstant().atZone(zone).toLocalDate());
        endDate.setValue(searchVO.endDate.toInstant().atZone(zone).toLocalDate());
        stockID.setText(searchVO.stock1);

        //设置股票信息
        kLineVOS = kLines;
        average = ave;
        if (!kLineVOS.isEmpty()) {
            stockNameLabel.setText(kLineVOS.get(0).stockName);
            stockIDLabel.setText(kLineVOS.get(0).stockCode);
        }

        setStockInfo();


        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                confirm();
            }
        });

        SearchNotificatePane searchNotificatePane = new SearchNotificatePane(stockID, searchNotificationPane, nullLabel, searchScrollPane);
        searchNotificatePane.setTextField(150, 25, 90);
    }

    /**
     * 设置股票信息
     */
    private void setStockInfo() {

        stockNameLabel.setText(kLineVOS.get(0).stockName);
        stockIDLabel.setText(kLineVOS.get(0).stockCode);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //获得kline数据大小
                int num = kLineVOS.size();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

                //记录数据 分别为open close high low
                double[][] data = new double[num][5];

                //记录日期
                double[] dates = new double[num];
                String[] ds = new String[num];

                String d;
                for (int i = 0; i < num; i++) {
                    d = sdf.format(kLineVOS.get(i).date);
                    ds[i] = d;
                    data[i][1] = kLineVOS.get(i).open;
                    data[i][2] = kLineVOS.get(i).close;
                    data[i][3] = kLineVOS.get(i).high;
                    data[i][4] = kLineVOS.get(i).low;
                }

                //获得均线图信息
                DecimalFormat df = new DecimalFormat("#.000");
                if (!average.isEmpty()) {
                    double[][] ave = new double[num][6];

                    for (int i = 0; i < num; i++) {
                        for (int j = 0; j < 6; j++) {
                            ave[i][j] = 0;
                        }
                    }

                    for (int i = 0; i < average.size(); i++) {
                        int length = average.get(i).size() - 1;
                        for (int j = num - 1; j > num - average.get(i).size() - 1 && j >= 0; j--) {
                            String s = df.format(average.get(i).get(length).average);
                            ave[j][i] = Double.parseDouble(s);
                            length--;
                        }
                    }

                    //绘制均线图
                    kLinePane.getChildren().clear();
                    Task<Void> task1 = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    kLinePane.getStylesheets().add("ui/Ensemble_AdvCandleStickChart.css");
                                    kLinePane.getChildren().add(new AdvCandleStickChart(data, ds, ave));
                                }
                            });
                            return null;
                        }
                    };
                    Thread thread1 = new Thread(task1);
                    thread1.start();
                    closeLoading();
                }

            }
        });


    }


    public void closeLoading() {
        Platform.runLater(() -> loadingPane.setLayoutY(720));
    }

    /**
     * 按钮响应设置
     */
    @FXML
    private void turnRed() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void turnWhite() {
        confirmButton.setStyle("-fx-text-fill: #689cc4;  -fx-background-color: transparent; -fx-border-color: #689cc4; -fx-border-width: 1");
    }

    /**
     * 获得SearchVO
     *
     * @return
     */
    private SearchVO getSearchVO() {
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        Date sDate = datePickerSettings.getDate(startDate);
        Date eDate = datePickerSettings.getDate(endDate);
        return new SearchVO(stockID.getText(), "", sDate, eDate);
    }

    /**
     * 开始时间选择器监听，选择开始时间后，结束时间默认为开始时间的后两个月
     */
    @FXML
    private void changeEndDate() {
        ZoneId zone = ZoneId.systemDefault();
        try {
            if (startDate.getValue().plusMonths(2).
                    isAfter(sdf.parse("2014-4-29").toInstant().atZone(zone).toLocalDate())) {
                // 若开始时间的两个月后在2014／12／31之后，则设置结束时间为2014／12／31
                endDate.setValue(sdf.parse("2014-4-29").toInstant().atZone(zone).toLocalDate());
            } else {
                endDate.setValue(startDate.getValue().plusMonths(2));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
