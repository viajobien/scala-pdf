package com.viajobien.scalapdf

import java.net.URLClassLoader
import java.nio.file.{ Files, Path, Paths }

import org.scalatest.WordSpecLike
import org.xhtmlrenderer.pdf.ITextRenderer

import scala.io.Source

class PdfRendererSpec extends WordSpecLike {

  val examples: Path = Paths.get("./example")
  val `test.pdf`: Path = examples resolve "test.pdf"

  // Font should be rendered from anywhere
  val rendererWithFonts: ITextRenderer = new ITextRenderer
  rendererWithFonts.getFontResolver.addFontDirectory(examples.toAbsolutePath.toString + "/fonts", true)

  s"""|============================
    |PLEASE CHECK IF THE RENDERED
    |PDF CONTAINS TWO IMAGES AND
    |A CUSTOM FONT.
    |
    |${`test.pdf`}
    |============================""".stripMargin in {
    val classLoader = new URLClassLoader(Array(examples.toUri.toURL))

    val body = {
      val html = classLoader.getResourceAsStream("pdf.html")
      Source.fromInputStream(html).mkString
    }

    val pdf = {
      val renderer = new PdfRenderer(classLoader, rendererWithFonts)
      renderer.toBytes(body)
    }

    // Files write (`test.pdf`, pdf)

    // Todo Write proper tests please

  }
}
