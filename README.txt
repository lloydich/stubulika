
Send a POST with a header of Content-Type application-json and a body:

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