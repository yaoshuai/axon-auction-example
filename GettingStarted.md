# Introduction #

Explains how to run the example application.

# Details #

The application isn't complete yet in any way, but...

You can run a test method and trace the commands and queries:

  1. Check out all projects from the SVN trunk
  1. Build the projects with Maven using the [auction-root](http://code.google.com/p/axon-auction-example/source/browse/#svn/trunk/auction-root) project
  1. Start the local ActiveMQ, DerbyDB and SMTP Server using [InfrastructureStart](http://code.google.com/p/axon-auction-example/source/browse/trunk/auction-infrastructure/src/main/java/org/fuin/auction/infrastructure/InfrastructureStart.java) class - If you use Eclipse you can use this [launch configuration](http://code.google.com/p/axon-auction-example/source/browse/trunk/auction-infrastructure/Start+Auction+Infrastructure.launch)
  1. Run the [SQL Script](http://code.google.com/p/axon-auction-example/source/browse/trunk/auction-command-server/src/main/sql/create-db.sql) to create the command server tables (The tables for the query server are created automatically).
  1. Start the [Command Server](http://code.google.com/p/axon-auction-example/source/browse/trunk/#trunk/auction-command-server) and [Query Server](http://code.google.com/p/axon-auction-example/source/browse/trunk/#trunk/auction-query-server) web applications (using Tomcat 6 for example)
  1. Run the [Category Test](http://code.google.com/p/axon-auction-example/source/browse/trunk/auction-client-test/src/test/java/org/fuin/auction/client/test/CategoryUseCaseTest.java)
  1. Don't forget to shutdown the local ActiveMQ, DerbyDB and SMTP Server using [InfrastructureStop](http://code.google.com/p/axon-auction-example/source/browse/trunk/auction-infrastructure/src/main/java/org/fuin/auction/infrastructure/InfrastructureStop.java) class when you're finished - If you use Eclipse you can use this [launch configuration](http://code.google.com/p/axon-auction-example/source/browse/trunk/auction-infrastructure/Stop+Auction+Infrastructure.launch)

That's it for now...