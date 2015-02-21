package org.objectstyle.linkrest.cms;

import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;

import org.apache.cayenne.access.dbsync.CreateIfNoSchemaStrategy;
import org.apache.cayenne.access.dbsync.SchemaUpdateStrategy;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.configuration.server.ServerRuntimeBuilder;
import org.glassfish.jersey.server.ResourceConfig;
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

	public JaxRsApplication() {

		// init persistence layer...
		this.derbyManager = createDerby();
		this.cayenneRuntime = createCayenne(derbyManager.getUrl(), derbyManager.getDriver());

		// init and bootstrap LinkRest
		LinkRestRuntime lrRuntime = createLinkRest(cayenneRuntime);
		register(lrRuntime.getFeature());

		// expose application REST endpoints
		packages(BaseLinkRestResource.class.getPackage().getName());
	}

	@PreDestroy
	public void preDestroy() {
		LOGGER.info("Container shutdown...");

		if (cayenneRuntime != null) {
			LOGGER.info("Shutting down Cayenne");
			cayenneRuntime.shutdown();
			cayenneRuntime = null;
		}

		if (derbyManager != null) {
			LOGGER.info("Shutting down Derby");
			derbyManager.shutdown();
			derbyManager = null;
		}
	}

	private static LinkRestRuntime createLinkRest(ServerRuntime cayenneRuntime) {
		return new LinkRestBuilder().cayenneRuntime(cayenneRuntime).build();
	}

	private static ServerRuntime createCayenne(String url, String driver) {
		return new ServerRuntimeBuilder()
				// ensure test schema is created...
				.addModule(binder -> binder.bind(SchemaUpdateStrategy.class).to(CreateIfNoSchemaStrategy.class))
				.jdbcDriver(driver).url(url).addConfig("cayenne-project.xml").build();
	}

	private static DerbyManager createDerby() {
		return new DerbyManager();
	}
}
