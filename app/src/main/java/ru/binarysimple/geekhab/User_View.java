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

        ImageView iv_likes = (ImageView) findViewById(R.id.iv_Like);
        assert iv_likes != null;
        iv_likes.setOnClickListener(new View.OnClickListener() {
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

        Picasso.with(context)
                .load(user.getAvatar_url())
                .into(avatar);
    }

    private void setContent() {

        Intent intent = getIntent();
        _id = intent.getStringExtra("id");

        WorkDB workDB = new WorkDB();
        this.user = workDB.getUserById(this, _id);

        TextView username = (TextView) findViewById(R.id.uv_username);
        TextView tvid = (TextView) findViewById(R.id.uv_tvId);
        TextView tvUrl = (TextView) findViewById(R.id.uv_tvUrl);
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

    }

    private void setLikeStatus(int status) {
        ImageView iv_likes = (ImageView) findViewById(R.id.iv_Like);
        if (status > 0) iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_btn1));
        else iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.ic_like_btn0));
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
