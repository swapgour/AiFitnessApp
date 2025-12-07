package com.example.aifitnessapp.data.ai

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenAIClient {

    fun create(apiKey: String): OpenAIService {

        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $apiKey")
                            .build()
                        chain.proceed(request)
                    }.build()
            )
            .build()
            .create(OpenAIService::class.java)
    }
}
