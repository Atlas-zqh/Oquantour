package ui.singleStock;

import bl.stock.StockBL;
import bl.stock.StockBLService;
import exception.FirstStockNotExistException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ui.*;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;
import ui.util.SearchNotificatePane;
import vo.DailyAverageVO;
import vo.KLineVO;
import vo.SearchVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;


/**
 * Created by island on 2017/3/5.
 */
public class SearchSingleStockController implements ControlledStage {
    private StageController stageController = new StageController();

    private String resource = "SearchSingleStockView.fxml";
    private StockBLService stockBLService;

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private TextField stockID;

    @FXML
    private Button confirmButton;

    @FXML
    private Pane searchPane;

    @FXML
    private Pane searchNotificationPane;

    @FXML
    private ScrollPane searchScrollPane;

    @FXML
    private Label nullLabel;

    /**
     * 确认按钮方法
     */
    @FXML
    private void confirm() {
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
        mainViewController.showLoading();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (stockID.getText().equals("")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            AutoCloseStage autoCloseStage = new AutoCloseStage();
                            autoCloseStage.showErrorBox("请输入股票代码或名称");
                        }
                    });
                } else {
                    MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");
                    SearchVO searchVO = getSearchVO();
                    stockBLService = new StockBL();
                    try {
                        List<KLineVO> kLineVOS = stockBLService.getKLineInfo(searchVO);
                        List<List<DailyAverageVO>> ave = stockBLService.getDailyAverageInfo(searchVO);
                        mainViewController.setInfoPane(getSearchVO(), kLineVOS, ave);
                    } catch (FirstStockNotExistException e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                AutoCloseStage autoCloseStage = new AutoCloseStage();
                                autoCloseStage.showErrorBox("找不到数据");
                            }
                        });
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


    /**
     * 按钮响应设置
     */
    @FXML
    private void turnRed() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void turnWhite() {
        confirmButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1");
    }

    /**
     * 搜索单个股票界面初始化方法
     */
    public void init() {
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        datePickerSettings.getStartSettings(startDate);
        datePickerSettings.getEndSettings(startDate, endDate);

        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                turnRed();
                confirm();
            }
        });

        SearchNotificatePane searchNotificatePane = new SearchNotificatePane(stockID, searchNotificationPane, nullLabel, searchScrollPane);
        searchNotificatePane.setTextField(269, 18, 55);
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
