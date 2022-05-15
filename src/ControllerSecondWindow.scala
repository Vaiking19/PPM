import InitSubScene._
import Utils._
import javafx.fxml.FXML
import javafx.scene.{Node, SubScene}
import javafx.scene.control._
/*
Ecra utilizado para visualizar a octree e os objectos,
Foram tambem adicionados botoes que permitem escalar a octree, aplicar o filtro "sepia",
remover a cor verde, e deslocar a camera
 */

class ControllerSecondWindow {

  @FXML private var subScene1:SubScene = _

  @FXML
  def initialize(): Unit = {
    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
  }

  //botao que aplica o filtro "sepia"
  def Sepia(): Unit = {
    FxApp.tree = FxApp.images.mapColourEffect(applySepiaToList, FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  //botao que remove a cor verde
  def RemoveGreen(): Unit = {
    FxApp.tree = FxApp.images.mapColourEffect(removeGreen, FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  //aumentar a octree
  def GrowTree(): Unit = {
    FxApp.tree = FxApp.images.scaleOctree(2,FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  //reduzir a octree
  def HalveTree(): Unit = {
    FxApp.tree = FxApp.images.scaleOctree(0.5,FxApp.tree)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  // deslocar a camera para cima / esquerda
  def MouseUp(): Unit = {
    camVolume.setTranslateX(camVolume.getTranslateX + 10)
    cameraTransform.setTranslateX(cameraTransform.getTranslateX + 10)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

  //deslocar a camera para baixo /direita
  def MouseDown(): Unit = {
    camVolume.setTranslateX(camVolume.getTranslateX - 10)
    cameraTransform.setTranslateX(cameraTransform.getTranslateX - 10)
    changeSectionColorIfCamInside(FxApp.images.worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], camVolume)
  }

}
