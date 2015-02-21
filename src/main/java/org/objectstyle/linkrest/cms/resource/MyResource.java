package org.objectstyle.linkrest.cms.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("hi")
public class MyResource extends BaseLinkRestResource {

	@GET
	public String get() {
		return "Hi, REST!";
	}
}
