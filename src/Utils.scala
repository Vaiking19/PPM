import scala.io.Source
import scala.util.Try
import Utils._

import scala.io.StdIn.readLine
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.{Group, Node, PerspectiveCamera, SceneAntialiasing, SubScene}
import javafx.scene.paint.{Color, Material, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Line, Shape3D}
import javafx.scene.transform.Rotate

import scala.annotation.tailrec
import scala.collection.SortedMap

object Utils {

  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size

  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node]) //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))


  //T6 desenvolver uma text-based User Interface permitindo escolher o ficheiro
  //de configuração, lançar (uma única vez antes de terminar a execução) a
  //visualização do ambiente 3D e aplicar os métodos desenvolvido (p.e.
  //scaleOctree);

  def showPrompt(): Unit = {
    println("Please state the name of the configuration file without the extension: ")
  }

  def getUserInput(): String = scala.io.StdIn.readLine()

  def getUserInputInt(): Int = scala.io.StdIn.readInt()

  def getUserInputDouble(): Double = scala.io.StdIn.readDouble()

  def printChoose(): Unit = {
    println("Please choose a number: ")
    println("1 - OcScale")
    println("2 - Colour effect ")
    println("0 - Octree normal")
    println("Number:")
  }

  //------------------------------------------------------------------------//

  def newColour(red: Int, green: Int, blue: Int): PhongMaterial = {
    val new_colour = new PhongMaterial()

    val newRed = red.min(255)
    val newGreen = green.min(255)
    val newBlue = blue.min(255)

    new_colour.setDiffuseColor(Color.rgb(newRed, newGreen, newBlue))
    new_colour
  }

  //--------------------------------------------------------------------------------//

  def applySepiaToList(color: Color): Color = {
      val newRed = (0.4 * color.getRed +  0.77 * color.getGreen + 0.20 * color.getBlue).min(1.0)
      val newGreen = (0.35 * color.getRed +  0.69 * color.getGreen + 0.17 * color.getBlue).min(1.0)
      val newBlue = (0.27 * color.getRed +  0.53 * color.getGreen  + 0.13 * color.getBlue).min(1.0)

    val newColor = new Color(newRed,newGreen,newBlue,color.getOpacity)
    newColor
    }

  def removeGreen(color: Color): Color = {
       val newColor = new Color(color.getRed,0.0,color.getBlue,color.getOpacity)
    newColor
  }

  def myToInt(s:String): Int = {
    if (Try(s.toInt).isSuccess) s.toInt else -1
  }

  def readFromFile(file: String): List[Node] = {
    val bufferedSource = Source.fromFile(file)

    var boxArr: Array[Shape3D] = Array()
    var cyArr: Array[Shape3D] = Array()
    // val objects: List[Node] = List()

    for (line <- bufferedSource.getLines) {
      val new_item = line.split(" ")
      val objName = new_item(0)
      val temp = new_item(1).replaceAll("\\(", "").replaceAll("\\)","").split(",")
      val colourList = temp.toList.map(x => x.toInt)
//      print(s"colorList $colourList")

      if (new_item.size > 2) {
        objName match {
          case "Cylinder" => cyArr = cyArr :+ new Cylinder(0.5, 1, 10)
            cyArr.last.setTranslateX(new_item(2).toInt)
            cyArr.last.setTranslateY(new_item(3).toInt)
            cyArr.last.setTranslateZ(new_item(4).toInt)
            cyArr.last.setScaleX(new_item(5).toDouble)
            cyArr.last.setScaleY(new_item(6).toDouble)
            cyArr.last.setScaleZ(new_item(7).toDouble)
            cyArr.last.setMaterial(newColour(colourList(0),colourList(1),colourList(2)))

          case "Box" => boxArr = boxArr :+ new Box(1, 1, 1)
            boxArr.last.setTranslateX(new_item(2).toInt)
            boxArr.last.setTranslateY(new_item(3).toInt)
            boxArr.last.setTranslateZ(new_item(4).toInt)
            boxArr.last.setScaleX(new_item(5).toDouble)
            boxArr.last.setScaleY(new_item(6).toDouble)
            boxArr.last.setScaleZ(new_item(7).toDouble)
            boxArr.last.setMaterial(newColour(colourList(0),colourList(1),colourList(2)))
        }
      }
      else {
        objName match {
          case "Cylinder" => cyArr = cyArr :+ new Cylinder(0.5, 1, 10)
            cyArr.last.setTranslateX(0)
            cyArr.last.setTranslateY(0)
            cyArr.last.setTranslateZ(0)
            cyArr.last.setScaleX(1)
            cyArr.last.setScaleY(1)
            cyArr.last.setScaleZ(1)
            cyArr.last.setMaterial(newColour(colourList(0),colourList(1),colourList(2)))

          case "Box" => boxArr = boxArr :+ new Box(1, 1, 1)
            boxArr.last.setTranslateX(0)
            boxArr.last.setTranslateY(0)
            boxArr.last.setTranslateZ(0)
            boxArr.last.setScaleX(1)
            boxArr.last.setScaleY(1)
            boxArr.last.setScaleZ(1)
            boxArr.last.setMaterial(newColour(colourList(0),colourList(1),colourList(2)))
        }
      }
    }
    val objects: List[Node]  = cyArr.toList.concat(boxArr.toList)
    bufferedSource.close
    objects
  }



  //AUXILIAR

  //FUNCAO PARA GERAR A LISTA DE OBJECTOS QUE ESTEJAM CONTIDOS DENTRO DE DETERMINADO BOX
  def boxObjects(box: Box, listObject: List[Node], worldRoot : Group): List[Node] =
    listObject match {
      case Nil => Nil
      case head :: tail => {
        if (box.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent))
          head :: boxObjects(box, tail, worldRoot)
        else {
          worldRoot.getChildren.remove(head)
          boxObjects(box, tail, worldRoot)
        }
      }
    }

  //Função auxiliar para criar a lista com todos os elementos contidos
  def getList(a: List[Node], c: Box, option: Int): List[Node] = {
    //println(s"3. tamanho da lista a ${a.size}")
    a match {
      case Nil => Nil
      case head :: tail => {
        if (option == 1) {
          if (c.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent)) {
//            println("cubo contem? " + c.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent))
            head :: getList(tail, c, option)
          }
          else getList(tail, c, option)
        } else {
          if (c.getBoundsInParent.intersects(head.asInstanceOf[Shape3D].getBoundsInParent))
            head :: getList(tail, c, option)
          else getList(tail, c, option)
        }
      }
    }
  }

  def boxGenerator(placement: Placement):Box = {
    val sizeDaCox = placement._2
    val cox = new Box(sizeDaCox, sizeDaCox, sizeDaCox)

    cox.setTranslateX(placement._1._1 + sizeDaCox / 2)
    cox.setTranslateY(placement._1._2 + sizeDaCox / 2)
    cox.setTranslateZ(placement._1._3 + sizeDaCox / 2)
    cox.setMaterial(newColour(255,0,0))
    cox.setDrawMode(DrawMode.LINE)

    //worldRoot.getChildren.add(cox)
    cox
  }

  //FUNCAO PARA GERAR UM ELEMENTO DO TIPO BOX PARA UMA DETERMINADA SECCAO
  def getNextBoxes(placement: Placement): List[Box] = {

    val size = placement._2 / 2.0
    val x = placement._1._1
    val y = placement._1._2
    val z = placement._1._3

    //(0,0,0)
    val box1: Box = boxGenerator(((x, y, z), size))
    //(0,size,0)
    val box2: Box = boxGenerator(((x, y + size, z), size))
    //(0,0,size)
    val box3: Box = boxGenerator(((x, y, z + size), size))
    //(size,0,0)
    val box4: Box = boxGenerator(((x + size, y, z), size))
    //(0,size,size)
    val box5: Box = boxGenerator(((x, y + size, z + size), size))
    //(size, 0,size)
    val box6: Box = boxGenerator(((x + size, y, z + size), size))
    val box7: Box = boxGenerator(((x + size, y + size, z), size))
    val box8: Box = boxGenerator(((x, y + size, z + size), size))

    val empty = Nil
    val boxList: List[Box] = box1 :: box2 :: box3 :: box4 :: box5 :: box6 :: box7 :: box8 :: empty
    boxList
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
          case head :: tail =>
            if (b.getBoundsInParent.intersects(head.asInstanceOf[Shape3D].getBoundsInParent) //caso de intersetar e nao conter
              && !b.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent))
              true
            else
              runThroughObjects(b, tail)
        }
      listBoxes match {
        case Nil => false
        case head :: tail =>
          if (runThroughObjects(head, listObjects))
            true
          else childNodesIntersect(tail, listObjects)
      }
    }


    //---------------------------------------------------------------------//

    /*
      T2 criar uma octree de acordo com os modelos gráficos previamente carregados e permitir
      a sua visualização (as partições espaciais são representadas com wired cubes). A octree
      oct1 presente no código fornecido poderá ajudar na interpretação;
      */

  //FUNCAO PARA GERAR TODAS AS OCTREES DAS 8 SECCOES QUE PROSSEGUEM DETERMINADO NODO
  def generateChild(listBoxes: List[Box], listObjects: List[Node], worldRoot: Group): List[Octree[Placement]] =
    listBoxes match {
      case Nil => Nil
      case head :: tail => {
        val secX = head.getTranslateX - head.getHeight/2
        val secY = head.getTranslateY - head.getHeight/2
        val secZ = head.getTranslateZ - head.getHeight/2

        //nodo filho nao tem nenhuma lista contida então é empty
        val boxElements = getList(listObjects,head,1)

        if (boxElements.isEmpty)
          OcEmpty :: generateChild(tail,listObjects,worldRoot)
        else {
          if(!worldRoot.getChildren.contains(head))
            worldRoot.getChildren.add(head)

          //nodo filho tem elementos contidos entao vai verificar se os filhos desse filho intersectam alguma coisa
          val listNextBoxes = getNextBoxes((secX, secY, secZ),head.getHeight)

          //se os filhos do nodo filho intersectarem entao o nodo filho é uma folha
          if (childNodesIntersect(listNextBoxes, boxElements)) {
            val sec:Section = new Section(((secX,secY,secZ),head.getHeight),boxElements)
            OcLeaf(sec) :: generateChild(tail,listObjects,worldRoot)
          } else {
            //caso os filhos nao interceptem irá ser necessario fazer com que sejam criados os nodos filhos
            OcNode[Placement](new Placement((secX, secY, secZ),head.getWidth),generateChild(listNextBoxes,listObjects,worldRoot).apply(0),generateChild(listNextBoxes,listObjects,worldRoot).apply(1),
              generateChild(listNextBoxes,listObjects, worldRoot).apply(2),generateChild(listNextBoxes,listObjects,worldRoot).apply(3),generateChild(listNextBoxes,listObjects,worldRoot).apply(4),
              generateChild(listNextBoxes,listObjects,worldRoot).apply(5),generateChild(listNextBoxes,listObjects,worldRoot).apply(6),generateChild(listNextBoxes,listObjects,worldRoot).apply(7),
            ):: generateChild(tail,listObjects,worldRoot)
          }
        }
      }
    }


  //Funcao para criar a OcTree como deve ser
  def makeTree(p: Placement, box : Box, list: List[Node], worldRoot: Group): Octree[Placement] = {

    //println(s" 43. elemetos do world ${worldRoot.getChildren.size()}")

    //WIRED BOX SO ACEITE OBJECTOS CONTIDOS SE NAO CONTIVER CORTA FORA OS OBJECTOS

    val wiredListObjects:List[Node] = boxObjects(box,list,worldRoot)    //LISTA OBJECTOS DA WIREBOX

    if(wiredListObjects.isEmpty) return OcEmpty       //SOU VAZIO ? SOU OCEMPTY

    val listNextBoxes = getNextBoxes(p) //Lista das 8 proximas caixas com objectos contidos

    if(childNodesIntersect(listNextBoxes,wiredListObjects)){ //FUNCAO QUE RECEBE SECCOES E VAI VER LISTA DE OBJECTOS DA ROOT E VÊ SE ALGUM OBJECTO É INTERSECTADo MAS NAO CONTIDO
      val fatherOcleaf:Octree[Placement] = OcLeaf((p,wiredListObjects))
      return fatherOcleaf // CASO ALGUM DER TRUE ELE ACABA E O PAI É FOLHA
    }



    val childPopulate:List[Octree[Placement]] = generateChild(listNextBoxes,wiredListObjects,worldRoot) //FUNCAO PARA GERAR ARVORES A PARTIR DAS SECCOES DOS FILHOS

    //RETORNO FINAL É A OCNODE(PLACEMENTE WIREBOX, GERAR_FILHO(SECCAO FILHO 1), GERAR FILHO(SECCAO FILHO 2),...., GERAR_FILHO(SECCAO FILHO 8)

    val finalTree:Octree[Placement] = OcNode(p,childPopulate.apply(0),childPopulate.apply(1),childPopulate.apply(2),
      childPopulate.apply(3),childPopulate.apply(4),childPopulate.apply(5),childPopulate.apply(6),childPopulate.apply(7))
    finalTree
  }

  //-------------------------------------------------------------------//

  def treePlacement(oct: Octree[Placement]): Placement = {

    oct match {
      case OcEmpty => null

      case OcNode(coords,_,_,_,_,_,_,_,_) =>
        val placement: Placement = (coords._1, coords._2)
        placement

      case OcLeaf(section:Section) =>
        val placement: Placement = (section._1._1, section._1._2)
        placement
    }

  }

  def scaleOctree(fact:Double, oct:Octree[Placement]):Octree[Placement] = {
    if (fact != 0.5 && fact != 2.0)
      throw new IllegalArgumentException("factor invalido! :(((((((")
    else {

      oct match {
        case OcEmpty => OcEmpty

        case OcNode(coords, t1, t2, t3, t4, t5, t6, t7, t8) =>
          val placement: Placement = (coords._1, coords._2 * fact)
          val scaledTree: Octree[Placement] = new OcNode(placement, t1, t2, t3, t4, t5, t6, t7, t8)
          scaledTree

        case OcLeaf(section:Section) =>
          val placement: Placement = (section._1._1, section._1._2 * fact)
          val scaledTree: Octree[Placement] = new OcLeaf(placement,section._2)
          scaledTree
      }
    }
  }



  def scaleList(fact : Double, list: List[Node]) : List[Node] = {
    list match{
      case Nil => Nil
      case head :: tail =>
        head.setTranslateX(head.getTranslateX * fact)
        head.setTranslateY(head.getTranslateY * fact)
        head.setTranslateZ(head.getTranslateZ * fact)
        head match {
          case cylinder: Cylinder =>
            cylinder.setRadius(cylinder.getRadius * fact)
            cylinder.setHeight(cylinder.getHeight * fact)
          case _ =>
            //Criar novo objeto box com escala modificada?
            val size = head.asInstanceOf[Box].getWidth
            head.setScaleX(size * fact)
            head.setScaleY(size * fact)
            head.setScaleZ(size * fact)
        }
        head :: scaleList(fact, tail)
    }
  }




//-------------------------------------------------------------------------------------//

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
      case head :: tail =>
        if (camVolume.getBoundsInParent.intersects(head.getBoundsInParent) && (head.isInstanceOf[Box] && head.asInstanceOf[Box].getDrawMode == DrawMode.LINE)) {
          head.asInstanceOf[Shape3D].setMaterial(newColour(0,255,0))
        } else if (head.isInstanceOf[Box] && head.asInstanceOf[Box].getDrawMode == DrawMode.LINE)
          head.asInstanceOf[Shape3D].setMaterial(newColour(255,0,0))
        isInsideObj(tail, camVolume)
    }
  }


}
