package com.deval.event.Retrofit

import com.deval.event.Model.GlobalResult
import com.deval.event.Model.ModelListWrapper
import com.deval.event.Models.*
import com.deval.event.Models.Unit
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
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

    @GET("api/ongkir/{id}")
    fun ongkir(
        @Path("id") idDesa: Int
    ): Observable<GlobalResult>

    //Kode diatas untuk contoh
    //Kode untuk aplikasi mulai dari sini


    @GET("game")
    fun getAllGames(): Observable<ModelListWrapper<Games>>

    @GET("register/{id}")
    fun getQR(
        @Path("id") id: Int
    ): Observable<ModelWrapper<Peserta>>

    @GET("game/{slug}")
    fun getGameShow(
        @Path("slug") slug: String
    ): Observable<ModelWrapper<Games>>

//    @PATCH("register/{id}")
//    fun postPeserta(
//        @Path("id") id: String,
//        @Query("name") name: String,
//        @Query("hp") hp: String,
//        @Query("position_id") position: String
//    ): Observable<Peserta>

    @FormUrlEncoded
    @PUT("register/{id}")
    fun postPeserta(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("hp") hp: String,
        @Field("position_id") position_id: String
    ): Observable<PesertaUpdate>

    @FormUrlEncoded
    @PUT("finish/{id}")
    fun postScore(
        @Path("id") id: Int,
        @FieldMap fields: HashMap<String, Int>
    ): Observable<PesertaFinish>

    @PUT("finish/{id}")
    fun scanOut(
        @Path("id") id: String
    ): Observable<ModelWrapper<PesertaFinish>>

    @GET("finish/{id}")
    fun checkScore(
        @Path("id") id: String
    ): Observable<ModelWrapper<PesertaFinish>>

    @Multipart
    @POST("image/{id}")
    fun postImage(
        @Path("id") id: Int,
        @Part img: MultipartBody.Part
    ): Observable<PesertaFinish>


    @GET("position")
    fun getUnit(): Observable<ModelListWrapper<Unit>>

    @GET("travel/")
    fun getWisata(
        @Query("search") search: String
    ): Observable<ModelListWrapper<Games>>
}
