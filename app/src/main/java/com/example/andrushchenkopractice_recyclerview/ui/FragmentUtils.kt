package com.example.andrushchenkopractice_recyclerview.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.andrushchenkopractice_recyclerview.App
import com.example.andrushchenkopractice_recyclerview.Navigator

class ViewModelFactory (
    private val app: App
    ) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass){
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.usersService)
            }
            UserDetailsViewModel::class.java -> {
                UserDetailsViewModel(app.usersService)
            }
            else -> {
                throw IllegalStateException("Unknown ViewModel class")
            }
        }
        return viewModel as T
    }
    }

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator