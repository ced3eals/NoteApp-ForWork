package sebogo.lin.noteappbyced.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull


@Entity
class Note() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Long? = null
    var description: String = ""
    var startDate: String = ""
    var endDate: String = ""

    override fun describeContents() = 0
    constructor(source: Parcel) : this()


    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Note> = object : Parcelable.Creator<Note> {
            override fun createFromParcel(source: Parcel): Note = Note(source)
            override fun newArray(size: Int): Array<Note?> = arrayOfNulls(size)
        }
    }
}