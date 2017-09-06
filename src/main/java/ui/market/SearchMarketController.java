package ui.market;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import exception.NotTransactionDayException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ui.ControlledStage;
import ui.MainViewController;
import ui.StageController;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import vo.MarketTemperatureVO;
import vo.SearchVO;

/**
 * Created by island on 2017/3/5.
 */
public class SearchMarketController implements ControlledStage {
    private StageController stageController = new StageController();

    private String resource = "SearchMarketView.fxml";
    private StockBLService stockBLService;

    @FXML
    private DatePicker searchDate;

    @FXML
    private Button confirmButton;

    @FXML
    private Pane searchPane;

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @FXML
    private void confirm() {
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        mainViewController.showLoading();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                stockBLService = new StockBL();
                try {
                    MarketTemperatureVO marketTemperatureVO = stockBLService.getMarketInfo(wrapSearchInfo());
                    mainViewController.setMarketPane(wrapSearchInfo());
                } catch (NotTransactionDayException e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            AutoCloseStage autoCloseStage = new AutoCloseStage();
                            autoCloseStage.showErrorBox("该日不是交易日");
                        }
                    });
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
//                System.out.println("all:" + System.currentTimeMillis());
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
        SearchVO searchVO = new SearchVO(datePickerSettings.getDate(searchDate));
        return searchVO;
    }

    /**
     * 搜索市场信息界面初始化方法
     */
    public void initial() {
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePickerSettings.getStartSettings(searchDate);

        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                turnRed();
                confirm();
            }
        });
    }
}
