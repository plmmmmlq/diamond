package com.wingsoft.demo.global

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wingsoft.demo.BuildConfig
import com.wingsoft.demo.model.Bean
import com.wingsoft.demo.model.TodayEvent
import io.reactivex.Flowable
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * ServiceFactory
 *
 * @author 祁连山
 * @date 2019-04-25
 * @version 1.0
 */
class ServiceFactory {

    interface ServiceApi {

        companion object {
            val HOST = "http://api.juheapi.com/japi/"
        }

        @GET("toh")
        fun getEventOfToday(@QueryMap map: HashMap<String, String>): Flowable<Bean<ArrayList<TodayEvent>>>
    }

    companion object {

        fun create() = provideRetrofit().create(ServiceApi::class.java)

        private fun createRetrofit(builder: Retrofit.Builder, client: OkHttpClient, gson: Gson, url: String) =
            builder.baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        private fun provideRetrofit() = createRetrofit(
            Retrofit.Builder(),
            provideHttpClient(OkHttpClient.Builder()),
            provideGsonObject(),
            ServiceApi.HOST
        )

        private fun provideGsonObject(): Gson = GsonBuilder().create()

        private fun provideHttpClient(builder: OkHttpClient.Builder): OkHttpClient {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }
            val cacheFile = File(Constants.PATH_CACHE)
            val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())
            val cacheInterceptor = Interceptor { chain ->
                var request = chain.request()
                if (!AppContext.i.isNetworkConnected()) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                }
                val response = chain.proceed(request)
                if (AppContext.i.isNetworkConnected()) {
                    val maxAge = 0
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .removeHeader("Pragma")
                        .build()
                } else {
                    // 无网络时，设置超时为4周
                    val maxStale = 60 * 60 * 24 * 28
                    response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
                }
                response
            }
            // Interceptor apikey = new Interceptor() {
            //     @Override
            //     public Response intercept(Chain chain) throws IOException {
            //         Request request = chain . request ();
            //         request = request.newBuilder()
            //             .addHeader("apikey", Constants.KEY_API)
            //             .build();
            //         return chain.proceed(request);
            //     }
            // }
            // 设置统一的请求头部参数
            // builder.addInterceptor(apikey);
            //设置缓存
            builder.addNetworkInterceptor(cacheInterceptor)
            builder.addInterceptor(cacheInterceptor)
            builder.cache(cache)
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS)
            builder.readTimeout(20, TimeUnit.SECONDS)
            builder.writeTimeout(20, TimeUnit.SECONDS)
            //错误重连
            builder.retryOnConnectionFailure(true)
            return builder.build()
        }
    }
}