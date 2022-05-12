import InitSubScene._
import Utils._
import javafx.fxml.FXML
import javafx.scene.SubScene
import javafx.scene.control._

class ControllerSecondWindow {

  @FXML private var subScene1:SubScene = _
  @FXML private var button1:Button = _
  @FXML private var button2:Button = _
  @FXML private var mouseUp:Button = _
  @FXML private var mouseDown:Button = _
  @FXML private var slider1:Slider = _


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
    }

  def RemoveGreen(): Unit = {
    FxApp.tree = FxApp.images.mapColourEffect(removeGreen, FxApp.tree)
  }

  def ScaleTree(): Unit = {
    FxApp.tree = FxApp.images.scaleOctree(slider1.getScaleX,FxApp.tree)
  }

  def MouseUp(): Unit = {
    root.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)
    })
  }

  def MouseDown(): Unit = {
    root.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX - 2)
    })
  }

}
