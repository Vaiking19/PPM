import InitSubScene.cameraView
import InitSubScene_text._
import Utils.{getClass, _}
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.{Insets, Pos}
import javafx.scene._
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage

import scala.annotation.tailrec

class Main_visual extends Application {

  override def start(stage: Stage): Unit = {
    //    Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    stage.setTitle("PPM Project 21/22")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val graphicScene = new Scene(mainViewRoot)
    stage.setScene(graphicScene)
    stage.show()

  }


  override def init(): Unit = {
    println("init")
  }

  override def stop(): Unit = {
    println("\n=== You have closed the application ===")
  }

}

object FxApp {

  //  val images:ImageCollection = new ImageCollection(new Group(getWiredbox(32,255,0,0),
  //    getCamVolume(0,0,255),getLineX(200,Color.GREEN),getLineY(200,Color.YELLOW),
  //    getLineZ(200,Color.AQUAMARINE)),readFromFile(s"src/$userInput.txt")) //relative path

  //  val tree = makeTree(new Placement((0, 0, 0), 32.0), getWiredbox(32,255,0,0), images.objects,images.worldRoot)

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main_visual], args: _*)

    // criar var com a nova class criada que será um novo object que terá dentro dele o
    // worldRoot e outra coisa necessario como a lista de objectos
    // para o utils é redundante o case class pode ser só object
    //
    //a interface grafica terá acesso a este objecto var
    //criar o mainLoop para a interface textual
    //usar o FileShooter para a interface grafica dar a escolher o caminho
    //do ficheiro a importar

  }
}