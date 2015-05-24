 Inspired by stub.by this is a stub application for serving canned responses based on different requests and exposes a REST API for administering creating
 stubbed requests/responses.   
 Written in spring boot / java 8, it has an embeded server and can be started up if you have Java 8 on your path using the following
 command from within the checked out project folder:

  **./gradlew build  && java -jar build/libs/stubulika-0.1.0.jar**

  1.   To create a stub request/response POST a json body to /admin on your server instance with a header of Content-Type application-json and a JSON body constituting your request/response (see examples below) to **admin/** . A **body** requires a request & response. A request requires a url & method whilst a response requires a status. Everything else is optional.
  2.   Retrieval of a stubResponse requires you to send the stubRequest as an actual request to the url that was saved at the admin endpoint. If you send a stubRequest POST which matches url & method, this will overwrite the existing stubRequest & stubResponse. Headers are matched if the saved stubRequest collection of headers is a subset of the actual request resulting in a stubbed Response being returned.



###Example requests/responses:

---------------------------------------
####GET XML
```json
{ 
   "request":{
      "url":"test",
      "method":"GET"
   },
   "response":{
      "status":200,
      "headers":{
         "Content-type":[
            "application/xml"
         ]
      },
      "body":"<xml><to>You</to><from>Me</from><heading>Message</heading><body>This is some xml</body></xml>"
   }
} 
```

---------------------------------------
####GET JSON
```json
{ 
   "request":{
      "url":"test",
      "method":"GET"
   },
   "response":{
      "status":200,
      "headers":{
         "Content-type":[
            "application/json"
         ]
      },
      "body":"{\"foo\":\"bar\"}"
   }
}
```

---------------------------------------
####POST JSON
```json
{
   "request":{
      "url":"testtext",
      "method":"POST",
      "body":"{\"foo\":\"bar\"}",
      "headers":{
         "Content-type":[
            "application/json"
         ]
      }
   },
   "response":{
      "status":202,
      "body":"Accepted"
   }
}
```