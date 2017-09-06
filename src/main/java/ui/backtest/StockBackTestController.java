package ui.backtest;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import bl.strategy.BackTestingBLService;
import bl.strategy.BackTestingBLServiceImpl;
import bl.strategy.WinningRateBLService;
import bl.strategy.WinningRateBLServiceImpl;
import bl.tools.StrategyType;
import exception.FormativePeriodNotExistException;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.apache.commons.lang.StringUtils;
import ui.ControlledStage;
import ui.MainViewController;
import ui.StageController;
import ui.charts.SingleAreaChart;
import ui.charts.SingleBarChart;
import ui.charts.SingleLineChart;
import ui.charts.SingleTableViewOfBackTest;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import vo.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by island on 2017/3/26.
 */
public class StockBackTestController implements ControlledStage {

    private StageController stageController;

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private List<Button> buttons = new ArrayList<>();

    private List<Button> selectedStocks = new ArrayList<>();

    private List<String> selectedStocksName = new ArrayList<>();

    private List<BackTestingSingleChartVO> backTestingSingleChartVOS;

    private StrategyStatisticsVO strategyStatisticsVO;

    private BackTestingBLService backTestingBLService;

    private List<WinningRateVO> winningRateVOS;

    private List<WinnerInfoVO> winnerInfoVOS;

    private BackTestingJudgeVO backTestingJudgeVO;
    //固定周期的名字
    private String type;
    //是否选择哦那光亮
    private boolean momentumIsSelected;
    //SHOWALL
    private boolean showAll = false;
    //选择的板块名
    private String plateName = "";

    private boolean[] checkBoxs = {false, false, false};

    @FXML
    private Button confirmButton;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Label annualizedReturnRateLabel;

    @FXML
    private Label stdAnnualizedReturnRateLabel;

    @FXML
    private Label informationRatioLabel;

    @FXML
    private Label betaLabel;

    @FXML
    private Label alphaLabel;

    @FXML
    private Label sharpeLabel;

    @FXML
    private Label algorithmVolatilityLabel;

    @FXML
    private Label benchmarkVolatilityLabel;

    @FXML
    private Label maxDrawdownLabel;

    @FXML
    private Pane contentPane1;

    @FXML
    private Pane contentPane2;

    @FXML
    private Tab plateTab;

    @FXML
    private Tab stockTab;

    @FXML
    private TextField holdingPeriod;

    /**
     * 均值回归相关
     */

    @FXML
    private TextField meanMostNumOfStock;

    @FXML
    private ChoiceBox meanTypeChoiceBox;

    @FXML
    private TextField meanMostNumOfStock1;

    @FXML
    private ChoiceBox meanTypeChoiceBox1;

    @FXML
    private Tab meanTab;

    /**
     * 动量策略相关
     */
    @FXML
    private TextField momentumFormativePeriod;

    @FXML
    private Tab momentumTab;

    /**
     * 板块选择相关
     */
    @FXML
    private Button mainBoardButton;

    @FXML
    private Button startUpBoardButton;

    @FXML
    private Button smeBoardButton;

    @FXML
    private Pane momentumPane;

    @FXML
    private Pane meanPane;

    @FXML
    private Pane stockPane;

    @FXML
    private Label numOfStocksLabel;

    @FXML
    private TextField stockTextField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane arrorImage;

    @FXML
    private Pane searchNotificationPane;

    @FXML
    private ScrollPane searchScrollPane;

    @FXML
    private Label nullLabel;

    @FXML
    private TabPane stockTabPane;

    @FXML
    private TabPane strategyTabPane;

    @FXML
    private TabPane periodTabPane;

    @FXML
    private TextField periodTextField;

    @FXML
    private Label periodLabel;

    @FXML
    private Pane statisticPane;

    @FXML
    private CheckBox mainCheckBox;

    @FXML
    private CheckBox stCheckBox;

    @FXML
    private CheckBox smeCheckBox;

