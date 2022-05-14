import InitSubScene._
import Utils._
import javafx.fxml.FXML
import javafx.scene.{Node, SubScene}
import javafx.scene.control._

class ControllerSecondWindow {

  @FXML private var subScene1:SubScene = _
//  @FXML private var button1:Button = _
//  @FXML private var button2:Button = _
//  @FXML private var mouseUp:Button = _
//  @FXML private var mouseDown:Button = _
//  @FXML private var grow:Button = _
//  @FXML private var decrease:Button = _

  @FXML
  def initialize(): Unit = {
    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
  }

  //method automatically invoked after the @FXML fields have been injected
  //  @FXML
    def Sepia(): Unit = {
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

  def MouseUp(): Unit = {
    camVolume.setTranslateX(camVolume.getTranslateX + 10)
    cameraTransform.setTranslateX(cameraTransform.getTranslateX + 10)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
//    cameraView.setX(cameraView.getX + 2)


  }

  def MouseDown(): Unit = {
    camVolume.setTranslateX(camVolume.getTranslateX - 10)
    cameraTransform.setTranslateX(cameraTransform.getTranslateX - 10)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)

//    cameraView.setX(cameraView.getX - 2)
//    cameraView.getT

  }

}
