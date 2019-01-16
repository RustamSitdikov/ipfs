package ru.mail.technotrack.ipfs.data.repository

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.mail.technotrack.ipfs.data.dao.IPFSDao
import ru.mail.technotrack.ipfs.data.network.IPFSApi
import ru.mail.technotrack.ipfs.model.Document
import ru.mail.technotrack.ipfs.model.Info
import javax.inject.Singleton
import javax.inject.Inject


class IPFSRepository constructor(private val api: IPFSApi, private val dao: IPFSDao) {

    fun getInfo(): Observable<Info> {
        return api.getInfo()
    }

    fun getDocuments(): Observable<MutableList<Document>> {
        return Observable.concatArray(
            getDocumentsFromDatabase(),
            getDocumentsFromApi())
    }

    private fun getDocumentsFromDatabase(): Observable<MutableList<Document>> {
        return dao.selectDocuments()
    }

    private fun getDocumentsFromApi(): Observable<MutableList<Document>> {
        return api.getDocuments()
            .doOnNext {
                storeDocumentsInDatabase(it)
            }
    }

    private fun storeDocumentsInDatabase(documents: MutableList<Document>) {
        Observable.fromCallable { dao.insertDocuments(documents) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe()
    }
}