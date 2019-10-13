# [CV](../../README.md) ⮞ [Code guide](../../code-guide.md) ⮞ [HTML templates](../html-templates.md) ⮞ Template format

All HTML files are complete and well-formed XHTML files.
They can be viewed in a browser, and edited with any HTML editor.

They must be XHTML because the processor reads them as XML files.

## Declaring templates

Every part of the HTML file that is not especially marked gets ignored.
Template regions are marked with special attributes.

An HTML tag that has the attribute `data-cv-template-name` gets processed as a template.
Every HTML file can contain any number of templates, all with different names.

A template name must be a valid Scala identifier, usually in lower case.

## Omitting the enclosing tag

When the template gets rendered, the tag with its content gets written to the output, except all of its attributes that
 start with `data-cv-`.

The enclosing tag itself can be excluded from rendering by adding the attribute `data-cv-template-extract="child"` to
 the template tag.
This will cause the template to render only the tag's children.

## Template parameters

Templates may have parameters.
Those are declared as a comma-separated list in the attribute `data-cv-template-params`.

Every parameter name must be a valid Scala identifier.
Whitespace in the parameter list gets ignored.

Parameter names are usually written in lower case.

## Dynamic includes

Templates can include any of the following, as part of their body:
* A parameter of the template.
* Another template that has no parameters.
* A phrase (see below).

In all of the above cases, the identifier of the included part must be written in curly braces, for example as
 `{myGreatParameter}`.

Due to the way template code generation works (described below), when including another template, the name must be
 capitalized.
For example, in order to include the template `breadcrumbs`, insert the string `{Breadcrumbs}` into the HTML file.

Includes work both in the body of the template tag, as well as in attribute values.

## Curly brace escape

Due to the special handling of curly braces, any verbatim `{` in the HTML text must be escaped.
This is done through duplication:

In order to insert the literal symbol `{` as HTML text, write it as `{{` instead.

`}` needs no escaping.

## Nested templates

Templates can be nested.
But every template that is nested inside another one becomes its own top-level template.
Its contents do not appear as part of the enclosing template.

[Next: Phrases](phrases.md)
