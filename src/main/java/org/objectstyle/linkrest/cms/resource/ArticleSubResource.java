package org.objectstyle.linkrest.cms.resource;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.objectstyle.linkrest.cms.cayenne.Article;
import org.objectstyle.linkrest.cms.cayenne.Domain;

import com.nhl.link.rest.DataResponse;
import com.nhl.link.rest.LinkRest;

/**
 * A sub-resource executed in a context of a given Domain ID.
 *
 */
@Path("article")
public class ArticleSubResource {

	@Context
	private Configuration config;
	private int domainId;

	public ArticleSubResource(int domainId) {
		this.domainId = domainId;
	}

	@GET
	public DataResponse<Article> getAll(@Context UriInfo uriInfo) {
		return LinkRest.select(Article.class, config).toManyParent(Domain.class, domainId, Domain.ARTICLES)
				.uri(uriInfo).select();
	}

	@GET
	@Path("{articleId}")
	public DataResponse<Article> getOne(@PathParam("articleId") int id, @Context UriInfo uriInfo) {
		return LinkRest.select(Article.class, config).toManyParent(Domain.class, domainId, Domain.ARTICLES).byId(id)
				.uri(uriInfo).select();
	}

	@POST
	public DataResponse<Article> create(String data) {
		return LinkRest.create(Article.class, config).toManyParent(Domain.class, domainId, Domain.ARTICLES)
				.process(data);
	}

	@PUT
	public DataResponse<Article> createOrUpdate(String data) {
		return LinkRest.createOrUpdate(Article.class, config).toManyParent(Domain.class, domainId, Domain.ARTICLES)
				.process(data);
	}
}
