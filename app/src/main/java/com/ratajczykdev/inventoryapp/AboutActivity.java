package com.ratajczykdev.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity
{
    private ImageView imageViewStore;
    private TextView textViewCreatedBy;
    private TextView textViewAuthor;
    private TextView textViewEmail;

    private static final int EXPLODE_TRANSITION_DURATION_IN_MS = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setUiReferences();

        //  ...ActivityOptions.makeSceneTransitionAnimation... must be called in previous activity
        getWindow().setEnterTransition(getCustomExplodeAnimation());
    }

    private void setUiReferences()
    {
        imageViewStore = findViewById(R.id.activity_about_store_imageview);
        textViewCreatedBy = findViewById(R.id.activity_about_created_textview);
        textViewAuthor = findViewById(R.id.activity_about_author_textview);
        textViewEmail = findViewById(R.id.activity_about_email_textview);
    }

    private Explode getCustomExplodeAnimation()
    {
        Explode explode = new Explode();
        explode.setDuration(EXPLODE_TRANSITION_DURATION_IN_MS);
        explode.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in));
        explode.addTarget(imageViewStore);
        explode.addTarget(textViewCreatedBy);
        explode.addTarget(textViewAuthor);
        explode.addTarget(textViewEmail);

        return explode;
    }
}
