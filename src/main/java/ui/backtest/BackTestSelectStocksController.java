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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.apache.commons.lang.StringUtils;
import ui.ControlledStage;
import ui.MainViewController;
import ui.StageController;
import ui.util.AutoCloseStage;
import ui.util.SearchNotificatePane;
import vo.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by island on 2017/3/28.
 */
public class BackTestSelectStocksController implements ControlledStage {

    private StageController stageController;

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //选择了动量策略
    private boolean momentumIsSelected = false;
    //选择了均值回归
    private boolean meanIsSelected = false;
    //开始日期
    private Date startDate;
    //结束日期
    private Date endDate;
    //策略种类
    private String type;
    //所有的股票按钮
    private List<Button> buttons = new ArrayList<>();
    //选中的股票按钮
    private List<Button> selectedStocks = new ArrayList<>();
    //SHOWALL
    private boolean showAll = false;
    //选择的板块名
    private String plateName = "";
    //固定的period种类
    private String period = "";
    //backtestingjudgevo
    BackTestingJudgeVO backTestingJudgeVO;
    //随机选择的股票票数
    private int randomSelectNum = 200;
    //checkbox是否被选择
    private boolean[] checkBoxs = {false, false, false};
    //策略胜率
    private List<WinningRateVO> winningRateVOS;

    @FXML
    private Button backButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button mainBoardButton;

    @FXML
    private Button startUpBoardButton;

    @FXML
    private Button smeBoardButton;

    @FXML
    private Label strategyLabel;

    @FXML
    private Label sDateLabel;

    @FXML
    private Label eDateLabel;

    @FXML
    private Pane momentumPane;

    @FXML
    private Pane meanPane;

    @FXML
    private TextField meanHoldingPeriod;

    @FXML
    private TextField meanMostNumOfStock;

    @FXML
    private ChoiceBox meanTypeChoiceBox;

    @FXML
    private TextField momentumHoldingPeriod;

    @FXML
    private TextField momentumFormativePeriod;

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
    private Tab stockPlateTab;

    @FXML
    private CheckBox mainCheckBox;

    @FXML
    private CheckBox stCheckBox;

    @FXML
    private CheckBox smeCheckBox;

    @FXML
    private TabPane stockTabPane;

    @FXML
    private Pane momentumPeriodPane;

    @FXML
    private TextField periodTextField;

    @FXML
    private Label periodLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Pane meanPeriodPane;

    @FXML
    private ChoiceBox meanTypeChoiceBox1;

    @FXML
    private TextField meanMostNumOfStock1;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    /**
     * 回测股票选择界面初始化方法
     *
     * @param sDate
     * @param eDate
     * @param type
     */
    public void initial(Date sDate, Date eDate, String type, String period) {
        this.type = type;
        ObservableList<String> means = FXCollections.observableArrayList();
        means.addAll("5日均线", "10日均线", "20日均线");

        startDate = sDate;
        endDate = eDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sDateLabel.setText(sdf.format(startDate));
        eDateLabel.setText(sdf.format(endDate));
        this.period = period;
        if (type.equals("动量策略")) {
            if (period.equals("")) {
                momentumIsSelected = true;
                strategyLabel.setText("动 量 策 略");
                momentumPane.setVisible(true);
            }
            if (period.equals("形成期")) {
                momentumIsSelected = true;
                typeLabel.setText("B E S T     P E R I O D");
                strategyLabel.setText("动 量 策 略");
                momentumPeriodPane.setVisible(true);
                periodLabel.setText("形  成  期");
                periodTextField.setPromptText("形成期：");
            }
            if (period.equals("持有期")) {
                momentumIsSelected = true;
                strategyLabel.setText("动 量 策 略");
                momentumPeriodPane.setVisible(true);
                periodLabel.setText("持  有  期");
                periodTextField.setPromptText("持有期：");
                typeLabel.setText("B E S T     P E R I O D");
            }
        } else {
            if (period.equals("")) {
                meanIsSelected = true;
                strategyLabel.setText("均 值 回 归");
                meanPane.setVisible(true);
                meanTypeChoiceBox1.setItems(means);
            } else {
                meanIsSelected = true;
                strategyLabel.setText("均 值 回 归");
                meanPeriodPane.setVisible(true);
                meanTypeChoiceBox.setItems(means);
            }
        }
        setAllStock();

        SearchNotificatePane searchNotificatePane = new SearchNotificatePane(stockTextField, searchNotificationPane, nullLabel, searchScrollPane);
        searchNotificatePane.setTextField(125, 30, 110);

        //preset();
    }

