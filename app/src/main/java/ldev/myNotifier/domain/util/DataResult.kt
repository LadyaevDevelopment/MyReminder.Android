package ldev.myNotifier.domain.util

class DataResult<T> private constructor(
    val success: Boolean,
    val data: T? = null,
    val errorMessage: String? = null
) {
    companion object {
        fun <T> success(data: T): DataResult<T> {
            return DataResult(success = true, data = data)
        }
        fun <T> error(errorMessage: String?): DataResult<T> {
            return DataResult(success = false, errorMessage = errorMessage)
        }
    }
}