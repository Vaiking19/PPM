import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.{Group, Node}
import javafx.scene.paint.{Color, Material, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Shape3D}

import scala.io.Source
import scala.util.Try
import Utils._

case class Utils() {

  def newColour(n1: Int, n2: Int, n3: Int): PhongMaterial = Utils.newColour(n1,n2,n3)
  def readFromFile(file: String): List[Node] = Utils.readFromFile(file)
  def boxObjects(box: Box, listObject: List[Node], worldRoot : Group): List[Node] = Utils.boxObjects(box, listObject, worldRoot)
  def boxGenerator(place: ((Double, Double, Double),Double)): Box = Utils.boxGenerator(place)
  def getList(a: List[Node], c: Box, option: Int): List[Node] = Utils.getList(a,c, option)

  def getNextBoxes(place: ((Double, Double, Double),Double)): List[Box] = Utils.getNextBoxes(place)

object Utils {

//  //Auxiliary types
//  type Point = (Double, Double, Double)
//  type Size = Double
//  type Placement = (Point, Size) //1st point: origin, 2nd point: size
//
//  //Shape3D is an abstract class that extends javafx.scene.Node
//  //Box and Cylinder are subclasses of Shape3D
//  type Section = (Placement, List[Node]) //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))

  //Materials to be applied to the 3D objects
//  val redMaterial = new PhongMaterial()
//  redMaterial.setDiffuseColor(Color.rgb(150, 0, 0))
//
//  val greenMaterial = new PhongMaterial()
//  greenMaterial.setDiffuseColor(Color.rgb(0, 255, 0))
//
//  val blueMaterial = new PhongMaterial()
//  blueMaterial.setDiffuseColor(Color.rgb(0, 0, 150))
//

  def newColour(red: Int, green: Int, blue:Int): PhongMaterial = {
    val new_colour = new PhongMaterial()
    new_colour.setDiffuseColor(Color.rgb(red,green,blue))
    new_colour
  }


  def myToInt(s:String): Int = {
    if (Try(s.toInt).isSuccess) s.toInt else -1
  }

//  def concat[A](apd:A, xs:List[A]) : List[A] = xs match {
//    case Nil => Nil
//    case xs => xs :+ apd
//  }
//
//  def concatList[A](apd:List[A], xs:List[A]) : List[A] =
//      apd match {
//        case Nil => xs
//        case head::tail => head::concatList(tail,xs)
//  }


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



//  def readFromFile(file: String, materialList : List[Material]): List[Node] = {
//    val bufferedSource = Source.fromFile(file)
//
//    var boxArr: Array[Shape3D] = Array()
//    var cyArr: Array[Shape3D] = Array()
//    // val objects: List[Node] = List()
//
//    for (line <- bufferedSource.getLines) {
//      val new_item = line.split(" ")
//      val objName = new_item(0)
//      val colourList = new_item(1).toList
//
//      if (new_item.size > 2) {
//        objName match {
//          case "Cylinder" => cyArr = cyArr :+ new Cylinder(0.5, 1, 10)
//            cyArr.last.setTranslateX(new_item(2).toInt)
//            cyArr.last.setTranslateY(new_item(3).toInt)
//            cyArr.last.setTranslateZ(new_item(4).toInt)
//            cyArr.last.setScaleX(new_item(5).toDouble)
//            cyArr.last.setScaleY(new_item(6).toDouble)
//            cyArr.last.setScaleZ(new_item(7).toDouble)
//            new_item(1) match {
//              case "(255,0,0)" => cyArr.last.setMaterial(materialList(0))
//              case "(0,255,0)" => cyArr.last.setMaterial(materialList(1))
//              case "(0,0,255)" => cyArr.last.setMaterial(materialList(2))
//            }
//          case "Box" => boxArr = boxArr :+ new Box(1, 1, 1)
//            boxArr.last.setTranslateX(new_item(2).toInt)
//            boxArr.last.setTranslateY(new_item(3).toInt)
//            boxArr.last.setTranslateZ(new_item(4).toInt)
//            boxArr.last.setScaleX(new_item(5).toDouble)
//            boxArr.last.setScaleY(new_item(6).toDouble)
//            boxArr.last.setScaleZ(new_item(7).toDouble)
//            new_item(1) match {
//              case "(255,0,0)" => boxArr.last.setMaterial(materialList(0))
//              case "(0,255,0)" => boxArr.last.setMaterial(materialList(1))
//              case "(0,0,255)" => boxArr.last.setMaterial(materialList(2))
//            }
//        }
//      }
//      else {
//        objName match {
//          case "Cylinder" => cyArr = cyArr :+ new Cylinder(0.5, 1, 10)
//            cyArr.last.setTranslateX(0)
//            cyArr.last.setTranslateY(0)
//            cyArr.last.setTranslateZ(0)
//            cyArr.last.setScaleX(1)
//            cyArr.last.setScaleY(1)
//            cyArr.last.setScaleZ(1)
//            new_item(1) match {
//              case "(255,0,0)" => cyArr.last.setMaterial(materialList(0))
//              case "(0,255,0)" => cyArr.last.setMaterial(materialList(1))
//              case "(0,0,255)" => cyArr.last.setMaterial(materialList(2))
//            }
//          case "Box" => boxArr = boxArr :+ new Box(1, 1, 1)
//            boxArr.last.setTranslateX(0)
//            boxArr.last.setTranslateY(0)
//            boxArr.last.setTranslateZ(0)
//            boxArr.last.setScaleX(1)
//            boxArr.last.setScaleY(1)
//            boxArr.last.setScaleZ(1)
//            new_item(1) match {
//              case "(255,0,0)" => boxArr.last.setMaterial(materialList(0))
//              case "(0,255,0)" => boxArr.last.setMaterial(materialList(1))
//              case "(0,0,255)" => boxArr.last.setMaterial(materialList(2))
//            }
//        }
//      }

    }
    val objects: List[Node]  = cyArr.toList.concat(boxArr.toList)
    bufferedSource.close
    objects
  }



  //AUXILIAR

  //FUNCAO PARA GERAR A LISTA DE OBJECTOS QUE ESTEJAM CONTIDOS DENTRO DE DETERMINADO
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
