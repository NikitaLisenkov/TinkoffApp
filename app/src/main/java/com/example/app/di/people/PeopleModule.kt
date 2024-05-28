package com.example.app.di.people

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.PeopleRepositoryImpl
import com.example.app.di.ViewModelKey
import com.example.app.domain.repo.PeopleRepository
import com.example.app.presentation.people.PeopleViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PeopleModule {
    @Binds
    @PeopleScope
    fun peopleRepository(peopleRepositoryImpl: PeopleRepositoryImpl): PeopleRepository

    @PeopleScope
    @Binds
    @IntoMap
    @ViewModelKey(PeopleViewModel::class)
    fun bindPeopleViewModel(peopleViewModel: PeopleViewModel): ViewModel
}
