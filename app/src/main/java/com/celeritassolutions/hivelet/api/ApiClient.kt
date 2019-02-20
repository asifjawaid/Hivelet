import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    var retrofit: Retrofit? = null
    val baseURL: String = "http://ec2-52-4-106-227.compute-1.amazonaws.com/demoassessment/"

    val client: Retrofit?
        get(){
            if (retrofit == null) {
                //TODO While release in Google Play Change the Level to NONE
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .build()

                retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }



}
/*

class ApiClient {

    companion object {

        val baseURL: String = "http://ec2-52-4-106-227.compute-1.amazonaws.com"
        var retofit: Retrofit? = null

        val client: Retrofit
            get() {

                if (retofit == null) {
                    retofit = Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
                return retofit!!
            }
    }
}*/