    @FXML
    private Pane barPane;

    @FXML
    private Label winningRateLabel;

    @FXML
    private Label plusLabel;

    @FXML
    private Label minusLabel;

    @FXML
    private Pane winningPane;

    @FXML
    private Label titleLabel;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }


    public void initial(BackTestingInfoVO backTestingInfoVO, List<BackTestingSingleChartVO> backTestingSingleChartVOS, StrategyStatisticsVO strategyStatisticsVO, List<WinnerInfoVO> winnerInfoVOS, String type,  List<WinningRateVO> winningRateVOS, boolean[] checkBoxs){
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        startDate.setDayCellFactory(datePickerSettings.getStartSettings(startDate));
        endDate.setDayCellFactory(datePickerSettings.getBackTestEndSettings(startDate, endDate));

        ObservableList<String> means = FXCollections.observableArrayList();
        means.addAll("5日均线", "10日均线", "20日均线");
        setAllStock();
        setTextField();
        this.type = type;
        this.checkBoxs = checkBoxs;
        //设置时间
        endDate.setValue(backTestingInfoVO.endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        startDate.setValue(backTestingInfoVO.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        selectedStocksName = backTestingInfoVO.stocks;
        if(type.equals("")) {
            strategyTabPane.setVisible(true);
            strategyTabPane.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Tab>() {
                        @Override
                        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                            if (strategyTabPane.getSelectionModel().isSelected(0)){
                                momentumIsSelected = true;
                            }
                            if (strategyTabPane.getSelectionModel().isSelected(1)){
                                meanTypeChoiceBox.setItems(means);
                                momentumIsSelected = false;
                            }
                        }
                    }
            );
            holdingPeriod.setText(backTestingInfoVO.holdingPeriod + "");
            //动量
            if (backTestingInfoVO.strategyType == StrategyType.MOMENTUM) {
                momentumFormativePeriod.setText(backTestingInfoVO.formativePeriod + "");
                momentumIsSelected = true;
            }
            //均值
            else {
                strategyTabPane.getSelectionModel().select(1);
                int ma = backTestingInfoVO.ma_length;
                meanTypeChoiceBox.setItems(means);
                meanTypeChoiceBox.setValue(ma + "日均线");
                meanMostNumOfStock.setText(backTestingInfoVO.maxholdingStocks + "");
                momentumIsSelected = false;
            }
        }else{
            periodTabPane.setVisible(true);
            periodTabPane.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Tab>() {
                        @Override
                        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                            if(periodTabPane.getSelectionModel().isSelected(0)){
                                momentumIsSelected = true;
                            }
                            if (periodTabPane.getSelectionModel().isSelected(1)){
                                meanTypeChoiceBox1.setItems(means);
                                momentumIsSelected = false;
                            }
                        }
                    }
            );
            holdingPeriod.setVisible(false);
            statisticPane.setVisible(false);
            //动量
            if (backTestingInfoVO.strategyType == StrategyType.MOMENTUM) {
                if(type.equals("形成期")){
                    periodTextField.setText(backTestingInfoVO.formativePeriod + "");
                    periodLabel.setText("形  成  期");
                }
                else{
                    periodTextField.setText(backTestingInfoVO.holdingPeriod + "");
                    periodLabel.setText("持  有  期");
                }
                momentumIsSelected = true;
            }
            //均值
            else {
                periodTabPane.getSelectionModel().select(1);
                int ma = backTestingInfoVO.ma_length;
                meanTypeChoiceBox1.setItems(means);
                meanTypeChoiceBox1.setValue(ma + "日均线");
                meanMostNumOfStock1.setText(backTestingInfoVO.maxholdingStocks + "");
                momentumIsSelected = false;
            }


        }

        String plateName = backTestingInfoVO.stockPlateName;
        //选择板块
        if(!plateName.equals("")){
            if(plateName.equals("主板"))
                selectMainBoard();
            else if(plateName.equals("中小板"))
                selectSMEBoard();
            else if(plateName.equals("创业板"))
                selectStartUpBoard();
        }
        //选择股票
        else{
            stockTabPane.getSelectionModel().select(1);
            for(int i = 0; i < selectedStocksName.size(); i++){
                for(int j = 0; j < buttons.size(); j++){
                    Button b = buttons.get(j);
                    if(selectedStocksName.get(i).equals(b.getText().split(" ")[0])) {
                        selectedStocks.add(b);
                        updateLabel();
                        b.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                        break;
                    }
                }
            }
            if(checkBoxs[0] == true)
                mainCheckBox.selectedProperty().setValue(true);
            if(checkBoxs[1] == true)
                stCheckBox.selectedProperty().setValue(true);
            if(checkBoxs[2] == true)
                smeCheckBox.selectedProperty().setValue(true);

        }



        if(type.equals("")) {
            backTestingBLService = BackTestingBLServiceImpl.getInstance();
            List<Double> returnRateDistributionVOS = backTestingBLService.getReturnRateDistribution();
            updateBackTest(backTestingSingleChartVOS, strategyStatisticsVO, returnRateDistributionVOS, winnerInfoVOS);
        }else{
            contentPane1.setLayoutY(460);
            contentPane2.setLayoutY(1160);
            updateBestPeriod(winningRateVOS);
        }
    }

    /**
     * 选择一种板块的所有股票
     * @param plateName
     */
    private void selectOneBoard(String plateName, CheckBox checkBox){
        StockBLService stockBLService = new StockBL();
        List<String> stocks = stockBLService.getAllStockCodeAndNameInPlate(plateName);
        String s;
        if(checkBox.isSelected()) {
            if(plateName.equals("深市主板"))
                checkBoxs[0] = true;
            if(plateName.equals("创业板"))
                checkBoxs[1] = true;
            if(plateName.equals("中小板"))
                checkBoxs[2] = true;

            for(int i = 0; i < stocks.size(); i++){
                s = stocks.get(i).split(";")[0] + " " + stocks.get(i).split(";")[1];
                for(int j = 0; j < buttons.size(); j++){
                    if(s.equals(buttons.get(j).getText())){
                        if(!selectedStocks.contains(buttons.get(j))){
                            buttons.get(j).setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                            selectedStocks.add(buttons.get(j));
                            break;
                        }
                    }
                }
            }
        }
        else {
            if(plateName.equals("深市主板"))
                checkBoxs[0] = false;
            if(plateName.equals("创业板"))
                checkBoxs[1] = false;
            if(plateName.equals("中小板"))
                checkBoxs[2] = false;
            for(int i = 0; i < stocks.size(); i++){
                s = stocks.get(i).split(";")[0] + " " + stocks.get(i).split(";")[1];
                for(int j = 0; j < buttons.size(); j++){
                    if(s.equals(buttons.get(j).getText())){
                        if(selectedStocks.contains(buttons.get(j))){
                            buttons.get(j).setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                            selectedStocks.remove(buttons.get(j));
                            break;
                        }
                    }
                }
            }        }
        updateLabel();
    }

    /**
     * 选择所有主板股票
     */
    @FXML
    private void selectMain(){
        selectOneBoard("深市主板", mainCheckBox);
    }

    /**
     * 选择所有创业板股票
     */
    @FXML
    private void selectSt(){
        selectOneBoard("创业板", stCheckBox);
    }

    /**
     * 选择所有中小板股票
     */
    @FXML
    private void selectSme(){
        selectOneBoard("中小板", smeCheckBox);
    }


    public void updateBackTest(List<BackTestingSingleChartVO> backTestingSingleChartVOS, StrategyStatisticsVO strategyStatisticsVO, List<Double> returnRateDistribution, List<WinnerInfoVO> winnerInfoVOS){
        this.backTestingSingleChartVOS = backTestingSingleChartVOS;
        List<Double> backtestValues = new ArrayList<>();
        List<Double> stdValues = new ArrayList<>();
        List<Date> dateList = new ArrayList<>();
        BackTestingSingleChartVO backTestingSingleChartVO;
        for(int i = 0; i < backTestingSingleChartVOS.size(); i++){
            backTestingSingleChartVO = backTestingSingleChartVOS.get(i);
            backtestValues.add(backTestingSingleChartVO.backTestValue);
            stdValues.add(backTestingSingleChartVO.stdValue);
            dateList.add(backTestingSingleChartVO.date);
        }

        contentPane1.getChildren().clear();
        contentPane1.getChildren().add(new SingleLineChart(backtestValues, stdValues, dateList, dateList, "策略",  "基准", "策略和基准的累计收益率比较图"));

        contentPane2.getChildren().clear();
        contentPane2.getChildren().add(new SingleBarChart(returnRateDistribution));

        barPane.setVisible(true);
        double plus = 0;
        double minus = 0;
        for(int i = 0; i < returnRateDistribution.size(); i++){
            if(returnRateDistribution.get(i) >= 0 )
                plus++;
            else
                minus++;

        }

        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat df1 = new DecimalFormat("#");
        double rate =  plus / (plus+minus);
        plusLabel.setText(df1.format(plus));
        minusLabel.setText(df1.format(minus));
        winningRateLabel.setText(df.format(rate * 100) + "%");

        this.strategyStatisticsVO = strategyStatisticsVO;
        alphaLabel.setText(df.format(strategyStatisticsVO.alpha * 100) + "%");
        betaLabel.setText(df.format(strategyStatisticsVO.beta));
        algorithmVolatilityLabel.setText(df.format(strategyStatisticsVO.algorithmVolatility * 100) + "%");
        annualizedReturnRateLabel.setText(df.format(strategyStatisticsVO.annualizedReturnRate * 100) + "%");
        benchmarkVolatilityLabel.setText(df.format(strategyStatisticsVO.benchmarkVolatility * 100) + "%");
        informationRatioLabel.setText(df.format(strategyStatisticsVO.informationRatio));
        maxDrawdownLabel.setText(df.format(strategyStatisticsVO.maxDrawdown * 100) + "%");
        sharpeLabel.setText(df.format(strategyStatisticsVO.sharpe));
        stdAnnualizedReturnRateLabel.setText(df.format(strategyStatisticsVO.stdAnnualizedReturnRate * 100) + "%");

        winningPane.getChildren().clear();
        winningPane.getChildren().add(titleLabel);
        int height = 52;
        if(winnerInfoVOS.get(0).getWinners().size() <= 10){
            height += winnerInfoVOS.get(0).getWinners().size() * 25;
        }else{
            height += 250;
        }
        winningPane.setPrefHeight(30 + (height+20) * winnerInfoVOS.size());
        for(int i = 0; i < winnerInfoVOS.size(); i++){
            Pane pane = new SingleTableViewOfBackTest(winnerInfoVOS.get(i), height);
            winningPane.getChildren().add(pane);
            pane.setLayoutY(30 + (height+20) * i);
            pane.setLayoutX(20);
        }

    }

    private void updateBestPeriod(List<WinningRateVO> winningRateVOS){
        this.winningRateVOS = winningRateVOS;
        List<Double> extraReturnRates = new ArrayList<>();
        List<Double> winningRates = new ArrayList<>();
        List<Integer> days = new ArrayList<>();
        for(int i = 0; i < winningRateVOS.size(); i++){
            extraReturnRates.add(winningRateVOS.get(i).getExtraReturnRate());
            winningRates.add(winningRateVOS.get(i).getWinningRate());
            days.add(winningRateVOS.get(i).getDays());
        }
        contentPane1.getChildren().clear();
        contentPane1.getChildren().add(new SingleAreaChart(extraReturnRates, days, "超额收益 vs 全样本-不同计算周期", type));

        contentPane2.getChildren().clear();
        contentPane2.getChildren().add(new SingleAreaChart(winningRates, days, "策略胜率% - 不同计算周期", type));

    }
    /**
     * 设置输入框监听
     */
    private void setTextField(){
        stockTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchNotificationPane.getChildren().clear();
                String s = stockTextField.getText();
                //判断当前输入框中有无输入
                if(!s.equals("")) {
                    List<String> possibleStocks = new ArrayList<>();
                    String stockID = "";
                    String stockName = "";
                    for (int i = 0; i < buttons.size(); i++) {
                        Button b = buttons.get(i);
                        stockID = b.getText().split(" ")[0];
                        stockName = b.getText().split(" ")[1];
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
                        searchNotificationPane.setPrefHeight(110);
                    } else {
                        nullLabel.setVisible(false);
                        searchNotificationPane.setPrefHeight(possibleStocks.size() * 30);
                        for (int i = 0; i < possibleStocks.size(); i++) {
                            Button stockButton = new Button(possibleStocks.get(i));
                            stockButton.setLayoutY(i * 30);
                            stockButton.setPrefHeight(30);
                            stockButton.setPrefWidth(150);
                            stockButton.setStyle("-fx-background-color: #ffffff00; -fx-text-fill: #fffffff");
                            searchNotificationPane.getChildren().add(stockButton);
                            stockButton.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                                stockButton.setStyle("-fx-background-color: #689cc4; -fx-text-fill: #ffffff");
                            });
                            stockButton.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                                stockButton.setStyle("-fx-background-color: #ffffff00; -fx-text-fill: #ffffff");
                            });
                            stockButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                                stockTextField.setText(stockButton.getText());
                                searchScrollPane.setVisible(false);
                                search();
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

    /**
     * 搜索按钮响应
     */
    @FXML
    private void search(){
        searchScrollPane.setVisible(false);
        String stock = stockTextField.getText();
        String stockID = "";
        String stockName = "";
        if(stock.equals("")) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("请输入股票ID或股票名称");
            return;
        }
        for(int i = 0; i < buttons.size(); i++){
            String[] s = buttons.get(i).getText().split(" ");
            stockID = s[0];
            stockName = s[1];
            if(stock.equals(stockID) || stock.equals(stockName)){
                scrollPane.setVvalue( (buttons.get(i).getLayoutY()) / (stockPane.getPrefHeight() - 60));
                int row = i % 4;
                if(row == 0)
                    arrorImage.setLayoutX(-5);
                else if(row == 1)
                    arrorImage.setLayoutX(155);
                else if(row == 2)
                    arrorImage.setLayoutX(315);
                else if(row == 3)
                    arrorImage.setLayoutX(475);

                int line = i / 4;
                arrorImage.setLayoutY(20 + line * 50);
                arrorImage.setOpacity(1);

                break;
            }
            if(i == buttons.size() - 1){
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("没有该支股票");
            }
        }
    }

    /**
     * 初始化所有股票
     */
    private void setAllStock(){
        StockBLService stockBLService = new StockBL();
        List<String> allStocks = stockBLService.getAllStockCodeAndName();
        String[][] stocks = new String[allStocks.size()][2];
        stockPane.setPrefHeight(((allStocks.size() / 4) + 1) * 50 + 10);
        for(int i = 0; i < allStocks.size(); i++){
            stocks[i][0] = allStocks.get(i).split(";")[0];
            stocks[i][1] = allStocks.get(i).split(";")[1];
        }

        for(int i = 0; i < stocks.length; i++){
            Button b = new Button(stocks[i][1] + " " + stocks[i][0]);
            stockPane.getChildren().add(b);
            int row = i % 4;
            if(row == 0)
                b.setLayoutX(10);
            else if(row == 1)
                b.setLayoutX(170);
            else if(row == 2)
                b.setLayoutX(330);
            else if(row == 3)
                b.setLayoutX(490);

            int line = i / 4;
            b.setLayoutY(10 + line * 50);
            b.setPrefWidth(140);
            b.setPrefHeight(35);

            b.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            b.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                if(b.getStyle().equals("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand"))
                    b.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            });
            b.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                if(b.getStyle().equals("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand"))
                    b.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            });
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                arrorImage.setOpacity(0);
                if(b.getStyle().equals("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand") ||
                    b.getStyle().equals("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand")
                        ) {
                    b.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                    selectedStocks.add(b);
                }
                else {
                    b.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                    selectedStocks.remove(b);
                }
                updateLabel();

            });
            buttons.add(b);

        }
    }

    /**
     * 更新已选择股票数信息label
     */
    private void updateLabel(){
        numOfStocksLabel.setText("已选" + selectedStocks.size() + "支股票");
    }

    /**
     * 取消所有已选中股票
     */
    @FXML
    private void cancelAll(){
        if(!selectedStocks.isEmpty()) {
            for (int i = selectedStocks.size() - 1; i >= 0; i--) {
                selectedStocks.get(i).setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                selectedStocks.remove(selectedStocks.get(i));
            }
            updateLabel();
        }
    }


    /**
     * 选择主板
     */
    @FXML
    private void selectMainBoard(){
        initAllButton();
        plateName = "深市主板";
        mainBoardButton.setStyle("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    /**
     * 选择创业板
     */
    @FXML
    private void selectStartUpBoard(){
        initAllButton();
        plateName = "创业板";
        startUpBoardButton.setStyle("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    /**
     * 选择中小板
     */
    @FXML
    private void selectSMEBoard(){
        initAllButton();
        plateName = "中小板";
        smeBoardButton.setStyle("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    /**
     * 随机选择150支股票方法
     */
    @FXML
    private void randomSelect(){
        int maxNum = buttons.size() - 1;

        if(selectedStocks.size() + 200 > buttons.size()){
            if(selectedStocks.size() != buttons.size()) {
                for (int i = 0; i < buttons.size(); i++) {
                    if (!selectedStocks.contains(buttons.get(i))) {
                        buttons.get(i).setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                        selectedStocks.add(buttons.get(i));
                    }
                }
                updateLabel();
            }else{
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("超出股票总数");
            }
        }
        else {
            Random rand = new Random();
            int num = 0;
            for (int i = 0; i < 200; i++) {
                while (selectedStocks.contains(buttons.get(num))) {
                    num = rand.nextInt(maxNum);
                }
                selectedStocks.add(buttons.get(num));
                buttons.get(num).setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            }
            updateLabel();
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
                if(endDate.getValue()!=null) {
                    if (endDate.getValue().isBefore(startDate.getValue().plusMonths(2)))
                        endDate.setValue(startDate.getValue().plusMonths(2));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认按钮响应
     */
    @FXML
    private void confirm(){
        stageController = new StageController();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");

        int holding = 0;
        int formative = 0;
        int ma = 0;
        int mostNumOfStock = 0;


        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();


        //选择动量策略
        if(momentumIsSelected){
            if(type.equals("")) {
                String holdingPeriodS = holdingPeriod.getText();
                String formativePeriod = momentumFormativePeriod.getText();
                if (holdingPeriod.equals("") || formativePeriod.equals("")) {
                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                    autoCloseStage.showErrorBox("请输入形成期／持有期");
                } else {
                    if (StringUtils.isNumeric(holdingPeriodS) && StringUtils.isNumeric(formativePeriod)) {
                        holding = Integer.parseInt(holdingPeriodS);
                        formative = Integer.parseInt(formativePeriod);
                        getMomemtumStockInfo(formative, holding);
                    } else {
                        AutoCloseStage autoCloseStage = new AutoCloseStage();
                        autoCloseStage.showErrorBox("形成期／持有期请输入整数");
                    }
                }
            }
            else {
                String periodNum = periodTextField.getText();
                if (periodNum.equals("") || !StringUtils.isNumeric(periodNum)){
                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                    autoCloseStage.showErrorBox("请输入正确的周期");
                }else {
                    int num = Integer.parseInt(periodNum);
                    if (type.equals("持有期")) {
                        formative = num;
                        holding = num;
                    }
                    if (type.equals("形成期")) {
                        formative = num;
                        holding = num;
                    }
                    getMomemtumStockInfo(formative, holding);
                }
            }
        }
        //选择均值回归
        else {
            String maType = "";
            String mostNum = "";
            ChoiceBox choiceBox ;
            TextField textField ;
            if (type.equals("")) {
                choiceBox = meanTypeChoiceBox;
                textField = meanMostNumOfStock;

            }else{
                choiceBox = meanTypeChoiceBox1;
                textField = meanMostNumOfStock1;
            }
            if (choiceBox.getSelectionModel().getSelectedItem() != null)
                maType = (String) choiceBox.getSelectionModel().getSelectedItem().toString();
            mostNum = textField.getText();
            if ( maType.equals("") || mostNum.equals("")) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请正确填写均线种类／最多持仓股票数目");
            } else {
                if (StringUtils.isNumeric(mostNum)) {
                    mostNumOfStock = Integer.parseInt(mostNum);
                    if (maType.equals("5日均线"))
                        ma = 5;
                    if (maType.equals("10日均线"))
                        ma = 10;
                    if (maType.equals("20日均线"))
                        ma = 20;
                    if(type.equals("")){
                        String holdingPeriodS = holdingPeriod.getText();
                        if(StringUtils.isNumeric(holdingPeriodS)) {
                            holding = Integer.parseInt(holdingPeriodS);
                            getMeanStockInfo(ma, holding, mostNumOfStock);
                        }else{
                            AutoCloseStage autoCloseStage = new AutoCloseStage();
                            autoCloseStage.showErrorBox("持有期请输入整数");
                        }
                    }
                    else{
                        holding = ma;
                        getMeanStockInfo(ma, holding, mostNumOfStock);
                    }

                } else {
                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                    autoCloseStage.showErrorBox("最多持仓股票数目请输入整数");
                }
            }

        }
    }

    private void getMomemtumStockInfo(int formative, int holding){
        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        boolean plate = plateTab.isSelected();
        BackTestingInfoVO backTestingInfoVO;
        List<String> stocks = new ArrayList<>();
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        Date sDate = datePickerSettings.getDate(startDate);
        Date eDate = datePickerSettings.getDate(endDate);
        //选择板块
        if (plate) {
            if (plateName.equals("")) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请选择一个板块");
            } else {
                backTestingInfoVO = new BackTestingInfoVO(sDate, eDate, showAll, plateName, stocks, formative, holding, StrategyType.MOMENTUM);
                defineBackTest(backTestingInfoVO);
            }
        }
        //选择股票
        else {
            for (int i = 0; i < selectedStocks.size(); i++) {
                stocks.add(selectedStocks.get(i).getText().split(" ")[0]);
            }
            if (showAll)
                stocks.clear();

            backTestingInfoVO = new BackTestingInfoVO(sDate, eDate, showAll, "", stocks, formative, holding, StrategyType.MOMENTUM);

            if (stocks.size() < 100 && !showAll) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请至少选择100支股票");
            } else {
                defineBackTest(backTestingInfoVO);
            }
        }
    }

    public void getMeanStockInfo(int ma, int holding, int mostNumOfStock){
        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");

        boolean plate = plateTab.isSelected();
        BackTestingInfoVO backTestingInfoVO;
        List<String> stocks = new ArrayList<>();
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        Date sDate = datePickerSettings.getDate(startDate);
        Date eDate = datePickerSettings.getDate(endDate);
        //选择板块
        if (plate) {
            if(plateName.equals("")){
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请选择一个板块");
            }
            else {
                backTestingInfoVO = new BackTestingInfoVO(sDate, eDate, showAll, plateName, ma, stocks, holding, StrategyType.MEANREVERSION, mostNumOfStock);
                defineBackTest(backTestingInfoVO);
            }
        }
        //选择股票
        else {
            for (int i = 0; i < selectedStocks.size(); i++) {
                stocks.add(selectedStocks.get(i).getText().split(" ")[0]);
            }
            if (showAll)
                stocks.clear();

            backTestingInfoVO = new BackTestingInfoVO(sDate, eDate, showAll, "", ma, stocks, holding, StrategyType.MEANREVERSION, mostNumOfStock);

            if (stocks.size() < 100 && !showAll) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请至少选择100支股票");
            } else {
                defineBackTest(backTestingInfoVO);
            }
        }
    }

    /**
     * 设置界面为选择股票面板
     */
    private void defineBackTest(BackTestingInfoVO backTestingInfoVO){
        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        if(backTestingInfoVO.holdingPeriod <= 1 && !type.equals("形成期")) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("持有期必须大于1");
        }else {
            mainViewController.showLoading1();
            mainViewController.closeWaiting2();
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    backTestingBLService.setBackTestingInfo(backTestingInfoVO);
                    try {
                        backTestingJudgeVO = backTestingBLService.canBackTest();
                    } catch (SelectStocksException e) {
                        AutoCloseStage autoCloseStage = new AutoCloseStage();
                        autoCloseStage.showErrorBox("股票选择有问题");
                    }
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.start();


            task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                public void handle(WorkerStateEvent event) {
                    mainViewController.closeLoading1();
                    if (backTestingJudgeVO.getCanBackTest()) {
                        stageController = new StageController();
                        stageController.loadStage("backtest/BackTestErrorView.fxml");
                        BackTestErrorController backTestErrorController = (BackTestErrorController) stageController.getController("backtest/BackTestErrorView.fxml");
                        backTestErrorController.init(backTestingJudgeVO, backTestingInfoVO, 1, checkBoxs, type);
                    } else {
                        AutoCloseStage autoCloseStage = new AutoCloseStage();
                        autoCloseStage.showErrorBox("可回测股票总数少于100支");
                    }
                }
            });
        }
    }


    /**
     * 按钮响应
     */
    @FXML
    private void TurnOrange() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void TurnWhite() {
        confirmButton.setStyle("-fx-text-fill: #689cc4;  -fx-background-color: transparent; -fx-border-color: #689cc4; -fx-border-width: 1");
    }


    @FXML
    private void mTurnOrange() {
        if(!mainBoardButton.getStyle().equals("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            mainBoardButton.setStyle("-fx-text-fill: #838383; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void mTurnWhite() {
        if(!mainBoardButton.getStyle().equals("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            mainBoardButton.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #838383; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void stTurnOrange() {
        if(!startUpBoardButton.getStyle().equals("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            startUpBoardButton.setStyle("-fx-text-fill: #838383; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void stTurnWhite() {
        if(!startUpBoardButton.getStyle().equals("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            startUpBoardButton.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #838383; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void smTurnOrange() {
        if(!smeBoardButton.getStyle().equals("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            smeBoardButton.setStyle("-fx-text-fill: #838383; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void smTurnWhite() {
        if(!smeBoardButton.getStyle().equals("-fx-text-fill: #D97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            smeBoardButton.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #838383; -fx-border-width: 1; -fx-border-radius: 50");
    }

    private void initAllButton(){
        smeBoardButton.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #838383; -fx-border-width: 1; -fx-border-radius: 50");
        startUpBoardButton.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #838383; -fx-border-width: 1; -fx-border-radius: 50");
        mainBoardButton.setStyle("-fx-text-fill: #838383;  -fx-background-color: transparent; -fx-border-color: #838383; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void allSelect() {
        for (int i = 0; i < buttons.size(); i++) {
            if (!selectedStocks.contains(buttons.get(i))) {
                buttons.get(i).setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                selectedStocks.add(buttons.get(i));
            }
        }
        updateLabel();
    }
}
