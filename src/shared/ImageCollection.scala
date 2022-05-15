package shared

import javafx.scene._
import javafx.scene.paint._
import javafx.scene.shape._
import shared.Utils._

case class ImageCollection(worldRoot: Group, objects :List[Node]) {
  def updateWorld(): Group = ImageCollection.updateWorld(this)
  def scaleOctree(fact: Double, oct:Octree[Placement]): Octree[Placement] = ImageCollection.scaleOctree(this,fact,oct)
  def mapColourEffect(func: Color => Color, oct:Octree[Placement]) :Octree[Placement] = ImageCollection.mapColourEffect(this,func,oct)
}

object ImageCollection {

  def updateWorld(img: ImageCollection): Group ={
    //Adicionar ao WorlGroup os Node obtidos no ficheiro
    img.objects.map(x => img.worldRoot.getChildren.add(x))
    img.worldRoot
  }

  //T4
  // scaleOctree(fact:Double, oct:shared.Octree[Placement]):shared.Octree[Placement]
  //operação de ampliação/redução de uma octree e dos modelos gráficos nela
  //inseridos, segundo o fator fornecido (assumir somente a utilização dos
  //fatores 0.5 e 2 – para controlar a complexidade). A ampliação poderá resultar
  //numa octree com dimensão máxima superior a 32 unidades;

  def scaleOctree(img: ImageCollection, fact:Double, oct:Octree[Placement]): Octree[Placement] = {
    if (fact != 0.5 && fact != 2.0)
      throw new IllegalArgumentException("factor invalido! :(((((((")
    else {
      img.worldRoot.getChildren.removeIf(x=> {!img.objects.contains(x) && x.isInstanceOf[Box]})

      oct match {
        case OcNode(coords, _, _, _, _, _, _, _, _) =>
          val placement: Placement = (coords._1, coords._2 * fact)
          val oldBox = createBox(coords) //caixa  tamanho original
          val newBox = createBox(placement)

          img.worldRoot.getChildren.add(newBox)

          val scaledList = scaleList(fact, getObjectsInsideBox(oldBox,img.objects))
          val newTree = makeTree(placement, scaledList, img.worldRoot)

          img.worldRoot.getChildren.removeIf(x=> {
            img.objects.contains(x)
          })
          scaledList.map(x => {
            img.worldRoot.getChildren.add(x)
          })
          newTree
        case OcLeaf(sec : Section) =>
          val placement: Placement = (sec._1._1, sec._1._2 * fact)
          val newBox = createBox(placement)
          img.worldRoot.getChildren.add(newBox)
          val scaledList = scaleList(fact, img.objects)
          val newTree = makeTree(placement, scaledList, img.worldRoot)
          newTree
        case OcEmpty => OcEmpty
      }

    }
  }


 def mapColourEffect(img: ImageCollection, func: Color => Color, oct:Octree[Placement]): Octree[Placement] = {
    oct match {
      case OcEmpty => OcEmpty
      case OcNode(coords, _, _, _, _, _, _, _, _) =>
        val newList = img.objects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        makeTree(coords,newList,img.worldRoot)
      case OcLeaf(sec : Section) =>
        val placement: Placement = (sec._1._1, sec._1._2)
        val newList = img.objects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        makeTree(placement,newList,img.worldRoot)
    }
  }

/*
  def mapColourEffect(img: ImageCollection, func: Color => Color, oct:Octree[Placement]): Octree[Placement] = {
    oct match {
      case OcEmpty => OcEmpty
      case OcNode(place, _, _, _, _, _, _, _, _) || OcLeaf(place,_)=>
        val newList = img.objects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        makeTree(place,newList,img.worldRoot)
    }
  }*/


  ///------------------------------------------------------------------------///


}