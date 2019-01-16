package ru.mail.technotrack.ipfs.di.module

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.mail.technotrack.ipfs.viewmodel.IPFSViewModel
import ru.mail.technotrack.ipfs.viewmodel.ViewModelFactory
import ru.mail.technotrack.ipfs.viewmodel.ViewModelKey


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(IPFSViewModel::class)
    abstract fun bindViewModel(ipfsViewModel: IPFSViewModel): IPFSViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelFactory
}