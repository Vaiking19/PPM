import InitSubScene.{cameraView, root, subScene}
import Utils.{isInsideObj, newColour}
import javafx.geometry.{Insets, Pos}
import javafx.scene.layout.StackPane
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Line}
import javafx.scene.transform.Rotate
import javafx.scene._

object InitSubScene_text {

  def getLineX(value: Int, color: Color): Line = {
    val lineX = new Line(0, 0, value, 0)
    lineX.setStroke(color)
    lineX
  }

  def getLineY(value: Int, color: Color): Line = {
    val lineY = new Line(0, 0, 0, value)
    lineY.setStroke(color)
    lineY
  }

  def getLineZ(value: Int, color: Color): Line = {
    val lineZ = new Line(0, 0, value, 0)
    lineZ.setStroke(color)
    lineZ.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS))
    lineZ
  }

  //3D objects
  def getCamVolume(color1: Int, color2: Int, color3: Int): Cylinder = {
    val camVolume = new Cylinder(10, 50, 10)
    camVolume.setTranslateX(1)
    camVolume.getTransforms().add(new Rotate(45, 0, 0, 0, Rotate.X_AXIS))
    camVolume.setMaterial(newColour(color1, color2, color3))
    camVolume.setDrawMode(DrawMode.LINE)
    camVolume
  }

  def getWiredbox(size: Int, color1: Int, color2: Int, color3: Int): Box = {
    val wiredBox = new Box(size, size, size)
    wiredBox.setTranslateX(size / 2)
    wiredBox.setTranslateY(size / 2)
    wiredBox.setTranslateZ(size / 2)
    wiredBox.setMaterial(newColour(color1, color2, color3))
    wiredBox.setDrawMode(DrawMode.LINE)
    wiredBox
  }

  def getCamera(): PerspectiveCamera = {
    // Camera
    val camera = new PerspectiveCamera(true)
    camera
  }

  def getCameraTransformer(x: Int, y: Int, z: Int, nearClip: Double, farClip: Double, translZ: Int, fieldView: Int, ryAngle: Double, rxAngle: Double): CameraTransformer = {

    val cameraTransform = new CameraTransformer
    cameraTransform.setTranslate(x, y, z)
    cameraTransform.getChildren.add(getCamera)
    getCamera.setNearClip(nearClip)
    getCamera.setFarClip(farClip)

    getCamera().setTranslateZ(translZ)
    getCamera().setFieldOfView(fieldView)
    cameraTransform.ry.setAngle(ryAngle)
    cameraTransform.rx.setAngle(rxAngle)
    cameraTransform
  }

  def getCameraView(subScene: SubScene, fitWidth: Int, fitHeight: Int, rxAngle: Int, setZ: Int, setY: Int, transZ: Int): CameraView = {

    val cameraView = new CameraView(subScene)
    cameraView.setFirstPersonNavigationEabled(true)
    cameraView.setFitWidth(fitWidth)
    cameraView.setFitHeight(fitHeight)
    cameraView.getRx.setAngle(rxAngle)
    cameraView.getT.setZ(setZ)
    cameraView.getT.setY(setY)
    cameraView.getCamera.setTranslateZ(transZ)
    cameraView.startViewing
    cameraView
  }


  def getSubscene(worldRoot: Group, width: Double, height: Double, color: Color): SubScene = {
    val subScene = new SubScene(worldRoot, width, height, true, SceneAntialiasing.BALANCED)
    subScene.setFill(color)
    subScene.setCamera(getCamera)
    subScene
  }

  def getRoot(subScene: SubScene, cameraView: CameraView): StackPane = {
    val root = new StackPane(subScene,cameraView)
    subScene.widthProperty.bind(root.widthProperty)
    subScene.heightProperty.bind(root.heightProperty)
    root
  }

  def getScene(worldRoot: Group,width: Double, height: Double): Scene = {

    //SUBSCENE
    val subJohnCena = getSubscene(worldRoot,800,600,Color.DARKSLATEGRAY)
    val view = getCameraView(subJohnCena,350,225,-45,-100,-500,-50)
    val root = getRoot(subJohnCena,view)

    StackPane.setAlignment(cameraView, Pos.BOTTOM_RIGHT)
    StackPane.setMargin(cameraView, new Insets(3))

    getCameraTransformer(worldRoot,0,0,0,0.1,10000.0,-500,20,-45.0,-45.0)

    //SCENE
    val scenario = new Scene(root, width, height)
    val volume = getCamVolume(0,0,255)

    //MOUSE LOOP
    //Mouse left click interaction
    scenario.setOnMouseClicked((event) => {
      volume.setTranslateX(volume.getTranslateX + 2)
      worldRoot.getChildren.removeAll()
      //comando para activar a mudança de cor com a câmara
      isInsideObj(worldRoot.getChildren.toArray.toList.asInstanceOf[List[Node]], volume)
    })

    scenario
  }

}

