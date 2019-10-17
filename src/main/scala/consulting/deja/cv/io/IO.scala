package consulting.deja.cv.io

import java.nio.charset.Charset

import consulting.deja.cv.language.Language

/** Functional I/O implementation.
 *
 * Out of all of the methods that return `Self`, only one may be called on any given instance. Afterwards, the instance
 * counts as used. Further appending methods need to be called on its return value. */
trait IO[+Self<:IO[Self]] {
  this:Self=>

  /** Writes the HTML file and renders it as PDF. Missing directories in between the given file paths are created. */
  def renderPDF(contents:CharAppendable, htmlFilesPrefix:String, pdfFile:String):Self

  /** Write the output of the given [[CharAppendable]] to the file at the given path. Missing directories in between
   * are created. */
  def write(appendable:CharAppendable, file:String, charset:Charset):Self

  /** Write the output of the given [[HTMLAppendable]] to the file at the given path. Missing directories in between
   * are created. */
  def write(appendable:HTMLAppendable, file:String, language:Language, charset:Charset):Self =
    write(appendable toCharAppendableFor language, file, charset)
}
