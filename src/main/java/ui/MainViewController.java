package ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ui.backtest.BackTestSelectStocksController;
import ui.backtest.SearchBackTestController;
import ui.bestperiod.SearchBestPeriodController;
import ui.backtest.StockBackTestController;
import ui.compare.SearchTwoStocksController;
import ui.compare.StockCompareViewController;
import ui.market.SearchMarketController;
import ui.market.StockMarketViewController;
import ui.singleStock.SearchSingleStockController;
import ui.singleStock.StockInfoViewController;
import ui.util.AutoCloseStage;
import vo.*;

import java.util.Date;
import java.util.List;

/**
 * Created by st on 2017/3/4.
 */
public class MainViewController implements ControlledStage {

    private PaneAdder paneAdder = new PaneAdder();

    private StageController stageController;

    private String resourse = "MainView.fxml";

    private static int count = 0;

    @FXML
    private Button compareButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button marketButton;

    @FXML
    private Button backtestButton;

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button closeButton;

    @FXML
    private ImageView mainPic;

    @FXML
    private Pane inputPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane contentPane;

    @FXML
    private Label introLabel1;

    @FXML
    private Label introLabel2;

    @FXML
    private Pane loadingPane;

    @FXML
    private Pane loadingPane1;

    @FXML
    private Button bestperiodButton;

    @FXML
    private Pane waitingPane1;

    @FXML
    private Pane waitingPane2;


    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    /**
     * 导航栏
     * home按钮
     * 回到主界面方法
     */
    @FXML
    private void backToHome() {
        initAllButton();
        scrollPane.setLayoutY(1280);
        inputPane.getChildren().clear();
        introLabel1.setText("Find interactive charts, historical information and");
        introLabel2.setText("stock analysis on all A shares in China.");
        count = 0;
    }

    /**
     * 导航栏
     * search按钮
     * 跳转方法
     */
    @FXML
    private void searchSingle() {
        initAllButton();
        inputPane.getChildren().clear();
        paneAdder.addPane(inputPane, "singleStock/SearchSingleStockView.fxml", 0, 0);
        searchButton.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555");
        SearchSingleStockController searchSingleStockController = (SearchSingleStockController) paneAdder.getController();
        searchSingleStockController.init();
        introLabel1.setText("Get the information of a single stock");
        introLabel2.setText("during a certain period in history.");
        scrollPane.setLayoutY(720);
        count = 1;
    }

