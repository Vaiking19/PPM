import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
import javafx.stage._

class ControllerSecondWindow {

//  @FXML
//  private var subScene1:SubScene = _

  @FXML
  private var button2:Button = _

  //method automatically invoked after the @FXML fields have been injected
//  @FXML
//  def initialize(): Unit = {
//    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
//    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
//    subScene1.setRoot(InitSubScene.root)
//  }

  def onButton2Clicked():Unit = {

    val stage:Stage = new Stage()
    val fileChooser:FileChooser = new FileChooser()
    fileChooser.showOpenDialog(stage)

    fileChooser.setTitle("Open Resource File")


  }
}
