package ru.binarysimple.geekhab;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGen {

        public static final String BASE_URL = "https://api.github.com/";

        public static <T> T createRetrofitService(final Class<T> service) {

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return client.create(service);
        }

    }

