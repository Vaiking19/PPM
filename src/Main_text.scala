import textUI.InitSubScene_text._
import shared.Utils._
import javafx.application.Application
import javafx.geometry.{Insets, Pos}
import javafx.scene._
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import shared.{ImageCollection, Octree}
import scala.annotation.tailrec

class Main_text extends Application {

  override def start(stage: Stage): Unit = {
    //    Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    //setup and start the Stage
    stage.setTitle("PPM Project 21/22")

    val userInput = getInputFromUser()
    val size = getSizeInput()

    val placement1 = new Placement((0, 0, 0), size)
    val camVolume = createCamVolume(0, 0, 255)
    val fileObjects = getObjectsInsideBox(createBox(placement1), readFromFile(s"src/$userInput.txt"))

    val images: ImageCollection = new ImageCollection(new Group(createWiredbox(32, 255, 0, 0),
      camVolume, createLineX(200, Color.GREEN), createLineY(200, Color.YELLOW),
      createLineZ(200, Color.AQUAMARINE)), fileObjects)
    val tree = makeTree(placement1, images.objects, images.worldRoot)

    @tailrec
    def mainLoop(oct: Octree[Placement], img: ImageCollection): Octree[Placement] = {
      //escolher opcao de configuração
      printChoose()
      val userInput2 = scala.io.StdIn.readInt()

      userInput2 match {
        case 1 =>
          println(s" Please choose a factorial between 0.5 or 2")
          val userInputFact = scala.io.StdIn.readDouble()
          val scaledTree: Octree[Placement] = img.scaleOctree(userInputFact, oct)
          mainLoop(scaledTree, img)

        case 2 =>
          println(s" Please choose a format color for your tree:")
          println(s" 1 - applySepiaToList")
          println(s" 2 - removeGreen")
          println(s" 3 - exit")
          val userInputFunc = scala.io.StdIn.readInt()

          userInputFunc match {
            case 1 =>
              val coloredTree = img.mapColourEffect(applySepiaToList, oct)
              mainLoop(coloredTree, images)
            case 2 =>
              val coloredTree = img.mapColourEffect(removeGreen, oct)
              mainLoop(coloredTree, img)
            case _ =>
              print("s adeus aí velho")
              null
          }
        case _ =>
          print("s adeus aí velho")
          null
      }
    }

    //val imgRoot = mainLoop(tree, new shared.ImageCollection(images.getUpdatedWorld,images.objects))
    mainLoop(tree, new ImageCollection(images.updateWorld, images.objects))

    //USER TEXT INTERFACE

    val scene = createScene(images.worldRoot, 810, 610)
    StackPane.setAlignment(scene._2, Pos.TOP_LEFT)
    StackPane.setMargin(scene._2, new Insets(3))

    scene._1.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)
      images.worldRoot.getChildren.removeAll()
      //comando para activar a mudança de cor com a câmara
      changeSectionColorIfCamInside(images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
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

object FxAppText {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main_text], args: _*)
  }
}