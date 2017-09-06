package ui.util;

import ui.ControlledStage;
import ui.StageController;

/**
 * Created by Pxr on 2017/3/16.
 */
public class OpenAnimationController implements ControlledStage {
    private StageController stageController;
    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }
}
