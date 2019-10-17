package consulting.deja.cv.io

import java.io._
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.FileSystems

import scala.language.postfixOps

/** [[IO]] implementation based on the Java I/O API. */
object FileIO extends FileIO {
  private final case class WriterCharAppend(out:Writer, charset:Charset) extends CharAppend[WriterCharAppend] {
    override def append(ch:Char) = {out.append(ch); this}
    override def append(str:String) = {out.append(str); this}
    override def append(str:String, startIndex:Int, endIndex:Int) = {out.append(str, startIndex, endIndex); this}
    override def raw(str:String) = {out.append(str); this}
  }

  private def makeDirsForFile(file:String):Unit = Option(new File(file).getParentFile).foreach(_.mkdirs)
}
class FileIO private() extends IO[FileIO] {
  import FileIO._


  override def renderPDF(contents:CharAppendable, htmlFilesPrefix:String, pdfFile:String):FileIO = {
    val mainHTMLFile = s"$htmlFilesPrefix.html"
    write(contents, mainHTMLFile, UTF_8)
    makeDirsForFile(pdfFile)
    import scala.sys.process._
    Seq("docker", "run", "--rm", "-v", s"${FileSystems.getDefault.getPath(".").toAbsolutePath}:/data",
      "pbaumgartner/weasyprint", mainHTMLFile, pdfFile).! match {
      case 0 => this
      case nonZero => sys.error(s"Docker process exited with $nonZero.")
    }
  }

  def write(appendable:CharAppendable, file:String, charset:Charset):FileIO = {
    makeDirsForFile(file)
    val outputStream = new FileOutputStream(file)
    try {
      val writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset))
      appendable(WriterCharAppend(writer, charset))
      writer.flush()
      this
    }
    finally {outputStream.close()}
  }
}
