package com.pharmacy.inventory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val responsible: String?,
    val salesPrice: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)

data class MedicineResponse(
    val Name: String,
    val Responsible: String?,
    @SerializedName("Sales Price") val SalesPrice: Double
)

fun MedicineResponse.toMedicine() = Medicine(
    name = Name,
    responsible = Responsible,
    salesPrice = SalesPrice
)
