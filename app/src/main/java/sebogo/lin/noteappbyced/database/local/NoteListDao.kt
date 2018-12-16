package sebogo.lin.noteappbyced.database.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import sebogo.lin.noteappbyced.model.Note

@Dao
interface NoteListDao: BaseDoa<Note> {

    @Query("SELECT * from Note ORDER BY id ASC")
    fun getAllNotes(): Flowable<List<Note>>
}