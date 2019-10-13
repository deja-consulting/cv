# [CV](../README.md) ⮞ [Code guide](../code-guide.md) ⮞ Main routine

The `Main` object extends `IOApp`.
It gets called when the PDF generation gets triggered, for example via `sbt run`.

Its main function is to call `IO.renderPDF`, for the various CV variants and languages.

A CV variant is represented by the `Variant` trait.
This is a certain edition of the CV, describing how exactly the data gets filtered and composed.
An example can be found in `Variant.StandardOverview`, which leads the reader to the other types used for compiling the
 "standard overview" edition of the CV.

Every variant can be rendered for all the languages, which means that `Variant` itself is language-independent.

`IO.renderPDF` involves writing the HTML file to the `target` folder, and then calling the dockerized HTML-to-PDF
 conversion process.
The implementation used at runtime is `FileIO`, so refer to that class for the code that does the job.

CSS generation is pretty elaborate.
The variants call their specific types for generating their CSS code.
A good example is `StandardOverviewCSS`, used for the standard overview CSS.
When scrolling down in this type's source, the reader will find that all fonts, colors and measurements get defined in
 enclosed scopes towards the end of the file, and all concrete CSS definitions refer to those.
This way, it is easy to make global changes, such as changing the color scheme, exchanging fonts used for certain
 purposes, or changing the various sizes and measurements.

[Back to: Code guide](../code-guide.md)
