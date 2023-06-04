package ldev.myNotifier.domain.util

data class DataResult<T>(
    val success: Boolean,
    val data: T? = null,
    val errorMessage: String? = null
)