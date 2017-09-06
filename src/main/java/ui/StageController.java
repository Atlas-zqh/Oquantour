package ui;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;

/**
 * Created by st on 2017/3/4.
 */
public class StageController {
    private Stage stage;
    private FXMLLoader loader;

    public static HashMap<String, Stage> stages = new HashMap<String, Stage>();

    private static HashMap<String, FXMLLoader> loaders = new HashMap<String, FXMLLoader>();


    public void loadStage(String resource) {

        double width = 0;
        double height = 0;
        try {
            //加载FXML资源文件
            loader = new FXMLLoader(getClass().getResource(resource));
            loaders.put(resource, loader);
            Pane pane = (Pane) loader.load();
            width = pane.getPrefWidth();
            height = pane.getPrefHeight();

            //通过Loader获取FXML对应的ViewCtr，并将本StageController注入到ViewCtr中
            ControlledStage controlledStage = (ControlledStage) loader.getController();
            controlledStage.setStageController(this);

            //构造对应的Stage
            Scene scene = new Scene(pane);
            scene.setFill(null);
            stage = new Stage();
            stage.setScene(scene);
            //无法改变stage窗口
            stage.setResizable(false);
            //将该stage添加至stages
            stages.put(resource, stage);
            //stage背景透明
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            //去除stage外框
            stage.initStyle(StageStyle.UNDECORATED);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setX((bounds.getWidth() - width) / 2);
            stage.setY((bounds.getHeight() - height) / 2);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Stage getStage(String resource){
        return stages.get(resource);
    }

    public ControlledStage getController(){
        return loader.getController();
    }

    public ControlledStage getController(String resource){
        return loaders.get(resource).getController();
    }


    public void closeStage(String resource){
        getStage(resource).close();
        stages.remove(resource);
    }

    public void putIntoLoaders(String resource, FXMLLoader loader){
        loaders.put(resource, loader);
    }
}
