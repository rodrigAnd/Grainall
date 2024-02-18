package com.fiap.grainall.domain.repository

import com.fiap.grainall.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginRepositoryImpl(private val firebaseDatabase: FirebaseDatabase): LoginRepository {
    override fun addUsuario(user: User) {
        //firebaseDatabase.getReference("users").child(user.username).setValue(user)
    }

    override fun getUserLogged(): User {
        return User()
    }

    override suspend fun login(user: User): Boolean {
        var response = false
        if (user.email.isNullOrBlank().not() || user.password.isNullOrBlank()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener {
                    response = it.isSuccessful
                }
        }
        return response
    }

    override fun logout() {
    }
}