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


class RegViewModel(private val mRepository: RegRepo): ViewModel(), LifecycleObserver {
    var idNote: Long? = null
    val description = ObservableField<String>("")
    val startdate = ObservableField<String>("")
    val endate = ObservableField<String>("")
    val publishErro = PublishSubject.create<Pair<Boolean, Int>>()!!
    val publishSucces = PublishSubject.create<Boolean>()!!
    val showProgress = ObservableField<Boolean>() ///////////////////////////////////////////
    val disposable = CompositeDisposable()


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        disposable.add(description.rxObservable()
            .debounce ( 1000,  TimeUnit.MILLISECONDS)
            .filter { it.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                publishErro.onNext(Pair(true, 0))
            }
        )
        disposable.add(startdate.rxObservable()
            .debounce ( 1000,  TimeUnit.MILLISECONDS)
            .filter { it.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                publishErro.onNext(Pair(true, 1))
            })
        disposable.add(endate.rxObservable()
            .debounce ( 1000,  TimeUnit.MILLISECONDS)
            .filter { it.length > 1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                publishErro.onNext(Pair(true, 2))
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
            startdate.set(note.startDate)
            endate.set(note.endDate)
        }
    }

    private fun createNote(): Single<Note> {
        return Single.create<Note> {
            val note = Note()
            note.description = description.get().toString()
            note.startDate = startdate.get().toString()
            note.endDate = endate.get().toString()
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
        startdate.set("")
        endate.set("")
        idNote = null
    }
    fun validDados2(): Single<Boolean> {
        return Single.create<Boolean> {
            if(description.get() == null || !description.get()?.isStringValid()!!){
                publishErro.onNext(Pair(false, 0))
                it.onError(Exception("Invalid Date !"))
            }else if (startdate.get() == null || !startdate.get()!!.isDate() && startdate.get()!!.dateInFormat("##/##/####") == null ){
                publishErro.onNext(Pair(false, 1))
                it.onError(Exception("Invalid Date !"))
            }else if (endate.get() == null || !endate.get()!!.isDate() && endate.get()!!.dateInFormat("##/##/####") == null ){
                publishErro.onNext(Pair(false, 2))
                it.onError(Exception("Invalid Date !"))
            }else{
                it.onSuccess(true)
            }
        }
    }

    fun validerDao(): Boolean{
        return if(description.get() == null || !description.get()?.isStringValid()!!){
            publishErro.onNext(Pair(false, 0))
            false
        }else if (startdate.get() == null || !startdate.get()!!.isDate() && startdate.get()!!.dateInFormat("##/##/####") == null ){
            publishErro.onNext(Pair(false, 1))
            false
        }else if (endate.get() == null || !endate.get()!!.isDate() && endate.get()!!.dateInFormat("##/##/####") == null ){
            publishErro.onNext(Pair(false, 2))
            false
        }else{
            true
        }
    }

    class Factory(
        val mRepository: RegRepo
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = RegViewModel(mRepository) as T
    }

}