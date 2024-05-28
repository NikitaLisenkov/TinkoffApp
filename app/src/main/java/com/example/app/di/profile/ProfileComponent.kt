package com.example.app.di.profile

import com.example.app.di.app.AppComponent
import com.example.app.di.app.modules.DataSourceModule
import com.example.app.di.app.modules.ViewModelModule
import com.example.app.presentation.profile.ProfileFragment
import dagger.Component

@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        ProfileModule::class,
        ViewModelModule::class,
        DataSourceModule::class
    ]
)
@ProfileScope
interface ProfileComponent {
    fun inject(profileFragment: ProfileFragment)
}