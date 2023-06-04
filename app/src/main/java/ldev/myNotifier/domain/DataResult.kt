package ldev.myNotifier.domain

data class DataResult<T>(
    val success: Boolean,
    val data: T? = null,
    val errorMessage: String? = null
)