    public void setInfoPane(SearchVO searchVO, List<KLineVO> kLines, List<List<DailyAverageVO>> ave) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contentPane.setPrefHeight(580);
                contentPane.getChildren().clear();
                paneAdder.addPane(contentPane, "singleStock/StockInfoView.fxml", 0, 0);
                paneAdder.putIntoLoaders("singleStock/StockInfoView.fxml");
                StockInfoViewController stockInfoViewController = (StockInfoViewController) stageController.getController("singleStock/StockInfoView.fxml");
                Task<Void> task1 = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stockInfoViewController.init(searchVO, kLines, ave);
                            }
                        });
                        return null;
                    }
                };
                Thread thread1 = new Thread(task1);
                thread1.start();

                scrollPane.setLayoutY(130);
            }
        });


    }

    /**
     * 导航栏
     * compare按钮
     * 跳转方法
     */
    @FXML
    private void compareTwo() {
        initAllButton();
        scrollPane.setLayoutY(1280);
        inputPane.getChildren().clear();
        paneAdder.addPane(inputPane, "compare/SearchTwoStocksView.fxml", 0, 0);
        compareButton.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555");
        SearchTwoStocksController searchTwoStocksController = (SearchTwoStocksController) paneAdder.getController();
        searchTwoStocksController.initial();
        introLabel1.setText("Compare market performances of two stocks");
        introLabel2.setText(" and get analytical charts.");
        count = 2;
    }

    public void setComparePane(SearchVO searchVO) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contentPane.setPrefHeight(1800);
                contentPane.getChildren().clear();
                paneAdder.addPane(contentPane, "compare/StockCompareView.fxml", 0, 0);
                StockCompareViewController stockCompareViewController = (StockCompareViewController) paneAdder.getController();
                Task<Void> task1 = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stockCompareViewController.initial(searchVO);
                            }
                        });
                        return null;
                    }
                };
                Thread thread1 = new Thread(task1);
                thread1.start();

                scrollPane.setLayoutY(130);
            }
        });


    }

    /**
     * 导航栏
     * market按钮
     * 跳转方法
     */
    @FXML
    private void searchMarket() {
        initAllButton();
        scrollPane.setLayoutY(1280);
        inputPane.getChildren().clear();
        marketButton.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555");
        paneAdder.addPane(inputPane, "market/SearchMarketView.fxml", 0, 0);
        SearchMarketController searchMarketController = (SearchMarketController)paneAdder.getController();
        searchMarketController.initial();
        introLabel1.setText("Find stock market activity and compare performances");
        introLabel2.setText("of indexes, including change net and share volume.");
        count = 3;

    }

    public void setMarketPane(SearchVO searchVO) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                contentPane.setPrefHeight(1500);
                contentPane.getChildren().clear();
                paneAdder.addPane(contentPane, "market/StockMarketView.fxml", 0, 0);
                StockMarketViewController stockMarketViewController = (StockMarketViewController)paneAdder.getController();
                Task<Void> task1 = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stockMarketViewController.initial(searchVO);
                            }
                        });
                        return null;
                    }
                };
                Thread thread1 = new Thread(task1);
                thread1.start();

                scrollPane.setLayoutY(130);
            }
        });
    }

    /**
     * 导航栏
     * backtest按钮
     * 跳转方法
     */
    @FXML
    public void backTest(){
        initAllButton();
        scrollPane.setLayoutY(1280);
        inputPane.getChildren().clear();
        backtestButton.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555");
        paneAdder.addPane(inputPane, "backtest/SearchBackTestView.fxml", 0, 0);
        SearchBackTestController searchBackTestController = (SearchBackTestController) paneAdder.getController();
        searchBackTestController.initial();
        introLabel1.setText("Construct the best portfolio based on the selected stocks to analyze and");
        introLabel2.setText("backtest portfolio returns, risk characteristics, and annual returns.");
        count = 4;
    }

    /**
     * 回测时股票选择模块
     */
    public void setBackTestStockSelectPane(Date sDate, Date eDate, String type, String period) {
        inputPane.getChildren().clear();
        paneAdder.addPane(inputPane, "backtest/BackTestSelectStocksView.fxml", 0, 0);
        BackTestSelectStocksController backTestSelectStocksController = (BackTestSelectStocksController) paneAdder.getController();
        backTestSelectStocksController.initial(sDate, eDate, type, period);
    }

    /**
     * 从回测股票选择界面回到回测搜索界面
     * @param sDate
     * @param eDate
     * @param type
     */
    public void backToBackTest(Date sDate, Date eDate, String type){
        inputPane.getChildren().clear();
        paneAdder.addPane(inputPane, "backtest/SearchBackTestView.fxml", 0, 0);
        SearchBackTestController searchBackTestController = (SearchBackTestController) paneAdder.getController();
        searchBackTestController.backFromStockSelect(sDate, eDate, type);
    }

    public void setBackTestPane(BackTestingInfoVO backTestingInfoVO, List<BackTestingSingleChartVO> backTestingSingleChartVOS, StrategyStatisticsVO strategyStatisticsVO, List<WinnerInfoVO> winnerInfoVOS, String type, List<WinningRateVO> winningRateVOS, boolean[] checkBoxs){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(type.equals(""))
                    contentPane.setPrefHeight(2710);
                else
                    contentPane.setPrefHeight(1770);

                contentPane.getChildren().clear();
                paneAdder.addPane(contentPane, "backtest/StockBackTestView.fxml", 0, 0);
                StockBackTestController stockBackTestController = (StockBackTestController) paneAdder.getController();
                Task<Void> task1 = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stockBackTestController.initial(backTestingInfoVO, backTestingSingleChartVOS, strategyStatisticsVO, winnerInfoVOS, type, winningRateVOS, checkBoxs);
                            }
                        });
                        return null;
                    }
                };
                Thread thread1 = new Thread(task1);
                thread1.start();

                scrollPane.setLayoutY(130);
            }
        });
    }

    /**
     * 导航栏
     * bestperiod按钮
     * 跳转方法
     */
    @FXML
    private void bestPeriod(){
        initAllButton();
        scrollPane.setLayoutY(1280);
        inputPane.getChildren().clear();
        bestperiodButton.setStyle("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555");
        paneAdder.addPane(inputPane, "bestperiod/SearchBestPeriodView.fxml", 0, 0);
        SearchBestPeriodController searchBestPeriodController = (SearchBestPeriodController) paneAdder.getController();
        searchBestPeriodController.initial();
        introLabel1.setText("Find the best back test period to reap ");
        introLabel2.setText("maximum profit in market.");
        count = 5;
    }

    /**
     * 从股票选择界面返回搜索最佳周期界面
     * @param sDate
     * @param eDate
     * @param type
     * @param period
     */
    public void backToBestPeriod(Date sDate, Date eDate, String type, String period){
        inputPane.getChildren().clear();
        paneAdder.addPane(inputPane, "bestperiod/SearchBestPeriodView.fxml", 0, 0);
        SearchBestPeriodController searchBestPeriodController = (SearchBestPeriodController) paneAdder.getController();
        searchBestPeriodController.backFromStockSelect(sDate, eDate, type, period);
    }

    /**
     * 后退按钮方法
     */
    @FXML
    private void back() {
        if(count==0)
            count = 6;
        count--;
        change();
    }

    /**
     * 前进按钮方法
     */
    @FXML
    private void next() {
        count++;
        change();
    }

    /**
     * 切换主界面方法
     */
    private void change() {
        int type = Math.abs(count % 6);
        switch (type) {
            case 0:
                backToHome();
                break;
            case 1:
                searchSingle();
                break;
            case 2:
                compareTwo();
                break;
            case 3:
                searchMarket();
                break;
            case 4:
                backTest();
                break;
            case 5:
                bestPeriod();
                break;
        }
    }

    @FXML
    private void setting() {
        AutoCloseStage autoCloseStage = new AutoCloseStage();
        autoCloseStage.showErrorBox("敬请期待...");
    }

    public void init(){

    }

    public void showLoading(){
        Platform.runLater(() -> loadingPane.setLayoutY(130));
        closeWaiting1();
    }

    public void closeLoading(){
        Platform.runLater(() -> loadingPane.setLayoutY(720));
    }

    public void showLoading1(){
        Platform.runLater(() -> loadingPane1.setLayoutY(130));
    }

    public void closeLoading1(){
        Platform.runLater(() -> loadingPane1.setLayoutY(720));
    }

    public void showWaiting1() {
        Platform.runLater(() -> waitingPane1.setVisible(true));
    }

    public void showWaiting2() {
        Platform.runLater(() -> waitingPane2.setVisible(true));
    }

    public void closeWaiting1() {
        Platform.runLater(() -> waitingPane1.setVisible(false));
    }

    public void closeWaiting2() {
        Platform.runLater(() -> waitingPane2.setVisible(false));
    }
    /**
     * 关闭程序方法
     */
    @FXML
    private void close() {
        System.exit(0);
    }


    /**
     * 以下为按钮响应设置代码
     */
    @FXML
    private void hTurnRed() {
        if(!homeButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            homeButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent;");
    }

    @FXML
    private void hTurnWhite() {
        if(homeButton.getStyle().equals("-fx-text-fill: #722828; -fx-background-color: transparent;")
                || !homeButton.getStyle().equals("-fx-text-fill: #722828;  -fx-background-color: transparent; -fx-border-color: #722828"))
            homeButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }

    @FXML
    private void cTurnRed() {
        if(!compareButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            compareButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent;");
    }

    @FXML
    private void cTurnWhite() {
        if(compareButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent;")
                || !compareButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
        compareButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }

    @FXML
    private void sTurnRed() {
        if(!searchButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            searchButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent;");
    }

    @FXML
    private void sTurnWhite() {
        if(searchButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent;")
                || !searchButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            searchButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }

    @FXML
    private void mTurnRed() {
        if(!marketButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            marketButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent;");
    }

    @FXML
    private void mTurnWhite() {
        if(marketButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent;")
                || !marketButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            marketButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }

    @FXML
    private void bTurnRed() {
        if(!backtestButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            backtestButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent;");
    }

    @FXML
    private void bTurnWhite() {
        if(backtestButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent;")
                || !backtestButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            backtestButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }

    @FXML
    private void beTurnRed() {
        if(!bestperiodButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            bestperiodButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent;");
    }

    @FXML
    private void beTurnWhite() {
        if(bestperiodButton.getStyle().equals("-fx-text-fill: #d97555; -fx-background-color: transparent;")
                || !bestperiodButton.getStyle().equals("-fx-text-fill: #d97555;  -fx-background-color: transparent; -fx-border-color: #d97555"))
            bestperiodButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }

    private void initAllButton(){
        homeButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
        searchButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
        marketButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
        compareButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
        backtestButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
        bestperiodButton.setStyle("-fx-text-fill: #1f5686;  -fx-background-color: transparent;");
    }


    public ScrollPane getScrollPane(){
        return scrollPane;
    }

    public Pane getPane(){
        return contentPane;
    }
}
