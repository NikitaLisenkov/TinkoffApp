package com.example.app.presentation.profile

import com.example.app.domain.repo.ProfileRepository
import com.example.app.presentation.base.BaseReducer
import com.example.app.presentation.profile.ProfileViewModel.Action
import com.example.app.presentation.profile.ProfileViewModel.State
import com.example.app.utils.runSuspendCatching
import javax.inject.Inject

class ProfileReducer @Inject constructor(
    private val repo: ProfileRepository
) : BaseReducer<State, Action> {

    override suspend fun reduce(currentState: State, action: Action): State {
        return when (action) {
            is Action.LoadData -> {
                runSuspendCatching { repo.getOwnUserProfile() }
                    ?.let {
                        State.Content(
                            avatarUrl = it.avatarUrl,
                            name = it.fullName,
                            isOnline = it.isActive
                        )
                    } ?: State.Error
            }
        }
    }

}