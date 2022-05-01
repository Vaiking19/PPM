import Utils._
import javafx.fxml.FXML
import javafx.scene.control.{Button, ToggleButton}
import javafx.scene.effect.InnerShadow
import javafx.scene.paint.Color



class Controller {

  @FXML
  private var button1: ToggleButton = _

  @FXML
  private var button2: Button = _

//  @FXML
//  private var button3: Checkbox = _
//
//  @FXML
//  private var button4: Checkbox = _

  @FXML
  private var button5: ToggleButton = _

  def onButton1Clicked(): Unit = {
//    textField1.setText("Mouse entered Toggle Button")
//    button1.set
    val shadow  = new InnerShadow()
    shadow.setWidth(50)
    shadow.setHeight(50)
    if(button1.getEffect() == null ) {
    button1.setEffect(shadow)
    button1.setTextFill(Color.WHITE)
    }else{
      button1.setEffect(null)
      button1.setTextFill(Color.BLUE)
    }



  }


}
