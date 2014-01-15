package com.xenoage.zong.webserver;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.fatal;
import static com.xenoage.utils.log.Report.remark;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.sound.midi.MidiUnavailableException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.gson.Gson;
import com.xenoage.utils.Parser;
import com.xenoage.utils.PlatformUtils;
import com.xenoage.utils.error.BasicErrorProcessing;
import com.xenoage.utils.error.Err;
import com.xenoage.utils.jse.JsePlatformUtils;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.log.DesktopLogProcessing;
import com.xenoage.utils.jse.settings.Settings;
import com.xenoage.utils.log.Log;
import com.xenoage.zong.Zong;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.webserver.init.DBInit;
import com.xenoage.zong.webserver.servlet.ActionServlet;

/**
 * Main class of the Webviewer server.
 * 
 * @author Andreas Wenger
 */
public class Server {

	public static final String projectName = "webserver";
	public static final String filename = Zong.filename + "/" + projectName + "/";
	public static final String webPath = "data/web/";

	public static Server instance = null;
	public static int port = 8080;

	private org.eclipse.jetty.server.Server server;
	private List<Handler> handlers = new ArrayList<Handler>();
	private Connection dbConnection = null;
	private Gson gson = null;


	public static void main(String... args) {
		instance = new Server();
		instance.start();
		try {
			instance.join();
		} catch (InterruptedException ex) {
			Log.log(remark("Server interrupted."));
		}
	}

	public Server() {
		//init IO and logging
		PlatformUtils.init(new JsePlatformUtils());
		DesktopIO.init(filename);
		Log.init(new DesktopLogProcessing(Zong.getNameAndVersion(projectName)));
		Err.init(new BasicErrorProcessing());

		//load settings
		Settings.setErrorProcessing(null);
		Settings.getInstance();

		//prepare gson
		gson = new Gson();

		//open the database
		try {
			openDBConnection();
		} catch (Exception ex) {
			handle(fatal("Could not open DB connection", ex));
		}

		//init audio engine
		try {
			SynthManager.init(true);
		} catch (MidiUnavailableException ex) {
			handle(fatal("Could not init audio engine", ex));
		}

		//server
		port = Parser.parseInt(getSetting("port"), 8080);
		this.server = new org.eclipse.jetty.server.Server(port);
		//TODO: set threadpool size - this.server.setThreadPool(new QueuedThreadPool(10));
		ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletHandler.setContextPath("/action");
		servletHandler.addServlet(new ServletHolder(new ActionServlet()), "/");
		handlers.add(servletHandler);
		//enable webserver
		enableWebServer();
	}

	/**
	 * Starts the server.
	 */
	public void start() {
		try {
			//Handler setzen
			HandlerList handlerList = new HandlerList();
			for (Handler handler : handlers)
				handlerList.addHandler(handler);
			handlerList.addHandler(new DefaultHandler());
			server.setHandler(handlerList);
			//Server starten
			server.start();
			String log = "Server started. Listening on port " + port + ".";
			System.out.println(log);
			log(remark(log));
		} catch (Exception ex) {
			handle(fatal(ex));
		}
	}

	/**
	 * Blocks the server's thread as long as the server is running.
	 */
	public void join()
		throws InterruptedException {
		server.join();
	}

	/**
	 * Stops the server.
	 */
	public void stop() {
		try {
			server.stop();

		} catch (Exception ex) {
			handle(fatal(ex));
		}
	}

	private void enableWebServer() {
		if (server.isRunning())
			throw new IllegalStateException("Webserver can not be enabled while running");
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setWelcomeFiles(new String[] { "index.html" });
		resourceHandler.setResourceBase(webPath);
		resourceHandler.setCacheControl("max-age=600,public"); //cache 10 minutes
		ContextHandler contentHandler = new ContextHandler();
		contentHandler.setContextPath("/");
		contentHandler.setResourceBase(".");
		contentHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
		contentHandler.setHandler(resourceHandler);
		handlers.add(contentHandler);
	}

	private void openDBConnection()
		throws Exception {
		try {
			//new File("data/db/" + getSetting("dbdatabase") + ".h2.db").delete(); //TEST
			//new database?
			boolean newDB = !(new File("data/db/" + getSetting("dbdatabase") + ".h2.db").exists());
			//open database
			Log.log(remark("Open connection to database..."));
			Class.forName("org.h2.Driver");
			dbConnection = DriverManager.getConnection("jdbc:h2:data/db/" + getSetting("dbdatabase"),
				getSetting("dbuser"), getSetting("dbpassword"));
			Log.log(remark("Connection established"));
			//init
			if (newDB)
				DBInit.initDatabase(dbConnection);
		} catch (ClassNotFoundException ex) {
			handle(fatal("Could not find JDBC driver for MySQL", ex));
			throw new ServletException("See log file");
		} catch (SQLException ex) {
			handle(fatal("SQLException: " + ex.getMessage(), ex));
			throw new ServletException("See log file");
		}
	}

	/**
	 * Gets the database connection. If it is closed, it is re-established.
	 */
	public Connection getDBConnection() {
		try {
			if (dbConnection.isClosed()) {
				Log.log(remark("Database connection is closed. Trying to reopen the connection..."));
				openDBConnection();
			}
		} catch (Exception ex) {
			handle(fatal("Exception: " + ex.getMessage(), ex));
			throw new RuntimeException(ex);
		}
		return dbConnection;
	}

	public Gson getGson() {
		return gson;
	}

	public String getSetting(String key) {
		String ret = Settings.getInstance().getSetting(key, "webviewer");
		if (ret == null)
			throw new RuntimeException("Missing config value for: " + key);
		return ret;
	}

}
