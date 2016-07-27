package ru.binarysimple.geekhab;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static String LOG_TAG = "geekhab";
    public static String TABLE_NAME = "geekhabTbl";

    SimpleCursorAdapter adapter;
    ListView lvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] from = new String[]{WorkDB.COLUMN_ID, WorkDB.COLUMN_LOGIN, WorkDB.COLUMN_URL};
        int[] to = new int[]{R.id.tvId, R.id.tvName, R.id.tvUrl};

        adapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(adapter);
        getSupportLoaderManager().initLoader(0, null, this);

/*        Button button = (Button) findViewById(R.id.button);

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUsers(view.getContext(), "voffka");
            }
        });*/
    }


    private void searchUsers(final Context context, String userName) {
        GitHubClient gitHubClient = ServiceGen.createRetrofitService(GitHubClient.class);
        Call<UserList> call = gitHubClient.getUsers(userName);
        //Async
        call.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                //Get results from response.body
                assert response.body() != null;
                Log.d(LOG_TAG, "onResponse " + response.body().getItems().size());
                WorkDB workDB = new WorkDB();
                workDB.insertUsersList(context, response.body());

            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d(LOG_TAG, "onFailure");
            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        System.out.println("onCreateLoader");
        return new MyCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        System.out.println("onLoadFinished");
        adapter.swapCursor(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class MyCursorLoader extends CursorLoader {

        WorkDB workDB;

        public MyCursorLoader(Context context) {
            super(context);
            this.workDB = new WorkDB();
            System.out.println("MyCursorLoader");
        }

        @Override
        public Cursor loadInBackground() {
            System.out.println("loadinBackground");
            return workDB.getUsersListCursor(getContext());
        }
    }
}
