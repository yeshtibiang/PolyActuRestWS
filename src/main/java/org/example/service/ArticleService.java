package org.example.service;


import com.google.gson.Gson;
import org.example.model.Article;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class ArticleService {

    private Connection dbConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String jdbcUrl = "jdbc:mysql://localhost:3306/polyactu";
            String username = "root";
            String password = "root";

            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            return connection;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> getAllArticles() throws SQLException {

        Connection conn = dbConnect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from articles");
        List<Article> articles = new ArrayList<Article>();

        while (rs.next()){
            articles.add(new Article(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(3), rs.getTimestamp("createdDate")));
        }
        articles.sort(new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getCreatedDate().compareTo(o1.getCreatedDate());
            }
        });

        return articles;

    }

    public String getArticlesGroupXml() throws SQLException {
        Connection conn = dbConnect();
        Statement stmt = conn.createStatement();
        // selectionne l'ensemble des groupes
        ResultSet grpRs = stmt.executeQuery("select distinct category from articles");
        // selectionne l'ensemble elements par groupes
        PreparedStatement grpDataPs = conn.prepareStatement("select * from articles where category = ?");
        ResultSet grpData = null;

        StringBuilder returnValue = new StringBuilder();

        returnValue.append("<articles>");

        while (grpRs.next()){
            grpDataPs.setString(1, grpRs.getString("category"));
            grpData = grpDataPs.executeQuery();

            returnValue.append("<").append(grpRs.getString("category")).append(">");

            while (grpData.next()){
                returnValue.append("<article>");
                returnValue.append("<id>").append(grpData.getInt("id")).append("</id>");
                returnValue.append("<title>").append(grpData.getString("title")).append("</title>");
                returnValue.append("<content>").append(grpData.getString("content")).append("</content>");
                returnValue.append("<category>").append(grpData.getString("category")).append("</category>");
                returnValue.append("<createdDate>").append(grpData.getTimestamp("createdDate")).append("</createdDate>");
                returnValue.append("</article>");
            }
            returnValue.append("</").append(grpRs.getString("category")).append(">");
        }
        returnValue.append("</articles>");

        System.out.println(returnValue.toString());
        return returnValue.toString();
    }

    public String getArticlesGroupJson() throws SQLException {
        Connection conn = dbConnect();
        Statement stmt = conn.createStatement();
        // selectionne l'ensemble des groupes
        ResultSet grpRs = stmt.executeQuery("select distinct category from articles");
        // selectionne l'ensemble elements par groupes
        PreparedStatement grpDataPs = conn.prepareStatement("select * from articles where category = ? order by createdDate desc");
        ResultSet grpData = null;

        //créer le list map
        Map<String, Object> finalJsonObjet = new LinkedHashMap<String, Object>();

        // get the meta data
        ResultSetMetaData grpMeta = grpRs.getMetaData();
        int colnum = grpMeta.getColumnCount();
        List<String> grpName = new ArrayList<String>();

        for (int i = 1; i <= colnum; i++){
            grpName.add(grpMeta.getColumnName(i));
        }

        while (grpRs.next()){

            List<Map<String, Object>> categoryArticle = new LinkedList<Map<String, Object>>();
            grpDataPs.setString(1, grpRs.getString("category"));
            grpData = grpDataPs.executeQuery();

            while (grpData.next()){
                Map<String, Object> categoryElt = new LinkedHashMap<String, Object>();
                categoryElt.put("id", grpData.getInt("id"));
                categoryElt.put("title", grpData.getString("title"));
                categoryElt.put("content", grpData.getString("content"));
                categoryElt.put("category", grpData.getString("category"));
                categoryElt.put("createdDate", grpData.getTimestamp("createdDate"));

                categoryArticle.add(categoryElt);
            }

            finalJsonObjet.put(grpRs.getString("category"), categoryArticle);

        }

        Gson gson = new Gson();

        //return finalJsonObjet;
        return gson.toJson(finalJsonObjet);
    }

    public List<Article> getArticlesByCategory(String catego) throws SQLException {
        Connection conn = dbConnect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from articles where category = '"+catego+"' order by createdDate");

        List<Article> articles = new ArrayList<Article>();

        while (rs.next()){
            articles.add(new Article(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(3), rs.getTimestamp("createdDate")));
        }
        articles.sort(new Comparator<Article>() {
            @Override
            public int compare(Article o1, Article o2) {
                return o2.getCreatedDate().compareTo(o1.getCreatedDate());
            }
        });
        System.out.println(articles);

        return articles;

    }
}
