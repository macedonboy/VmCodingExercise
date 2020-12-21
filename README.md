# VmCodingExercise

BasicProcessor does the computations
BasicProcessorTest tests the scenarios from CodingExercise.docx

I've assumed:
- For sorting purposes, classes typically implement Comparable. So for the sake of simplicity, SortableFieldEnum uses Comparable.
- For Scenario 3, unclassified and whitespace categories are grouped together.
- Only one each of grouping and sorting as this is pretty normal. If necessary, I could've used Apache commons to nest and chain fields.
- It's not clear how the the filtering constraints are defined, so the Processor will accept a Collection of Predicates
