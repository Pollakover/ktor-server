package com.example.database.warehouses

import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object Warehouses: Table("warehouses") {
    val warehouseId = varchar("id", 50)
    val name = varchar("name", 25)
    val address = varchar("address", 50)
    val postal_address = varchar("postal_address", 25)
    val user_login = varchar("user_login", 25)

    fun insert(warehouseDTO: WarehouseDTO) {
        transaction {
            Warehouses.insert {
                it[warehouseId] = warehouseDTO.warehouseId
                it[name] = warehouseDTO.name
                it[address] = warehouseDTO.address
                it[postal_address] = warehouseDTO.postal_address
                it[user_login] = warehouseDTO.user_login
            }
        }
    }

    fun fetchWarehouses(login: String): List<WarehouseDTO> {
        return try {
            transaction {
                Warehouses.selectAll().where { user_login eq login }.toList().map {
                    WarehouseDTO(
                        warehouseId = it[warehouseId],
                        name = it[name],
                        address = it[address],
                        postal_address = it[postal_address],
                        user_login = it[user_login]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getWarehouseById(warehouseId: String): WarehouseDTO? {
        return try {
            transaction {
                Warehouses.selectAll().where { Warehouses.warehouseId eq warehouseId }.singleOrNull()?.let {
                    WarehouseDTO(
                        warehouseId = it[Warehouses.warehouseId],
                        name = it[name],
                        address = it[address],
                        postal_address = it[postal_address],
                        user_login = it[user_login],
                    )
                }
            }
        } catch (e: Exception) {
            null
        }
    }
}