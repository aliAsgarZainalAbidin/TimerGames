package com.selayar.history.Retrofit

import android.os.Build
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.selayar.history.BuildConfig
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

//helper untuk retrofit tanpa melalui injector
object ApiFactory {

    //pakai coroutine untuk proses yang jalan di background
    //kasih timeout untuk proses yang kira2 makan waktu lama buat request
    fun create(useCoroutine: Boolean = false, timeOut: Long = 60): ApiInterface {
        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .supportsTlsExtensions(true)
                .tlsVersions(
                    TlsVersion.TLS_1_3,
                    TlsVersion.TLS_1_2,
                    TlsVersion.TLS_1_1,
                    TlsVersion.TLS_1_0
                )
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
                )
                .build()

            httpClient.connectionSpecs(Collections.singletonList(spec))
        }

        httpClient.readTimeout(timeOut, TimeUnit.SECONDS)
        httpClient.connectTimeout(timeOut, TimeUnit.SECONDS)
        httpClient.addInterceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }

        val client = httpClient.build()

        val retrofit =
            if (useCoroutine)
                Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .build()
            else
                Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .build()

        return retrofit.create(ApiInterface::class.java)
    }
}