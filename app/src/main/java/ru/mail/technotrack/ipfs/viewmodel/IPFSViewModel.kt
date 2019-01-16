package ru.mail.technotrack.ipfs.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import ru.mail.technotrack.ipfs.data.repository.IPFSRepository
import ru.mail.technotrack.ipfs.model.Document
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.mail.technotrack.ipfs.model.Info


class IPFSViewModel : ViewModel() {
    lateinit var infoLiveData: LiveData<Info>
    lateinit var documentsLiveData: MutableLiveData<MutableList<Document>>
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var repository: IPFSRepository

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
        if(!::documentsLiveData.isInitialized) {
            documentsLiveData = MutableLiveData()
            compositeDisposable = CompositeDisposable()
            loadDocuments()
        }
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