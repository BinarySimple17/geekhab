package ru.binarysimple.geekhab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

//TODO ROTATE SCREEN CHANGE LONGTEXT SIZE

public class User_View extends AppCompatActivity {
    private String _id;
    private User user;
    private ImageView avatar;
    private int lines = 5; //костыль 5 строк

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user__view);

        setContent();
        setLikeStatus(user.getStatus());
        loadAvatar(this);


        ImageButton button = (ImageButton) findViewById(R.id.uv_button);
        assert button != null;

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO после поворота возвращается старый размер текствью
                ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.uv_container);
                TextView longText = (TextView) findViewById(R.id.uv_longText);
                int newSize = longText.getLineCount() * longText.getLineHeight();
                longText.setMaxLines(Integer.MAX_VALUE);

                TransitionManager.beginDelayedTransition(sceneRoot);
                ViewGroup.LayoutParams params = longText.getLayoutParams();
                params.height = newSize;
                longText.setLayoutParams(params);
                lines = longText.getLineCount(); //save lines count to reload if screen rotates
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLikeUnlike();
            }
        });

    }

    private String genLongText(String str) {
        /*
        /WARNING INDIAN CODE
        /eyes may bleeding
        */
        return str + str + str + str + str + str + str + str + str + str;
    }

    private void loadAvatar(Context context) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("onBitmapLoaded");
                LayerDrawable oldLayerDrawable = (LayerDrawable) avatar.getDrawable();
                Drawable[] layers = new Drawable[2];
                layers[0] = new BitmapDrawable(getBaseContext().getResources(), bitmap);
                layers[1] = oldLayerDrawable.getDrawable(1);
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                avatar.setImageDrawable(layerDrawable);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        avatar.setTag(target); //save target from GC

        Picasso.with(context)
                .load(user.getAvatar_url())
                .into(target);
    }

    private void setContent() {

        Intent intent = getIntent();
        _id = intent.getStringExtra("id");

        WorkDB workDB = new WorkDB();
        this.user = workDB.getUserById(this, _id);

        TextView username = (TextView) findViewById(R.id.uv_username);
        TextView tvid = (TextView) findViewById(R.id.uv_tvId);
        TextView tvUrl = (TextView) findViewById(R.id.uv_tvUrl);
        //TextView longText = (TextView) findViewById(R.id.uv_longText);
        TextView likesCount = (TextView) findViewById(R.id.uv_likesCount);
        avatar = (ImageView) findViewById(R.id.avatar);

        //add likes img to textview
        Drawable img = getResources().getDrawable(R.drawable.ic_like);
        img.setBounds(0, 0, 50, 50);
        likesCount.setCompoundDrawables(img, null, null, null);
        likesCount.setText(String.valueOf(user.getLikes()));

        tvid.setText(user.getId());
        username.setText(user.getLogin());
        tvUrl.setText(user.getUrl());

/*        longText.setText(genLongText(user.getUrl()));
        ViewGroup.LayoutParams params = longText.getLayoutParams();
        params.height = longText.getLineHeight() * lines; //TODO костыль на 5 строк
        longText.setLayoutParams(params);*/
    }

    private void setLikeStatus(int status) {
        LayerDrawable oldLayerDrawable = (LayerDrawable) avatar.getDrawable();
        Drawable[] layers = new Drawable[2];
        layers[0] = oldLayerDrawable.getDrawable(0);
        if (status > 0) layers[1] = getResources().getDrawable(R.drawable.ic_like_btn1);
        else layers[1] = getResources().getDrawable(R.drawable.ic_like_btn0);
        //params.height = newSize;
        //longText.setLayoutParams(params);
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.getDrawable(1).setBounds(50, 50, 50, 50);
        avatar.setImageDrawable(layerDrawable);
        TextView likesCount = (TextView) findViewById(R.id.uv_likesCount);
        likesCount.setText(String.valueOf(user.getLikes()));

    }

    private void setLikeUnlike() {
        int currentStatus = user.getStatus();
        int newStatus = currentStatus ^ 1; //invert

        user.setStatus(newStatus);
        if (newStatus > 0) user.setLikes(user.getLikes() + 1);
        else user.setLikes(user.getLikes() - 1);

        WorkDB workDB = new WorkDB();
        workDB.updateUser(this, user);

        user = workDB.getUserById(this, user.getId()); //get data from DB back

        setLikeStatus(user.getStatus());

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("lines", lines);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lines = savedInstanceState.getInt("lines");
        System.out.println(lines);
    }

    protected void onResume() {
        super.onResume();
        //longtext here, because of screen rotation
        TextView longText = (TextView) findViewById(R.id.uv_longText);
        longText.setText(genLongText(user.getUrl()));
        ViewGroup.LayoutParams params = longText.getLayoutParams();
        params.height = longText.getLineHeight() * lines; //TODO костыль на 5 строк
        longText.setLayoutParams(params);
    }

}
