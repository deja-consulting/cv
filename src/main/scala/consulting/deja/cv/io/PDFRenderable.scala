package consulting.deja.cv.io

/** Extends [[HTMLAppendable]] by a couple of PDF-specific properties. */
trait PDFRenderable {
  /** Root appendable for the main HTML document. */
  def root:HTMLAppendable
}
