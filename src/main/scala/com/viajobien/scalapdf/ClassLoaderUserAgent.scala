package com.viajobien.scalapdf

import org.xhtmlrenderer.layout.SharedContext
import org.xhtmlrenderer.pdf.ITextOutputDevice
import org.xhtmlrenderer.pdf.ITextUserAgent

class ClassLoaderUserAgent(
    outputDevice: ITextOutputDevice,
    classLoader: ClassLoader,
    sharedContext: SharedContext) extends ITextUserAgent(outputDevice) {

  setSharedContext(sharedContext)

  override protected def resolveAndOpenStream(uri: String) =
    Option(classLoader getResourceAsStream uri) getOrElse super.resolveAndOpenStream(uri)

  override protected def resolveURI(uri: String): String = uri
}
