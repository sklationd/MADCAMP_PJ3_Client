package com.example.project3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class RetroClient {

    private RetroBaseApiService apiService;
    public static String baseUrl = RetroBaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;
    String Token;

    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }

    public static RetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private RetroClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        Token = sf.getString("Token", null);
    }

    public RetroClient createBaseApi() {
        apiService = create(RetroBaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void addVideo(VideoInfo videoInfo, final RetroCallback callback) {
        apiService.addVideo(videoInfo, Token).enqueue((new Callback<ResponseInfo_posting>() {
            @Override
            public void onResponse(Call<ResponseInfo_posting> call, Response<ResponseInfo_posting> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseInfo_posting> call, Throwable t) {
                callback.onError(t);
            }
        }));
    }
    public void getVideoByGenre(int genre, final RetroCallback callback) {
        apiService.getVideoByGenre(genre, Token).enqueue(new Callback<ResponseInfo>() {
            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                if (response.isSuccessful()) {
                    ResponseInfo body = response.body();
                    if (body.getData() != null) {
                        Log.d("body", body.getData().toString() + "");
                    }
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                Log.d("dasfgdfsdfssdfssdf", "sdfsfkajshdlhioghaoierg");
                callback.onError(t);
            }
        });
    }
    public void getAllVideo(final RetroCallback callback) {
        apiService.getAllVideo().enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

//    public void deleteContact(String id, String name, final RetroCallback callback) {
//        apiService.deleteContact(id, name).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }
//
//    public void getAllGallery(String id, final RetroCallback callback) {
//        apiService.getAllGallery(id).enqueue(new Callback<List<GalleryInfo>>() {
//            @Override
//            public void onResponse(Call<List<GalleryInfo>> call, Response<List<GalleryInfo>> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<GalleryInfo>> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }

//    public void addGallery(GalleryInfo galleryInfo, final RetroCallback callback) {
//        apiService.addGallery(galleryInfo).enqueue((new Callback<GalleryInfo>() {
//            @Override
//            public void onResponse(Call<GalleryInfo> call, Response<GalleryInfo> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GalleryInfo> call, Throwable t) {
//                callback.onError(t);
//            }
//        }));
//    }
//
//    public void deleteGallery(String id, String name, final RetroCallback callback) {
//        apiService.deleteGallery(id, name).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }
//
//
//    public void getBoard(final RetroCallback callback) {
//        apiService.getBoard().enqueue(new Callback<List<coordinates>>() {
//            @Override
//            public void onResponse(Call<List<coordinates>> call, Response<List<coordinates>> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<coordinates>> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }
//
//    public void addPoint(coordinates coordinates, final RetroCallback callback) {
//        apiService.addPoint(coordinates).enqueue((new Callback<coordinates>() {
//            @Override
//            public void onResponse(Call<coordinates> call, Response<coordinates> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<coordinates> call, Throwable t) {
//                callback.onError(t);
//            }
//        }));
//    }
//
//    public void deleteBoard(final RetroCallback callback) {
//        apiService.deleteBoard().enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }
//
//    public void addUser(String id, final RetroCallback callback) {
//        apiService.addUser(id).enqueue((new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                callback.onError(t);
//            }
//        }));
//    }
//
//    public void delUser(String id, final RetroCallback callback) {
//        apiService.delUser(id).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }
//
//    public void getUser(String id, final RetroCallback callback) {
//        apiService.getUser(id).enqueue((new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                if (response.isSuccessful()) {
//                    callback.onSuccess(response.code(), response.body());
//                } else {
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//                callback.onError(t);
//            }
//        }));
//    }
}
