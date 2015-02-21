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

	public JaxRsApplication(@Context ServletContext context) {

		// init Cayenne runtime to use in-memory derby database.. make sure
		// Cayenne will be shutdown properly
		ServerRuntime cayenneRuntime = new ServerRuntimeBuilder()
				// ensure test schema is created in the derby in-memory db
				.addModule(binder -> binder.bind(SchemaUpdateStrategy.class).to(CreateIfNoSchemaStrategy.class))
				.addConfig("cayenne-project.xml").build();
		register(new CayenneShutdownListener(cayenneRuntime));

		// bootstrap LinkRest with the minimal set of options
		LinkRestRuntime lrRuntime = new LinkRestBuilder().cayenneRuntime(cayenneRuntime).build();

		// register LinkRest as a JAX RS "feature"
		register(lrRuntime.getFeature());

		// declare the location of the application REST endpoints
		packages(BaseLinkRestResource.class.getPackage().getName());
	}

	class CayenneShutdownListener implements ContainerLifecycleListener {

		private ServerRuntime runtime;

		CayenneShutdownListener(ServerRuntime runtime) {
			this.runtime = runtime;
		}

		@Override
		public void onShutdown(Container container) {
			LOGGER.info("Container shutdown, stopping Cayenne runtime...");
			runtime.shutdown();
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
