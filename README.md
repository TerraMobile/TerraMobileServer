# TerraMobileServer
TerraMobile RESTful Web Service Application


TerraMobileServer is a webservice for exchanging files and was implemented as a software component that allows to create a service layer for exchanging files between TerraMobile Android application and TerraMobile plugin over the internet.

TerraMobile Server 1.0-beta contains the functions to send and receive project files to and from TerraMobile application that operates on a mobile device.
To fulfill this function, we implemented three commands:
- The first provides a list of existing project files on the server and depends on their filter parameters to present the list of new project files or the project files from the fieldwork;
- The second sends a project file from the existing in the list;
- The third receive a project from the fieldwork or from the plugin;

This server has been implemented using the API JAX-WS, which is part of the Java Enterprise Edition specifications, and RESTful as architectural style for web communications, and its operation depends on components that implement this specification. The framework was used Jersey https://jersey.java.net/.

For the implementation of this server is recommended to use an implementation of Servlet container like Apache Tomcat, the Apache Software Foundation, http://tomcat.apache.org/, or JBoss, maintained by Red Hat, http: // www .jboss.org /.

This project use the Maven.

To generate the "WAR file" run: mvn clean install

The WAR file, Web application ARchive, is a format used to publish a Java software,  in compliance the JEE specifications,  on any Java EE container.

To run it is only necessary one instance of the Servlet container.
Copy this file to publication directory, for example, on Apache Tomcat search a directory named "webapps".

Needed one step:
---------------------------------------------------------
 1 - Put the file in the webapps directory.

 2 - Make a directory structure on your server as the following: /opt/TerraMobileServer

 3 - Apply write permission to your apache tomcat user in directory created as instructed in the second step.

To testing the server, run junit test.

Build a develop project:
---------------------------------------------------------
To use it on Eclipse, use the maven command:

mvn eclipse:eclipse -Dwtpversion=2.0

This command will create Eclipse config files, them you will be able to import it to eclipse as an existing project.
