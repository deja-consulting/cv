# [CV](../README.md) ⮞ [Code guide](../code-guide.md) ⮞ Writing HTML

The codebase contains simple types for writing out the generated HTML code at runtime.
They offer a way to compose HTML in a streaming, typesafe and functional way.

Understanding this is a prerequisite to understanding how HTML templates are used and composed.

## `HTMLAppend`

Simplified excerpt:

```scala
trait HTMLAppend[+Self<:HTMLAppend[Self]] {
  def apply(appendable:HTMLAppendable):Self
  def apply(str:String):Self
  def raw(str:String):Self
  def tagAutoClose(label:String, attributes:(String,String)*):Self
  def tagClose(label:String):Self
  def tagOpen(label:String, attributes:(String,String)*):Self

  def charset:Charset
  def language:Language
}
```

A simple way of appending HTML to some output.

### Important rule

There is one very important rule to assure streaming output and consistency:
For every instance of `HTMLAppend`, only _up to one_ method that returns `Self` may be called.
Once one of these methods has been called, the `HTMLAppend` instance must be regarded as "used up", and furthermore,
 only methods that do not return `Self` may be called on that instance.

However, in the `Self` instance that has been returned, another `Self`-returning method may be called afterwards.

Disregarding that rule may lead to unpredictable results.

### Appending HTML

The `apply` methods can be used to append a plain string (which gets escaped for HTML safety), or appending an instance
 of `HTMLAppendable`, which can append a whole HTML subtree.

Using `raw`, non-escaped text may be appended, with the possible danger of violating XHTML validity.

The `tag...` methods insert (sub-)tags.
* `tagAutoclose` appends a tag with no body, such as `<br />`.
* `tagOpen` starts a new tag, such as `<div>`.
  The returned `Self` appends to the body of the new tag.
* `tagClose` closes the currently open tag, for example `</div>`.
  The returned `Self` appends to the parent `HTMLAppendable`, after the closed tag.
  This means that calls to `tagOpen` and `tagClose` must be balanced.
  Closing a tag at root level (where no tag is open) must be avoided, as the result is unpredictable.

### Output information

The methods `charset` and `language` may be called at any time.
They provide information about the charset of the output, or about the language to write, respectively.

## `HTMLAppendable`

Excerpt:

```scala
trait HTMLAppendable {
  def apply[A<:HTMLAppend[A]](append:A):A
  def ++(that:HTMLAppendable):HTMLAppendable
}
```

Given an instance of `HTMLAppend`, an `HTMLAppendable` can be used to append a certain piece of HTML to it, by calling
 its `apply` method.

`HTMLAppendable` instances can be concatenated by using the `++` operator.

The `HTMLAppendable` companion object offers some utility methods for creating simple instances.

## `CharAppend` and `CharAppendable`

Those two are very similar to `HTMLAppend` and `HTMLAppendable`, except they do not stream out HTML, but a character
 sequence.

The main `HTMLAppend` implementation outputs the HTML code as characters into a given `CharAppend`.

[Next: HTML templates](html-templates.md)
