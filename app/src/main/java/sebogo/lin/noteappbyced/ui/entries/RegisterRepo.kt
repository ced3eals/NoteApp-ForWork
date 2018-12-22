package sebogo.lin.noteappbyced.ui.entries

import io.reactivex.Single
import sebogo.lin.noteappbyced.database.api.ApiRegService
import sebogo.lin.noteappbyced.database.local.NoteDao
import sebogo.lin.noteappbyced.model.Note


class RegisterRepo(private val mNoteDao: NoteDao, private val mApiNote: ApiRegService) {

    fun registerNote(note: Note): Single<Boolean> {

        return mApiNote.addaNote(0, note.startDate, note.endDate)
            .map {
                if (!it.sucess) {
                    throw Exception("Cannot save your note !")
                }
                mNoteDao.insert(note)
            }
            .map {
                it > 0
            }
    }
}