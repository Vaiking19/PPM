package visualUI

import javafx.fxml.FXML
import javafx.scene.{Node, SubScene}

class ControllerSecondWindow {

  @FXML private var subScene1:SubScene = _

  @FXML
  def initialize(): Unit = {
    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
  }

  //method automatically invoked after the @FXML fields have been injected
  //  @FXML
    def applySepia(): Unit = {
    FxApp.tree = FxApp.images.mapColourEffect(applySepiaToList, FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
    }

  def RemoveGreen(): Unit = {
    FxApp.tree = FxApp.images.mapColourEffect(removeGreen, FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  def GrowTree(): Unit = {
    FxApp.tree = FxApp.images.scaleOctree(2,FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  def HalveTree(): Unit = {
    FxApp.tree = FxApp.images.scaleOctree(0.5,FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  def moveCamRight(): Unit = {
    camVolume.setTranslateX(camVolume.getTranslateX + 10)
    cameraTransform.setTranslateX(cameraTransform.getTranslateX + 10)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  def moveCamLeft(): Unit = {
    camVolume.setTranslateX(camVolume.getTranslateX - 10)
    cameraTransform.setTranslateX(cameraTransform.getTranslateX - 10)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

}
