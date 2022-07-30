package org.example.ressources;

import org.example.model.Article;
import org.example.service.ArticleService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Path("articles")
public class ArticleRessource {

    ArticleService articleService = new ArticleService();

    @Path("all_xml")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Article> listAllArticlesXml() throws SQLException{
        return articleService.getAllArticles();
    }

    @Path("all_json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> listAllArticlesJson() throws SQLException{
        return articleService.getAllArticles();
    }

    @Path("all_group_xml")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String listArtilceGroupXml() throws SQLException {
        return articleService.getArticlesGroupXml();
    }

    @Path("all_group_json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String  listArtilceGroupJson() throws SQLException {
        return articleService.getArticlesGroupJson();
    }

    @Path("{catego}/xml")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Article> listArticleByCatXml(@PathParam("catego") String category) throws SQLException {
        return articleService.getArticlesByCategory(category);
    }

    @Path("{catego}/json")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Article> listArticleByCatJSON(@PathParam("catego") String category) throws SQLException {
        return articleService.getArticlesByCategory(category);
    }
}
