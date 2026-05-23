package com.example.database.orders

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll

object Orders  : Table("orders") {
    val orderId = varchar("id", 50)
    val amount = integer("amount")
    val delivery_date = varchar("delivery_date", 10)
    val user_login = varchar("user_login", 25)
    val status = varchar("status", 25)
    val number = integer("number")
    val product = varchar("product", 50)
    val price = double("price")

    fun insert(orderDTO: OrderDTO) {
        transaction {
            Orders.insert {
                it[orderId] = orderDTO.orderId
                it[amount] = orderDTO.amount
                it[delivery_date] = orderDTO.delivery_date
                it[status] = orderDTO.status
                it[user_login] = orderDTO.user_login
                it[product] = orderDTO.product
                it[price] = orderDTO.price
            }
        }
    }

    fun fetchOrders(login: String): List<OrderDTO> {
        return try {
            transaction {
                Orders.selectAll().where { user_login eq login }.toList().map {
                    OrderDTO(
                        orderId = it[orderId],
                        amount = it[amount],
                        delivery_date = it[delivery_date],
                        status = it[status],
                        user_login = it[user_login],
                        product = it[product],
                        number = it[number],
                        price = it[price]
                    )
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}