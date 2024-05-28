package com.example.app.di.people

import com.example.app.di.app.AppComponent
import com.example.app.di.app.modules.ViewModelModule
import com.example.app.presentation.people.PeopleFragment
import dagger.Component

@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        PeopleModule::class,
        ViewModelModule::class
    ]
)

@PeopleScope
interface PeopleComponent {
    fun inject(peopleFragment: PeopleFragment)
}