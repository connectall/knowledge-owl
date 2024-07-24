# knowledge-owl
Universal Adapter for KnowledgeOwl

Entities supported:
* Article
* Article-Version

Notes:
The Article entity only fetches the current version of an article.
The Article-Version fetches each major or minor version of an article, regardless of whether or not that version has been published.

The available fields for Articles and Article-Versions differ. See https://support.knowledgeowl.com/help/endpoint-reference for details.

# How to create a connection in ConnectALL
1. Create an API token for KnowledgeOwl: https://support.knowledgeowl.com/help/use-api#api-keys
2. In ConnectALL, go to the Connections screen
3. Click "add connection"
4. Choose "KnowledgeOwl" from the list of available connections
5. Use https://app.knowledgeowl.com as the URL
6. Add your API token into the User Name field
7. Add "x" to the password field
8. Click "Save and Update"

# Useful flow filters

*** Note: when a dollar sign ($) is present in a filter, it may need to be encoded as %24

*** Example: status[%24in][]=review instead of status[$in][]=review

Articles object:

* status[$in][]=review       //filter articles that are in the "Review" state

* status[$in][]=published    //filter articles that are in the "Published" state

Article-Versions object:

* ready=true                //filter article versions that are flagged ready for review.

* ready=false               //filter article versions that are not flagged ready for review

* ready[$exists]            //filter article versions where the ready state is either true or false (and not null)

* ready[$ne]=true           //filter article versions that are either false or null

* ready[$ne]=false          //filter article versions that are either true or null
