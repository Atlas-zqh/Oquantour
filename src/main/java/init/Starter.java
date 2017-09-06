package init;/**
 * Created by island on 2017/3/5.
 */


import javafx.application.Application;
import javafx.stage.Stage;
import ui.MainViewController;
import ui.StageController;

public class Starter extends Application {

    private StageController stageController = new StageController();


    @Override
    public void start(Stage stage) {
        intGUI();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void intGUI(){
        stageController = new StageController();
        stageController.loadStage("MainView.fxml");
        MainViewController mainViewController = (MainViewController) stageController.getController();
        mainViewController.init();

    }
}
