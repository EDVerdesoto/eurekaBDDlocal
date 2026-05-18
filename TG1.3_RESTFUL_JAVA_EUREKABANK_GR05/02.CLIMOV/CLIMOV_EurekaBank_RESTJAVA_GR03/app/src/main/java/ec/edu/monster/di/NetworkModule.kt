package ec.edu.monster.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ec.edu.monster.services.EurekaBankApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://javasoto.dr00p3r.top/WS_EurekaBank_RESTJAVA_GR03/api/eurekabank/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideEurekaBankApi(retrofit: Retrofit): EurekaBankApi {
        return retrofit.create(EurekaBankApi::class.java)
    }
}
