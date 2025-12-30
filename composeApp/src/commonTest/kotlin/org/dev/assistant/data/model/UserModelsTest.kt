package org.dev.assistant.data.model

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserModelsTest {

    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }

    @Test
    fun testUserCreateRequestSerialization() {
        val request = UserCreateRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123",
            fullName = "Test User"
        )

        val jsonString = json.encodeToString(request)
        assertTrue(jsonString.contains("\"username\":\"testuser\""))
        assertTrue(jsonString.contains("\"email\":\"test@example.com\""))
        assertTrue(jsonString.contains("\"password\":\"password123\""))
        assertTrue(jsonString.contains("\"full_name\":\"Test User\""))
    }

    @Test
    fun testUserCreateRequestDeserialization() {
        val jsonString = """
            {
                "username": "testuser",
                "email": "test@example.com",
                "password": "password123",
                "full_name": "Test User"
            }
        """.trimIndent()

        val request = json.decodeFromString<UserCreateRequest>(jsonString)
        assertEquals("testuser", request.username)
        assertEquals("test@example.com", request.email)
        assertEquals("password123", request.password)
        assertEquals("Test User", request.fullName)
    }

    @Test
    fun testUserCreateRequestWithoutFullName() {
        val request = UserCreateRequest(
            username = "testuser",
            email = "test@example.com",
            password = "password123"
        )

        val jsonString = json.encodeToString(request)
        val decoded = json.decodeFromString<UserCreateRequest>(jsonString)
        assertEquals(null, decoded.fullName)
    }

    @Test
    fun testUserUpdateRequestSerialization() {
        val request = UserUpdateRequest(
            username = "newusername",
            email = "newemail@example.com",
            isActive = false
        )

        val jsonString = json.encodeToString(request)
        assertTrue(jsonString.contains("\"username\":\"newusername\""))
        assertTrue(jsonString.contains("\"email\":\"newemail@example.com\""))
        assertTrue(jsonString.contains("\"is_active\":false"))
    }

    @Test
    fun testUserUpdateRequestDeserialization() {
        val jsonString = """
            {
                "username": "newusername",
                "email": "newemail@example.com",
                "is_active": false
            }
        """.trimIndent()

        val request = json.decodeFromString<UserUpdateRequest>(jsonString)
        assertEquals("newusername", request.username)
        assertEquals("newemail@example.com", request.email)
        assertEquals(false, request.isActive)
    }

    @Test
    fun testUserLoginRequestSerialization() {
        val request = UserLoginRequest(
            username = "testuser",
            password = "password123"
        )

        val jsonString = json.encodeToString(request)
        assertTrue(jsonString.contains("\"username\":\"testuser\""))
        assertTrue(jsonString.contains("\"password\":\"password123\""))
    }

    @Test
    fun testUserLoginResponseDeserialization() {
        val jsonString = """
            {
                "user_id": "user123",
                "username": "testuser",
                "email": "test@example.com",
                "token": "abc123token",
                "message": "Login successful"
            }
        """.trimIndent()

        val response = json.decodeFromString<UserLoginResponse>(jsonString)
        assertEquals("user123", response.userId)
        assertEquals("testuser", response.username)
        assertEquals("test@example.com", response.email)
        assertEquals("abc123token", response.token)
        assertEquals("Login successful", response.message)
    }

    @Test
    fun testUserLoginResponseWithoutToken() {
        val jsonString = """
            {
                "user_id": "user123",
                "username": "testuser",
                "email": "test@example.com"
            }
        """.trimIndent()

        val response = json.decodeFromString<UserLoginResponse>(jsonString)
        assertEquals("user123", response.userId)
        assertEquals(null, response.token)
        assertEquals("Login successful", response.message)
    }

    @Test
    fun testUserResponseDeserialization() {
        val jsonString = """
            {
                "user_id": "user123",
                "username": "testuser",
                "email": "test@example.com",
                "full_name": "Test User",
                "created_at": "2024-01-01T00:00:00Z",
                "updated_at": "2024-01-02T00:00:00Z",
                "is_active": true,
                "last_login": "2024-01-03T00:00:00Z"
            }
        """.trimIndent()

        val response = json.decodeFromString<UserResponse>(jsonString)
        assertEquals("user123", response.userId)
        assertEquals("testuser", response.username)
        assertEquals("test@example.com", response.email)
        assertEquals("Test User", response.fullName)
        assertEquals("2024-01-01T00:00:00Z", response.createdAt)
        assertEquals("2024-01-02T00:00:00Z", response.updatedAt)
        assertEquals(true, response.isActive)
        assertEquals("2024-01-03T00:00:00Z", response.lastLogin)
    }

    @Test
    fun testUserListResponseDeserialization() {
        val jsonString = """
            {
                "users": [
                    {
                        "user_id": "user1",
                        "username": "user1",
                        "email": "user1@example.com",
                        "full_name": null,
                        "created_at": "2024-01-01T00:00:00Z",
                        "updated_at": "2024-01-01T00:00:00Z",
                        "is_active": true,
                        "last_login": null
                    },
                    {
                        "user_id": "user2",
                        "username": "user2",
                        "email": "user2@example.com",
                        "full_name": "User Two",
                        "created_at": "2024-01-01T00:00:00Z",
                        "updated_at": "2024-01-01T00:00:00Z",
                        "is_active": true,
                        "last_login": "2024-01-02T00:00:00Z"
                    }
                ],
                "count": 2
            }
        """.trimIndent()

        val response = json.decodeFromString<UserListResponse>(jsonString)
        assertEquals(2, response.count)
        assertEquals(2, response.users.size)
        assertEquals("user1", response.users[0].userId)
        assertEquals("user2", response.users[1].userId)
        assertEquals("User Two", response.users[1].fullName)
    }

    @Test
    fun testUserOperationResponseDeserialization() {
        val jsonString = """
            {
                "success": true,
                "message": "Operation completed successfully",
                "user_id": "user123",
                "data": {
                    "key1": "value1",
                    "key2": "value2"
                }
            }
        """.trimIndent()

        val response = json.decodeFromString<UserOperationResponse>(jsonString)
        assertEquals(true, response.success)
        assertEquals("Operation completed successfully", response.message)
        assertEquals("user123", response.userId)
        assertNotNull(response.data)
        assertEquals("value1", response.data?.get("key1"))
        assertEquals("value2", response.data?.get("key2"))
    }

    @Test
    fun testUserOperationResponseMinimal() {
        val jsonString = """
            {
                "success": false,
                "message": "Operation failed"
            }
        """.trimIndent()

        val response = json.decodeFromString<UserOperationResponse>(jsonString)
        assertEquals(false, response.success)
        assertEquals("Operation failed", response.message)
        assertEquals(null, response.userId)
        assertEquals(null, response.data)
    }
}

