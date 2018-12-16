package sebogo.lin.noteappbyced.database.api

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import sebogo.lin.noteappbyced.model.Note


interface ApiRegService {

    @FormUrlEncoded
    @POST("v2/5c13b90b3400003832ece252")
    fun addaNote(@Field("id") id: Long,
                 //@Field( "description") description: String, //added
                 @Field("startDate") startDate: String,
                 @Field("endDate") endDate: String):
            Single<ApiAnswer<Note>>
}