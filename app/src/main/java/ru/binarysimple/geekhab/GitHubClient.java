package ru.binarysimple.geekhab;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubClient {

    @GET("search/users")
    Call<UserList> getUsers (@Query("q") String userName);

}
