package org.objectstyle.linkrest.cms;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Context;

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
 * A JAX-RS "application" class that demonstrates how to bootstrap LinkRest and
 * Cayenne.
 */
@ApplicationPath("rest")
public class JaxRsApplication extends ResourceConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JaxRsApplication.class);

	public JaxRsApplication(@Context ServletContext context) {

		// init Cayenne runtime.. make sure it will be shutdown properly
		ServerRuntime cayenneRuntime = new ServerRuntimeBuilder().addConfig("cayenne-project.xml").build();
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
			LOGGER.info("Shutting down Cayenne runtime...");
			runtime.shutdown();
		}

		@Override
		public void onReload(Container container) {
			// do nothing
		}

		@Override
		public void onStartup(Container container) {
			// do nothing
		}
	}

}
