
//import Utils.{Placement, boxGenerator, getList}

import Utils._
import javafx.application.Application
import javafx.collections.ObservableList
import javafx.geometry.{Insets, Pos}
import javafx.scene._
import javafx.scene.layout.StackPane
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape._
import javafx.scene.transform.Rotate
import javafx.stage.Stage

import java.util.stream.Collectors
import scala.::
import scala.annotation.tailrec

class Main extends Application {

  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size

  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node]) //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))

  /*
    Additional information about JavaFX basic concepts (e.g. Stage, Scene) will be provided in week7
   */
  override def start(stage: Stage): Unit = {

    //Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    //Materials to be applied to the 3D objects
    val redMaterial = new PhongMaterial()
    redMaterial.setDiffuseColor(Color.rgb(150, 0, 0))

    val greenMaterial = new PhongMaterial()
    greenMaterial.setDiffuseColor(Color.rgb(0, 255, 0))

    val blueMaterial = new PhongMaterial()
    blueMaterial.setDiffuseColor(Color.rgb(0, 0, 150))

    //3D objects
    val lineX = new Line(0, 0, 200, 0)
    lineX.setStroke(Color.BLACK)

    val lineY = new Line(0, 0, 0, 200)
    lineY.setStroke(Color.BLACK)

    val lineZ = new Line(0, 0, 200, 0)
    lineZ.setStroke(Color.BLACK)
    lineZ.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS))

    val camVolume = new Cylinder(10, 50, 10)
    camVolume.setTranslateX(1)
    camVolume.getTransforms().add(new Rotate(45, 0, 0, 0, Rotate.X_AXIS))
    camVolume.setMaterial(blueMaterial)
    camVolume.setDrawMode(DrawMode.LINE)

    val wiredBox = new Box(32, 32, 32)
    wiredBox.setTranslateX(32 / 2)
    wiredBox.setTranslateY(32 / 2)
    wiredBox.setTranslateZ(32 / 2)
    wiredBox.setMaterial(redMaterial)
    wiredBox.setDrawMode(DrawMode.LINE)

    val cylinder1 = new Cylinder(0.5, 1, 10)
    cylinder1.setTranslateX(2)
    cylinder1.setTranslateY(2)
    cylinder1.setTranslateZ(2)
    cylinder1.setScaleX(2)
    cylinder1.setScaleY(2)
    cylinder1.setScaleZ(2)
    cylinder1.setMaterial(greenMaterial)

    val box1 = new Box(1, 1, 1) //
    box1.setTranslateX(5)
    box1.setTranslateY(5)
    box1.setTranslateZ(5)
    box1.setMaterial(greenMaterial)

  val helper = new Utils()

//    val objects: List[Node] = Utils.readFromFile("src/conf.txt", List(redMaterial, greenMaterial, blueMaterial)) //relative path
    val objects: List[Node] = helper.readFromFile("src/conf.txt") //relative path

    // 3D objects (group of nodes - javafx.scene.Node) that will be provide to the subScene
    val worldRoot: Group = new Group(wiredBox, camVolume, lineX, lineY, lineZ)


    println(s"worldroot size before  ${worldRoot.getChildren.size()}")

    //Função para alimentar o WorlGroup com os dados lidos no ficheiro
