package com.example.app.di.profile

import androidx.lifecycle.ViewModel
import com.example.app.data.repository.ProfileRepositoryImpl
import com.example.app.di.ViewModelKey
import com.example.app.domain.repo.ProfileRepository
import com.example.app.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ProfileModule {
    @Binds
    @ProfileScope
    fun profileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @ProfileScope
    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel
}
