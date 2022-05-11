import javafx.fxml._
import javafx.scene.control._
import javafx.scene._

class ControllerFirstWindow {

  @FXML
  var button1: Button = _

  @FXML
  var button2: Button = _

  def OnButton1Clicked(): Unit = {

//    val secondStage: Stage = new Stage()
    /*secondStage.setHeight(100)
    secondStage.setWidth(100)
    secondStage.centerOnScreen()*/
//    secondStage.initModality(Modality.APPLICATION_MODAL)
//    secondStage.initOwner(button1.getScene().getWindow)
    val fxmlLoader2 = new FXMLLoader(getClass.getResource("Controller2.fxml"))
    val mainViewRoot: Parent = fxmlLoader2.load()
//    val scenario = new Scene(mainViewRoot)
//    secondStage.setScene(scenario)
//    secondStage.show()
    button1.getScene.setRoot(mainViewRoot)
  }

  def OnButton2Clicked(): Unit = {
    val fxmlLoader3 = new FXMLLoader(getClass.getResource("Controller3.fxml"))
    val mainViewRoot: Parent = fxmlLoader3.load()
    button2.getScene.setRoot(mainViewRoot)
  }
}