//    def fillWorldGroup(nodes: List[Node], world: Group): Unit = {
//      nodes match {
//        case Nil => Nil
//        case head :: tail => {
//          world.getChildren.add(head)
//          fillWorldGroup(tail, world)
//        }
//      }
//    }
//
//
//    fillWorldGroup(objects, worldRoot) //Comando para preencher o worldRoot com os elementos lidos no ficheiro

    //Adicionar ao WorlGroup os Node obtidos no ficheiro
    objects.map(x => worldRoot.getChildren.add(x))

    // Camera
    val camera = new PerspectiveCamera(true)

    val cameraTransform = new CameraTransformer
    cameraTransform.setTranslate(0, 0, 0)
    cameraTransform.getChildren.add(camera)
    camera.setNearClip(0.1)
    camera.setFarClip(10000.0)

    camera.setTranslateZ(-500)
    camera.setFieldOfView(20)
    cameraTransform.ry.setAngle(-45.0)
    cameraTransform.rx.setAngle(-45.0)
    worldRoot.getChildren.add(cameraTransform)

    // SubScene - composed by the nodes present in the worldRoot
    val subScene = new SubScene(worldRoot, 800, 600, true, SceneAntialiasing.BALANCED)
    subScene.setFill(Color.WHITE)
    subScene.setCamera(camera)

    // CameraView - an additional perspective of the environment
    val cameraView = new CameraView(subScene)
    cameraView.setFirstPersonNavigationEabled(true)
    cameraView.setFitWidth(350)
    cameraView.setFitHeight(225)
    cameraView.getRx.setAngle(-45)
    cameraView.getT.setZ(-10)
    cameraView.getT.setY(-50)
    cameraView.getCamera.setTranslateZ(-50)
    cameraView.startViewing

    // Position of the CameraView: Right-bottom corner
    StackPane.setAlignment(cameraView, Pos.TOP_LEFT)
    StackPane.setMargin(cameraView, new Insets(5))

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

    //Mouse left click interaction
    scene.setOnMouseClicked((event) => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)
      worldRoot.getChildren.removeAll()
      //comando para activar a mudança de cor com a câmara
      val list = worldRoot.getChildren.toArray().toList.asInstanceOf[List[Node]] //SHAFSDHAFSADFJSDFLSAF
      isInsideObj(list, camVolume)

    })

    //setup and start the Stage
    stage.setTitle("PPM Project 21/22")
    stage.setScene(scene)
    stage.show

    /*
    T2 criar uma octree de acordo com os modelos gráficos previamente carregados e permitir
    a sua visualização (as partições espaciais são representadas com wired cubes). A octree
    oct1 presente no código fornecido poderá ajudar na interpretação;
    */

    //oct1 - example of an Octree[Placement] that contains only one Node (i.e. cylinder1)
    //In case of difficulties to implement task T2 this octree can be used as input for tasks T3, T4 and T5



    //Função para criar nova lista para a seccção especifica
    // vai verificar se todos os objectos existentes estão contidos na área da secção
//    def updateSectionList(b: Box, allObjects: List[Node], option: Int): Section = {
//      val size = s._1._2
//      val caixinha = new Box(size, size, size)
//      caixinha.setTranslateX(s._1._1._1 + size / 2)
//      caixinha.setTranslateY(s._1._1._2 + size / 2)
//      caixinha.setTranslateZ(s._1._1._3 + size / 2)
//
//      println(s"4. tamanho da caixinha ${caixinha.getHeight}")
//      val listaComTodos = helper.getList(allObjects, caixinha, option)
//      println(s"5. tamanho da lista Objects ${allObjects.size}")
//      println(s"6. tamanho da listaComTodos ${listaComTodos.size}")
//
//      if (listaComTodos.size == 0) s
//      else new Section(s._1, listaComTodos)
//    }
//
//    def groupOfSections(s: List[Section]): List[Section] =
//      s match {
//        case Nil => Nil
//        case head :: tail => updateSectionList(head, objects, 1) :: groupOfSections(tail)
//      }

    //CRIA 8 SECCOES A CADA PASSO DE NOVA ARVORE

//    def getSectionList(section: Section): List[Section] = {
//      val size = section._1._2 / 2.0
//      val x = section._1._1._1
//      val y = section._1._1._2
//      val z = section._1._1._3
//
//      val sec1: Section = (((x, y, z), size), List())
//      val sec2: Section = (((x, y + size, z), size), List())
//      val sec3: Section = (((x, y, z + size), size), List())
//      val sec4: Section = (((x + size, y, z), size), List())
//      val sec5: Section = (((x + size, y + size, size), size), List())
//      val sec6: Section = (((x + size, y, z + size), size), List())
//      val sec7: Section = (((x + size, y + size, z), size), List())
//      val sec8: Section = (((x, y + size, z + size), size), List())
//
//      val empty = Nil
//      val newSec: List[Section] = sec1 :: sec2 :: sec3 :: sec4 :: sec5 :: sec6 :: sec7 :: sec8 :: empty
//
//      newSec
//    }


