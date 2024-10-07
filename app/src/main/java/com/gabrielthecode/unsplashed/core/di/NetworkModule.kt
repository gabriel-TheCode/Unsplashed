package com.gabrielthecode.unsplashed.core.di

import com.gabrielthecode.unsplashed.BuildConfig
import com.gabrielthecode.unsplashed.core.remote.RemoteDataSourceImpl
import com.gabrielthecode.unsplashed.datasource.UnsplashApiRemoteService
import com.gabrielthecode.unsplashed.datasource.UnsplashApiRemoteServiceImpl
import com.gabrielthecode.unsplashed.datasource.network.api.UnsplashApi
import com.gabrielthecode.unsplashed.datasource.network.mapper.SearchMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	private const val BASE_URL = "https://api.unsplash.com/"
	private const val CLIENT_ID_QUERY = "client_id"
	private const val REQUEST_TIMEOUT = 60

	@Singleton
	@Provides
	fun provideGsonBuilder(): Gson {
		return GsonBuilder().create()
	}

	@Singleton
	@Provides
	fun provideRetrofit(gson: Gson): Retrofit.Builder {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.client(getOkHttpService())
	}

	@Singleton
	@Provides
	fun provideUnsplashService(retrofit: Retrofit.Builder): UnsplashApi {
		return retrofit
			.build()
			.create(UnsplashApi::class.java)
	}

	@Singleton
	@Provides
	fun provideRemoteService(
		api: UnsplashApi
	): UnsplashApiRemoteService {
		return UnsplashApiRemoteServiceImpl(api)
	}

	@Singleton
	@Provides
	fun provideRemoteDataSource(
		remoteService: UnsplashApiRemoteService,
		searchMapper: SearchMapper
	): RemoteDataSourceImpl {
		return RemoteDataSourceImpl(
			remoteService,
			searchMapper
		)
	}

	private fun getOkHttpService(): OkHttpClient {
		val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
			.connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
			.readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
			.writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
		if (BuildConfig.DEBUG) {
			val interceptor = HttpLoggingInterceptor()
			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
			httpClient.addInterceptor(interceptor)
		}

		httpClient.addInterceptor(BasicAuthInterceptor())

		return httpClient.build()
	}

	class BasicAuthInterceptor : Interceptor {
		@Throws(IOException::class)
		override fun intercept(chain: Chain): Response {
			val request = chain.request()
			val newUrl =
				request.url.newBuilder().addQueryParameter(CLIENT_ID_QUERY, BuildConfig.CLIENT_ID)
					.build()
			val newRequest = request.newBuilder().url(newUrl).build()
			return chain.proceed(newRequest)
		}
	}
}
