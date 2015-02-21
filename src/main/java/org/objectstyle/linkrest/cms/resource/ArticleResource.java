package org.objectstyle.linkrest.cms.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.objectstyle.linkrest.cms.cayenne.Article;

import com.nhl.link.rest.DataResponse;
import com.nhl.link.rest.runtime.ILinkRestService;

@Path("article")
public class ArticleResource {

	@Inject
	private ILinkRestService linkRestService;

	@GET
	public DataResponse<Article> getAll(@Context UriInfo uriInfo) {
		return linkRestService.forSelect(Article.class).with(uriInfo).select();
	}

	@GET
	@Path("{id}")
	public DataResponse<Article> getOne(@PathParam("id") int id, @Context UriInfo uriInfo) {
		return linkRestService.forSelect(Article.class).byId(id).with(uriInfo).select();
	}

	@POST
	public DataResponse<Article> create(String data) {
		return linkRestService.create(Article.class).process(data);
	}

	@PUT
	public DataResponse<Article> createOrUpdate(String data) {
		return linkRestService.createOrUpdate(Article.class).process(data);
	}
}
