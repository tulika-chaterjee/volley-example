package com.dt.volleyexample.objects;

import android.text.Html;

import org.json.JSONObject;

public class Article {

    private long id;
    private String title;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Article fromJson(JSONObject jsonData) {
        Article article = new Article();
        article.setTitle(jsonData.optJSONObject("title").optString("$t"));
        article.setContent(Html.fromHtml(jsonData.optJSONObject("content").optString("$t")).toString());

        // Save only integer part of id
        String[] splitedId = jsonData.optJSONObject("id").optString("$t").split("-");
        article.setId(Long.decode(splitedId[2]));

        return article;
    }
}
