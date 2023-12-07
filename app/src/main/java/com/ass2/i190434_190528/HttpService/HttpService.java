package com.ass2.i190434_190528.HttpService;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HttpService {

    @Multipart
    @POST("upload_profile_pic.php")
    Call<UserProfileModel> callUploadApi(@Part MultipartBody.Part image,
                                         @Part("userEmail") RequestBody email,
                                         @Part("imageType") RequestBody imageType);

    @Multipart
    @POST("update_profile.php") // Replace with the actual API endpoint for updating the profile
    Call<UserProfileModel> callUpdateProfile(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("upload_cover_photo.php")
    Call<UserProfileModel> callUploadApiCoverPhoto(@Part MultipartBody.Part image, @Part("userEmail") RequestBody email);

    @Multipart
    @POST("Ass02API/multi_upload.php")
    Call<UserProfileModel> callMultipleUploadApi(@Part List<MultipartBody.Part> image);
}