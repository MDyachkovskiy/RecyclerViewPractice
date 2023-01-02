package com.example.andrushchenkopractice_recyclerview

import android.app.Application
import com.example.andrushchenkopractice_recyclerview.model.UsersService

class App : Application() {
    val usersService = UsersService()
}