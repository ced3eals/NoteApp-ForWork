package sebogo.lin.noteappbyced.database.local

import android.arch.persistence.room.Dao
import io.reactivex.Single
import sebogo.lin.noteappbyced.model.Note
import android.arch.persistence.room.Query


@Dao
interface NoteDao: BaseDoa<Note> {

    @Query("DELETE FROM Note")
    fun deleteAll()

    @Query("SELECT * FROM Note WHERE id = :idNote")
    fun getNotesById(idNote: Long): Single<Note>
}