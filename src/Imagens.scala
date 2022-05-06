import Utils._
import javafx.scene._
import javafx.scene.paint._
import javafx.scene.shape._

case class Imagens(worldRoot: Group, objects :List[Node]) {

  def getUpdatedWorld(): Group = Imagens.getUpdatedWorld(this)
  def getUpdatedWorld(n: Node): Group = Imagens.getUpdatedWorld2(this,n)
  def UpdateObjects(): List[Node] = Imagens.updateObjects(this.objects,lst: List[Node])
  def callScaleOctree(fact: Double, oct:Octree[Placement]):Octree[Placement] = Imagens.callScaleOctree(this,fact,oct)
  def mapColourEffect(func: Color => Color, oct:Octree[Placement]): Octree[Placement] = Imagens.mapColourEffect(this,func,oct)

}

object Imagens {

  def updateObjects(objectsLst: List[Node], lst: List[Node]): List[Node] = {
    objectsLst.concat(lst)
    objectsLst
  }

  def getUpdatedWorld(img: Imagens): Group ={
  //Adicionar ao WorlGroup os Node obtidos no ficheiro
  img.objects.map(x => img.worldRoot.getChildren.add(x))
    img.worldRoot
  }

  def getUpdatedWorld2(img: Imagens,n: Node): Group = {
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


  def callScaleOctree(img: Imagens,fact:Double, oct:Octree[Placement]):Octree[Placement] = {
    val placement:Placement = treePlacement(oct)
    val newTree:Octree[Placement] = scaleOctree(fact,oct)

    newTree match {
      case OcEmpty => OcEmpty

      case OcNode(coords, t1, t2, t3, t4, t5, t6, t7, t8) =>
        val alteredBox = boxGenerator(coords) // caixa ampliada ou reduzida

        if(!img.worldRoot.getChildren.contains(alteredBox))
          img.worldRoot.getChildren.add(alteredBox)

        val originalBox = boxGenerator(placement) //caixa  tamanho original
        val scaledList = scaleList(fact, getList(img.objects, originalBox, 1))
        makeTree(coords, originalBox, scaledList,img.worldRoot)

      case OcLeaf(sec : Section) =>
        val coords: Placement = (sec._1._1, sec._1._2 * fact)
        val alteredBox = boxGenerator(coords)

        if(!img.worldRoot.getChildren.contains(alteredBox))
          img.worldRoot.getChildren.add(alteredBox)

        val originalBox = boxGenerator(sec._1) //caixa  tamanho original
        val scaledList = scaleList(fact, getList(img.objects, originalBox, 1))
        makeTree(placement, alteredBox, scaledList,img.worldRoot)
    }
  }

  def mapColourEffect(img: Imagens, func: Color => Color, oct:Octree[Placement]): Octree[Placement] = {
    oct match {
      case OcEmpty => OcEmpty
      case OcNode(coords, _, _, _, _, _, _, _, _) => {
        val box: Box = boxGenerator(coords)
        val wiredListObjects:List[Node] = boxObjects(box,img.objects,img.worldRoot)    //LISTA OBJECTOS DA WIREBOX
        val newList = wiredListObjects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        val newTree:Octree[Placement] = makeTree(coords,box,newList,img.worldRoot)
        newTree
      }
      case OcLeaf(sec : Section) => {
        val placement: Placement = (sec._1._1, sec._1._2)
        val box: Box = boxGenerator(placement)
        val wiredListObjects:List[Node] = boxObjects(box,img.objects,img.worldRoot)    //LISTA OBJECTOS DA WIREBOX
        val newList = wiredListObjects.asInstanceOf[List[Shape3D]]
        newList.map(x => {
          val color = x.getMaterial.asInstanceOf[PhongMaterial].getDiffuseColor
          val newColor = func(color)
          val newFong = new PhongMaterial()
          newFong.setDiffuseColor(newColor)
          x.setMaterial(newFong)
        })
        val newTree:Octree[Placement] = makeTree(placement,box,newList,img.worldRoot)
        newTree
      }
    }

  }


}
