package org.objectstyle.linkrest.cms;

import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;

import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.configuration.server.ServerRuntimeBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.objectstyle.linkrest.cms.derby.DerbyManager;
import org.objectstyle.linkrest.cms.resource.ArticleSubResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;

/**
 * A Jersey framework-specific JAX-RS Application class that allows us to
 * bootstrap Cayenne and LinkRest.
 */
@ApplicationPath("/")
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

		// ... this enables LinkRest JAX RS extensions (ExceptionMappers,
		// MessageBodyWriters, etc)
		register(lrRuntime.getFeature());

		// expose application REST endpoints
		packages(ArticleSubResource.class.getPackage().getName());
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

		// TODO: once https://issues.apache.org/jira/browse/CAY-1988 is fixed
		// (in Cayenne 4.0.M3 perhaps),
		// we can remove DataNode from cayenne-project.xml, so a "synthetic"
		// node can be created by the builder; then we'll also need to set
		// CreateIfNoSchemaStrategy here in the builder

		return new ServerRuntimeBuilder().jdbcDriver(driver).url(url).addConfig("cayenne-project.xml").build();
	}

	private static DerbyManager createDerby() {
		return new DerbyManager();
	}
}
