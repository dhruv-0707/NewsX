package com.example.newsx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.carousel.CarouselLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<NewsCard_data> data =new ArrayList<>();
    RecyclerView recyclerView;
    EditText search_bar;
    ImageButton search_button;
    String api_url;
    ProgressBar pg;
    Toolbar toolbar_button,toolbar_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_bar=findViewById(R.id.toolbar_searchbar);
        toolbar_button=findViewById(R.id.toolbar_button );

        setSupportActionBar(toolbar_button);
        getSupportActionBar().setTitle("Top Headlines");

        search_bar = findViewById(R.id.search_bar);
        search_button =  findViewById(R.id.search_button);
        recyclerView= findViewById(R.id.recycler);
        pg = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        api_url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=af8d9359f71146beb2a4d0309f69e686";
        fetch(api_url);
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setSupportActionBar(toolbar_button);
                search();
                toolbar_bar.setVisibility(View.GONE);
                toolbar_button.setVisibility(View.VISIBLE);
                return true;
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar.setText("");
                toolbar_bar.setVisibility(View.VISIBLE);
                toolbar_button.setVisibility(View.GONE);
                setSupportActionBar(toolbar_bar);
                search_bar.setActivated(true);
                search_bar.findFocus();
            }
        });
//        Log.d("data_stored", data.get(1).url);
    }

    private void search() {

        if (!search_bar.getText().toString().equals("")) {
            pg.setVisibility(View.VISIBLE);
            String keyword = search_bar.getText().toString();
            getSupportActionBar().setTitle("Search results/ "+keyword);
            api_url = "https://newsapi.org/v2/everything?q=" + keyword + "&apiKey=af8d9359f71146beb2a4d0309f69e686";
            fetch(api_url);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search_bar.getWindowToken(), 0);
            search_bar.clearFocus();
        }
        else{
            Toast.makeText(MainActivity.this, "Enter keyword to search", Toast.LENGTH_SHORT).show();
        }
    }

    void fetch(String api) {
        data.clear();
        AndroidNetworking.initialize(this);
        AndroidNetworking.get(api)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response_api", response.toString());
                        String title,content,urltoimage,url,datetime,source;
                        try {
                            JSONArray arr_articles = response.getJSONArray("articles");
//                            Log.d("data_stored", arr_articles.toString());
                            for(int i=0;i<arr_articles.length();i++){

                                JSONObject jsonObject = arr_articles.getJSONObject(i);

                                title=jsonObject.getString("title");
                                content=jsonObject.getString("content");
                                url=jsonObject.getString("url");
                                urltoimage=jsonObject.getString("urlToImage");
                                datetime=jsonObject.getString("publishedAt");
                                source=jsonObject.getJSONObject("source").getString("name")+" ("+jsonObject.getString("author")+")";
                                data.add(new NewsCard_data(title,content,urltoimage,url,datetime,source));
//                                Log.d("url_data",data.get(i).source);

                            }
                            pg.setVisibility(View.GONE);

                            recyclerView.setAdapter(new NewsCardAdapter(MainActivity.this,data));
                        } catch (JSONException e) {
                            Log.e("response_api", e.toString());
                            Toast.makeText(MainActivity.this,"Some error occured in parsing",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("response_api", anError.toString());
                        Toast.makeText(MainActivity.this,"Some error occured",Toast.LENGTH_SHORT).show();
                        anError.printStackTrace();
                    }
                });
    }

}