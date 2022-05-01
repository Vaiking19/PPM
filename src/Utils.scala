import scala.io.Source
import scala.util.Try
import Utils._

import scala.io.StdIn.readLine
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.{Group, Node}
import javafx.scene.paint.{Color, Material, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Shape3D}

import scala.annotation.tailrec
import scala.collection.SortedMap

case class Utils() {

  def newColour(n1: Int, n2: Int, n3: Int): PhongMaterial = Utils.newColour(n1,n2,n3)
  def readFromFile(file: String): List[Node] = Utils.readFromFile(file)
//  def boxObjects(box: Box, listObject: List[Node], worldRoot : Group): List[Node] = Utils.boxObjects(box, listObject, worldRoot)
  def boxGenerator(place: ((Double, Double, Double),Double)): Box = Utils.boxGenerator(place)
  def getList(a: List[Node], c: Box, option: Int): List[Node] = Utils.getList(a,c,option)
  def applySepiaToList(color: Color): Color = Utils.applySepiaToList(color)
  def removeGreen(color: Color): Color = Utils.removeGreen(color)
  def getNextBoxes(place: ((Double, Double, Double),Double)): List[Box] = Utils.getNextBoxes(place)

  def childNodesIntersect(listBoxes: List[Box], listObjects: List[Node]): Boolean = Utils.childNodesIntersect(listBoxes, listObjects)
  def showPrompt(): Unit =  Utils.showPrompt()
  def getUserInput(): String = Utils.getUserInput()
  def getUserInputInt(): Int = Utils.getUserInputInt()
  def getUserInputDouble(): Double = Utils.getUserInputDouble()
  def printChoose(): Unit = Utils.printChoose

  object Utils {

  //T6 desenvolver uma text-based User Interface permitindo escolher o ficheiro
  //de configuração, lançar (uma única vez antes de terminar a execução) a
  //visualização do ambiente 3D e aplicar os métodos desenvolvido (p.e.
  //scaleOctree);

  def showPrompt(): Unit = {
    println("Please state the name of the configuration file without the extension: ")
  }

  def getUserInput(): String = scala.io.StdIn.readLine()

    def getUserInputInt() : Int = scala.io.StdIn.readInt()

    def getUserInputDouble() : Double = scala.io.StdIn.readDouble()

  def printChoose():Unit = {
    println("Please choose a number: ")
    println("1 - OcScale")
    println("2 - Colour effect ")
    println("0 - Normal Octree")
    println("Number:")
  }

    def newColour(red: Int, green: Int, blue: Int): PhongMaterial = {
    val new_colour = new PhongMaterial()

    val newRed = red.min(255)
    val newGreen = green.min(255)
    val newBlue = blue.min(255)

    new_colour.setDiffuseColor(Color.rgb(newRed,newGreen,newBlue))
    new_colour
  }


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

  def boxGenerator(placement: ((Double, Double, Double),Double)):Box = {
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
  def getNextBoxes(placement: ((Double, Double, Double),Double)): List[Box] = {

    val size = placement._2 / 2.0
    val x = placement._1._1
    val y = placement._1._2
    val z = placement._1._3

    val box1: Box = boxGenerator(((x, y, z), size))
    val box2: Box = boxGenerator(((x, y + size, z), size))
    val box3: Box = boxGenerator(((x, y, z + size), size))
    val box4: Box = boxGenerator(((x + size, y, z), size))
    val box5: Box = boxGenerator(((x + size, y + size, size), size))
    val box6: Box = boxGenerator(((x + size, y, z + size), size))
    val box7: Box = boxGenerator(((x + size, y + size, z), size))
    val box8: Box = boxGenerator(((x, y + size, z + size), size))

    val empty = Nil
    val boxList: List[Box] = box1 :: box2 :: box3 :: box4 :: box5 :: box6 :: box7 :: box8 :: empty
    boxList
    }


  //Função auxiliar para criar a lista com todos os elementos contidos
  def getList(a: List[Node], c: Box, option: Int): List[Node] = {
    println(s"3. tamanho da lista a ${a.size}")
    a match {
      case Nil => Nil
      case head :: tail => {
        if (option == 1) {
          if (c.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent)) {
            print("cubo contem? " + c.getBoundsInParent.contains(head.asInstanceOf[Shape3D].getBoundsInParent))
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

}

}
