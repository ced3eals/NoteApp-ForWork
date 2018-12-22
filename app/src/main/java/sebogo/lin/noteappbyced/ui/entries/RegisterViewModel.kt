package sebogo.lin.noteappbyced.ui.entries

import android.arch.lifecycle.*
import android.databinding.ObservableField
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import io.reactivex.subjects.PublishSubject
import sebogo.lin.noteappbyced.model.Note
import sebogo.lin.noteappbyced.utilities.dateInFormat
import sebogo.lin.noteappbyced.utilities.isDate
import sebogo.lin.noteappbyced.utilities.isStringValid
import sebogo.lin.noteappbyced.utilities.rxObservable

class RegisterViewModel(private val mRepository: RegisterRepo): ViewModel(), LifecycleObserver {

    val description = ObservableField<String>("")
    val start = ObservableField<String>("")
    val endDate = ObservableField<String>("")
    val publishError = PublishSubject.create<Pair<Boolean, Int>>()!!
    val publishSucces = PublishSubject.create<Boolean>()!!
    var idNote: Long? = null
    val showProgress = ObservableField<Boolean>() ///////////////////////////////////////////
    val disposable = CompositeDisposable()


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        disposable.add(description.rxObservable()
            .debounce ( 1000,  TimeUnit.MILLISECONDS)
            .filter { it.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                publishError.onNext(Pair(true, 0))
            }
        )
        disposable.add(start.rxObservable()
            .debounce ( 1000,  TimeUnit.MILLISECONDS)
            .filter { it.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                publishError.onNext(Pair(true, 1))
            })
        disposable.add(endDate.rxObservable()
            .debounce ( 1000,  TimeUnit.MILLISECONDS)
            .filter { it.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                publishError.onNext(Pair(true, 2))
            })
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        disposable.clear()
    }

    fun setEditNote(note: Note?){
        if(note != null) {
            idNote = note.id
            description.set(note.description)
            start.set(note.startDate)
            endDate.set(note.endDate)
        }
    }

    private fun createNote(): Single<Note> {
        return Single.create<Note> {
            val note = Note()
            note.description = description.get().toString()
            note.startDate = start.get().toString()
            note.endDate = endDate.get().toString()
            note.id = idNote
            it.onSuccess(note)
        }
    }

    fun saveNote(){
        if (validerDao()){
            disposable.add(createNote()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { showProgress.set(true) }
                .flatMap{ mRepository.registerNote(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { showProgress.set(false) }
                .subscribe ({
                    publishSucces.onNext(it)
                    clearData()
                },{
                    publishSucces.onNext(false)
                }))
        }

    }

    private fun clearData() {
        description.set("")
        start.set("")
        endDate.set("")
        idNote = null
    }
    fun validerDao2(): Single<Boolean> {
        return Single.create<Boolean> {
            if(description.get() == null || !description.get()?.isStringValid()!!){
                publishError.onNext(Pair(false, 0))
                it.onError(Exception("Invalid Date !"))
            }else if (start.get() == null || !start.get()!!.isDate() && start.get()!!.dateInFormat("##/##/####") == null ){
                publishError.onNext(Pair(false, 1))
                it.onError(Exception("Invalid Date !"))
            }else if (endDate.get() == null || !endDate.get()!!.isDate() && endDate.get()!!.dateInFormat("##/##/####") == null ){
                publishError.onNext(Pair(false, 2))
                it.onError(Exception("Invalid Date !"))
            }else{
                it.onSuccess(true)
            }
        }
    }

    fun validerDao(): Boolean{
        return if(description.get() == null || !description.get()?.isStringValid()!!){
            publishError.onNext(Pair(false, 0))
            false
        }else if (start.get() == null || !start.get()!!.isDate() && start.get()!!.dateInFormat("##/##/####") == null ){
            publishError.onNext(Pair(false, 1))
            false
        }else if (endDate.get() == null || !endDate.get()!!.isDate() && endDate.get()!!.dateInFormat("##/##/####") == null ){
            publishError.onNext(Pair(false, 2))
            false
        }else{
            true
        }
    }

    class Factory(
        val mRepository: RegisterRepo
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = RegisterViewModel(mRepository) as T
    }

}