package com.selayar.history.Retrofit

import com.selayar.history.Model.*
import com.selayar.history.Models.Gambar
import com.selayar.history.Models.WisataSejarah
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {
    //BACKGROUND TREAD (COROUTINE)
//    @GET("api/kasir/menu")
//    suspend fun getMenuAsync(@Header("Authorization") authToken: String, @Query("outlet_id") outlet_id: Int, @Query("nomor_urut") nomor_urut: Int): Response<ModelWrapper<Menu>>

//    @FormUrlEncoded
//    @POST("api/kasir/pelanggan")
//    suspend fun addPelanggan(
//            @Header("Authorization") authToken: String,
//            @Field("unique_id") unique_id: String,
//            @Field("email") email: String?,
//            @Field("nama_pelanggan") nama_pelanggan: String,
//            @Field("no_hp") no_hp: String?,
//            @Field("jk") jk: String?
//    ) : Response<GlobalResult>

    @FormUrlEncoded
    @POST("api/login")
    suspend fun signInBackground(
        @Field("auth") auth: String,
        @Field("password") password: String,
        @Field("is_gmail") isGmail: Int,
        @Field("token_gmail") tokenGmail: String
    ): Response<ResponseBody>

    //MAIN THREAD (RX)
    @FormUrlEncoded
    @POST("api/login")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String
    ): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("api/checkout")
    fun checkout(
        @Header("X-Dapur-Api") tokenGmail: String?,
        @FieldMap fields: HashMap<String, String>
    ): Call<ResponseBody>

    @GET("api/v2.1/search")
    fun getRestaurantsBySearch(
        @Query("entity_id") entity_id: String?,
        @Query("entity_type") entity_type: String?,
        @Query("q") query: String?,
        @Header("user-key") userkey: String?
    ): Call<String?>?

    @FormUrlEncoded
    @POST("api/hapus_alamat")
    fun hapusAlamat(
        @Header("X-Dapur-Api") token: String?,
        @Field("id_alamat") id_alamat: Int?
    ): Observable<GlobalResult>

    @Multipart
    @POST("api/konfirmasi_pembayaran")
    fun konfirmPembayaran(
        @Header("X-Dapur-Api") token: String?,
        @Part gambar: MultipartBody.Part,
        @Part id_checkout: MultipartBody.Part
    ): Observable<GlobalResult>

    @GET("api/ongkir/{id}")
    fun ongkir(
        @Path("id") idDesa: Int
    ): Observable<GlobalResult>

    //Kode diatas untuk contoh
    //Kode untuk aplikasi mulai dari sini


    @GET("travel/")
    fun getAllWisata():Observable<ModelListWrapper<WisataSejarah>>

    @GET("travel/{slug}")
    fun getQR(
        @Path("slug") slug: String
    ):Observable<ModelListWrapper<WisataSejarah>>
}
