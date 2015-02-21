package org.objectstyle.linkrest.cms;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.objectstyle.linkrest.cms.resource.MyResource;

@ApplicationPath("rest")
public class JaxRsApplication extends ResourceConfig {

	public JaxRsApplication() {
		packages(MyResource.class.getPackage().getName());
	}
}
