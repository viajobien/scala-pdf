import java.net.URLClassLoader
import java.nio.file.{ Paths, Files }
import net.kaliber.pdf.PdfRenderer
import org.qirx.littlespec.Specification
import scala.io.Source

object Test extends Specification {

  val examples = Paths.get("./example")
  val `test.pdf` = examples resolve "test.pdf"

s"""|============================
    |PLEASE CHECK IF THE RENDERED
    |PDF CONTAINS TWO IMAGES AND
    |A CUSTOM FONT.
    |
    |${`test.pdf`}
    |============================""".stripMargin - {
     val classLoader = new URLClassLoader(Array(examples.toUri.toURL))

     val body = {
       val html = classLoader.getResourceAsStream("pdf.html")
       Source.fromInputStream(html).mkString
     }

     val pdf = {
       val renderer = new PdfRenderer(classLoader)
       renderer.toBytes(body)
     }

     Files write (`test.pdf`, pdf)

     todo
   }
}
