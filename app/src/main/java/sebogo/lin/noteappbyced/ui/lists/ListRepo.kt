package sebogo.lin.noteappbyced.ui.lists

import io.reactivex.Flowable
import io.reactivex.Single
import sebogo.lin.noteappbyced.database.local.NoteListDao
import sebogo.lin.noteappbyced.model.Note


class ListRepo(private val mListDao: NoteListDao) {

    private var shownotes: Flowable<List<Note>>? = null
    fun listAllNotes(): Flowable<List<Note>> {
        if (shownotes == null ){
            shownotes = mListDao.getAllNotes()
        }
        return shownotes!!
    }

    fun deleteNote(note: Note): Single<Unit> {
        return Single.create {
            mListDao.delete(note)
            it.onSuccess(Unit)
        }
    }
}