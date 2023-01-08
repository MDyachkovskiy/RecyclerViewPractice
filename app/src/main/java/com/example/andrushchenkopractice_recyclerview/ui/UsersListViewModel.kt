package com.example.andrushchenkopractice_recyclerview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.andrushchenkopractice_recyclerview.model.User
import com.example.andrushchenkopractice_recyclerview.model.UsersListener
import com.example.andrushchenkopractice_recyclerview.model.UsersService
import com.example.andrushchenkopractice_recyclerview.tasks.EmptyResult
import com.example.andrushchenkopractice_recyclerview.tasks.ErrorResult
import com.example.andrushchenkopractice_recyclerview.tasks.PendingResult
import com.example.andrushchenkopractice_recyclerview.tasks.SuccessResult

data class UserListItem (
    val user: User,
    val isInProgress: Boolean)

class UsersListViewModel(
    private val usersService: UsersService
) : BaseViewModel() {

    private val _users = MutableLiveData <Result<List<UserListItem>>>()
    val users: LiveData<Result<List<UserListItem>>> = _users

    private val userIdsInProgress = mutableSetOf<Long>()

    private var usersResult: Result <List<User>> = EmptyResult()
    set(value) {
        field = value
        notifyUpdates()
    }

    private val listener: UsersListener = {
        usersResult = if (it.isEmpty()) {
            EmptyResult()
        } else {
            SuccessResult(it)
        }
    }

    init {
        loadUsers()
        usersService.addListener(listener)
    }

    private fun notifyUpdates() {
        _users.postValue(usersResult.map { users ->
            users.map{user -> UserListItem(user, isInProgress(user))}
        })
    }

    private fun addProgressTo (user: User) {
        userIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom (user: User){
        userIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean{
        return userIdsInProgress.contains(user.id)
    }


    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)
    }

    fun loadUsers() {
        usersResult = PendingResult()
        usersService.loadUsers()
            .onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

    fun moveUser(user: User, moveBy: Int) {
        if(isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, moveBy)
            .onSucces {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
            }
            .autoCancel()
    }

    fun deleteUser (user: User) {
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.deleteUser(user)
            .onSucces{
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
            }
            .autoCancel()
    }

}