package consulting.deja.cv.io

import java.nio.charset.Charset

/** Functional and streaming appending of characters to an output.
 *
 * Out of all of the methods that return `Self`, only one may be called on any given instance. Afterwards, the instance
 * counts as used. Further appending methods need to be called on its return value. */
trait CharAppend[+Self<:CharAppend[Self]] {
  /** Append a single character. */
  def append(ch:Char):Self

  /** Append the given string. */
  def append(str:String):Self

  /** Apend the part of the given string that lies beyond the given start index, inclusive, and the given end index,
   * exclusive. */
  def append(str:String, startIndex:Int, endIndex:Int):Self

  /** How the output bytes are encoded. */
  def charset:Charset
}
object CharAppend {
  final class ToString(val charset:Charset) extends CharAppend[ToString] {
    private val buffer = new StringBuilder

    def append(ch:Char):ToString = {buffer append ch; this}
    def append(str:String):ToString = {buffer append str; this}
    def append(str:String, startIndex:Int, endIndex:Int):ToString = {buffer append (str substring (startIndex, endIndex)); this}

    def getString:String = buffer.toString()
  }
}
