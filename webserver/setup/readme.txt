


OBSOLETE - OLD INFORMATION
was valid when deployed in Glassfish - but now Jetty is embedded
TODO: update this doc






To deploy the server,

1) copy the lib directory of the compiled viewer project into WebContent/WEB-INF/
2) compile the project in Eclipse and export it as a WAR
3) copy the data folders from the projects "shared" and "viewer" and the contents of the data subfolder of this directory onto the server in directory /home/{user}/.zong/webviewer-server
4) Create and init a MySQL database (see database.sql) and update the login information in data/config/tileserver.settings
5) Install this the WAR file in the application server (e.g. GlassFish 3)
6) Read http://yourserver:8080/webviewer/
7) Have fun


FAQ
***

Error on server:
java.lang.NoClassDefFoundError: Could not initialize class sun.awt.X11GraphicsEnvironment
To avoid this error we need to set headless JVM option to true in the server startup scripts.
How to set Headless=true in Glassfish v31 Admin console?
This can be done from the Glassfish administration console.
    * Login to Glassfish Admin console
    * Go to - Configurations - Server Config - JVM Settings - JVM Options TAB - Select Add JVM Option
    * Add "-Djava.awt.headless=true" in the text field.
    * Hit "Save" button.
    * Restart the server.
    
    
    
TODO:

TRIED TO SOLVE THIS BE RE-ESTABLISHING CONNECTION WHEN CLOSED, SEE Server.getDBConnection() - 2011/11/05

Handle this exception, that happens when the server idles for a long time:

Stack trace:
com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: No operations allowed after connection closed.Connection was implicitly closed by the driver.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:406)
	at com.mysql.jdbc.Util.getInstance(Util.java:381)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:985)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:956)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:926)
	at com.mysql.jdbc.ConnectionImpl.throwConnectionClosedException(ConnectionImpl.java:1160)
	at com.mysql.jdbc.ConnectionImpl.checkClosed(ConnectionImpl.java:1147)
	at com.mysql.jdbc.ConnectionImpl.prepareStatement(ConnectionImpl.java:4224)
	at com.mysql.jdbc.ConnectionImpl.prepareStatement(ConnectionImpl.java:4190)
	at com.xenoage.zong.webviewer.server.util.Database.stmt(Database.java:81)
	at com.xenoage.zong.webviewer.server.actions.OpenAction.perform(OpenAction.java:66)
	at com.xenoage.zong.webviewer.server.model.requests.OpenRequest.respond(OpenRequest.java:83)
	at com.xenoage.zong.webviewer.server.Server.doPost(Server.java:120)
	at com.xenoage.zong.webviewer.server.Server.doGet(Server.java:99)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:735)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:848)
	at org.apache.catalina.core.StandardWrapper.service(StandardWrapper.java:1534)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:281)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:175)
	at org.apache.catalina.core.StandardPipeline.doInvoke(StandardPipeline.java:655)
	at org.apache.catalina.core.StandardPipeline.invoke(StandardPipeline.java:595)
	at com.sun.enterprise.web.WebPipeline.invoke(WebPipeline.java:98)
	at com.sun.enterprise.web.PESessionLockingStandardPipeline.invoke(PESessionLockingStandardPipeline.java:91)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:162)
	at org.apache.catalina.connector.CoyoteAdapter.doService(CoyoteAdapter.java:326)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:227)
	at com.sun.enterprise.v3.services.impl.ContainerMapper.service(ContainerMapper.java:170)
	at com.sun.grizzly.http.ProcessorTask.invokeAdapter(ProcessorTask.java:822)
	at com.sun.grizzly.http.ProcessorTask.doProcess(ProcessorTask.java:719)
	at com.sun.grizzly.http.ProcessorTask.process(ProcessorTask.java:1013)
	at com.sun.grizzly.http.DefaultProtocolFilter.execute(DefaultProtocolFilter.java:225)
	at com.sun.grizzly.DefaultProtocolChain.executeProtocolFilter(DefaultProtocolChain.java:137)
	at com.sun.grizzly.DefaultProtocolChain.execute(DefaultProtocolChain.java:104)
	at com.sun.grizzly.DefaultProtocolChain.execute(DefaultProtocolChain.java:90)
	at com.sun.grizzly.http.HttpProtocolChain.execute(HttpProtocolChain.java:79)
	at com.sun.grizzly.ProtocolChainContextTask.doCall(ProtocolChainContextTask.java:54)
	at com.sun.grizzly.SelectionKeyContextTask.call(SelectionKeyContextTask.java:59)
	at com.sun.grizzly.ContextTask.run(ContextTask.java:71)
	at com.sun.grizzly.util.AbstractThreadPool$Worker.doWork(AbstractThreadPool.java:532)
	at com.sun.grizzly.util.AbstractThreadPool$Worker.run(AbstractThreadPool.java:513)
	at java.lang.Thread.run(Thread.java:662)
Caused by: com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: The last packet successfully received from the server was 96,315,836 milliseconds ago.  The last packet sent successfully to the server was 96,315,836 milliseconds ago. is longer than the server configured value of 'wait_timeout'. You should consider either expiring and/or testing connection validity before use in your application, increasing the server configured values for client timeouts, or using the Connector/J connection property 'autoReconnect=true' to avoid this problem.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:406)
	at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:1074)
	at com.mysql.jdbc.MysqlIO.send(MysqlIO.java:3313)
	at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1940)
	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2109)
	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2648)
	at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:2077)
	at com.mysql.jdbc.PreparedStatement.executeQuery(PreparedStatement.java:2228)
	at com.xenoage.zong.webviewer.server.actions.OpenAction.perform(OpenAction.java:68)
	... 30 more
Caused by: java.net.SocketException: Broken pipe
	at java.net.SocketOutputStream.socketWrite0(Native Method)
	at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:92)
	at java.net.SocketOutputStream.write(SocketOutputStream.java:136)
	at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:65)
	at java.io.BufferedOutputStream.flush(BufferedOutputStream.java:123)
	at com.mysql.jdbc.MysqlIO.send(MysqlIO.java:3294)
	... 36 more
