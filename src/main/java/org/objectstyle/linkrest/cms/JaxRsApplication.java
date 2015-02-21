package org.objectstyle.linkrest.cms;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.objectstyle.linkrest.cms.resource.BaseLinkRestResource;

import com.nhl.link.rest.runtime.LinkRestBuilder;
import com.nhl.link.rest.runtime.LinkRestRuntime;

/**
 * A JAX-RS "application" class that demonstrates how to bootstrap LinkRest and
 * Cayenne.
 */
@ApplicationPath("rest")
public class JaxRsApplication extends ResourceConfig {

	public JaxRsApplication() {

		// bootstrap LinkRest with the minimal set of options
		LinkRestRuntime lrRuntime = new LinkRestBuilder().cayenneRuntime(cayenneRuntime).build();

		// register LinkRest as a JAX RS "feature"
		register(lrRuntime.getFeature());

		// declare the location of the application REST endpoints
		packages(BaseLinkRestResource.class.getPackage().getName());
	}
}
