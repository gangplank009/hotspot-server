package me.gangplank.serverhotspot

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import me.gangplank.forecastmvvm.data.network.response.CurrentWeatherResponse
import me.gangplank.forecastmvvm.data.network.response.FutureWeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val SERVER_KEY = "AAAASXJMKMc:APA91bFMPzDnt8z-1YBAB3nQ-uv7jfHevetOBx3D8dmA8647O9HZ7OZGmHfhk68tMSKo6uzZ99XoGFN-Q2y9JFAdxdOh6uB4F5zRD9jB_bRDtnmQWzwtGKiL4ejheCsypHE8s1DVkYIe"

interface FirebaseServerInterface {

    @POST()
    fun getCurrentWeather(
        @Query(value = "q") location: String,
        @Query(value = "lang") languageCode: String = "en"
    ): Deferred<CurrentWeatherResponse>


    companion object {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://fcm.googleapis.com/fcm/send")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FirebaseServerInterface::class.java)
        }
    }
}