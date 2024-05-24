package com.example.app.presentation.people

import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.UserModel
import com.example.app.domain.repo.PeopleRepository
import com.example.app.presentation.base.BaseViewModel
import com.example.app.presentation.people.model.PeopleUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    override val reducer: PeopleReducer,
    repo: PeopleRepository
) : BaseViewModel<PeopleViewModel.State, PeopleViewModel.Action>(
    initState = State.Loading
) {

    init {
        repo.getAllUsersFlow()
            .onEach { sendAction(Action.UpdateUsers(it)) }
            .launchIn(viewModelScope)

        sendAction(Action.LoadData)
    }

    sealed interface State {
        data object Loading : State
        data class Content(val people: List<PeopleUi>) : State
        data object Error : State
    }

    sealed interface Action {
        data object LoadData : Action
        data class UpdateUsers(val users: List<UserModel>) : Action
    }

}