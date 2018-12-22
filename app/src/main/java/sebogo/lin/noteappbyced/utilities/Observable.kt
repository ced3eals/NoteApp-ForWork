package sebogo.lin.noteappbyced.utilities

import android.databinding.Observable
import android.databinding.ObservableField


@Suppress("UNCHECKED_CAST")
inline fun <R> ObservableField<R>.rxObservable(): io.reactivex.Observable<R> = io.reactivex.Observable.create { subscriber ->
    object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(observable: Observable, id: Int) = try {
            subscriber.onNext(this@rxObservable.get() as R)
        } catch (e: Exception) {
            subscriber.onError(e)
        }
    }.let {
        subscriber.setCancellable { this.removeOnPropertyChangedCallback(it) }
        this.addOnPropertyChangedCallback(it)
    }
}