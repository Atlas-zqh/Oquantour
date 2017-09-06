package ui.backtest;

import bl.strategy.BackTestingBLService;
import bl.strategy.BackTestingBLServiceImpl;
import bl.strategy.WinningRateBLService;
import bl.strategy.WinningRateBLServiceImpl;
import exception.FormativePeriodNotExistException;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import exception.WinnerSelectErrorException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import ui.ControlledStage;
import ui.MainViewController;
import ui.StageController;
import ui.charts.SingleTableView;
import ui.charts.SingleTableViewOfStockInfo;
import ui.util.AutoCloseStage;
import vo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by island on 2017/4/8.
 */
public class BackTestErrorController implements ControlledStage {
    private StageController stageController = new StageController();

    private BackTestingJudgeVO backTestingJudgeVO;

    private BackTestingInfoVO backTestingInfoVO;

    private List<String> invalid;

    private List<String> halt;

    boolean jmp = true;

    @FXML
    private Label totalStocksLabel;

    @FXML
    private Label canBackTestStockLabel;

    @FXML
    private Tab invalidTab;

    @FXML
    private AnchorPane invalidStocksPane;

    @FXML
    private Tab haltTab;

    @FXML
    private AnchorPane haltStocksPane;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label numberLabel;

    @FXML
    private Label noDataLabel;


    @FXML
    private TabPane tabPane;

    private int type;

    private boolean[] checkBoxs;

    private List<WinningRateVO> winningRateVOS;

    private String period;

    private List<BackTestingSingleChartVO> backTestingSingleChartVOS;

    private StrategyStatisticsVO strategyStatisticsVO;

    private List<WinnerInfoVO> winnerInfoVOS;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    public void init(BackTestingJudgeVO backTestingJudgeVO, BackTestingInfoVO backTestingInfoVO, int type, boolean[] checkBoxs, String period){
        this.type = type;
        this.checkBoxs = checkBoxs;
        this.backTestingInfoVO = backTestingInfoVO;
        this.backTestingJudgeVO = backTestingJudgeVO;
        this.period = period;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                totalStocksLabel.setText((backTestingJudgeVO.getValidStocksCodes().size() + backTestingJudgeVO.getInvalidStockNames_Halt().size() + backTestingJudgeVO.getInvalidStockNames_Statistics().size()) + "");
                canBackTestStockLabel.setText(backTestingJudgeVO.getValidStocksCodes().size() + "");
                numberLabel.setText("*共" + backTestingJudgeVO.getInvalidStockNames_Statistics().size() + "支无数据");
                if(backTestingJudgeVO.getInvalidStockNames_Statistics().size() == 0){
                    noDataLabel.setVisible(true);
                }else{
                    noDataLabel.setVisible(false);
                }

                invalid = backTestingJudgeVO.getInvalidStockNames_Statistics();
                halt = backTestingJudgeVO.getInvalidStockNames_Halt();

                setTabPane(invalidStocksPane, invalid);
                setTabPane(haltStocksPane, halt);


                tabPane.getSelectionModel().selectedItemProperty().addListener(
                        new ChangeListener<Tab>() {
                            @Override
                            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                                noDataLabel.setVisible(false);
                                if (tabPane.getSelectionModel().isSelected(0)) {
                                    numberLabel.setText("*共" + invalid.size() + "支无数据");
                                    if(invalid.size() == 0){
                                        noDataLabel.setVisible(true);
                                    }else{
                                        noDataLabel.setVisible(false);
                                    }
                                }
                                if (tabPane.getSelectionModel().isSelected(1)){
                                    numberLabel.setText("*共" + halt.size() + "支停牌");
                                if(halt.size() == 0) {
                                    noDataLabel.setVisible(true);
                                }else{
                                    noDataLabel.setVisible(false);
                                }
                                }
                            }
                        }
                );
            }
        });
    }

    private void setTabPane(AnchorPane anchorPane, List<String> stocks){
                anchorPane.getChildren().clear();
                if (stocks.size() > 0) {
                    anchorPane.getChildren().add(new SingleTableViewOfStockInfo(stocks));
                }
    }

    @FXML
    private void cancelBackTest(){
        stageController.closeStage("backtest/BackTestErrorView.fxml");
    }

    @FXML
    private void continueBackTest(){
        cancelBackTest();

        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        if (type == 0) {
            mainViewController.showLoading();
            mainViewController.closeWaiting1();
        }
        if (type == 1){
            mainViewController.showLoading1();
            mainViewController.closeWaiting2();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(period.equals("")) {
                    try {
                        BackTestingBLService backTestingBLService = BackTestingBLServiceImpl.getInstance();
                        backTestingSingleChartVOS = backTestingBLService.getBackTestResult();
                        strategyStatisticsVO = backTestingBLService.getBackTestStatistics();
                        winnerInfoVOS = backTestingBLService.getWinnersInfo();
                    }catch (ParameterErrorException e){
                        jmp = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("该时间段内板块数据缺失");
                            }
                        });
                        //e.printStackTrace();
                    }catch (WinnerSelectErrorException e2){
                        jmp = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("赢家选择有错误");
                                cancel();
                            }
                        });
                    }
                }
                else{
                    try {
                        WinningRateBLService winningRateBLService = new WinningRateBLServiceImpl();
                        if(period.equals("形成期"))
                            winningRateVOS = winningRateBLService.getWinningRate_FixedFormative(backTestingInfoVO);
                        if(period.equals("持有期"))
                            winningRateVOS = winningRateBLService.getWinningRate_FixedHolding(backTestingInfoVO);
                    }catch (SelectStocksException e1){
                        jmp = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("股票选择有问题");
                            }
                        });
                    }catch (FormativePeriodNotExistException e2){
                        jmp = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("该形成期内无股票数据");
                            }
                        });
                    }catch (ParameterErrorException e3){
                        jmp = false;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("该时间段内板块数据缺失");
                            }
                        });
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (jmp) {
                            if (period.equals("")) {
                                mainViewController.setBackTestPane(backTestingInfoVO, backTestingSingleChartVOS, strategyStatisticsVO, winnerInfoVOS, "", new ArrayList<>(), checkBoxs);
                            } else {
                                mainViewController.setBackTestPane(backTestingInfoVO, new ArrayList<>(), new StrategyStatisticsVO(), new ArrayList<>(), period, winningRateVOS, checkBoxs);
                            }
                        }
                        if(type == 0)
                            mainViewController.closeWaiting1();
                        if(type == 1)
                            mainViewController.closeWaiting2();
                    }
                });
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            public void handle(WorkerStateEvent event) {
                if(type == 0)
                    mainViewController.closeLoading();
                if(type == 1) {
                    mainViewController.closeLoading1();
                }
            }
        });

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(type == 0)
                    mainViewController.showWaiting1();
                if(type == 1)
                    mainViewController.showWaiting2();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 10000);
    }

    /**
     * 按钮响应
     */
    @FXML
    private void coTurnOrange() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void coTurnWhite() {
        confirmButton.setStyle("-fx-text-fill: #689cc4;  -fx-background-color: transparent; -fx-border-color: #689cc4; -fx-border-width: 1");
    }

    @FXML
    private void caTurnOrange() {
        cancelButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void caTurnWhite() {
        cancelButton.setStyle("-fx-text-fill: #689cc4;  -fx-background-color: transparent; -fx-border-color: #689cc4; -fx-border-width: 1");
    }


}
