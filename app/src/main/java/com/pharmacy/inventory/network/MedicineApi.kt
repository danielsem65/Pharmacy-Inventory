package com.pharmacy.inventory.network

import com.pharmacy.inventory.data.model.MedicineResponse
import retrofit2.http.GET

interface MedicineApi {
    @GET("public/5b34b860-5791-454a-9838-c30b35d3883f")
    suspend fun getMedicines(): List<MedicineResponse>
}
