package com.example.andrushchenkopractice_recyclerview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.andrushchenkopractice_recyclerview.model.UserDetails
import com.example.andrushchenkopractice_recyclerview.model.UsersService
import com.example.andrushchenkopractice_recyclerview.tasks.EmptyResult
import com.example.andrushchenkopractice_recyclerview.tasks.PendingResult
import com.example.andrushchenkopractice_recyclerview.tasks.SuccessResult

class UserDetailsViewModel (
    private val usersService : UsersService
        ) : BaseViewModel() {
    private val _state = MutableLiveData<State>()
    val state: LiveData<State> = _state

    private val currentState: State get() = state.value!!

    init {
        state.value = State(
            userDetailResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId: Long) {
        if (currentState.userDetailResult is SuccessResult) return

        _state.value = currentState.copy(userDetailResult = PendingResult())

       usersService.getById(userId)
           .onSucces {
               _state.value = currentState.copy(userDetailResult = SuccessResult(it))
           }
           .onError {

           }
           .autoCancel()
    }

    fun deleteUser() {
        val userDetailsResult = currentState.userDetailResult
        if (userDetailsResult is SuccessResult) return
        _state.value = currentState.copy(deletingInProgress = true)
        usersService.deleteUser(userDetailsResult.data.user)
            .onSucces {

            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
            }
    }

    data class State(
        val userDetailResult: Result<UserDetails>,
        private val deletingInProgress: Boolean
    ) {
        val showContent: Boolean get() = userDetailResult is SuccessResult
        val showProgress: Boolean get() = userDetailResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress
    }
}