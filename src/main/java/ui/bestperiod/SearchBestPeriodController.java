package ui.bestperiod;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import ui.ControlledStage;
import ui.MainViewController;
import ui.StageController;
import ui.util.AutoCloseStage;
import ui.util.DatePickerSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by island on 2017/4/13.
 */
public class SearchBestPeriodController implements ControlledStage {

    private StageController stageController = new StageController();

    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private boolean momentumIsSelected = false;

    private boolean meanIsSelected = false;

    @FXML
    private Pane searchPane;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button confirmButton;

    @FXML
    private Button momentumButton;

    @FXML
    private Button meanButton;

    @FXML
    private ChoiceBox periodChoiceBox;


    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    /**
     * 初始化方法
     */
    public void initial(){
        DatePickerSettings datePickerSettings = new DatePickerSettings();
        startDate.setDayCellFactory(datePickerSettings.getStartSettings(startDate));
        endDate.setDayCellFactory(datePickerSettings.getBackTestEndSettings(startDate, endDate));

        Date date = new Date(110,5,15);
        startDate.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        date = new Date(110,7,15);
        endDate.setValue(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        // 键盘监听，回车键发起搜索
        searchPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                turnOrange();
            }
        });

    }

    public  void backFromStockSelect(Date sDate, Date eDate, String type, String period) {
        initial();
        endDate.setValue(eDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        startDate.setValue(sDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if(type.equals("动量策略"))
            selectMomentum();
        if(type.equals("均值回归"))
            selectMean();
        periodChoiceBox.setValue(period);
    }

    /**
     * 按钮响应
     */
    @FXML
    private void turnOrange() {
        confirmButton.setStyle("-fx-text-fill: #d97555; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1");
    }

    @FXML
    private void turnWhite() {
        confirmButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1");
    }

    @FXML
    private void moTurnOrange() {
        if(!momentumButton.getStyle().equals("-fx-text-fill: #ffb199; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            momentumButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void moTurnWhite() {
        if(!momentumButton.getStyle().equals("-fx-text-fill: #ffb199; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            momentumButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void meTurnOrange() {
        if(!meanButton.getStyle().equals("-fx-text-fill: #ffb199; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            meanButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
    }

    @FXML
    private void meTurnWhite() {
        if(!meanButton.getStyle().equals("-fx-text-fill: #ffb199; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50"))
            meanButton.setStyle("-fx-text-fill: white;  -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
    }
    /**
     * 选择动量策略
     */
    @FXML
    private void selectMomentum(){
        momentumButton.setStyle("-fx-text-fill: #ffb199; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
        meanButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
        momentumIsSelected = true;
        meanIsSelected = false;
        ObservableList<String> means = FXCollections.observableArrayList();
        means.addAll("形成期", "持有期");
        periodChoiceBox.setItems(means);
    }

    /**
     * 选择均值回归
     */
    @FXML
    private void selectMean(){
        meanButton.setStyle("-fx-text-fill: #ffb199; -fx-background-color: transparent; -fx-border-color: #d97555; -fx-border-width: 1; -fx-border-radius: 50");
        momentumButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 50");
        momentumIsSelected = false;
        meanIsSelected = true;
        ObservableList<String> means = FXCollections.observableArrayList();
        means.addAll("形成期");
        periodChoiceBox.setItems(means);
    }


    /**
     * 确认按钮
     */
    @FXML
    private void confirm(){
        MainViewController mainViewController = (MainViewController) stageController.getController("MainView.fxml");

        DatePickerSettings datePickerSettings = new DatePickerSettings();
        Date sDate = datePickerSettings.getDate(startDate);
        Date eDate = datePickerSettings.getDate(endDate);
        String period = (String) periodChoiceBox.getSelectionModel().getSelectedItem();
        if(period == null){
            AutoCloseStage autoCloseStage = new AutoCloseStage();
            autoCloseStage.showErrorBox("请选择一种固定周期");
        }else {
            if (meanIsSelected == false && momentumIsSelected == false) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        AutoCloseStage autoCloseStage = new AutoCloseStage();
                        autoCloseStage.showErrorBox("请选择一种回测策略");
                    }
                });
            } else if (meanIsSelected == true && momentumIsSelected == false) {
                mainViewController.setBackTestStockSelectPane(sDate, eDate, "均值回归", period);
            } else if (meanIsSelected == false && momentumIsSelected == true) {
                mainViewController.setBackTestStockSelectPane(sDate, eDate, "动量策略", period);
            }
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
                if(endDate.getValue().isBefore(startDate.getValue().plusMonths(2)))
                    endDate.setValue(startDate.getValue().plusMonths(2));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
