package ru.binarysimple.geekhab;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    public static String LOG_TAG = "geekhab";
    public static String TABLE_NAME = "geekhabTbl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button) findViewById(R.id.button);

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUsers(view.getContext(), "voffka");
            }
        });
    }



    private void searchUsers (final Context context, String userName){
        GitHubClient gitHubClient = ServiceGen.createRetrofitService(GitHubClient.class);
        Call<UserList> call = gitHubClient.getUsers(userName);
        //Async
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                //Get results from response.body
                assert response.body() != null;
                Log.d(LOG_TAG,"onResponse "+response.body().getItems().size());
                WorkDB workDB = new WorkDB();
                workDB.insertUsersList(context, response.body());

            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
            }
        });


    }

}
