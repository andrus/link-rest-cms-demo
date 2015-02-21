package org.objectstyle.linkrest.cms.resource;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;

import com.nhl.link.rest.runtime.ILinkRestService;
import com.nhl.link.rest.runtime.LinkRestRuntime;

/**
 * A base superclass of a LinkRest resource that simplifies access to
 * {@link ILinkRestService} in concrete subclasses. This class provides a
 * container-independent way of getting a hold of LinkRest.
 * <p>
 * In most real-life cases an app would use some form of injection though (CDI,
 * Jersey injection, etc.). Though we are keeping the example as vanilla as
 * possible.
 */
public abstract class BaseLinkRestResource {

	@Context
	private Configuration config;

	/**
	 * Returns ILinkRestService instance that allows to access the app's
	 * LinkRest stack.
	 */
	protected ILinkRestService getLinkRest() {
		return LinkRestRuntime.service(ILinkRestService.class, config);
	}

}