    /**
     * 选择一种板块的所有股票
     *
     * @param plateName
     */
    private void selectOneBoard(String plateName, CheckBox checkBox) {
        StockBLService stockBLService = new StockBL();
        List<String> stocks = stockBLService.getAllStockCodeAndNameInPlate(plateName);
        String s;
        if (checkBox.isSelected()) {
            if (plateName.equals("深市主板"))
                checkBoxs[0] = true;
            if (plateName.equals("创业板"))
                checkBoxs[1] = true;
            if (plateName.equals("中小板"))
                checkBoxs[2] = true;
            for (int i = 0; i < stocks.size(); i++) {
                s = stocks.get(i).split(";")[0] + " " + stocks.get(i).split(";")[1];
                for (int j = 0; j < buttons.size(); j++) {
                    if (s.equals(buttons.get(j).getText())) {
                        if (!selectedStocks.contains(buttons.get(j))) {
                            buttons.get(j).setStyle("-fx-text-fill: #ffb199;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                            selectedStocks.add(buttons.get(j));
                            break;
                        }
                    }
                }
            }
        } else {
            if (plateName.equals("深市主板"))
                checkBoxs[0] = false;
            if (plateName.equals("创业板"))
                checkBoxs[1] = false;
            if (plateName.equals("中小板"))
                checkBoxs[2] = false;
            for (int i = 0; i < stocks.size(); i++) {
                s = stocks.get(i).split(";")[0] + " " + stocks.get(i).split(";")[1];
                for (int j = 0; j < buttons.size(); j++) {
                    if (s.equals(buttons.get(j).getText())) {
                        if (selectedStocks.contains(buttons.get(j))) {
                            buttons.get(j).setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                            selectedStocks.remove(buttons.get(j));
                            break;
                        }
                    }
                }
            }
        }
        updateLabel();
    }

    /**
     * 选择所有主板股票
     */
    @FXML
    private void selectMain() {
        selectOneBoard("深市主板", mainCheckBox);
    }

    /**
     * 选择所有创业板股票
     */
    @FXML
    private void selectSt() {
        selectOneBoard("创业板", stCheckBox);
    }

    /**
     * 选择所有中小板股票
     */
    @FXML
    private void selectSme() {
        selectOneBoard("中小板", smeCheckBox);
    }

    /**
     * 初始化所有股票
     */
    private void setAllStock() {
        StockBLService stockBLService = new StockBL();
        List<String> allStocks = stockBLService.getAllStockCodeAndName();
        String[][] stocks = new String[allStocks.size()][2];
        stockPane.setPrefHeight(((allStocks.size() / 4) + 1) * 50 + 10);
        for (int i = 0; i < allStocks.size(); i++) {
            stocks[i][0] = allStocks.get(i).split(";")[0];
            stocks[i][1] = allStocks.get(i).split(";")[1];
        }

        for (int i = 0; i < stocks.length; i++) {
            Button b = new Button(stocks[i][1] + " " + stocks[i][0]);
            stockPane.getChildren().add(b);
            int row = i % 4;
            if (row == 0)
                b.setLayoutX(10);
            else if (row == 1)
                b.setLayoutX(170);
            else if (row == 2)
                b.setLayoutX(330);
            else if (row == 3)
                b.setLayoutX(490);

            int line = i / 4;
            b.setLayoutY(10 + line * 50);
            b.setPrefWidth(140);
            b.setPrefHeight(35);

            b.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            b.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
                if (b.getStyle().equals("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand"))
                    b.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            });
            b.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
                if (b.getStyle().equals("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand"))
                    b.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            });
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                arrorImage.setOpacity(0);
                if (b.getStyle().equals("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand")) {
                    b.setStyle("-fx-text-fill: #ffb199;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                    selectedStocks.add(b);
                } else {
                    b.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
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
    private void updateLabel() {
        numOfStocksLabel.setText("已选" + selectedStocks.size() + "支股票");
    }

    /**
     * 取消所有已选中股票
     */
    @FXML
    private void cancelAll() {
        if (!selectedStocks.isEmpty()) {
            for (int i = selectedStocks.size() - 1; i >= 0; i--) {
                selectedStocks.get(i).setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color:  #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                selectedStocks.remove(selectedStocks.get(i));
            }
            updateLabel();
        }
    }

    /**
     * 随机选择200支股票方法
     */
    @FXML
    private void randomSelect() {
        int maxNum = buttons.size() - 1;

        if (selectedStocks.size() + 200 > buttons.size()) {
            if(selectedStocks.size() != buttons.size()) {
                for (int i = 0; i < buttons.size(); i++) {
                    if (!selectedStocks.contains(buttons.get(i))) {
                        buttons.get(i).setStyle("-fx-text-fill: #ffb199;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                        selectedStocks.add(buttons.get(i));
                    }
                }
                updateLabel();
            }else{
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("超出股票总数");
            }
        } else {
            Random rand = new Random();
            int num = 0;
            for (int i = 0; i < 200; i++) {
                while (selectedStocks.contains(buttons.get(num))) {
                    num = rand.nextInt(maxNum);
                }
                selectedStocks.add(buttons.get(num));
                buttons.get(num).setStyle("-fx-text-fill: #ffb199;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
            }
            updateLabel();
        }
    }


    /**
     * 搜索按钮响应
     */
    @FXML
    private void search() {
        searchScrollPane.setVisible(false);
        String stock = stockTextField.getText();
        String stockID = "";
        String stockName = "";
        if (stock.equals("")) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("请输入股票ID或股票名称");
            return;
        }
        for (int i = 0; i < buttons.size(); i++) {
            String[] s = buttons.get(i).getText().split(" ");
            stockID = s[0];
            stockName = s[1];
            if (stock.equals(stockID) || stock.equals(stockName)) {
                scrollPane.setVvalue((buttons.get(i).getLayoutY()) / (stockPane.getPrefHeight() - 60));
                int row = i % 4;
                if (row == 0)
                    arrorImage.setLayoutX(-5);
                else if (row == 1)
                    arrorImage.setLayoutX(155);
                else if (row == 2)
                    arrorImage.setLayoutX(315);
                else if (row == 3)
                    arrorImage.setLayoutX(475);

                int line = i / 4;
                arrorImage.setLayoutY(20 + line * 50);
                arrorImage.setOpacity(1);

                break;
            }
            if (i == buttons.size() - 1) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("没有该支股票");
            }
        }
    }

    /**
     * 确认按钮响应
     */
    @FXML
    private void confirm() {
        stageController = new StageController();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");

        int holding = 0;
        int formative = 0;
        int ma = 0;
        int mostNumOfStock = 0;


        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();


        //选择动量策略
        if (momentumIsSelected) {
            if (period.equals("")) {
                String holdingPeriod = momentumHoldingPeriod.getText();
                String formativePeriod = momentumFormativePeriod.getText();
                if (holdingPeriod.equals("") || formativePeriod.equals("")) {
                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                    autoCloseStage.showErrorBox("请输入形成期／持有期");
                } else {
                    if (StringUtils.isNumeric(holdingPeriod) && StringUtils.isNumeric(formativePeriod)) {
                        holding = Integer.parseInt(holdingPeriod);
                        formative = Integer.parseInt(formativePeriod);
                        getMomemtumStockInfo(formative, holding);
                    } else {
                        AutoCloseStage autoCloseStage = new AutoCloseStage();
                        autoCloseStage.showErrorBox("形成期／持有期请输入整数");
                    }
                }
            } else {
                String periodNum = periodTextField.getText();
                if (periodNum.equals("") || !StringUtils.isNumeric(periodNum)) {
                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                    autoCloseStage.showErrorBox("请输入正确的周期");
                } else {
                    int num = Integer.parseInt(periodNum);
                    if (period.equals("持有期")) {
                        formative = num;
                        holding = num;
                    }
                    if (period.equals("形成期")) {
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
            ChoiceBox choiceBox;
            TextField textField;
            if (period.equals("")) {
                choiceBox = meanTypeChoiceBox1;
                textField = meanMostNumOfStock1;

            } else {
                choiceBox = meanTypeChoiceBox;
                textField = meanMostNumOfStock;

            }
            if (choiceBox.getSelectionModel().getSelectedItem() != null)
                maType = (String) choiceBox.getSelectionModel().getSelectedItem().toString();
            mostNum = textField.getText();
            if (maType.equals("") || mostNum.equals("")) {
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
                    if (period.equals("")) {
                        String holdingPeriod = meanHoldingPeriod.getText();
                        if (StringUtils.isNumeric(holdingPeriod)) {
                            holding = Integer.parseInt(holdingPeriod);
                            getMeanStockInfo(ma, holding, mostNumOfStock);
                        } else {
                            AutoCloseStage autoCloseStage = new AutoCloseStage();
                            autoCloseStage.showErrorBox("持有期请输入整数");
                        }
                    } else {
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

    private void getMomemtumStockInfo(int formative, int holding) {
        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        boolean plate = stockPlateTab.isSelected();
        BackTestingInfoVO backTestingInfoVO;
        List<String> stocks = new ArrayList<>();

        //选择板块
        if (plate) {
            if (plateName.equals("")) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请选择一个板块");
            } else {
                showAll = false;

                    backTestingInfoVO = new BackTestingInfoVO(startDate, endDate, showAll, plateName, new ArrayList<>(), formative, holding, StrategyType.MOMENTUM);
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

            backTestingInfoVO = new BackTestingInfoVO(startDate, endDate, showAll, "", stocks, formative, holding, StrategyType.MOMENTUM);

            if (stocks.size() < 100 && !showAll) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请至少选择100支股票");
            } else {
                defineBackTest(backTestingInfoVO);
            }
        }
    }

    public void getMeanStockInfo(int ma, int holding, int mostNumOfStock) {
        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");

        boolean plate = stockPlateTab.isSelected();
        BackTestingInfoVO backTestingInfoVO;
        List<String> stocks = new ArrayList<>();

        //选择板块
        if (plate) {
            if (plateName.equals("")) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请选择一个板块");
            } else {
                showAll = false;
                backTestingInfoVO = new BackTestingInfoVO(startDate, endDate, showAll, plateName, ma, stocks, holding, StrategyType.MEANREVERSION, mostNumOfStock);
                defineBackTest(backTestingInfoVO);
                //mainViewController.setBackTestPane(backTestingInfoVO);
            }
        }
        //选择股票
        else {
            for (int i = 0; i < selectedStocks.size(); i++) {
                stocks.add(selectedStocks.get(i).getText().split(" ")[0]);
            }
            if (showAll)
                stocks.clear();

            backTestingInfoVO = new BackTestingInfoVO(startDate, endDate, showAll, "", ma, stocks, holding, StrategyType.MEANREVERSION, mostNumOfStock);

            if (stocks.size() < 100 && !showAll) {
                AutoCloseStage autoCloseStage = new AutoCloseStage();
                autoCloseStage.showErrorBox("请至少选择100支股票");
            } else {
                defineBackTest(backTestingInfoVO);

            }
        }
    }

    /**
     * 判断能否回测
     */
    private void defineBackTest(BackTestingInfoVO backTestingInfoVO) {
        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        if(backTestingInfoVO.holdingPeriod <= 1 && !period.equals("形成期")) {
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("持有期必须大于1");
        }else {
            mainViewController.showLoading();
            mainViewController.closeWaiting1();
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
                    MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
                    mainViewController.closeLoading();
                    if (backTestingJudgeVO.getCanBackTest()) {
                        stageController = new StageController();
                        stageController.loadStage("backtest/BackTestErrorView.fxml");
                        BackTestErrorController backTestErrorController = (BackTestErrorController) stageController.getController("backtest/BackTestErrorView.fxml");
                        backTestErrorController.init(backTestingJudgeVO, backTestingInfoVO, 0, checkBoxs, period);
                    } else {
                        AutoCloseStage autoCloseStage = new AutoCloseStage();
                        autoCloseStage.showErrorBox("可回测股票总数少于100支");
                    }
                }
            });
        }
    }


    /**
     * 返回按钮响应
     */
    @FXML
    private void back() {
        stageController = new StageController();
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        if (period.equals("")) {
            mainViewController.backToBackTest(startDate, endDate, type);
        } else {
            mainViewController.backToBestPeriod(startDate, endDate, type, period);
        }
    }

    /**
     * 选择主板
     */
    @FXML
    private void selectMainBoard() {
        initAllButton();
        mainBoardButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
        plateName = "深市主板";
    }

    /**
     * 选择创业板
     */
    @FXML
    private void selectStartUpBoard() {
        initAllButton();
        startUpBoardButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
        plateName = "创业板";
    }

    /**
     * 选择中小板
     */
    @FXML
    private void selectSMEBoard() {
        initAllButton();
        smeBoardButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
        plateName = "中小板";
    }

    /**
     * 按钮响应
     */
    @FXML
    private void cTurnOrange() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void cTurnWhite() {
        confirmButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1");
    }

    @FXML
    private void bTurnOrange() {
        backButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void bTurnWhite() {
        backButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1");
    }

    @FXML
    private void mTurnOrange() {
        if (!mainBoardButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            mainBoardButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void mTurnWhite() {
        if (!mainBoardButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            mainBoardButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void stTurnOrange() {
        if (!startUpBoardButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            startUpBoardButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void stTurnWhite() {
        if (!startUpBoardButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            startUpBoardButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void smTurnOrange() {
        if (!smeBoardButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            smeBoardButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void smTurnWhite() {
        if (!smeBoardButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            smeBoardButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
    }

    private void initAllButton() {
        smeBoardButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
        startUpBoardButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
        mainBoardButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void allSelect() {
        for (int i = 0; i < buttons.size(); i++) {
            if (!selectedStocks.contains(buttons.get(i))) {
                buttons.get(i).setStyle("-fx-text-fill: #ffb199;  -fx-background-color: transparent; -fx-border-color: #a29f9f; -fx-border-width: 1; -fx-border-radius: 50; -fx-cursor: hand");
                selectedStocks.add(buttons.get(i));
            }
        }
        updateLabel();
    }

}
