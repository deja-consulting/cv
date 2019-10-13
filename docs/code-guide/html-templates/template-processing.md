# [CV](../../README.md) ⮞ [Code guide](../../code-guide.md) ⮞ [HTML templates](../html-templates.md) ⮞ Template processing

Any file under `src/main/resources/html-templates` (or a subdirectory thereof) whose name ends in `.html` gets
 processed.

The processor scans every HTML file for templates and phrases, and auto-generates Scala sources for them.
Those are available to the main Scala scope, such that the Scala code can make use of templates and phrases.

Short version:
Given the following code inside `src/main/resources/html-templates/example/Greet.html`,

```html
<html>
  <body>
    <div data-cv-template-name="everyone" data-cv-template-extract="child">world</div>
    <div data-cv-template-name="greet" data-cv-template-params="greeting, greetee">
      {greeting}, {greetee}!
    </div>
    <dl data-cv-dictionary="English">
      <dd>WelcomeGreeting</dd> <dt>Hello</dt>
    </dl>
  </body>
</html>
```

the following sources will be auto-generated:

```scala
package consulting.deja.cv.template.example

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}
import consulting.deja.cv.template.Phrase._

object GreetTemplates {
  def everyone[A<:HTMLAppend[A]](append:A):A = ??? // left out

  object Everyone extends HTMLAppendable {
    def apply[A<:HTMLAppend[A]](append:A):A = everyone(append)
  }

  def greet[A<:HTMLAppend[A]](greeting:HTMLAppendable, greetee:HTMLAppendable, append:A):A = ??? // left out

  final case class Greet(greeting:HTMLAppendable, greetee:HTMLAppendable) extends HTMLAppendable {
    def apply[A<:HTMLAppend[A]](append:A):A = greet(greeting, greetee, append)
  }
}
```

```scala
package consulting.deja.cv.template

import consulting.deja.cv.io.HTMLAppend
import consulting.deja.cv.language.{Language, TechnicalPhrasesTranslation}

object Dictionary {
  import Phrase._

  def appendWelcomeGreeting[A<:HTMLAppend[A]](append:A):A = ??? // left out
}
```

```scala
package consulting.deja.cv.template

import consulting.deja.cv.io.{HTMLAppend, HTMLAppendable}

sealed trait Phrase extends HTMLAppendable
object Phrase {
  sealed trait LanguageVariant extends Phrase

  case object WelcomeGreeting extends HTMLAppendable {def apply[A<:HTMLAppend[A]](append:A):A = Dictionary.appendWelcomeGreeting(append)}
}
```

This can be used in the main Scala code like so:

```scala
GreetTemplates.greet(Phrase.WelcomeGreeting, GreetTemplates.Everyone, append)
```

Which will, of course, append an HTML `<div>` containing the string "Hello, world!", when executed for the English
 language.

Now follows the long version with more elaboration.

## Code generation for templates

Every template gets converted into _both_ a Scala function and a Scala case class or Scala object.

Both the function and case class (or object) will be put in an enclosing Scala `object` whose name is the same as the
 HTML file name, without the `.html` ending, but with the String `Templates` attached.

The Scala `object` gets put in a package that starts with `consulting.deja.cv.template`, and adds every additional
 sub-path element of the HTML file as a package element.

For example, a template in the file `src/main/resources/html-templates/standardoverview/StandardOverviewTemplate.html`
 will be put in an object with full Scala identifier
 `consulting.deja.cv.template.standardoverview.StandardOverviewTemplates`.

The function is called the same as the template.
The case class or object has the same name capitalized.

For example, a template called `root` gets converted into a function called `root` _and_ a case class or object called
 `Root`.
If a template already has a capitalized name, no function will be created for it.

The auto-generated function always has a signature like this:

```scala
def templateName[A<:HTMLAppend[A]](..., append:A):A
```

`...` gets filled with the template parameters, all of type `HTMLAppendable`.

The auto-generated case class always looks similar to:

```scala
final case class TemplateName(...) extends HTMLAppendable
```

Again, with the parameters instead of `...`.

If the template has no parameters, the case class will be an `object` instead.

The case class or object will simply implement `HTMLAppendable` by calling the function.
And the function, of course, will append the template body to the given `HTMLAppend` instance.

## Code generation for phrases

For every phrase:
* One `append...` method will be generated in `Dictionary`.
  For multi-lingual phrases, this will contain a `match` statement for the language, inserting the appropriate
   language-specific version.
* One `case object` embedded in the `Phrase` object, which implements `HTMLAppendable` by forwarding to
   `Phrases.append...`.

If the phrase is a technical phrase, one method will be added to the auto-generated `TechnicalPhrases` trait.
This must be implemented by the main-scoped trait `TechnicalPhrasesTranslation`.

### Missing translations

When a certain translation is missing for a phrase, the result will be a compile-time warning, because of a
 non-exhaustive `match` statement for the language in the auto-generated code.

[Back to: HTML templates](../html-templates.md)
