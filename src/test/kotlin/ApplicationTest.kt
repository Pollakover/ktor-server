package com.example

import com.example.database.tokens.Tokens
import com.example.database.users.Users
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertTrue
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {


//    @Test
//    fun testRoot() = testApplication {
//        application {
//            module()
//        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//        }
//
//    }

    // 1. Успешная регистрация
    @Test
    fun testRegisterSuccess() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )

        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }

        application {
            module()
        }

        val response = client.post("/register") {

            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "testings",
                    "password": "testings",
                    "email": "test@gmail.com"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("token"))
        assertTrue(responseText.contains("-"))
    }

    // 2. Попытка регистрации с длинным логином
    @Test
    fun testRegisterLongName() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )

        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }

        application {
            module()
        }

        val response = client.post("/register") {

            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "testingtestingtestingtestingtestingtestingtestingtestingtesting",
                    "password": "testing",
                    "email": "test@gmail.com"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    // 3. Регистрация с существующим логином
    @Test
    fun testRegisterDuplicateLogin() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )
        
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }

        application {
            module()
        }

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "testing",
                    "password": "testing",
                    "email": "test@gmail.com"
                }
            """.trimIndent())
        }

        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "testing",
                    "password": "testing",
                    "email": "new@mail.com"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.Conflict, response.status) // 409 Conflict
    }

    // 4. Регистрация без email
    @Test
    fun testRegisterMissingEmail() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )
        
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }
        
        application {
            module()
        }

        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "test",
                    "password": "password123"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    // 5. Некорректный формат email
    @Test
    fun testRegisterInvalidEmail() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )
        
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }
        
        application {
            module()
        }

        val response = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "test",
                    "password": "test",
                    "email": "email"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    // 6. Проверка входа
    @Test
    fun testLogin() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )
        
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }
        
        application {
            module()
        }

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "test",
                    "password": "test",
                    "email": "test@gmail.com"
                }
            """.trimIndent())
        }

        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "test",
                    "password": "test"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    // 7. Вход с неправильным паролем
    @Test
    fun testLoginWrongPassword() = testApplication {
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
            driver = "org.h2.Driver"
        )
        
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }
        
        application {
            module()
        }

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "test",
                    "password": "test",
                    "email": "test@gmail.com"
                }
            """.trimIndent())
        }

        val response = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "login": "test",
                    "password": "..."
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
