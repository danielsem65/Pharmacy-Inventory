package com.pharmacy.inventory.data.repository

import com.pharmacy.inventory.data.local.MedicineDao
import com.pharmacy.inventory.data.model.Medicine
import com.pharmacy.inventory.data.model.toMedicine
import com.pharmacy.inventory.network.MedicineApi
import kotlinx.coroutines.flow.Flow

class MedicineRepository(
    private val dao: MedicineDao,
    private val api: MedicineApi
) {
    val allMedicines: Flow<List<Medicine>> = dao.getAllMedicines()

    fun searchMedicines(query: String): Flow<List<Medicine>> = dao.searchMedicines(query)

    suspend fun getCount(): Int = dao.getCount()

    suspend fun refresh(): Result<Int> {
        return try {
            val response = api.getMedicines()
            dao.deleteAll()
            dao.insertAll(response.map { it.toMedicine() })
            Result.success(response.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
