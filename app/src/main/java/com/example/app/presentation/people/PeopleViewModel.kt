package com.example.app.presentation.people

import androidx.lifecycle.viewModelScope
import com.example.app.domain.model.UserModel
import com.example.app.domain.repo.PeopleRepository
import com.example.app.presentation.base.BaseViewModel
import com.example.app.presentation.people.model.PeopleUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PeopleViewModel @Inject constructor(
    override val reducer: PeopleReducer,
    repository: PeopleRepository
) : BaseViewModel<PeopleViewModel.State, PeopleViewModel.Action>(
    initState = State.Loading
) {

    init {
        val searchFlow = state.map { (it as? State.Content)?.searchText }
            .distinctUntilChanged()
            .debounce(500)

        combine(
            searchFlow,
            repository.getAllUsersFlow(),
        ) { searchText, users ->
            val filteredUsers = users.filter(searchText)
            sendAction(
                Action.UpdateUsers(filteredUsers)
            )
        }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)

        sendAction(Action.LoadData)
    }

    private fun List<UserModel>.filter(query: String?): List<UserModel> = if (query.isNullOrBlank()) this else {
        this.filter { it.fullName.contains(query, ignoreCase = true) }
    }

    sealed interface State {
        data object Loading : State

        data class Content(
            val people: List<PeopleUi>,
            val searchText: String?
        ) : State

        data object Error : State
    }

    sealed interface Action {
        data object LoadData : Action
        data class UpdateUsers(val users: List<UserModel>) : Action
        data class OnSearchText(val text: String) : Action
    }

}