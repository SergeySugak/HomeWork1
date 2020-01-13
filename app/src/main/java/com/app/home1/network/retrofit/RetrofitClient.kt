package com.app.home1.network.retrofit

import android.annotation.SuppressLint
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_CBC_SHA256
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_256_CBC_SHA256
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_128_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_128_CBC_SHA256
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_256_CBC_SHA
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_256_CBC_SHA256
import okhttp3.CipherSuite.Companion.TLS_RSA_WITH_AES_256_GCM_SHA384
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object RetrofitClient {
    private const val DEF_TIMEOUT: Long = 60
    private val loggingLevel = HttpLoggingInterceptor.Level.BODY

    private fun getRestAdapter(
        endpoint: String,
        interceptor: Interceptor?
    ): Retrofit {
        val okHttpClient = getOkHttpClient(interceptor)
        return Retrofit.Builder()
            .baseUrl(endpoint)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) //The addCallAdapterFactory(RxJavaCallAdapterFactory.create()) is crucial when using RxJava
            .build()
    }

    private fun getOkHttpClient(interceptor: Interceptor?): OkHttpClient {
        return try { // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @SuppressLint("TrustAllX509TrustManager")
                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.RESTRICTED_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                    TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                    TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,
                    TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                    TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,
                    TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                    TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,
                    TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                    TLS_RSA_WITH_AES_128_GCM_SHA256,
                    TLS_RSA_WITH_AES_256_GCM_SHA384,
                    TLS_RSA_WITH_AES_128_CBC_SHA256,
                    TLS_RSA_WITH_AES_256_CBC_SHA256,
                    TLS_RSA_WITH_AES_128_CBC_SHA,
                    TLS_RSA_WITH_AES_256_CBC_SHA
                )
                .build()
            val builder = OkHttpClient.Builder()
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = loggingLevel
            builder.addInterceptor(httpLoggingInterceptor)
            if (interceptor != null) builder.networkInterceptors().add(interceptor)
            builder.connectTimeout(
                DEF_TIMEOUT,
                TimeUnit.SECONDS
            )
            builder.readTimeout(DEF_TIMEOUT, TimeUnit.SECONDS)
            builder.writeTimeout(DEF_TIMEOUT, TimeUnit.SECONDS)
            builder.sslSocketFactory(
                sslSocketFactory,
                (trustAllCerts[0] as X509TrustManager)
            )
            builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
            builder.connectionSpecs(listOf(spec, ConnectionSpec.CLEARTEXT))
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun <T> getRetrofitService(
        serviceClass: Class<T>,
        endPoint: String,
        interceptor: Interceptor?
    ): T {
        return getRestAdapter(endPoint, interceptor).create(serviceClass)
    }

}