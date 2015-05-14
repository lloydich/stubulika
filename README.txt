
 Send a POST with a header of Content-Type application-json and a body (see examples below) to admin/
 A body requires a request & response. A request requires a url & method whilst a response requires a status.
 Everything else is optional.

 Retrieval of a stubResponse requires you to send the stubRequest as an actual request to the url that was saved at the admin endpoint.
 If you send a stubRequest POST which matches url & method this will overwrite the existing stubRequest & stubResponse.
 Headers are matched if the saved stubRequest collection of headers is a subset of the actual request then a stub Response will be returned.

{"request":
{
"url" : "test",
"method": "GET"
},
"response": {
"status":200,
"headers":
   {"Content-type" : ["application/xml"]},
"body": "<xml>\n<to>You</to>\n<from>Me</from>\n<heading>Message</heading>\n<body>This is some xml</body>\n</xml>"
}}

----------------------------------------------------------------------

{"request":
{
"url" : "test",
"method": "GET"
},
"response": {
"status":200,
"headers":
   {"Content-type" : ["application/json"]},
"body": "{\"foo\":\"bar\"}"
}}

----------------------------------------------------------------------

{"request":
{
"url" : "testtext",
"method": "POST" ,
"body": "{\"foo\":\"bar\"}",
"headers":
   {"Content-type" : ["application/json"]},
},
"response": {
"status":202,
"body": "Accepted"
}}