package kr.icclab.kyptowallet.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kr.icclab.kyptowallet.network.EtherScanService
import kr.icclab.kyptowallet.network.NetworkConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class NetworkModule {

    @Provides
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    fun getEtherScanService(okHttpClient: OkHttpClient): EtherScanService {
        var retrofit= Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
        return retrofit.baseUrl(NetworkConstants.etherscan_api_base_url)
            .build()
            .create(EtherScanService::class.java)
    }
}