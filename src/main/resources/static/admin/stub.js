var converter = new Showdown.converter();

var replacer= function (name, val) {
     if ( val === '' ) { //
        return undefined; // remove from result
    } else {
        return val; // return as is
    }
};

var StubForm = React.createClass({displayName: "StubForm",
    handleSubmit: function (e) {
        e.preventDefault();
        var requestUrl = this.refs.requestUrl.getDOMNode().value.trim();
        var requestHeaders = this.refs.requestHeaders.getDOMNode().value.trim();
        var requestBody = this.refs.requestBody.getDOMNode().value.trim();
        var requestMethod = this.refs.requestMethod.getDOMNode().value.trim();
        var responseStatusCode = this.refs.responseStatusCode.getDOMNode().value.trim();
        var responseHeaders = this.refs.responseHeaders.getDOMNode().value.trim();
        var responseBody = this.refs.responseBody.getDOMNode().value.trim();
        if (!requestUrl || !requestMethod || !responseStatusCode) {
            return;
        }
        //  data:[{"request":{"url":"test?foo=bar","method":"GET"},"response":{"status":200,"body":"{\"foo\":\"bar\"}"}}]
        this.props.onStubSubmit({request:{url: requestUrl, method: requestMethod, body: requestBody, headers: requestHeaders},
            response:{status : responseStatusCode, body: responseBody, headers: responseHeaders} });
        this.refs.requestUrl.getDOMNode().value = '';
        this.refs.requestMethod.getDOMNode().value = '';
        this.refs.requestHeaders.getDOMNode().value = '';
        this.refs.requestBody.getDOMNode().value = '';
        this.refs.responseStatusCode.getDOMNode().value = '';
        this.refs.responseHeaders.getDOMNode().value = '';
        this.refs.responseBody.getDOMNode().value = '';
    },
    render: function () {
        return (
          React.createElement("div",{className:"container"},
            React.createElement("form", {className: "form-horizontal", onSubmit: this.handleSubmit},
                React.createElement("h4", null,"Request"),
              React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3 control-label"},"URL"),
                         React.createElement("div", {className: "col-sm-9"},
                         React.createElement("input", {type: "text", className: "form-control", placeholder: "URL", ref: "requestUrl"})
              ))),

              React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3  control-label"},"Method"),
                    React.createElement("div", {className: "col-sm-5"},
                        React.createElement("input", {type: "text", className: "form-control", placeholder: "Method", ref: "requestMethod"})
              ))),

              React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3  control-label"},"Body"),
                    React.createElement("div", {className: "col-sm-9"},
                        React.createElement("textarea", {className: "form-control", rows: 4, placeholder: "Body", ref: "requestBody"})
              ))),

              React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3  control-label"},"Headers"),
                    React.createElement("div", {className: "col-sm-9"},
                        React.createElement("input", {type: "text", className: "form-control", placeholder: "Headers", ref: "requestHeaders"})
              ))),

              React.createElement("h4", null,"Response"),


                React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3  control-label"},"HTTP Status Code"),
                    React.createElement("div", {className: "col-sm-2"},
                        React.createElement("input", {type: "intNumber", min:100, max:599, className: "form-control", ref: "responseStatusCode"})
              ))),

              React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3  control-label"},"Body"),
                    React.createElement("div", {className: "col-sm-9"},
                        React.createElement("textarea", {className: "form-control", rows:4, placeholder: "Body", ref: "responseBody"})
              ))),

              React.createElement("div", {className: "row"},
                React.createElement("div", {className: "form-group"},
                    React.createElement("label", {className: "col-sm-3  control-label"},"Headers"),
                    React.createElement("div", {className: "col-sm-9"},
                        React.createElement("input", {type: "text", className: "form-control", placeholder: "Headers", ref: "responseHeaders"})
                ))),

              React.createElement("div", {className: "row"},
                    React.createElement("button", {type: "submit", className:"btn btn-primary"},"Add new Stub")
                )
            )
          )
        );
    }

});

var Stub = React.createClass({displayName: "Stub",
    render: function () {
        console.log("render() this.props:"+this.props);
        console.log("render() this.props.children:"+this.props.children);
        return (
            React.createElement("div", {className: "stub"},
                React.createElement("h2", null, this.props.requestUrl +" "+ this.props.requestMethod),
                React.createElement("span", null, JSON.stringify(this.props.stubStr,replacer,4)),
                React.createElement("hr", null)
            )
        );
    }
});
var StubList = React.createClass({displayName: "StubList",
    render: function () {
        var stubNodes = this.props.data.map(function (stub, index) {
            return (
                React.createElement(Stub, {requestUrl: stub.request.url,  requestMethod: stub.request.method,
                    requestHeaders: stub.request.headers,requestBody: stub.request.body,
                    responseStatusCode: stub.response.status,  responseHeaders: stub.response.headers,
                    responseBody: stub.response.body, stubStr: stub, key: index} )
            );
        });
        return (
            React.createElement("div", {className: "stubList"},
                stubNodes
            )
        );
    }
});

var StubBox = React.createClass({displayName: "StubBox",


handleStubSubmit: function (stub) {
        var stubs = this.state.data;
        stubs.push(stub);
        console.log("stubs:"+stubs);
        console.log("JSON.stringify(stub):"+JSON.stringify(stub,replacer,4));
        this.setState({data: stubs}, function () {
            $.ajax({
                url: this.props.url,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                type: 'POST',
                data: JSON.stringify(stub,replacer,4),
                success: function (data) {
                    console.log("handleStubSubmit() data:"+data);
                    this.setState({data: stubs});
                }.bind(this),
                error: function (xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }.bind(this)
            });
        });
    },
    loadStubsFromServer: function () {
        $.ajax({
            url: this.props.url,
            dataType: 'json',
            success: function (data) {
                console.log("loadStubsFromServer() data:"+data);
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },
    getInitialState: function () {
        return {data: this.props.data};
    },
    componentDidMount: function () {
        this.loadStubsFromServer();
        setInterval(this.loadStubsFromServer, this.props.pollInterval);
    },
    render: function () {
        return (
            React.createElement("div", {className: "stubBox"},
                React.createElement("h1", null, "Stubulika"),
                React.createElement(StubList, {data: this.state.data}),
                React.createElement(StubForm, {onStubSubmit: this.handleStubSubmit})
            )
        );
    }
});

var renderClient = function (stubs) {
    var data = stubs || [];
    React.render(
        React.createElement(StubBox, {data: data, url: "/admin", pollInterval: 5000}),
        document.getElementById("content")
    );
};

var renderServer = function (stubs) {
    var data = Java.from(stubs);
    return React.renderToString(
        React.createElement(StubBox, {data: data, url: "/admin", pollInterval: 5000})
    );
};