package com.emmm.mobv.data.api.model

import com.google.gson.annotations.SerializedName

data class TickerResponse(
    val _meta: TickerExternalPricesResponse
)

data class TickerExternalPricesResponse(
    val externalPrices: TickerExternalPriceResponse
)

data class TickerExternalPriceResponse(
    val USD_XLM: String
)

data class PaymentResponse(
    val _embedded: PaymentEmbeddedResponse
)

data class PaymentEmbeddedResponse(
    val records: List<PaymentRecordResponse>
)

data class PaymentRecordResponse(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("asset_type")
    val assetType: String,
    @SerializedName("asset_code")
    val assetCode: String,
    @SerializedName("asset_issuer")
    val assetIssuer: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("transaction_hash")
    val transactionHash: String,
    @SerializedName("type")
    val type: String,
)

