package sebogo.lin.noteappbyced.database.local

import android.arch.persistence.room.*

@Dao
interface BaseDoa<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Long
    @Update
    fun update(t: T)
    @Delete
    fun delete(t: T)
}