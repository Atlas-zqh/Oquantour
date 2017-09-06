package ui.compare;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import exception.BothStockNotExistException;
import exception.FirstStockNotExistException;
import exception.SecondStockNotExistException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.apache.avro.generic.GenericData;
import ui.ControlledStage;
import ui.StageController;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import ui.charts.SingleLineChart;
import ui.util.SearchNotificatePane;
import vo.SearchVO;
import vo.StockCompareVO;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by st on 2017/3/4.
 */
public class StockCompareViewController implements ControlledStage {

    private StageController stageController;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    private String resource = "StockCompareView.fxml";
    private StockBLService stockBLService;
    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    TextField stock1;
    @FXML
    TextField stock2;
    @FXML
    Label stockLabel1;
    @FXML
    Label upLabel1;
    @FXML
    Label maxLabel1;
    @FXML
    Label minLabel1;
    @FXML
    Label varianceLabel1;
    @FXML
    Label stockLabel2;
    @FXML
    Label upLabel2;
    @FXML
    Label maxLabel2;
    @FXML
    Label minLabel2;
    @FXML
    Label varianceLabel2;
    @FXML
    Pane closeValuePane;
    @FXML
    Pane logReturnPane;
    @FXML
    Pane searchPane;
    @FXML
    Button confirmButton;

    @FXML
    private Pane searchNotificationPane1;

    @FXML
    private ScrollPane searchScrollPane1;

    @FXML
    private Label nullLabel1;

    @FXML
    private Pane searchNotificationPane2;

    @FXML
    private ScrollPane searchScrollPane2;

    @FXML
    private Label nullLabel2;

