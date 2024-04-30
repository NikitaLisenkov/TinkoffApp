package com.example.app.di.profile

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.ProfileRepositoryImpl
import com.example.app.di.ViewModelKey
import com.example.app.domain.repo.ProfileRepository
import com.example.app.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [ProfileModule.Bindings::class])
class ProfileModule {

    @Module
    interface Bindings {

        @Binds
        @ProfileScope
        fun profileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

        @ProfileScope
        @Binds
        @IntoMap
        @ViewModelKey(ProfileViewModel::class)
        fun bindViewModel(profileViewModel: ProfileViewModel): ViewModel
    }
}