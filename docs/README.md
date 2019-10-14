# Deja Consulting CV

[![Travis CI][CI badge image]][CI badge link]

Scala program that generates and publishes PDFs for the CV of [Deja Consulting].

No word processor is involved with editing the CV, only changing the code in this repository.
Publishing of the rendered files to [the website][Deja Consulting] happens automatically via a cloud-based build
 process.

Static HTML generator with an attached HTML-to-PDF conversion process, running on [Travis CI].

## Contents

* [Tech stack](#tech-stack)
* [Local build](#local-build)
* [Automated publishing](#automated-publishing)
* [Code guide](#code-guide)
* [Trivia](#trivia)

## Tech stack

* The overall **HTML structure** is defined in pure HTML files.

  A minimalistic HTML processor, written in Scala, reads the HTML files on every SBT build.
  It extracts the template parts and generates Scala code from them, which gets called from the main application.

  The HTML processor uses [the standard Scala XML library][Scala-XML].

* **Internationalization** happens the same way.

  Certain parts of the HTML templates define language-specific terms.
  Those can be referenced in the code, or as part of other templates.

* The main application uses the HTML templates to compose the CV as a big HTML file.

* **PDF rendering** is done using [WeasyPrint].

  A dockerized version of WeasyPrint is used, in order to ensure consistent rendering results across all environments.
  The `docker` command that starts the dockerized rendering process is called by the main Scala application.

* **CSS** is generated in the Scala code, using [the ScalaCSS library][ScalaCSS].
  The generated CSS gets inlined into the generated HTML code.

* [Travis CI] is used for **build and publish automation**.

* **Fonts** mostly come from [Google Fonts], downloaded and embedded at runtime.
  In addition, [Font Awesome] is included directly for icons.

## Local build

### Prerequisites

* [SBT].
* [Docker], available as command `docker` in your `$PATH`.
* Optionally, before the first run, execute:
  ```bash
  docker pull pbaumgartner/weasyprint
  ```
  Otherwise, the pulling will be done on the first run, which will then take considerably longer (but only once).

### Local PDF generation

For generating the PDF:

```bash
sbt run
```

For continuous building while working on the source code, use `sbt ~run` instead.
(At least on a Mac, the "Preview" app used for displaying PDFs automatically reloads the PDF when switching to another
 window and back.
This greatly helps for a rapid, almost WYSIWYG way of editing.)

Generated PDFs can be found directly in the `target` folder, as well as the generated HTML files, which get converted
 to PDF.

## Automated publishing

[Travis CI] runs the build and tests upon every commit to the repository.

Two branches are treated in a special way:

* `release/latest`:
  Used for publishing to the main website.

  When pushing to this branch, Travis CI commits and pushes the generated files to
   [the repository deja-consulting/website][Deja Consulting website repo] after a successful build.
  Particulary, it will push to the `release/latest` branch in the website repository.

  This causes another build process over there, which deploys the new version to the main website.

  If the SBT project version of the CV ends in `-SNAPSHOT`, a build on this branch will fail.

  Also, a new [GitHub release][Deja Consulting CV releases] will automatically be created.
  Thus, older CV versions can still be looked up.

* `release/next`:
  Used for publishing to the staging website, for previewing.

  Similar to `release/latest`, except that it will push to the `release/next` branch of the website repository, causing
   a publish to [the staging website][Deja Consulting staging].

  The build will fail if the SBT project version _does not_ end in `-SNAPSHOT`.

  A GitHub release will also be created for builds on this branch, possibly overwriting a previous snapshot release
   with the same snapshot version.

For all other branches, Travis will simply generate the PDF and run the unit tests, but do no publishing.

There is no `master` branch.
Working branches are called similar to the GitHub issue number, for example `issue/4-readme/-`.

The trailing dash is to allow several sub-branches for the same GitHub issue.

## Code guide

For understanding the code in this repository, please follow [this guide](code-guide.md).

## Trivia

Some more subjective notes that did not make it into the other parts of the documentation.

1. [Motivation](#motivation)
2. [Development process](#development-process)
3. [PDF generation technology](#pdf-generation-technology)
4. [CSS generation technology](#css-generation-technology)
5. [Custom HTML template mechanism](#custom-html-template-mechanism)

### Motivation

Before this highly automated process, the CV was created manually, using a word processor and its PDF export function.
This involves a lot of manual editing and dependency on the word processor product.
The overally process is not very portable or scalable.

Then the desire arose to create several variants of the CV, such as a two-page overview and a longer, more detailed
 variant.
Also, those should be produced for the English and German language.

This would lead to at least 4 documents (2 variants × 2 languages), all of which would have to be kept consistent on
 every change.
And this number would rise for every new variant, which means that the manual process does not hold up in this scenario.

Also, automating the generation process enables automatic publishing to the website as a nice side effect.

### Development process

Issues get tracked using the [Deja Consulting project board].

Usually, closed issues get taken off the board after a main release.
So if the board is empty, this means nothing is currently planned, and everything has been done and released.

Issue development branches usually are named after the GitHub issue number and name, such as
 `issue/3-release-process/-`.
As mentioned before, the trailing dash is to allow for other sub-branches for the same issue.

Once an issue is completed, it usually gets squash-merged into `release/next`, subsequently removing the issue branch.

When enough issues have been done, a main release will be done.
The changes from `release/next` get put on `release/latest`, usually by rebasing.
Afterwards, the project version number on `release/latest` gets set to a non-snapshot version, and `relase/next` gets a
 new snapshot version.

### PDF generation technology

Generating PDF programatically is not trivial, considering:
* Results must be identical across all environments.
* The process must be runnable on a cloud-based CI server.
* Fonts should be embeddable, and good rendering hints must be provided.
* Page headers and footers must be customizable.

The premium solution that hits all marks (and more) would be [Prince XML].
It is unclear how Prince would run on CI, but this looks like a solvable problem.
However, Prince XML can be ruled out in this case because it is too expensive.

Initially, this project used [wkhtmltopdf], which is quite easy to use.
However, after some time, it was found that rendering on different environments would lead to inconsistent results,
 depending on the environment's graphics implementation and preinstalled fonts.
This problem became uncontrollable, so another solution had to be found.

There were not many options besides [WeasyPrint].
Thanks to several available Docker containers, reproducible rendering is a given.
WeasyPrint implements even slightly more complex CSS styles very well.
Defining document-wide headers and footers is extremely inconvenient using WeasyPrint, as they have to be defined using
 CSS `@page` definitions.
It is not easily possible to define custom HTML code for headers or footers.
([There is a way][WeasyPrint header and footer trick], but it involves using a custom Python script.
The additional rise in overall complexity appeared unacceptable for this particular project.)

Still, due to the good results, WeasyPrint turned out to be the best available solution.

### CSS generation technology

This project is very much driven by the opinion that _as much as possible should be defined in the code, and with as
 little programming languages as possible_ (preferably just one).

Using plain CSS is not an option, as this would lead to a lot of repetition and make it very hard to apply global design
 changes consistently.

CSS tools such as [Sass] would lead to the introduction of a new programming language.
Also, this would make it impossible to refer to CSS properties from within the code with compile-time safety.
In the beginning, it was unclear if this would be a requirement.

Therefore, the choice fell on [ScalaCSS].
This provides an all-Scala way to define CSS with ease.

This approach, as comfortable as it is, comes with some drawbacks:
* Dependency on a niche library, maintained mainly by one person and a small number of open-source contributors.
* Some quirkiness involving the extensive use of Scala macros, which would even be a hindrance in advancing from Scala
   2.12 to 2.13.
  (ScalaCSS works just fine with Scala 2.13, but the [IDE of choice][IntelliJ IDEA] had severe
   problems when using ScalaCSS on Scala 2.13.)
* No good way of defining a `@page` style with embedded styles like `@bottom-left` or `@top-center`, as necessary for
   doing headers and footers with WeasyPrint.
  The current implementation solves this by applying heavy string manipulation to the generated CSS.
  This works, but feels very hacky and overall does not really improve code quality.
* Inability to define one independent style sheet embedded into the main one, when declared as `object`.
  The inner one will always be included in the outer one.
  The reason is how ScalaCSS macros scan the definitions, so the only way out was to put the second
   stylesheet into a file of its own.
  This is not really bad, but removes some liberty from how the code can be organized.

As can be seen, there are no showstoppers in there.

Still, considering how everything turned out, maybe it would be worth reconsidering Sass at some point.
As everything works just fine for the time being, this does not have great priority though.

### Custom HTML template mechanism

It was a difficult decision writing a custom HTML template processor.

The processor started as an experiment, just to see how much complexity a minimalist template mini-engine would involve.
As the first result came in quick, did not involve much code, and was quite comfortable to use, it made sense to stick
 with the approach.

Quite unexpectedly, after a longer time of working with the custom template mechanism, it turned out that it held up
 very well.
Some of the observed advantages:
* The templates are really, _really_ simple.
  No logic, just plain HTML.
* All the logic is written in Scala, outside of the templates, which leads to a really good separation between models
   and representation.
* All templates are valid HTML files.
  They can be viewed in a browser, checked for W3C conformance, passed to a spell checker, the whole deal.
* HTML files can contain multiple templates.
  This turned out really well, as this keeps related things in the same file.
  Editing the code becomes much easier this way, when switching between multiple files for related, small templates is
   not necessary.
* Using the HTML feature of definition lists for defining internationalizable phrases is convenient.
  Again, the file can be easily viewed in a browser to get a quick overview.
  Other than Java resources, it is especially nice that phrase definitions can contain references to other phrases or
   simple templates.

No other Scala HTML template engine offers the same combination of simplicity, convenience, purity and consistency.
(At least, no engine known to the author.)
This is of course a highly subjective opinion.

Therefore, there are no plans to move to another template engine.

Maybe it would make sense to extract the simple template engine used in this project as a standalone open-source
 project.
There would be other uses for the author as well.
But again, this is not a high-priority consideration.
Maybe it will come one day, maybe not.

[CI badge image]: https://travis-ci.org/deja-consulting/cv.svg
[CI badge link]: https://travis-ci.org/deja-consulting/cv
[Deja Consulting]: https://deja.consulting
[Deja Consulting CV releases]: https://github.com/deja-consulting/cv/releases
[Deja Consulting project board]: https://github.com/orgs/deja-consulting/projects/1
[Deja Consulting staging]: https://staging.deja.consulting
[Deja Consulting website repo]: https://github.com/deja-consulting/website
[Docker]: https://www.docker.com/
[Font Awesome]: https://fontawesome.com/
[Google Fonts]: https://fonts.google.com/
[IntelliJ IDEA]: https://www.jetbrains.com/idea/
[Prince XML]: https://www.princexml.com/
[Sass]: https://sass-lang.com/
[SBT]: https://www.scala-sbt.org/
[ScalaCSS]: https://github.com/japgolly/scalacss
[Scala-XML]: https://github.com/scala/scala-xml
[Travis CI]: https://travis-ci.org/
[WeasyPrint]: https://weasyprint.org/
[WeasyPrint header and footer trick]: https://weasyprint.readthedocs.io/en/latest/tips-tricks.html#include-header-and-footer-of-arbitrary-complexity-in-a-pdf
[wkhtmltopdf]: https://wkhtmltopdf.org/
