package ui.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * 自动关闭的窗口
 * <p>
 * Created by keenan on 10/03/2017.
 */
public class AutoCloseStage {

    /**
     * 静态方法
     * <p>
     * 显示自动关闭的窗口
     *
     * @param text 错误原因
     */
    public  void showErrorBox(String text) {
        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);

        //stage背景透明
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(StageStyle.TRANSPARENT);

        //去除stage外框
        popup.initStyle(StageStyle.UNDECORATED);

        //无法改变stage窗口
        popup.setResizable(false);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ErrorBoxView.fxml"));
            Pane pane = (Pane) loader.load();
            Scene scene = new Scene(pane);
            popup.setScene(scene);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            double width = pane.getPrefWidth();
            double height = pane.getPrefHeight();
            popup.setX((bounds.getWidth() - width) / 2);
            popup.setY((bounds.getHeight() - height) / 2);
            popup.show();

            ErrorBoxController errorBoxController = (ErrorBoxController) loader.getController();
            errorBoxController.setLabel(text);

        }catch (IOException e){
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (popup.isShowing()) {
                    Platform.runLater(() -> popup.close());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
