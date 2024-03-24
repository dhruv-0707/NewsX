package com.example.newsx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class NewsCardAdapter extends RecyclerView.Adapter<NewsCardAdapter.ViewHolder> {
    Context context;
    ArrayList<NewsCard_data> value;
    WebView webView;
    NewsCardAdapter(Context context, ArrayList<NewsCard_data> value){
        this.context=context;
        this.value = value;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(value.get(position).title);
        holder.content.setText(value.get(position).content);
        holder.source.setText(value.get(position).source);
        holder.datetime.setText(value.get(position).datetime);
        String img_url = value.get(position).urltoimage;
        Log.d("url_null", value.get(0).urltoimage);
        if(img_url!=null){
            Picasso.get().load(value.get(position).urltoimage).into(holder.image);
        }else{
            holder.image.setImageResource(R.drawable.ic_baseline_image_24);
        }
        if(holder.image.getDrawable()==null){
            holder.image.setImageResource(R.drawable.ic_baseline_image_24);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WebViewActivity.class);
                intent.putExtra("url",value.get(holder.getAdapterPosition()).url);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return value.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title,content,datetime,source;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            datetime = itemView.findViewById(R.id.datetime);
            source = itemView.findViewById(R.id.source);

        }
    }
}
