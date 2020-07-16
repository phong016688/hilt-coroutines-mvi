package com.example.mvisamplecoroutines.data.source.locale

import com.example.mvisamplecoroutines.domain.entity.User

abstract class SessionManager {
    abstract fun currentUser(): User
    abstract fun cacheCurrentUser(user: User)
    abstract fun refreshCurrentUser(user: User)

    companion object SessionManagerImpl : SessionManager() {
        private var currentUser: User? = null

        override fun currentUser(): User {
            return currentUser ?: User()
        }

        override fun cacheCurrentUser(user: User) {
            currentUser = user
        }

        override fun refreshCurrentUser(user: User) {
            currentUser = user
        }

    }
}