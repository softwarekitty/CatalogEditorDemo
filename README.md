#CatalogEditorDemo#
=================

An xml-based University catalog editor that focuses on editing versions of Course Descriptions

- Catalog Editor Demo
- Professor Gadia
- Com S 490
- Carl Chapman


##Rationale##
Editing a text document by yourself is fairly straightforward, but creating a public, centrally important document like a University Catalog in an institution with many separate entities all having input is more complex.  The main concerns revolve around communication and coordination.  Is emailing documents back and forth an efficient solution?  Professor Gadia had a better idea that revolves around a central Catalog document organized using XML.  

##Summary of Features##
Unlike a relational database, modifications to the way the data is organized in an xml document can be done with minimal disruption.  Because it uses xml to store everything, the Catalog document is queriable using Xpath from within the application.  This is especially useful for administrating agents that need to prepare reports about the Catalog.  A user can save queries they find useful and see what queries others have saved.  A set of 'global' queries useful to people unfamiliar with Xpath is maintained by administrators.  The queriable nature of the document can also simplify the work of services such as the iastate.edu website, schedule planning software and any service that periodically needs up-to-date Catalog related information.

Permissions management is built in so that a user can only modify areas of the document that they have been designated an editor of.   The data is highly available, so that coordinating with someone else is as simple as looking at his or her latest version.  Communication is as simple as writing a comment on that version.  The GUI has various additional human-interaction-oriented sweeteners that are intended to make it convenient to use.
 
See the wiki for a [#visual tour#](www.google.com) of the product.

