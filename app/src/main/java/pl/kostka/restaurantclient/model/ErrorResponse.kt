package pl.kostka.restaurantclient.model

class ErrorResponse(val timestamp: String? = null,
                    val status: Int = 404,
                    val error: String = "NOT_FOUND",
                    val message: String = "Brak połączenia z serwerem",
                    val path: String? = null){

    fun getMsg(): String {
        return "$status $error: $message"
    }
}