//    //FUNCAO PARA GERAR A LISTA DE OBJECTOS QUE ESTEJAM CONTIDOS DENTRO DE DETERMINADO
//    def BoxObjects(box: Box, listObject: List[Node]): List[Node] =
//      listObject match {
//        case Nil => Nil
//        case head :: tail => {
//          if (box.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent))
//            head :: BoxObjects(box, tail)
//          else {
//            worldRoot.getChildren.remove(head)
//            BoxObjects(box, tail)
//          }
//        }
//      }


    //T4

    //TODO T4
    // scaleOctree(fact:Double, oct:Octree[Placement]):Octree[Placement]
    //operação de ampliação/redução de uma octree e dos modelos gráficos nela
    //inseridos, segundo o fator fornecido (assumir somente a utilização dos
    //fatores 0.5 e 2 – para controlar a complexidade). A ampliação poderá resultar
    //numa octree com dimensão máxima superior a 32 unidades;
    def scaleOctree(fact:Double, oct:Octree[Placement]):Octree[Placement] = {
      if (fact != 0.5 && fact != 2.0)
        throw new IllegalArgumentException("factor invalido! :(((((((")
      else {

        oct match {
          case OcEmpty => OcEmpty
          case OcNode(coords, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) => {
            val placement: Placement = (coords._1, coords._2 * fact)
            val sec: Section = (coords, List())
            val smallerBox = helper.boxGenerator(sec) //caixa  tamanho original
            val biggerBox = helper.boxGenerator(new Section(placement, List()))
            worldRoot.getChildren.add(biggerBox)
            val scaledList = scaleList(fact, helper.getList(objects, smallerBox, 1))

            makeTree(placement, smallerBox, scaledList)
          }
          case OcLeaf(sec : Section) => {
            val placement: Placement = (sec._1._1, sec._1._2 * fact)
            val smallerBox = helper.boxGenerator(sec) //caixa  tamanho original
            val biggerBox = helper.boxGenerator(new Section(placement, List()))
            worldRoot.getChildren.add(biggerBox)
            val scaledList = scaleList(fact, helper.getList(objects, smallerBox, 1))

            makeTree(placement, smallerBox, scaledList)
          }
        }

      }
    }

      def scaleList(fact : Double, list: List[Node]) : List[Node] = {
        list match{
          case Nil => Nil
          case head :: tail => {
            if(head.isInstanceOf[Cylinder]) {
              head.asInstanceOf[Cylinder].setRadius(head.asInstanceOf[Cylinder].getRadius * fact)
              head.asInstanceOf[Cylinder].setHeight(head.asInstanceOf[Cylinder].getHeight * fact)
            }else {
              //Criar novo objeto box com escala modificada?
              val size = head.asInstanceOf[Box].getWidth
              head.setScaleX(size * fact)
              head.setScaleY(size * fact)
              head.setScaleZ(size * fact)
            }
            head :: scaleList(fact, tail)
          }
        }
      }

    //FUNCAO PARA VALIDAR SE ALGUM DOS 8 SECCOES QUE PROSSEGUEM UM DETERMINADO NODO IRAO INTERSECTAR MAS NAO CONTER ALGUM ELEMENTO
    //OU SEJA, VALIDA SE É POSSIVEL QUE O ELEMENTO CONTIDO PODERÁ VIR A SER PARTIDO EM 8 PARTES OU NAO
    //CASO SEJA POSSIVEL SER PARTIDO EM 8 PARTES ENTAO RETORNA TRUE PARA QUE O NODO "PAI" SAIBA QUE PODERA PROCEDER EM DIVIDIR-SE
    //CASO RETORNE FALSE O NODO PAI IRA RECEBER A INFORMAÇÃO DE QUE NAO SE PODERÁ REPARTIR E TERA DE SER ELE A FOLHA
    @tailrec
    def childNodesIntersect(listBoxes: List[Box], listObjects: List[Node]): Boolean = {
      @tailrec
      def runThroughObjects(b: Box, listObjects: List[Node]): Boolean =
        listObjects match {
          case Nil => false
          case head :: tail => {
            if (b.getBoundsInParent.intersects(head.asInstanceOf[Shape3D].getBoundsInParent) //caso de intersetar e nao conter
              && !b.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent))
              true
            else
              runThroughObjects(b, tail)
          }
        }
      listBoxes match {
        case Nil => false
        case head :: tail => {
          if (runThroughObjects(head, listObjects))
              true
           else childNodesIntersect(tail, listObjects)
        }
      }
    }

    //FUNCAO PARA GERAR TODAS AS OCTREES DAS 8 SECCOES QUE PROSSEGUEM DETERMINADO NODO
    def generateChild(listBoxes: List[Box], lisObjects: List[Node]): List[Octree[Placement]] =
      listBoxes match {
        case Nil => Nil
        case head :: tail =>
          //nodo filho nao tem nenhuma lista contida então é empty
          if (head.isEmpty) OcEmpty :: generateChild(tail)
          else {
            //nodo filho tem elementos contidos entao vai verificar se os filhos desse filho intersectam alguma coisa
            val listNextBoxes = helper.getNextBoxes(head.getTranslateX)
            //se os filhos do nodo filho intersectarem entao o nodo filho é uma folha
            if (childNodesIntersect(listNextSections, head._2)) {
              val boxToPrint = helper.boxGenerator(head)
              worldRoot.getChildren.add(boxToPrint)
              OcLeaf(head)::generateChild(tail)
            } else {
              //caso os filhos nao interceptem irá ser necessario fazer com que sejam criados os nodos filhos
              OcNode[Placement](new Placement((head.getTranslateX,head.getTranslateY, head.getTranslateZ),head.getWidth),generateChild(listNextSections).apply(0),generateChild(listNextSections).apply(1),
                generateChild(listNextSections).apply(2),generateChild(listNextSections).apply(3),generateChild(listNextSections).apply(4),
                generateChild(listNextSections).apply(5),generateChild(listNextSections).apply(6),generateChild(listNextSections).apply(7),
              ):: generateChild(tail)
            }
          }
      }


    //Funcao para criar a OcTree como deve ser
    def makeTree(p: Placement, box : Box, list: List[Node]): Octree[Placement] = {

      //WIRED BOX SO ACEITE OBJECTOS CONTIDOS SE NAO CONTIVER CORTA FORA OS OBJECTOS

      val wiredListObjects:List[Node] = helper.boxObjects(box,list,worldRoot)    //LISTA OBJECTOS DA WIREBOX

      if(wiredListObjects.isEmpty) return OcEmpty       //SOU VAZIO ? SOU OCEMPTY

      val listNextBoxes = helper.getNextBoxes(p) //Lista das 8 proximas caixas com objectos contidos

      if(childNodesIntersect(listNextBoxes,wiredListObjects)){ //FUNCAO QUE RECEBE SECCOES E VAI VER LISTA DE OBJECTOS DA ROOT E VÊ SE ALGUM OBJECTO É INTERSECTADo MAS NAO CONTIDO
         val fatherOcleaf:Octree[Placement] = OcLeaf((p,wiredListObjects))
        return fatherOcleaf // CASO ALGUM DER TRUE ELE ACABA E O PAI É FOLHA
      }

      val childPopulate:List[Octree[Placement]] = generateChild(listNextBoxes,wiredListObjects) //FUNCAO PARA GERAR ARVORES A PARTIR DAS SECCOES DOS FILHOS

        //RETORNO FINAL É A OCNODE(PLACEMENTE WIREBOX, GERAR_FILHO(SECCAO FILHO 1), GERAR FILHO(SECCAO FILHO 2),...., GERAR_FILHO(SECCAO FILHO 8)

      val finalTree:Octree[Placement] = OcNode(p,childPopulate.apply(0),childPopulate.apply(1),childPopulate.apply(2),
        childPopulate.apply(3),childPopulate.apply(4),childPopulate.apply(5),childPopulate.apply(6),childPopulate.apply(7))
      finalTree
    }

