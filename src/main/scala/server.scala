import java.io.{DataInputStream, DataOutputStream}
import java.net.ServerSocket
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
object server extends App {
  val isbnname = Map("9780399588174"->"Born a Crime : Stories from a South African Childhood", "9789839820652"->"Reimagining Malaysia", "9789672464181"->"My Story: Justice in the Wilderness")
  val isbnprice = Map("9780399588174"->"141.47", "9789839820652"->"25.00", "9789672464181"->"85.00")
  // Register service on port 9000
  val server = new ServerSocket(9000, 100)
  while(true) {
    // Register service on port 1234
    val socket = server.accept()
    Future {
      //store local socket references for processing
      val client = socket
      try {
        // Get a communication stream associated with the socket
        val is = new DataInputStream(client.getInputStream)
        // Get a communication stream associated with the socket
        // Read from input stream
        var line: String = is.readLine()
        val bookname = isbnname(line)
        val bookprice = isbnprice(line)
        println(s"The book's name is $bookname, it is priced at RM$bookprice")
        val os = new DataOutputStream(client.getOutputStream)
        os.writeBytes("Hi")
        println(s"Client connected to check price of ISBN number $line")
        is.close()
        os.close()

      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        // Close the connection, but not the server socket
      }
    }
  }
}

