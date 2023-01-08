package com.example.andrushchenkopractice_recyclerview

import com.example.andrushchenkopractice_recyclerview.model.User

interface Navigator {

    fun showDetails(user : User)

    fun goBack()

    fun toast (messageRes: Int)
}