//    val rootTreeEmpty: Octree[Placement] = OcNode[Placement](placement1, OcLeaf(secT1), OcLeaf(secT2), OcLeaf(secT3), OcLeaf(secT4), OcLeaf(secT5), OcLeaf(secT6), OcLeaf(secT7), OcLeaf(secT8))

    //    //SECCOES COM COORDENADAS FIXAS QUE CONSTITUEM O 32 CUBO
    //    val size_cubo = wiredBox.getHeight / 2
    val placement1: Placement = ((0, 0, 0), 32.0)


    val tree = makeTree(placement1, wiredBox, objects)


    println(s"33. OcTree ${scaleOctree(2.0, tree)}")

    //example of bounding boxes (corresponding to the octree oct1) added manually to the world
val b2 = new Box(8, 8, 8)
//translate because it is added by defaut to the coords (0,0,0)
b2.setTranslateX(32 / 8)
b2.setTranslateY(8 / 2)
b2.setTranslateZ(8 / 2)
b2.setMaterial(blueMaterial)
b2.setDrawMode(DrawMode.LINE)

val b3 = new Box(4, 4, 4)
//translate because it is added by defaut to the coords (0,0,0)
b3.setTranslateX(4 / 2)
b3.setTranslateY(4 / 2)
b3.setTranslateZ(4 / 2)
b3.setMaterial(blueMaterial)
b3.setDrawMode(DrawMode.LINE)

//adding boxes b2 and b3 to the world
//      worldRoot.getChildren.add(b2)
//      worldRoot.getChildren.add(b3)


}
override def init(): Unit = {
  println("init")
}

override def stop(): Unit = {
  println("stopped")
}

}

object FxApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)

  }
}
