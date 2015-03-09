# OGoWS

This exposes some OGo(J) functionality as a JSON WebService API. I created this
to provide a backend to some ExtJS 5 toy application.

## Setup

This needs a running OGo database. The database connection is configured in the
Defaults.properties file. Like:

    DB               = jdbc:postgresql://127.0.0.1/OGo?user=OGo&password=OGo
    LSAttachmentPath = /var/lib/ogo/documents

Since the service is going to be accessed from a different webserver origin
(the website might be hosted at http://localhost/MyApp, but the WebService is
running as http://localhost:8181/) all the CORS stuff need to be configured.
Sample:

    WOAllowOrigins       = http://localhost:8181,http://127.0.01:8181,http://localhost
    WOAllowOriginHeaders = Origin, X-Requested-With, PROXY, STORE, Accept, X-OGo, Content-Type

All quite clumsy ;-) And it might still not work quite right (the browser might
prompt you again for auth credentials - depends on the browser).

When running the app, you can check whether the service works via:

    http://127.0.0.1:8181/OGoWS/

