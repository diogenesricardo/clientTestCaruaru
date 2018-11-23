package com.example.diogenes.listmedphone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diogenes.listmedphone.dao.ActorDB;
import com.example.diogenes.listmedphone.model.Actor;
import com.example.diogenes.listmedphone.util.DownloadImageTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ActorDetailActivity extends AppCompatActivity {

    boolean favorite = false;
    private TextView tvName;
    private TextView tvCreatedAt;
    private ImageView ivAvatar;
    private ImageView ivFavorite;
    private Actor actor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_detail);

        tvName = (TextView) findViewById(R.id.tv_name_detail);
        tvCreatedAt = (TextView) findViewById(R.id.tv_created_at_detail);
        ivFavorite = (ImageView) findViewById(R.id.iv_favorite);

        ivAvatar = (ImageView) findViewById(R.id.iv_avatar_detail);

        actor = (Actor) getIntent().getSerializableExtra("actor");

        if (actor.favorite) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(ivAvatar);
            downloadImageTask.execute(actor.avatar);
            ivFavorite.setImageResource(android.R.drawable.star_big_on);
        } else {
            ivAvatar.setImageResource(R.mipmap.ic_avatar_foreground);
        }

        tvName.setText(actor.name);
        tvCreatedAt.setText(actor.createdAt);
    }

    public void makeFavorite(View view) {

        if (favorite == true) {
            ivFavorite.setImageResource(android.R.drawable.star_big_off);
            favorite = false;
            //DELETE NO BANCO
        } else {

            try {
                ActorDB actorDB = new ActorDB(getBaseContext());
                actorDB.inserir(actor);
//                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(actor.avatar).getContent());
//                imageView.setImageBitmap(bitmap);
                DownloadImageTask downloadImageTask = new DownloadImageTask(ivAvatar);
                downloadImageTask.execute(actor.avatar);

                ivFavorite.setImageResource(android.R.drawable.star_big_on);
                favorite = true;
            } catch (NullPointerException e) {
                Log.e("medphone", e.getMessage());
                Toast.makeText(this, "Fail in database", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }


}
