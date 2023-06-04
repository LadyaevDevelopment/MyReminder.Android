package ldev.myNotifier.domain.util

data class OperationResult(
    val success: Boolean,
    val errorMessage: String? = null
)