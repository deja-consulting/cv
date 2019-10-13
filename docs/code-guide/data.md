# [CV](../README.md) ⮞ [Code guide](../code-guide.md) ⮞ Data

The codebase contains a static model of the structures represented in the CV.
This is a purely semantical model, independent of layout, formulation or design.

The data model can be found in the package `package consulting.deja.cv.data`.
This package contains types for modelling certain semantical entities, as well as their static lists and instances for
 the concrete CV.

To be found there, among other things:
* `Station`:
  A career station in professional history, for example a project.
  The lists of all stations, for various use cases, can be found in `Stations`.
* `Skill`:
  An item of knowledge or expertise, used to compile the skills overview of the CV, and for the various stations.
* `Subject`:
  Personal information of the subject whose history gets documented by the CV.

[Next: Main routine](main-routine.md)
