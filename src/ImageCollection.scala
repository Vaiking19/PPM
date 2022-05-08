import Utils._
import javafx.scene._
import javafx.scene.paint._
import javafx.scene.shape._

case class ImageCollection(worldRoot: Group, objects :List[Node]) {

  def getUpdatedWorld(): Group = ImageCollection.getUpdatedWorld(this)
  def getUpdatedWorld(n: Node): Group = ImageCollection.getUpdatedWorld2(this,n)
  def updateObjects(lst: List[Node]): List[Node] = ImageCollection.updateObjects(this.objects,lst)
  def scaleOctree(fact: Double, oct:Octree[Placement]):(Octree[Placement],Group,List[Node]) = ImageCollection.scaleOctree(this,fact,oct)
  def mapColourEffect(func: Color => Color, oct:Octree[Placement], world: Group, objects: List[Node]):(Octree[Placement],Group,List[Node],Placement) = ImageCollection.mapColourEffect(this,func,oct,world,objects)

}

object ImageCollection {

  def updateObjects(objectsLst: List[Node], lst: List[Node]): List[Node] = {
    objectsLst.concat(lst)
  }

  def getUpdatedWorld(img: ImageCollection): Group ={
  //Adicionar ao WorlGroup os Node obtidos no ficheiro
  img.objects.map(x => img.worldRoot.getChildren.add(x))
    img.worldRoot
  }

  def getUpdatedWorld2(img: ImageCollection, n: Node): Group = {
    img.worldRoot.getChildren.add(n)
    img.worldRoot
  }

  //T4
  // scaleOctree(fact:Double, oct:Octree[Placement]):Octree[Placement]
  //operação de ampliação/redução de uma octree e dos modelos gráficos nela
  //inseridos, segundo o fator fornecido (assumir somente a utilização dos
  //fatores 0.5 e 2 – para controlar a complexidade). A ampliação poderá resultar
  //numa octree com dimensão máxima superior a 32 unidades;


  /*  def mapColourEffect(func: Color => Color, oct:Octree[Placement]): Octree[Placement] = {
      oct match {
        case OcEmpty => OcEmpty
        case OcNode(p1, _, _, _, _, _, _, _, _) || OcLeaf ((p1,_)) =>
          val box: Box = boxGenerator(p1)
          val wiredListObjects:List[Node] = boxObjects(box,objects,worldRoot)    //LISTA OBJECTOS DA WIREBOX
          val newList = wiredListObjects.asInstanceOf[List[Shape3D]]
          newList.map(x => {
            val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
            val newColor = func(color)
            val newFong = new PhongMaterial()
            newFong.setDiffuseColor(newColor)
            x.setMaterial(newFong)
          })
          val newTree:Octree[Placement] = makeTree(p1,box,newList,worldRoot)
          newTree
      }

    }*/

def scaleOctree(img: ImageCollection, fact:Double, oct:Octree[Placement]):(Octree[Placement],Group,List[Node]) = {
  if (fact != 0.5 && fact != 2.0)
    throw new IllegalArgumentException("factor invalido! :(((((((")
  else {
    oct match {
      case OcNode(coords, _, _, _, _, _, _, _, _) => {
        val placement: Placement = (coords._1, coords._2 * fact)
        val oldBox = boxGenerator(coords) //caixa  tamanho original
        val newBox = boxGenerator(placement)
        img.worldRoot.getChildren.add(newBox)
        val scaledList = scaleList(fact, getList(img.objects, oldBox, 1))
        val newTree = makeTree(placement, oldBox, scaledList, img.worldRoot)

        (newTree, img.worldRoot, scaledList)

      }
      case OcLeaf(sec : Section) => {
        val placement: Placement = (sec._1._1, sec._1._2 * fact)
        val oldBox = boxGenerator(sec._1) //caixa  tamanho original
        val newBox = boxGenerator(placement)
        img.worldRoot.getChildren.add(newBox)
        val scaledList = scaleList(fact, img.objects)
        val newTree = makeTree(placement, oldBox, scaledList, img.worldRoot)

        (newTree, img.worldRoot, scaledList)
      }
      case OcEmpty => (OcEmpty,img.worldRoot,img.objects)
    }

  }
}



  def mapColourEffect(img: ImageCollection, func: Color => Color, oct:Octree[Placement], worldRoot: Group, objects: List[Node]): (Octree[Placement],Group,List[Node],Placement)= {
    val placement:Placement = treePlacement(oct)

    oct match {
      case OcEmpty => (OcEmpty,worldRoot,objects,placement)
      case OcNode(coords, _, _, _, _, _, _, _, _) => {
        val box: Box = boxGenerator(coords)
        val wiredListObjects:List[Node] = boxObjects(box,objects,worldRoot)    //LISTA OBJECTOS DA WIREBOX
        val newList = wiredListObjects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        val newTree:Octree[Placement] = makeTree(coords,box,newList,worldRoot)
        (newTree,worldRoot,newList,coords)
      }

      case OcLeaf(sec : Section) => {
        val placement: Placement = (sec._1._1, sec._1._2)
        val box: Box = boxGenerator(placement)
        val wiredListObjects:List[Node] = boxObjects(box,objects,worldRoot)    //LISTA OBJECTOS DA WIREBOX
        val newList = wiredListObjects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        val newTree:Octree[Placement] = makeTree(placement,box,newList,worldRoot)
        (newTree,worldRoot,newList,placement)
      }
    }

  }


///------------------------------------------------------------------------///


}
