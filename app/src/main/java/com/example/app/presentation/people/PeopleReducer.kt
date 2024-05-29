package com.example.app.presentation.people

import com.example.app.domain.repo.PeopleRepository
import com.example.app.presentation.base.BaseReducer
import com.example.app.presentation.people.PeopleViewModel.Action
import com.example.app.presentation.people.PeopleViewModel.State
import com.example.app.presentation.people.model.PeopleUi
import com.example.app.utils.runSuspendCatching
import javax.inject.Inject

class PeopleReducer @Inject constructor(
    private val repo: PeopleRepository
) : BaseReducer<State, Action> {

    override suspend fun reduce(
        currentState: State,
        action: Action
    ): State? {
        return when (action) {
            is Action.LoadData -> {
                val isError = runSuspendCatching { repo.fetchAllUsers() } == null
                if (isError) {
                    State.Error
                } else {
                    null
                }
            }

            is Action.UpdateUsers -> {
                val people = action.users.map { user ->
                    PeopleUi(
                        id = user.userId,
                        avatarUrl = user.avatarUrl,
                        fullName = user.fullName,
                        email = user.email,
                        isOnline = user.isActive
                    )
                }
                if (people.isEmpty()) {
                    State.Error
                } else {
                    State.Content(
                        people = people,
                        searchText = (currentState as? State.Content)?.searchText
                    )
                }
            }

            is Action.OnSearchText -> {
                (currentState as? State.Content)?.copy(searchText = action.text.trim())
            }
        }
    }
}