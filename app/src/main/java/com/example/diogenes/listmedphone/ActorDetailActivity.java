package com.example.diogenes.listmedphone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActorDetailActivity extends AppCompatActivity {

    boolean favorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_detail);
    }

    public void makeFavorite(View view) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.ib_favorite);
        if (favorite == true) {
            imageButton.setImageResource(android.R.drawable.star_big_off);
            favorite = false;
        } else{
            imageButton.setImageResource(android.R.drawable.star_big_on);
            favorite = true;
        }
    }
}
