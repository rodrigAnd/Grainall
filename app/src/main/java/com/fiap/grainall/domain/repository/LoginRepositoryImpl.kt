package com.fiap.grainall.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class LoginRepositoryImpl(private val firebaseDatabase: FirebaseDatabase) : LoginRepository {
    override fun addUsuario(user: User) {
        //firebaseDatabase.getReference("users").child(user.username).setValue(user)
    }

    override fun getUserLogged(): User {
        return User()
    }

    override suspend fun login(user: User): RequestState<FirebaseUser> {
        return try {
            val result =
                FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email!!, user.password!!)
                    .await()
            RequestState.Success(result.user!!)
        } catch (e: Exception) {
            RequestState.Error(e)
        }
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}