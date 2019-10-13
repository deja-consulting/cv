# [CV](../README.md) ⮞ [Code guide](../code-guide.md) ⮞ HTML templates

All of the HTML templates can be found under `src/main/resources/html-templates`.

The template processor code is under `project`, which means it gets compiled and executed in SBT build scope.
It is not available in main scope.

The HTML templating process described here is minimalistic and allows for no logic in the HTML files.
Every piece of logic, even simple iterations, must be written as part of the Scala code.

Processing the templates before main scope compilation guarantees compile-time safety:
If something is amiss, there will be a compile error or warning.

1. [Template format](html-templates/template-format.md): How to read and define HTML templates.
2. [Phrases](html-templates/phrases.md): How to define multi-lingual phrases in HTML.
3. [Template processing](html-templates/template-processing.md): How templates get processed into auto-generated code.

[Next: Data](data.md)
