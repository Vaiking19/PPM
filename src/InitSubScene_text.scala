import Utils._
import javafx.scene.layout.StackPane
import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Line}
import javafx.scene.transform.Rotate
import javafx.scene._

object InitSubScene_text {

  def createLineX(value: Int, color: Color): Line = {
    val lineX = new Line(0, 0, value, 0)
    lineX.setStroke(color)
    lineX
  }

  def createLineY(value: Int, color: Color): Line = {
    val lineY = new Line(0, 0, 0, value)
    lineY.setStroke(color)
    lineY
  }

  def createLineZ(value: Int, color: Color): Line = {
    val lineZ = new Line(0, 0, value, 0)
    lineZ.setStroke(color)
    lineZ.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS))
    lineZ
  }

  //3D objects
  def createCamVolume(color1: Int, color2: Int, color3: Int): Cylinder = {
    val camVolume = new Cylinder(10, 50, 10)
    camVolume.setTranslateX(1)
    camVolume.getTransforms().add(new Rotate(45, 0, 0, 0, Rotate.X_AXIS))
    camVolume.setMaterial(newColour(color1, color2, color3))
    camVolume.setDrawMode(DrawMode.LINE)
    camVolume
  }

  def createWiredbox(size: Int, color1: Int, color2: Int, color3: Int): Box = {
    val wiredBox = new Box(size, size, size)
    wiredBox.setTranslateX(size / 2)
    wiredBox.setTranslateY(size / 2)
    wiredBox.setTranslateZ(size / 2)
    wiredBox.setMaterial(newColour(color1, color2, color3))
    wiredBox.setDrawMode(DrawMode.LINE)
    wiredBox
  }

//  def createCylinder(transX: Int, transY: Int, transZ: Int, scaleX: Int, scaleY: Int, scaleZ: Int, color1: Int, color2: Int, color3: Int): Cylinder = {
//    val cylinder1 = new Cylinder(0.5, 1, 10)
//    cylinder1.setTranslateX(transX)
//    cylinder1.setTranslateY(transY)
//    cylinder1.setTranslateZ(transZ)
//    cylinder1.setScaleX(scaleX)
//    cylinder1.setScaleY(scaleY)
//    cylinder1.setScaleZ(scaleZ)
//    cylinder1.setMaterial(newColour(color1, color2, color3))
//    cylinder1
//  }
//
//  def createBox(transX: Int, transY: Int, transZ: Int, scaleX: Int, scaleY: Int, scaleZ: Int, color1: Int, color2: Int, color3: Int): Box = {
//    val box1 = new Box(0.5, 1, 10)
//    box1.setTranslateX(transX)
//    box1.setTranslateY(transY)
//    box1.setTranslateZ(transZ)
//    box1.setScaleX(scaleX)
//    box1.setScaleY(scaleY)
//    box1.setScaleZ(scaleZ)
//    box1.setMaterial(newColour(color1, color2, color3))
//    box1
//  }

  def createCamera(): PerspectiveCamera = {
    // Camera
    val camera = new PerspectiveCamera(true)
    camera
  }

  def createCameraTransformer(worldRoot: Group, x: Int, y: Int, z: Int, nearClip: Double, farClip: Double, transZ: Int, fieldView: Int, ryAngle: Double, rxAngle: Double, camera: PerspectiveCamera): CameraTransformer = {
    val cameraTransform = new CameraTransformer

    cameraTransform.setTranslate(x, y, z)
    cameraTransform.getChildren.add(camera)
    camera.setNearClip(nearClip)
    camera.setFarClip(farClip)

    camera.setTranslateZ(transZ)
    camera.setFieldOfView(fieldView)
    cameraTransform.ry.setAngle(ryAngle)
    cameraTransform.rx.setAngle(rxAngle)
    worldRoot.getChildren.add(cameraTransform)
    cameraTransform
  }

  def createSubscene(worldRoot: Group, width: Double, height: Double, color: Color, camera: PerspectiveCamera): SubScene = {
    val subScene = new SubScene(worldRoot, width, height, true, SceneAntialiasing.BALANCED)
    subScene.setFill(color)
    subScene.setCamera(camera)
    subScene
  }

  def createCameraView(subScene: SubScene, fitWidth: Int, fitHeight: Int, rxAngle: Int, setZ: Int, setY: Int, transZ: Int): CameraView = {

    val cameraView = new CameraView(subScene)

    cameraView.setFirstPersonNavigationEabled(true)
    cameraView.setFitWidth(fitWidth)
    cameraView.setFitHeight(fitHeight)
    cameraView.getRx.setAngle(rxAngle)
    cameraView.getT.setZ(setZ)
    cameraView.getT.setY(setY)
    cameraView.getCamera.setTranslateZ(transZ)
    cameraView
  }

  def createRoot(subScene: SubScene, cameraView: CameraView): StackPane = {
    val root = new StackPane(subScene, cameraView)
    subScene.widthProperty.bind(root.widthProperty)
    subScene.heightProperty.bind(root.heightProperty)
    root
  }

  // ---- -------- --- -------SCENE COMPLETE ------ -- ---------- ----------- -----//
  def createScene(worldRoot: Group, width: Double, height: Double): (Scene,CameraView) = {

    //SUBSCENE
    val camera = createCamera()
    val subJohnCena = createSubscene(worldRoot, 800, 600, Color.WHITE, camera)
    val view = createCameraView(subJohnCena, 350, 225, -45, -100, -500, -50)
    view.startViewing

    val root = createRoot(subJohnCena, view)

    createCameraTransformer(worldRoot, 0, 0, 0, 0.1, 10000.0, -500, 20, -45.0, -45.0, camera)

    //SCENE
    val scenario = new Scene(root, width, height, true, SceneAntialiasing.BALANCED)

    (scenario,view)
  }


}