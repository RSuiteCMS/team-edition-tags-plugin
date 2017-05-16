# team-edition-tags-plugin
Team Edition Tags plugin

This plugin allows users to assign tags (i.e. keywords) to content.

1) User clicks on content menu item and chooses Metadata > Set tags
2) User enters one or more tags
3) Tags are assigned as layered metadata
4) Tags are displayed as annotations on the content's title via an advisor
5) User can search against the tag using the additional search form

Implementation notes:
1) The LMD field is configured automatically the first time the user attempts to assign a tag for a given MO element 
   (including nonXML and ca).
2) The LMD field name can be set using the RSuite property rsuite.lmd.tag (e.g. rsuite.lmd.tag=RSuiteTag). The value 
   defaults to RSuiteTag. Because a search field for an LMD field can't be driven by the property value, the LMD names 
   "tag" and "RSuiteTag" were configured for the search form, making them the only viable choices.
3) All labels have been been moved into the tags message file to enable translation.

