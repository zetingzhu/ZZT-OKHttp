package com.example.zzt.okhttp.net.factoryV3


import retrofit2.Response

/**
 * @author: zeting
 * @date: 2025/4/10
 *
 */
sealed class ApiResponseV3<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "Unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponseV3<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) { // Handle empty body for success (204 No Content)
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: "Unknown error")
            }
        }
    }
}

/**
 * Separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponseV3<T>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponseV3<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponseV3<T>()