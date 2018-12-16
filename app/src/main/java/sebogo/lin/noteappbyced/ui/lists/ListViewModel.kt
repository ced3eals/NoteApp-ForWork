package sebogo.lin.noteappbyced.ui.lists

import android.arch.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import sebogo.lin.noteappbyced.model.Note
import sebogo.lin.noteappbyced.ui.NoteAdapter


class ListViewModel(private val mRepo: ListRepo): ViewModel(), LifecycleObserver {

    val goodTransaction = PublishSubject.create<Boolean>()!!
    val disposable = CompositeDisposable()
    val resulClickEdit = PublishSubject.create<Note>()!!
    val resultClickDelete = PublishSubject.create<Note>()!!

    private val resultCLick = object : NoteAdapter.inteAdapter{
        override fun onClickItem(note: Note, enumOptionMenu: NoteAdapter.EnumOptionMenu) {
            when(enumOptionMenu){
                NoteAdapter.EnumOptionMenu.EDIT -> resulClickEdit.onNext(note)
                NoteAdapter.EnumOptionMenu.DELETE -> resultClickDelete.onNext(note)
            }
        }

    }

    val noteAdapter = NoteAdapter(listOf(), resultCLick)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        disposable.add(mRepo
            .listAllNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                noteAdapter.listNotes = it
                noteAdapter.notifyDataSetChanged()
            })
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        disposable.clear()
    }

    fun deleteNote(note: Note){
        disposable.add(mRepo.deleteNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                goodTransaction.onNext(true)
            },{
                goodTransaction.onError(it)
            }))

    }

    class Factory(
        private val mRepo : ListRepo
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ListViewModel(mRepo) as T
    }
}