    /**
     * 初始化方法
     * 初始化日期选择器，设定初始值及选择范围
     */
    public void initial(SearchVO searchVO) {
        ZoneId zone = ZoneId.systemDefault();

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        startDate.setDayCellFactory(datePickerSettings.getStartSettings(startDate));
        endDate.setDayCellFactory(datePickerSettings.getEndSettings(startDate, endDate));
        startDate.setValue(searchVO.startDate.toInstant().atZone(zone).toLocalDate());
        endDate.setValue(searchVO.endDate.toInstant().atZone(zone).toLocalDate());

        stock1.setText(searchVO.stock1);
        stock2.setText(searchVO.stock2);

        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                searchInfo();
            }
        });

        updateInfo(searchVO);

        SearchNotificatePane searchNotificatePane1 = new SearchNotificatePane(stock1, searchNotificationPane1, nullLabel1, searchScrollPane1);
        searchNotificatePane1.setTextField(120, 25, 75);

        SearchNotificatePane searchNotificatePane2 = new SearchNotificatePane(stock2, searchNotificationPane2, nullLabel2, searchScrollPane2);
        searchNotificatePane2.setTextField(120, 25, 75);
    }

    /**
     * 搜索按钮响应方法，把开始日期、结束日期及两只股票打包成searchVO后进行搜索
     */
    @FXML
    private void searchInfo() {
        if (stock1.getText().equals(stock2.getText())) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("请输入两只不同股票");
        } else if (stock1.getText().equals("") || stock2.getText().equals("")) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("请输入股票代码或名称");
        } else {
            SearchVO searchVO = wrapSearchInfo();
            updateInfo(searchVO);
        }
        searchScrollPane1.setVisible(false);
        searchScrollPane2.setVisible(false);
    }

    /**
     * 更新信息方法，根据搜索条件，显示股票数据和图表
     *
     * @param searchVO
     */
    private void updateInfo(SearchVO searchVO) {
        stockBLService = new StockBL();
//        System.out.println("start: "+System.currentTimeMillis());
        try {
            List<StockCompareVO> stockCompareVOS = stockBLService.compareStock(searchVO);
            //        System.out.println("pxr: "+System.currentTimeMillis());
//        System.out.println("line133: "+stockCompareVOS.size());
            stockLabel1.setText(stockCompareVOS.get(0).stockName);
            stockLabel2.setText(stockCompareVOS.get(1).stockName);

            double upPercent1 = stockCompareVOS.get(0).chg;
            double upPercent2 = stockCompareVOS.get(1).chg;

            DecimalFormat decimalFormat1 = new DecimalFormat("#.##%");
            DecimalFormat decimalFormat2 = new DecimalFormat("#.#####E0");

            upLabel1.setText(decimalFormat1.format(upPercent1));
            upLabel2.setText(decimalFormat1.format(upPercent2));
            setLabelStyle(upLabel1, upPercent1);
            setLabelStyle(upLabel2, upPercent2);

            // 获得两只股票的最大值、最小值和对数收益率方差
            maxLabel1.setText(String.valueOf(stockCompareVOS.get(0).high));
            minLabel1.setText(String.valueOf(stockCompareVOS.get(0).low));
            maxLabel2.setText(String.valueOf(stockCompareVOS.get(1).high));
            minLabel2.setText(String.valueOf(stockCompareVOS.get(1).low));
            varianceLabel1.setText(decimalFormat2.format(stockCompareVOS.get(0).logReturnVariance));
            varianceLabel2.setText(decimalFormat2.format(stockCompareVOS.get(1).logReturnVariance));

//        // 获得每天涨跌幅
//        List<Double> chg1 = stockCompareVOS.get(0).chg;
//        List<Double> chg2 = stockCompareVOS.get(1).chg;
            // 获得每天收盘价
            List<Double> closeValues1 = new ArrayList<>();
            List<Double> closeValues2 = new ArrayList<>();
            // 获得每天对数收益率
            List<Double> logReturn1 = new ArrayList<>();
            List<Double> logReturn2 = new ArrayList<>();
            // 获得日期
            List<Date> dateList1 = stockCompareVOS.get(0).dateList;
            List<Date> dateList2 = stockCompareVOS.get(1).dateList;

            List<Date> dateList = new ArrayList<>();


            //处理数据，获得两只股票日期与数据的交集
            int i = 0;
            int j = 0;
            while(i < dateList1.size() && j < dateList2.size() ){
                if(dateList1.get(i).equals(dateList2.get(j))){
                    dateList.add(dateList1.get(i));
                    closeValues1.add(stockCompareVOS.get(0).closeList.get(i));
                    closeValues2.add(stockCompareVOS.get(1).closeList.get(j));
                    logReturn1.add(stockCompareVOS.get(0).logReturnList.get(i));
                    logReturn2.add(stockCompareVOS.get(1).logReturnList.get(j));
                    i++;
                    j++;
                }
                else if(dateList1.get(i).after(dateList2.get(j))){
                    j++;
                }
                else if(dateList2.get(j).after(dateList1.get(i))){
                    i++;
                }
            }
            System.out.print(dateList.size());
//        System.out.println("line163: valueSize "+closeValues1.size());
//        System.out.println("line164: dateSize "+dateList.size());

            String stockName1 = stockCompareVOS.get(0).stockName;
            String stockName2 = stockCompareVOS.get(1).stockName;

//        List<Double> closeList1 = new ArrayList<>();
//        List<Double> logReturnList1 = new ArrayList<>();
//        List<Date> dateList1 = new ArrayList<>();
//        for (CompareDataVO compareDataVO : stockCompareVOS.get(0).compareDataVOS) {
//            closeList1.add(compareDataVO.close);
//            logReturnList1.add(compareDataVO.logReturn);
//            dateList1.add(compareDataVO.date);
//        }
//
//        List<Double> closeList2 = new ArrayList<>();
//        List<Double> logReturnList2 = new ArrayList<>();
//        List<Date> dateList2 = new ArrayList<>();
//        for (CompareDataVO compareDataVO : stockCompareVOS.get(1).compareDataVOS) {
//            closeList2.add(compareDataVO.close);
//            logReturnList2.add(compareDataVO.logReturn);
//            dateList2.add(compareDataVO.date);
//        }


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    closeValuePane.getChildren().clear();
                    closeValuePane.getChildren().add(new SingleLineChart(closeValues1, closeValues2, dateList, dateList,
                            stockName1, stockName2, "每日收盘价"));
                    logReturnPane.getChildren().clear();
                    logReturnPane.getChildren().add(new SingleLineChart(logReturn1, logReturn2, dateList, dateList,
                            stockName1, stockName2, "对数收益率"));
                }
            });

//        System.out.println("st: "+System.currentTimeMillis());
        } catch (BothStockNotExistException b) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("找不到数据");
        } catch (FirstStockNotExistException f) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("找不到\n第一只股票的数据");
        } catch (SecondStockNotExistException s) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("找不到\n第二只股票的数据");
        }
    }

    /**
     * 获得用户输入的搜索信息，并打包成SearchVO
     *
     * @return
     */
    private SearchVO wrapSearchInfo() {
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        SearchVO searchVO = new SearchVO(stock1.getText(), stock2.getText(),
                datePickerSettings.getDate(startDate), datePickerSettings.getDate(endDate));
        return searchVO;
    }

    /**
     * 根据涨跌幅设置数字颜色，红色表示涨，绿色表示跌
     *
     * @param label
     * @param value
     */
    private void setLabelStyle(Label label, double value) {
        String text = label.getText();
//        System.out.println(text);
        if (value > 0) {
            label.setStyle("-fx-text-fill: #d97555");
//            label.setText(text + "   ∧");
        } else if (value < 0) {
            label.setStyle("-fx-text-fill: #4c9b8e");
//            label.setText(text + "   ∨");
//            label.setText(text + "   ∧");
        } else {
            label.setStyle("-fx-text-fill: white");
        }
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
}
