package net.kaliber.pdf

import java.io.{ByteArrayOutputStream, StringReader, StringWriter}

import org.w3c.tidy.Tidy
import org.xhtmlrenderer.pdf.ITextRenderer
import org.xhtmlrenderer.resource.XMLResource
import org.xhtmlrenderer.context.StyleReference
import org.xhtmlrenderer.extend.FontResolver

/**
 * Simple wrapper.
 *
 * `PdfRenderer` wraps the the rendering with iText
  * @param classLoader:ClassLoader - The class loader used to resolve assets
  * @param customRenderer:ITextRenderer - custom renderer to do changes on the fly
 */
class PdfRenderer(classLoader: ClassLoader, customRenderer: ITextRenderer = new ITextRenderer) {

  private val renderer = doto(customRenderer) { renderer =>
    // spaghetti with bolognese
    val sharedContext = renderer.getSharedContext
    val userAgent = new ClassLoaderUserAgent(
      renderer.getOutputDevice,
      classLoader,
      sharedContext
    )
    sharedContext.setUserAgentCallback(userAgent)
    sharedContext.setCss(new StyleReference(userAgent))
  }

  private val tidy = doto(new Tidy)(_ setXHTML true)

  def toBytes(body: String): Array[Byte] =
    toStream(body).toByteArray

  def toStream(body: String): ByteArrayOutputStream =
    doto(new ByteArrayOutputStream) { output =>
      try {
        val reader = new StringReader(tidify(body))
        val document = XMLResource.load(reader).getDocument
        renderer.setDocument(document, "")
        renderer.layout
        renderer.createPDF(output)
      } finally output.close()
    }

  private def tidify(body: String) =
    doto(new StringWriter) {
      tidy.parse(new StringReader(body), _)
    }.toString

  private def doto[T](t: T)(code: T => Unit): T = {code(t); t}
}
