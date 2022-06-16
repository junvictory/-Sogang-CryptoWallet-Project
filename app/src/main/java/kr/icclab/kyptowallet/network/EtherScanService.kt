package kr.icclab.kyptowallet.network

import kr.icclab.kyptowallet.network.models.SearchResponseDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EtherScanService {
    //Sort = ASC 오름차순 DESC 내림차순
    @GET("api?module=account&action=txlist&startblock=0&endblock=99999999&page=1&offset=10&sort=desc&apikey=${NetworkConstants.etherscan_api_key}")
    fun getTransactionsForAddress(@Query("address") address: String): Call<SearchResponseDto>

}