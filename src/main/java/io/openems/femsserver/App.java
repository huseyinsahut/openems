package io.openems.femsserver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.openems.femsserver.influx.Influxdb;
import io.openems.femsserver.odoo.Odoo;
import io.openems.femsserver.websocket.Websocket;

public class App {
	private static Logger log = LoggerFactory.getLogger(App.class);

	private static int websocketPort = 8086;

	public static void main(String[] args) throws Exception {
		log.info("FEMS-Server started");

		// Configure everything
		Properties config = getConfig();
		initOdoo(config);
		initInfluxdb(config);
		
		// Start websocket
		Websocket ws = new Websocket(websocketPort);
		ws.start();
	}
	
	private static Properties getConfig() throws IOException {
		Path configLocation = Paths.get("config.properties");
		try (InputStream stream = Files.newInputStream(configLocation)) {
            Properties config = new Properties();
            config.load(stream);
            return config;
        }
	}
	
	private static void initOdoo(Properties config) throws IOException {
		String url = config.getProperty("odoo.url");
		int port = Integer.valueOf(config.getProperty("odoo.port"));
		String database = config.getProperty("odoo.database");
		String username = config.getProperty("odoo.username");
		String password = config.getProperty("odoo.password");
		Odoo.initialize(url, port, database, username, password);
	}
	
	private static void initInfluxdb(Properties config) throws Exception {
		String database = config.getProperty("influx.database");
		String url = config.getProperty("influx.url");
		int port = Integer.valueOf(config.getProperty("influx.port"));
		String username = config.getProperty("influx.username");
		String password = config.getProperty("influx.password");
		Influxdb.initialize(database, url, port, username, password);
	}
}
