# [CV](../../README.md) ⮞ [Code guide](../../code-guide.md) ⮞ [HTML templates](../html-templates.md) ⮞ Phrases

Phrases are elements similar to parameters or templates.
They can be defined in several variants.
Upon rendering, the variant that gets picked may depend on the language.

Just like templates and parameters, phrases are identified by valid Scala identifiers.

Every phrase can be defined in those variants:
* Language-specific, e.g. `English` or `German`.
* `LanguageInvariant`:
  Static contents that is the same for every language.
  If a language-invariant definition is present for a phrase, there may be no language-specific definition.
* `Technical`:
  When a technical phrase gets rendered, some code defined in Scala scope gets called.
  This way, code can be "injected" into the rendering process.

  Of couse, a technical phrase cannot be crossed with a language-specific or language-invariant definition for the same
   phrase.

## Phrase definition

Any HTML file can define any number of phrases.

A list of phrases is always defined as an HTML `<dl>` tag, with the attribute `data-cv-dictionary`.
The attribute value is either a language (such as `English`), or either `LanguageInvariant` or `Technical`.

The `<dl>` tag contains several pairs of `<dt>` and `<dd>` tags, as defined by the HTML standard.
When viewed in the browser, this conveniently gets shown as an HTML _definition list_.

Every `<dd>` tag contains the phrase name, usually in upper case.
It gets immediately followed by a `<dt>` tag that contains the phrase contents.

Every other children of the dictionary `<dl>` tag get ignored.

Just like templates, phrase definitions can also include parameterless templates or other phrases.

When declaring a technical phrase, the contents of the `<dd>` tag gets ignored.
Instead, the phrase becomes a method in an auto-generated Scala trait:

`consulting.deja.cv.template.Dictionary.TechnicalPhrases`

That trait must be implemented by the main scope Scala trait:

`consulting.deja.cv.language.TechnicalPhrasesTranslation`

[Next: Template processing](template-processing.md)
