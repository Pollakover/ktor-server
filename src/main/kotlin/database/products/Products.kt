package com.example.database.products

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object Products : Table("products") {
    val productId = varchar("id", 50)
    val name = varchar("name", 25)
    val amount_sold = integer("amount_sold")
    val category = varchar("category", 25)
    val price = double("price")
    val user_login = varchar("user_login", 25)
    val supplier = varchar("supplier", 50)
    val warehouse = varchar("warehouse", 50)
    val image_data = varchar("image_data", 100)
    val amount = integer("amount")

    fun insert(productDTO: ProductDTO) {
        transaction {
            Products.insert {
                it[productId] = productDTO.productId
                it[name] = productDTO.name
                it[amount_sold] = productDTO.amount_sold
                it[category] = productDTO.category
                it[price] = productDTO.price
                it[user_login] = productDTO.user_login
                it[supplier] = productDTO.supplier
                it[warehouse] = productDTO.warehouse
                it[image_data] = productDTO.image_data ?: ""
                it[amount] = productDTO.amount
            }
        }
    }

    fun fetchProducts(login: String): List<ProductDTO> {
        return try {
            transaction {
                Products.selectAll().where { user_login eq login }.toList().map {
                    ProductDTO(
                        productId = it[productId],
                        name = it[name],
                        amount_sold = it[amount_sold],
                        category = it[category],
                        price = it[price],
                        user_login = it[user_login],
                        supplier = it[supplier],
                        warehouse = it[warehouse],
                        image_data = it[image_data],
                        amount = it[amount],
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun fetchProductNamesBySupplier(supplierId: String): List<String> {
        return try {
            transaction {
                Products.selectAll()
                    .where { Products.supplier eq supplierId }
                    .map { it[Products.name] }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}