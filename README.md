OGoWebService
=============

OGoWS exposes some OGo/J functionality as a JSON WebService API. Created to
provide a backend to some ExtJS 5 toy application. Included, see YoYoGo below.

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

To run the service, start the main func in org.opengroupware.ws.OGoWS.

When running the app, you can check whether the service works via:

    http://127.0.0.1:8181/OGoWS/

## YoYoGo

You can find a tiny ExtJS 5.1 application in the YoYoGo folder. Just a proof
of concept on how to do this stuff.

Looks like this:

![](http://i.imgur.com/QG91eEO.png)

Don't you love the beauty of ExtJS? ...

###Why?!

Just wanted to try out ExtJS. If you are one of the five people still using
OGo and you would like to write an ExtJS frontend for it - this is a good place
to start ;-)

###Contact

[@helje5](http://twitter.com/helje5) | helge@alwaysrightinstitute.com

![](http://www.alwaysrightinstitute.com/ARI.png)
