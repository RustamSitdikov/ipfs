package ru.mail.technotrack.ipfs.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import ru.mail.technotrack.ipfs.data.repository.IPFSRepository
import ru.mail.technotrack.ipfs.model.Document
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.mail.technotrack.ipfs.App
import ru.mail.technotrack.ipfs.di.component.DaggerAppComponent
import ru.mail.technotrack.ipfs.model.Info


class IPFSViewModel @Inject constructor(val application: Application, val repository: IPFSRepository) : ViewModel() {
    lateinit var infoLiveData: LiveData<Info>
    var documentsLiveData: MutableLiveData<MutableList<Document>> = MutableLiveData()
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        (application as App).appComponent.inject(this)
    }

    fun loadDocuments() {
        val documentsDisposable = repository.getDocuments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::updateDocuments)
        compositeDisposable.add(documentsDisposable)
    }

    fun updateDocuments(documents: MutableList<Document> ) {
        documentsLiveData.value = documents
    }

    fun getDocuments(): LiveData<MutableList<Document>> {
        loadDocuments()
        return documentsLiveData
    }

    fun getInfo(): LiveData<Info> {
        infoLiveData = repository.getInfo().toLiveData()
        return infoLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun <T> Observable<T>.toLiveData() : LiveData<T> {

        return object : MutableLiveData<T>() {
            var disposable : Disposable? = null

            override fun onActive() {
                super.onActive()

                // Observable -> LiveData
                disposable = this@toLiveData.subscribe {
                    this.postValue(it)
                }
            }

            override fun onInactive() {
                disposable?.dispose()
                super.onInactive()
            }
        }
    }
}