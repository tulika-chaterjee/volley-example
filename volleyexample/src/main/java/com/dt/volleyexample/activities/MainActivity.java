package com.dt.volleyexample.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dt.volleyexample.Constants;
import com.dt.volleyexample.R;
import com.dt.volleyexample.objects.Article;
import com.dt.volleyexample.objects.ArticleCollection;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private RequestQueue mRequestQueue;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(this);
        loadData();
    }

    private void loadData() {
        progressDialog = ProgressDialog.show(this, "Please wait", "Loading data...", true, true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mRequestQueue.cancelAll(this);
                    }
                });

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, Constants.URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(Constants.LOG_TAG, response.toString());
                        try {
                            ArticleCollection articles = ArticleCollection.unloadJson(response);
                            updateUi(articles);
                        } catch (JSONException e) {
                            Log.e(Constants.LOG_TAG, e.getLocalizedMessage());
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Constants.LOG_TAG, error.getMessage());
                    }
                }
        );

        mRequestQueue.add(jr);
    }

    private void updateUi(ArticleCollection articles) {
        ListView list = (ListView) findViewById(R.id.list);
        ArticlesAdapter adapter = new ArticlesAdapter(articles);

        if (list.getAdapter() == null) {
            list.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class ArticlesAdapter extends BaseAdapter {

        private ArticleCollection articles;

        private ArticlesAdapter(ArticleCollection articles) {
            this.articles = articles;
        }

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public Article getItem(int i) {
            return articles.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;

            if (view == null) {
                vh = new ViewHolder();
                view = getLayoutInflater().inflate(R.layout.list_item, null);
                vh.tvTitle = (TextView) view.findViewById(R.id.txtTitle);
                vh.tvDesc = (TextView) view.findViewById(R.id.txtDesc);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }

            Article article = getItem(i);
            vh.tvTitle.setText(article.getTitle());
            vh.tvDesc.setText(article.getContent());
            return view;
        }

        class ViewHolder {
            TextView tvTitle;
            TextView tvDesc;
        }

    }
}
