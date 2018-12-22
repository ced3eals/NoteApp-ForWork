package sebogo.lin.noteappbyced.database.api

class ApiAnswer<T> (
        val sucess: Boolean,
        val data: T
)