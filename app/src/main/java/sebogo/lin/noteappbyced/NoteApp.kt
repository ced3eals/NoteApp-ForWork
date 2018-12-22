package sebogo.lin.noteappbyced

import android.app.Application


class NoteApp: Application() {
    companion object {
        var contextApp: NoteApp? = null
    }

    override fun onCreate() {
        NoteApp.contextApp = this
        super.onCreate()
    }
}