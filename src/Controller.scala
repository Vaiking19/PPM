import javafx.event._
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
import javafx.scene.SubScene
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage._

import java.io.File

class Controller {

//  @FXML
//  private var subScene1:SubScene = _

  @FXML
  private var fieldText1: TextField = _

  @FXML
  private var button1:Button = _

  //method automatically invoked after the @FXML fields have been injected
//  @FXML
//  def initialize(): Unit = {
//    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
//    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
//    subScene1.setRoot(InitSubScene.root)
//  }

  def onButton1Clicked():Unit = {

    val input = fieldText1.getText()

    val stage:Stage = new Stage()
    val fileChooser:FileChooser = new FileChooser()
    fileChooser.showOpenDialog(stage)

    fileChooser.setTitle("Open Resource File")


  }
}
