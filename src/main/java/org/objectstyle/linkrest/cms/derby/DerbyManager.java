package org.objectstyle.linkrest.cms.derby;

import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starts/stops/cleans up embedded Derby DB. This class is used for demo
 * purposes only, and will not be relevant in a real application.
 */
public class DerbyManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DerbyManager.class);

	public static final OutputStream DEV_NULL = new OutputStream() {

		@Override
		public void write(int b) {
		}
	};

	private Path derbyDir;

	public DerbyManager() {
		// ensure no derby.log is generated
		System.setProperty("derby.stream.error.field", DerbyManager.class.getName() + ".DEV_NULL");

		String tmpRoot = System.getProperty("java.io.tmpdir");
		this.derbyDir = Paths.get(tmpRoot, "cmsdemo-derby");
		
		LOGGER.info("Derby DB will be stored under " + derbyDir);
	}

	public String getUrl() {
		return "jdbc:derby:" + derbyDir + "/db;create=true";
	}

	public String getDriver() {
		return EmbeddedDriver.class.getName();
	}

	public void shutdown() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			// the exception is actually expected on shutdown... go figure...
		}

		// TODO: delete 'derbyDir'
	}
}
