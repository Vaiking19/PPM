import Utils._
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.{Insets, Pos}
import javafx.scene._
import javafx.scene.layout.StackPane
import javafx.scene.paint.{Color}
import javafx.stage.Stage

case class mainLoop(oct:Octree[Placement], img:Imagens){
  def looping(): Unit = mainLoop.looping(this.oct,this.img)
}

class Main extends Application {

  /*
    Additional information about JavaFX basic concepts (e.g. Stage, Scene) will be provided in week7
   */
  override def start(stage: Stage): Unit = {

//    Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)


    //USER TEXT INTERFACE
  def looping(oct:Octree[Placement], img:Imagens) {
  //escolher ficheiro
    showPrompt()
    val userInput = getUserInput()

    //escolher opcao de configuração
    printChoose()
    val userInput2 = getUserInputInt()



    // CameraView - an additional perspective of the environment
    val cameraView = getCameraView(subScene,350,225,-45,-10,-50,-50)

    // Position of the CameraView: Right-bottom corner
    StackPane.setAlignment(cameraView, Pos.TOP_LEFT)
    StackPane.setMargin(cameraView, new Insets(3))

    // Scene - defines what is rendered (in this case the subScene and the cameraView)
    val root = new StackPane(subScene, cameraView)
    subScene.widthProperty.bind(root.widthProperty)
    subScene.heightProperty.bind(root.heightProperty)

    val scene = new Scene(root, 810, 610, true, SceneAntialiasing.BALANCED)

    //T3 permitir que durante a visualização e mediante movimento da câmera (o
    //movimento é obtido, no código fornecido, clicando no botão esquerdo do
    //rato), sejam visualmente identificados, através de alteração da cor da
    //partição, as partições espaciais da octree que sejam visíveis a partir da
    //câmera (i.e., que intersetam o seu volume de visualização). O código dado
    //também fornece uma third person view (canto inferior direito) que permite
    //visualizar a octree de diferentes perspetivas (através do rato),
    //independentemente da posição da câmera;

    //função para que os elementos mudem de cor quando a camera passar por cima d
    @tailrec
    def isInsideObj(partitionsList: List[Node], camVolume: Cylinder): List[Node] = {
      partitionsList match {
        case List() => Nil
        case head :: tail => {
          if (camVolume.getBoundsInParent.intersects(head.getBoundsInParent) && (head.isInstanceOf[Box] && head.asInstanceOf[Box].getDrawMode == DrawMode.LINE)) {
            head.asInstanceOf[Shape3D].setMaterial(greenMaterial)
          } else if (head.isInstanceOf[Box] && head.asInstanceOf[Box].getDrawMode == DrawMode.LINE)
            head.asInstanceOf[Shape3D].setMaterial(redMaterial)
          isInsideObj(tail, camVolume)
        }
      }
    }


    //MOUSE LOOP
    //Mouse left click interaction
    scene.setOnMouseClicked((event) => {
      getCamVolume(0,0,255).setTranslateX(getCamVolume(0,0,255).getTranslateX + 2)
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

      userInput2 match {

        case 1 =>
        println(s" Please choose a factorial between 0.5 or 2")
          val userInputFact = getUserInputDouble
          val tree = makeTree(placement1, getWiredbox(32,255,0,0), images.objects,images.worldRoot)
            images.callScaleOctree(userInputFact,tree)

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
          val tree = makeTree(placement1, getWiredbox(32,255,0,0), images.objects,images.worldRoot)

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


  //    //SECCOES COM COORDENADAS FIXAS QUE CONSTITUEM O 32 CUBO
  //    val size_cubo = wiredBox.getHeight / 2

  // 3D objects (group of nodes - javafx.scene.Node) that will be provide to the subScene
  val camera = getCamera(0,0,0,0.1,10000.0,-500,20,-45.0,-45.0)

  val images:Imagens = new Imagens(new Group(getWiredbox(32,255,0,0),
    getCamVolume(0,0,255),
    getLineX(200,new Color(0,0,0,1)),
    getLineY(200,new Color(0,0,0,1)),
    getLineZ(200,new Color(0,0,0,1)),
    camera),
    readFromFile(s"src/$userInput.txt")) //relative path

  // SubScene - composed by the nodes present in the worldRoot
  getSubscene(images.worldRoot, 800, 600, true, SceneAntialiasing.BALANCED,Color.WHITE).setCamera(camera)


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
