package kr.icclab.kyptowallet.network.models

import com.google.gson.annotations.SerializedName


data class SearchResponseDto (

	@SerializedName("status") val status : Int? = null,
	@SerializedName("message") val message : String? = null,
	@SerializedName("result") val transactions : List<TransactionDto>? = null,

)