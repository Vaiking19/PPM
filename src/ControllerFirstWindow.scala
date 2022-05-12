import InitSubScene._
import Utils._
import javafx.fxml._
import javafx.scene.control._
import javafx.scene._
import javafx.stage._

class ControllerFirstWindow {

  @FXML private var button1: Button = _

  def OnButton1Clicked(): Unit = {

    val stage:Stage = new Stage()
    val fileChooser:FileChooser = new FileChooser()
    fileChooser.setTitle("Open Resource File")

    val path = fileChooser.showOpenDialog(stage).getName
    println(s"${path}")

    val fileObjects = readFromFile(s"src/$path")
    FxApp.images = new ImageCollection(worldRoot,fileObjects)
    FxApp.tree = makeTree(new Placement((0, 0, 0), 32.0), FxApp.images.objects,FxApp.images.worldRoot)

//    button2.setVisible(true)

    val fxmlLoader2 = new FXMLLoader(getClass.getResource("Controller2.fxml"))
    val mainViewRoot: Parent = fxmlLoader2.load()

    button1.getScene.setRoot(mainViewRoot)
  }

}
