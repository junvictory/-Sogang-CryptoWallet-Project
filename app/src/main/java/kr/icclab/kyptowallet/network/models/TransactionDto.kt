package kr.icclab.kyptowallet.network.models

import com.google.gson.annotations.SerializedName

data class TransactionDto(

    @SerializedName("blockNumber") val blockNumber: String? = null,
    @SerializedName("timeStamp") val timeStamp: String? = null,
    @SerializedName("hash") val hash: String? = null,
    @SerializedName("nonce") val nonce: String? = null,
    @SerializedName("blockHash") val blockHash: String? = null,
    @SerializedName("transactionIndex") val transactionIndex: String? = null,
    @SerializedName("from") val from: String? = null,
    @SerializedName("to") val to: String? = null,
    @SerializedName("value") val value: String? = null,
    @SerializedName("gas") val gas: String? = null,
    @SerializedName("gasPrice") val gasPrice: Double? = null,
    @SerializedName("isError") val isError: String? = null,
    @SerializedName("txreceipt_status") val txReceiptStatus: String? = null,
    @SerializedName("input") val input: String? = null,
    @SerializedName("contractAddress") val contractAddress: String? = null,
    @SerializedName("cumulativeGasUsed") val cumulativeGasUsed: String? = null,
    @SerializedName("gasUsed") val gasUsed: String? = null,
    @SerializedName("confirmations") val confirmations: String? = null,
)