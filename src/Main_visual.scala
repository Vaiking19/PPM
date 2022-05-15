
import Utils.Placement
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene._
import javafx.stage.Stage
/*
Class utilizada para gerar a interface grafica
 */
class Main_visual extends Application {

  override def start(stage: Stage): Unit = {
    //    Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    stage.setTitle("PPM Project 21/22")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("Controller1.fxml"))
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
  var images:ImageCollection = _
  var tree: Octree[Placement] = _


  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main_visual], args: _*)
  }
}