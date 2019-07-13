package com.example.project3.Networking;

import com.example.project3.Comment.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetroBaseApiService {
    final String Base_URL = "http://143.248.36.26:8080";
//        String Base_URL = "http://13.125.213.243:8080";

    // CREATE VIDEO
    @POST("/api/video")
    Call<ResponseInfo_posting> addVideo(@Body VideoInfo videoinfo, @Header("x-access-token") String token);

    // GET VIDEO BY GENRE
    @GET("/api/video/{genre}")
    Call<ResponseInfo> getVideoByGenre(@Path("genre") int genre, @Header("x-access-token") String token);

    // GET ALL VIDEO
    @GET("/api/video")
    Call<List<VideoInfo>> getAllVideo();

    //GET COMMENT BY GENRE AND VIDEOID
    @GET("/api/comment/{genre}/{videoId}")
    Call<ResponseInfo_comment> getCommentByGenreAndVideoId(@Path("genre") int genre, @Path("videoId") String videoId, @Header("x-access-token") String token);

    // CREATE VIDEO
    @POST("/api/comment/{username}")
    Call<ResponseInfo_comment_posting> addComment(@Body Comment commentinfo, @Path("username") String username, @Header("x-access-token") String token);


//    // DELETE CONTACT
//    @DELETE("/api/contacts/{id}/{name}")
//    Call<ResponseBody> deleteContact(@Path("id") String id, @Path("name") String name);
//
//
//    //GET ALL GALLERIES
//    @GET("/api/galleries/{id}")
//    Call<List<GalleryInfo>> getAllGallery(@Path("id") String id);
//
//    // ADD SINGLE GALLERY
//    @POST("/api/galleries")
//    Call<GalleryInfo> addGallery(@Body GalleryInfo galleryinfo);
//
//    // DELETE GALLERY
//    @DELETE("/api/galleries/{id}/{name}")
//    Call<ResponseBody> deleteGallery(@Path("id") String id, @Path("name") String name);
//
//
//    //GET CURRENT BOARD
//    @GET("/api/omok/")
//    Call<List<coordinates>> getBoard();
//
//    // ADD SINGLE POINT
//    @POST("/api/omok")
//    Call<coordinates> addPoint(@Body coordinates coordinates);
//
//    // DELETE BOARD
//    @DELETE("/api/omok/")
//    Call<ResponseBody> deleteBoard();
//
//
//    // ADD USER
//    @POST("/api/user/{id}")
//    Call<String> addUser(@Path("id") String id);
//
//    // DELETE USER
//    @DELETE("/api/user/{id}")
//    Call<ResponseBody> delUser(@Path("id") String id);
//
//    // GET USER
//    @GET("/api/user/{id}")
//    Call<List<String>> getUser(@Path("id") String id);
}

