import Utils.{Placement, _}
import javafx.scene._
import javafx.scene.paint._
import javafx.scene.shape._

case class ImageCollection(worldRoot: Group, objects :List[Node]) {

  def getUpdatedWorld(): Group = ImageCollection.getUpdatedWorld(this)
  def getUpdatedWorld(n: Node): Group = ImageCollection.getUpdatedWorld2(this,n)
  def updateObjects(lst: List[Node]): List[Node] = ImageCollection.updateObjects(this.objects,lst)
  def callScaleOctree(fact: Double, oct:Octree[Placement], world: Group, objects: List[Node]):(Octree[Placement],Group,List[Node],Placement) = ImageCollection.callScaleOctree(this,fact,oct,world, objects)
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


  def callScaleOctree(img: ImageCollection, fact:Double, oct:Octree[Placement], worldRoot: Group, objects: List[Node]): (Octree[Placement],Group,List[Node],Placement) = {
    //determinar o placement da arvore original
    val placement:Placement = treePlacement(oct)
    //obter a nova arvore escalada
    val newTree:Octree[Placement] = scaleOctree(fact,oct)

    newTree match {
      case OcEmpty => (OcEmpty,worldRoot,objects,placement)

      case OcNode(coords,_,_,_,_,_,_,_,_) =>
        val alteredBox = boxGenerator(coords) // caixa ampliada ou reduzida

        if(!img.worldRoot.getChildren.contains(alteredBox))
          img.worldRoot.getChildren.add(alteredBox)

        val originalBox = boxGenerator(placement) //caixa  tamanho original
        val scaledList = scaleList(fact, getList(objects, originalBox, 1))

        //retorna a arvore escalada, a worldRoot actualizada, lista de objectos escalados, placement da arvore escalada)
        (makeTree(coords, originalBox, scaledList,worldRoot),worldRoot,scaledList,coords)

      case OcLeaf(sec : Section) =>
        val coords: Placement = (sec._1._1, sec._1._2 * fact)
        val alteredBox = boxGenerator(coords)

        if(!img.worldRoot.getChildren.contains(alteredBox))
          img.worldRoot.getChildren.add(alteredBox)

        val originalBox = boxGenerator(sec._1) //caixa  tamanho original
        val scaledList = scaleList(fact, getList(objects, originalBox, 1))
        (makeTree(placement, alteredBox, scaledList,worldRoot),worldRoot,scaledList,coords)
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
