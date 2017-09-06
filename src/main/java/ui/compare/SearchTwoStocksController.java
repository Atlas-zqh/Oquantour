package ui.compare;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import exception.BothStockNotExistException;
import exception.FirstStockNotExistException;
import exception.SecondStockNotExistException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ui.ControlledStage;
import ui.MainViewController;
import ui.StageController;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import ui.util.SearchNotificatePane;
import vo.SearchVO;
import vo.StockCompareVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;

/**
 * Created by island on 2017/3/5.
 */
public class SearchTwoStocksController implements ControlledStage {
    private StageController stageController = new StageController();
    private StockBLService stockBLService;

    private String resource = "SearchTwoStocksView.fxml";
    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField firstStockID;

    @FXML
    private TextField secondStockID;

    @FXML
    private Button confirmButton;

    @FXML
    private Pane searchPane;

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
     * 搜索按钮响应结果
     */
    @FXML
    private void confirm() {
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        mainViewController.showLoading();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (firstStockID.getText().equals("") || secondStockID.getText().equals("")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            AutoCloseStage autoCloseStage = new AutoCloseStage();
                            autoCloseStage.showErrorBox("请输入股票代码或名称");
                        }
                    });
                } else {
                    if (firstStockID.getText().equals(secondStockID.getText())) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("请输入两只不同股票");
                            }
                        });

                    } else {
                        stockBLService = new StockBL();
                        try {
                            List<StockCompareVO> stockCompareVOS = stockBLService.compareStock(wrapSearchInfo());
                            mainViewController.setComparePane(wrapSearchInfo());
                        } catch (BothStockNotExistException b){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                                    autoCloseStage.showErrorBox("找不到数据");
                                }
                            });
                        } catch (FirstStockNotExistException f) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                                    autoCloseStage.showErrorBox("找不到\n第一只股票的数据");
                                }
                            });
                        } catch (SecondStockNotExistException s) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    AutoCloseStage autoCloseStage = new AutoCloseStage();
                                    autoCloseStage.showErrorBox("找不到\n第二只股票的数据");
                                }
                            });
                        }
                    }
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
            }
        });


    }

    @FXML
    private void turnRed() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void turnWhite() {
        confirmButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1");
    }

    /**
     * 获得用户输入的搜索信息，并打包成SearchVO
     *
     * @return
     */
    private SearchVO wrapSearchInfo() {
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        SearchVO searchVO = new SearchVO(firstStockID.getText(), secondStockID.getText(),
                datePickerSettings.getDate(startDate), datePickerSettings.getDate(endDate));
        return searchVO;
    }

    /**
     * 初始化日期选择器，设定默认值及选择范围
     */
    public void initial() {
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        startDate.setDayCellFactory(datePickerSettings.getStartSettings(startDate));
        endDate.setDayCellFactory(datePickerSettings.getEndSettings(startDate, endDate));

        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                turnRed();
                confirm();
            }
        });

        SearchNotificatePane searchNotificatePane1 = new SearchNotificatePane(firstStockID, searchNotificationPane1, nullLabel1, searchScrollPane1);
        searchNotificatePane1.setTextField(269, 18, 55);

        SearchNotificatePane searchNotificatePane2 = new SearchNotificatePane(secondStockID, searchNotificationPane2, nullLabel2, searchScrollPane2);
        searchNotificatePane2.setTextField(269, 18, 55);
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
