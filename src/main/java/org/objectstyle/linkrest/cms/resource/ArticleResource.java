package org.objectstyle.linkrest.cms.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.objectstyle.linkrest.cms.cayenne.Article;

import com.nhl.link.rest.DataResponse;

@Path("article")
public class ArticleResource extends BaseLinkRestResource {

	@GET
	public DataResponse<Article> getAll(@Context UriInfo uriInfo) {
		return getLinkRest().forSelect(Article.class).with(uriInfo).select();
	}

	@GET
	@Path("{id}")
	public DataResponse<Article> getOne(@PathParam("id") int id, @Context UriInfo uriInfo) {
		return getLinkRest().forSelect(Article.class).byId(id).with(uriInfo).select();
	}
	
	@POST
	public DataResponse<Article> create(String data) {
		return getLinkRest().create(Article.class).process(data);
	}
	
	@PUT
	public DataResponse<Article> createOrUpdate(String data) {
		return getLinkRest().createOrUpdate(Article.class).process(data);
	}
}
