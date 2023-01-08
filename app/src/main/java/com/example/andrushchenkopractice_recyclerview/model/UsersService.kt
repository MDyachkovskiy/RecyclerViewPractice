package com.example.andrushchenkopractice_recyclerview.model

import com.example.andrushchenkopractice_recyclerview.tasks.SimpleTask
import com.example.andrushchenkopractice_recyclerview.tasks.Task
import com.github.javafaker.Faker
import java.util.*
import java.util.concurrent.Callable

typealias UsersListener = (users: List<User>) -> Unit

class UsersService {

    private var users = mutableListOf<User>()

    private val listeners = mutableSetOf<UsersListener>()

    private var loaded = false


    fun loadUsers() : Task<Unit> = SimpleTask<Unit> (Callable{
        Thread.sleep(2000)
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map { User(
            id = it.toLong(),
            name = faker.name().name(),
            company = faker.company().name(),
            photo = IMAGES [it % IMAGES.size]
        ) }.toMutableList()
        loaded = true
        notifyChanges()
    })

    fun getUsers() : List<User> = users

    fun getById(id: Long): Task<UserDetails> = SimpleTask<UserDetails>(Callable{
        Thread.sleep(2000)
        val user = users.firstOrNull { it.id == id }
        return@Callable UserDetails(
            user = user!!,
            details = Faker.instance().lorem().paragraphs(3).joinToString("\n\n")
        )
    })

    fun deleteUser(user: User) : Task<Unit> = SimpleTask<Unit>(Callable {
        val indexToDelete : Int = users.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    })


    fun moveUser (user: User, moveBy: Int) : Task<Unit> = SimpleTask<Unit> (Callable{
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return@Callable
        val newIndex = oldIndex + moveBy
        if (newIndex <0 || newIndex >= users.size) return@Callable
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    })

    fun addListener (listener: UsersListener) {
        listeners.add(listener)
        if(loaded){
            listener.invoke(users)
        }
    }

    fun removeListener (listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        if (!loaded) return
        listeners.forEach{it.invoke(users)}
    }

    companion object {
        private val IMAGES = mutableListOf(
            "https://i.pravatar.cc/150?img=54",
            "https://i.pravatar.cc/150?img=69",
            "https://i.pravatar.cc/150?img=68",
            "https://i.pravatar.cc/150?img=60",
            "https://i.pravatar.cc/150?img=61",
            "https://i.pravatar.cc/150?img=56",
            "https://i.pravatar.cc/150?img=49",
            "https://i.pravatar.cc/150?img=47",
            "https://i.pravatar.cc/150?img=45",
            "https://i.pravatar.cc/150?img=36",
            "https://i.pravatar.cc/150?img=35",
            "https://i.pravatar.cc/150?img=32",
            "https://i.pravatar.cc/150?img=26",
            "https://i.pravatar.cc/150?img=18",
            "https://i.pravatar.cc/150?img=14",
            "https://i.pravatar.cc/150?img=12",
            "https://i.pravatar.cc/150?img=3"
        )
    }
}