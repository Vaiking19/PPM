import InitSubScene.cameraView
import InitSubScene_text._
import Utils._
import javafx.application.Application
import javafx.geometry.{Insets, Pos}
import javafx.scene._
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage

import scala.annotation.tailrec

class Main extends Application {

  override def start(stage: Stage): Unit = {

//    Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    //setup and start the Stage
    stage.setTitle("PPM Project 21/22")
//    val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
//    val mainViewRoot: Parent = fxmlLoader.load()

    //escolher ficheiro
    showPrompt()
    val userInput = getUserInput()

    val placement1 = new Placement((0, 0, 0), 32.0)

    val images:ImageCollection = new ImageCollection(new Group(createWiredbox(32,255,0,0),
      createCamVolume(0,0,255),createLineX(200,Color.GREEN),createLineY(200,Color.YELLOW),
      createLineZ(200,Color.AQUAMARINE)),readFromFile(s"src/$userInput.txt")) //relative path

    @tailrec
    def mainLoop(oct:Octree[Placement], img:ImageCollection, place: Placement): ImageCollection = {

      //escolher opcao de configuração
      printChoose()
      val userInput2 = getUserInputInt()

      userInput2 match {

        case 1 =>
          println(s" Please choose a factorial between 0.5 or 2")
          val userInputFact = getUserInputDouble
          val scaledTree = images.callScaleOctree(userInputFact, oct, img.worldRoot, img.objects)

          mainLoop(scaledTree._1, new ImageCollection(scaledTree._2, scaledTree._3), scaledTree._4)

        case 2 =>
          println(s" Please choose a format color for your tree:")
          println(s" 1 - applySepiaToList")
          println(s" 2 - removeGreen")
          println(s" 3 - exit")
          val userInputFunc = getUserInputInt()

          userInputFunc match {
            case 1 =>
              val coloredTree = images.mapColourEffect(applySepiaToList, oct, img.worldRoot, img.objects)
              mainLoop(coloredTree._1, new ImageCollection(coloredTree._2, coloredTree._3), coloredTree._4)

            case 2 =>
              val coloredTree = images.mapColourEffect(removeGreen, oct, img.worldRoot, img.objects)
               mainLoop(coloredTree._1, new ImageCollection(coloredTree._2, coloredTree._3), coloredTree._4)

            case _ =>
              print("s adeus aí velho")
              img

          }

         case _ =>
          print("s adeus aí velho")
          img

          }
    }


    val tree = makeTree(placement1, createWiredbox(32,255,0,0), images.objects,images.worldRoot)

    val imgRoot = mainLoop(tree,new ImageCollection(images.getUpdatedWorld,images.objects),placement1)

    //USER TEXT INTERFACE
      val scene = createScene(imgRoot.worldRoot,810,610)
      StackPane.setAlignment(scene._2, Pos.TOP_LEFT)
      StackPane.setMargin(scene._2, new Insets(3))

    val volume = createCamVolume(0, 0, 255)

    scene._1.setOnMouseClicked((event) => {
      volume.setTranslateX(volume.getTranslateX + 2)
//      imgRoot.worldRoot.getChildren.removeAll()
      //comando para activar a mudança de cor com a câmara
      isInsideObj(imgRoot.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], volume)
    })

    stage.setScene(scene._1)
      stage.show



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
