# TerraMobileServer
TerraMobile RESTful Web Service Application


TerraMobileServer is a webservice for exchanging files and was implemented as a software component that allows you to create a service layer for exchanging files with TerraMobile application over the internet.

The 1.0-alpha version, contains only the send function of project files to TerraMobile application that operates on a mobile device. To fulfill this function were implemented two commands:
  - The first to provide a list of existing project files on the server;
  - And the second to send a project file from the existing in the list.

This server has been implemented using the API JAX-WS, which is part of the Java Enterprise Edition specifications, and RESTful as architectural style for web communications, and its operation depends on components that implement this specification. The framework was used Jersey https://jersey.java.net/.

For the implementation of this server is recommended to use an implementation of Servlet container like Apache Tomcat, the Apache Software Foundation, http://tomcat.apache.org/, or JBoss, maintained by Red Hat, http: // www .jboss.org /.

This project use the Maven.

To generate the "WAR file" run: mvn clean install

The WAR file, Web application ARchive, is a format used to publish a Java software,  in compliance the JEE specifications,  on any Java EE container.

To run it is only necessary one instance of the Servlet container.
Copy this file to publication directory, for example, on Apache Tomcat search a directory named "webapps".

Needed steps:
---------------------------------------------------------
 1 - Put the file in the webapps directory.

 2 - Make a directory structure on your server as the following: /home/dados/terramobile/userName/

 3 - Put the Geopackage file inside the directory "userName"


To testing the server, use the following links on one browser.
---------------------------------------------------------
 1 - http://<your server IP or name>/TerraMobileServer/tmserver/projectservices/getlistfiles/userName
 2 - http://<your server IP or name>/TerraMobileServer/tmserver/projectservices/getprojects/userName/<your Geopackage name>.gpkg
 
The first link return the list of the Geopackages published on your TerraMobileServer instance and the second link allows download one package.

To use it on Eclipse, use the maven command:

mvn eclipse:eclipse -Dwtpversion=2.0

This command will create Eclipse config files, them you will be able to import it to eclipse as an existing project.
