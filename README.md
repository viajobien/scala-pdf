# Scala PDF module

This module helps in generating PDF documents dynamically from your Scala application.
It simply renders your HTML- and CSS-based templates to PDF.

It is based on the Flying Saucer library, which in turn uses iText for PDF generation.

## Installation
```
libraryDependencies += "com.viajobien" %% "scala-pdf" % "0.1.0"
```

## Usage

```
val body = /* some xhtml string */
val renderer = new PdfRenderer(classLoader)
val bytes = renderer.toBytes(body)
```

Please see the `PdfRendererSpec.scala` file for an example of loading.
