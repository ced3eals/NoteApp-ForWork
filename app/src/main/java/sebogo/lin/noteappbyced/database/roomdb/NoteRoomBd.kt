package sebogo.lin.noteappbyced.database.roomdb

import android.content.Context
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Room
import sebogo.lin.noteappbyced.database.local.NoteDao
import sebogo.lin.noteappbyced.database.local.NoteListDao
import sebogo.lin.noteappbyced.model.Note


@Database(entities = [(Note::class)], version = 1)
abstract class NoteRoomBd: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun listNoteDao(): NoteListDao

    companion object {
        private var INSTANCE: NoteRoomBd? = null
        fun getDatabase(context: Context): NoteRoomBd? {
            if (INSTANCE == null) {
                synchronized(NoteRoomBd::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context,
                                NoteRoomBd::class.java, "my_notes_database")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}