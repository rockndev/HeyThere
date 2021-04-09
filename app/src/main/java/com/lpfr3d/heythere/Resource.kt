package com.lpfr3d.heythere

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCESSO, data = data, message = null)

        fun <T> error(data: T?, message: String?): Resource<T> =
            Resource(status = Status.ERRO, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.CARREGANDO, data = data, message = null)
    }
}