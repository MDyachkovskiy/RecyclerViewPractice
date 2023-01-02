package com.example.andrushchenkopractice_recyclerview

import com.example.andrushchenkopractice_recyclerview.model.User

interface UserActionListener {

    fun onUserMove (user: User, moveBy: Int)

    fun onUserDelete(user: User)

    fun onUserDetails(user: User)
}