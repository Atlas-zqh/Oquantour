package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by st on 2017/3/5.
 */
public class PaneAdder {

    private FXMLLoader loader;


    public void addPane(Pane sourcePane, String resource, int x, int y ){
        try {
            loader = new FXMLLoader(getClass().getResource(resource));
            Pane singlePane = (Pane) loader.load();
            sourcePane.getChildren().add(singlePane);
            singlePane.setLayoutX(x);
            singlePane.setLayoutY(y);
            //通过Loader获取FXML对应的ViewCtr，并将本StageController注入到ViewCtr中

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ControlledStage getController(){
        return loader.getController();
    }

    public void putIntoLoaders(String resource){
        StageController stageController = new StageController();
        stageController.putIntoLoaders(resource, loader);

    }
}
