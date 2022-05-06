import FxApp._
import InitSubScene._
import InitSubScene_text._
import Utils._
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.{Insets, Pos}
import javafx.scene._
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage

import scala.annotation.tailrec

class Main extends Application {

  /*
    Additional information about JavaFX basic concepts (e.g. Stage, Scene) will be provided in week7
   */
  override def start(stage: Stage): Unit = {

//    Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)


    //MOUSE LOOP
    //Mouse left click interaction
    scene.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)
      images.worldRoot.getChildren.removeAll()
      //comando para activar a mudança de cor com a câmara
      val list = worldRoot.getChildren.toArray().toList.asInstanceOf[List[Node]] //SHAFSDHAFSADFJSDFLSAF
      isInsideObj(list, camVolume)

    })

    //setup and start the Stage
    stage.setTitle("PPM Project 21/22")
    val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    stage.setScene(scene)

    //escolher ficheiro
    showPrompt()
    val userInput = getUserInput()

    val placement1 = new Placement((0, 0, 0), 32.0)
    val tree = makeTree(placement1, getWiredbox(32,255,0,0), images.objects,images.worldRoot)
    val images:ImageCollection = new ImageCollection(new Group(getWiredbox(32,255,0,0),
      camVolume,lineX,lineY,lineZ),readFromFile(s"src/$userInput.txt")) //relative path


    //MOUSE LOOP
    //Mouse left click interaction
    scene.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)
      images.worldRoot.getChildren.removeAll()
      //comando para activar a mudança de cor com a câmara

      isInsideObj(images.worldRoot.getChildren.toArray().toList.asInstanceOf[List[Node]], camVolume)
    })

    mainLoop(tree,new ImageCollection(images.getUpdatedWorld,images.objects),placement1)

    @tailrec
    //USER TEXT INTERFACE
    def mainLoop(oct:Octree[Placement], img:ImageCollection, place: Placement) {

      //escolher opcao de configuração
      printChoose()
      val userInput2 = getUserInputInt()

      userInput2 match {

        case 1 =>
        println(s" Please choose a factorial between 0.5 or 2")
          val userInputFact = getUserInputDouble
          val tree = makeTree(place, getWiredbox(32,255,0,0), images.objects,images.worldRoot)
          images.callScaleOctree(userInputFact,tree,img.worldRoot,img.objects),)

        case 2 =>
          println(s" Please choose a format color for your tree:")
          println(s" 1 - applySepiaToList")
          println(s" 2 - removeGreen")
          val userInputFunc = getUserInputInt()
          val tree = makeTree(placement1, getWiredbox(32,255,0,0), images.objects,images.worldRoot)
          userInputFunc match {
            case 1 =>  val coloredTree:Octree[Placement] = images.mapColourEffect(applySepiaToList,tree)
            case 2 =>  val coloredTree:Octree[Placement] = images.mapColourEffect(removeGreen,tree)
            case _ => println("amigo, ou 1 ou 2 nada mais....burro...")
          }

        case 0 =>
          println(s" .....")

          mainLoop(tree,)

        case _ => println("Burro é um número que tens de escolher")

      }


    stage.show
  }

}

override def init(): Unit = {
  println("init")
}

override def stop(): Unit = {
  println("\n=== You have closed the application ===")
}

}

object FxApp {

  val images:ImageCollection = new ImageCollection(new Group(getWiredbox(32,255,0,0),
    camVolume,lineX,lineY,lineZ),List()) //relative path

   var tree: Octree[Placement] = OcNode(new Placement((0, 0, 0), 32.0),OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty)

   def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)

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
