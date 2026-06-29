package com.pharmacy.inventory

import android.app.Application
import com.pharmacy.inventory.data.local.MedicineDatabase
import com.pharmacy.inventory.data.repository.MedicineRepository
import com.pharmacy.inventory.network.MedicineApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PharmacyApp : Application() {
    val database by lazy { MedicineDatabase.getInstance(this) }

    val repository by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jsonsilo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        MedicineRepository(
            database.medicineDao(),
            retrofit.create(MedicineApi::class.java)
        )
    }
}
