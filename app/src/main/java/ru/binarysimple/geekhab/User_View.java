package ru.binarysimple.geekhab;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class User_View extends AppCompatActivity {
    private String _id;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user__view);

        //add likes img to textview
        TextView username = (TextView)findViewById(R.id.uv_username);
        TextView tvid = (TextView)findViewById(R.id.uv_tvId);
        TextView tvUrl = (TextView)findViewById(R.id.uv_tvUrl);
        TextView longText = (TextView)findViewById(R.id.uv_longText);
        TextView likesCount = (TextView)findViewById(R.id.uv_likesCount);
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Drawable img = getResources().getDrawable(R.drawable.ic_like);
        img.setBounds(0, 0, 50, 50);
        likesCount.setCompoundDrawables(img, null, null, null);

        Intent intent = getIntent();
        _id = intent.getStringExtra("id");

        WorkDB workDB = new WorkDB();
        this.user = workDB.getUserById(this,_id);

        tvid.setText(user.getId());
        username.setText(user.getLogin());
        tvUrl.setText(user.getUrl());
        //longText.setText(user.getUrl());
        longText.setText(genLongText(user.getUrl()));
        likesCount.setText(String.valueOf(user.getLikes()));

        Picasso.with(this)
                .load(user.getAvatar_url())
                .placeholder(R.drawable.ic_avatar_loading)
                .into(avatar);

        ImageButton button = (ImageButton) findViewById(R.id.uv_button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO после поворота возвращается старый размер текствью
                ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.uv_container);
                TextView longText = (TextView) findViewById(R.id.uv_longText);
                int newSize = getResources().getDimensionPixelSize(R.dimen.size_expanded);//TODO костыль

                TransitionManager.beginDelayedTransition(sceneRoot);
                ViewGroup.LayoutParams params = longText.getLayoutParams();
                //params.width = newSquareSize;
                params.height = newSize;
                longText.setLayoutParams(params);
                longText.setMaxLines(100);//TODO костыль
            }
        });

    }

    private String genLongText(String str){
        /*
        /WARNING INDIAN CODE
        /eyes may bleeding
        */
        return str+str+str+str+str+str+str+str+str+str;

    }
}
