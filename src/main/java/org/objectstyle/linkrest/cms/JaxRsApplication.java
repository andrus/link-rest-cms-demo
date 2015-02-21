package org.objectstyle.linkrest.cms;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

import org.apache.cayenne.access.dbsync.CreateIfNoSchemaStrategy;
import org.apache.cayenne.access.dbsync.SchemaUpdateStrategy;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.configuration.server.ServerRuntimeBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.objectstyle.linkrest.cms.derby.DerbyManager;
import org.objectstyle.linkrest.cms.resource.BaseLinkRestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;

/**
 * A JAX-RS "application" class bootstraps Cayenne and LinkRest.
 */
@ApplicationPath("rest")
public class JaxRsApplication extends ResourceConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JaxRsApplication.class);

	private ServerRuntime cayenneRuntime;
	private DerbyManager derbyManager;

	public JaxRsApplication(@Context ServletContext context) {

		// make sure Cayenne and Derby are shutdown properly
		register(new ShutdownListener());

		// init persistence layer...
		this.derbyManager = initDerby();
		this.cayenneRuntime = initCayenne(derbyManager.getUrl(), derbyManager.getDriver());

		// init and bootstrap LinkRest
		LinkRestRuntime lrRuntime = initLinkRest(cayenneRuntime);
		register(lrRuntime.getFeature());

		// expose application REST endpoints
		packages(BaseLinkRestResource.class.getPackage().getName());
	}

	private static LinkRestRuntime initLinkRest(ServerRuntime cayenneRuntime) {
		return new LinkRestBuilder().cayenneRuntime(cayenneRuntime).build();
	}

	private static ServerRuntime initCayenne(String url, String driver) {
		return new ServerRuntimeBuilder()
				// ensure test schema is created...
				.addModule(binder -> binder.bind(SchemaUpdateStrategy.class).to(CreateIfNoSchemaStrategy.class))
				.jdbcDriver(driver).url(url).addConfig("cayenne-project.xml").build();
	}

	private static DerbyManager initDerby() {
		return new DerbyManager();
	}

	class ShutdownListener implements ContainerLifecycleListener {

		@Override
		public void onShutdown(Container container) {
			LOGGER.info("Container shutdown, stopping Cayenne and Derby...");

			if (cayenneRuntime != null) {
				cayenneRuntime.shutdown();
				cayenneRuntime = null;
			}

			if (derbyManager != null) {
				derbyManager.shutdown();
				derbyManager = null;
			}
		}

		@Override
		public void onReload(Container container) {
			// do nothing
			LOGGER.info("Container reloaded, do nothing...");
		}

		@Override
		public void onStartup(Container container) {
			// do nothing
		}
	}

}
