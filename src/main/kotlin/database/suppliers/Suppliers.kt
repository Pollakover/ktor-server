package com.example.database.suppliers

import com.example.database.products.Products
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Suppliers : Table("suppliers") {
    val supplierId = varchar("id", 50)
    val name = varchar("name", 25)
    val phone_number = varchar("phone_number", 50)
    val user_login = varchar("user_login", 25)
    val type = varchar("type", 25)

    fun insert(supplierDTO: SupplierDTO) {
        transaction {
            Suppliers.insert {
                it[supplierId] = supplierDTO.supplierId
                it[name] = supplierDTO.name
                it[phone_number] = supplierDTO.phone_number
                it[type] = supplierDTO.type
                it[user_login] = supplierDTO.user_login
            }
        }
    }

    fun fetchSuppliers(login: String): List<SupplierDTO> {
        return try {
            transaction {
                Suppliers.selectAll().where { Suppliers.user_login eq login }.toList().map {
                    val productNames = Products.fetchProductNamesBySupplier(it[supplierId])
                    SupplierDTO(
                        supplierId = it[supplierId],
                        name = it[name],
                        phone_number = it[phone_number],
                        type = it[type],
                        user_login = it[user_login],
                        products = productNames
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}