import clientApp.{client, stage}
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, TextField}
import scalafxml.core.macros.sfxml
import scalafxml.core.{FXMLLoader, FXMLView, NoDependencyResolver}

import java.io.{DataInputStream, DataOutputStream}
import java.net.Socket

@sfxml
class controller(val inputnumber: TextField, val checkbutton: Button) {

  def checkPrice() = {
    try{
      val os = new DataOutputStream(client.getOutputStream)
      // write to server a string

      os.writeBytes(inputnumber.text.value)
      val is = new DataInputStream(client.getInputStream)
      var returntext:String = is.readLine()
        //read from server a string
      println(client.isConnected)
      //printing output
      new Alert(AlertType.Information) {
        initOwner(stage)
        title = "ISBN Check Result"
        headerText = "Your ISBN check has returned!"
        contentText = returntext
      }.showAndWait()
      is.close()
      os.close()
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      client.close()
    }

  }
}

object clientApp extends JFXApp3{
  var client: Socket = null
  override def start(): Unit = {

    try {
      //Open your connection to a server, at port 9000
      client = new Socket("127.0.0.1", 9000)
      // Get an input file handle from the socket
      client.setKeepAlive(true)
      val loader = new FXMLLoader(getClass.getResource("client.fxml"),
        NoDependencyResolver
      )
      loader.load()
      val root = FXMLView(getClass.getResource("client.fxml"), NoDependencyResolver)
      stage = new JFXApp3.PrimaryStage() {
        title = "ISBN Price Checker"
        scene = new Scene()
        scene.value.setRoot(root)
      }
    }


  }}
