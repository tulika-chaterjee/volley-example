package com.dt.volleyexample.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class ArticleCollection {

    Vector<Article> articles = new Vector<Article>();

    public Article get(int index) {
        return articles.elementAt(index);
    }

    public int size() {
        return articles.size();
    }

    private void add(Article article) {
        articles.addElement(article);
    }

    public static ArticleCollection unloadJson(JSONObject jsonData) throws JSONException {
        ArticleCollection articleCollection = new ArticleCollection();

        JSONObject jsonFeed = jsonData.getJSONObject("feed");
        JSONArray jsonEntry = jsonFeed.getJSONArray("entry");

        for (int i = 0; i < jsonEntry.length(); i++) {
            articleCollection.add(Article.fromJson(jsonEntry.getJSONObject(i)));
        }

        return articleCollection;
    }
}
