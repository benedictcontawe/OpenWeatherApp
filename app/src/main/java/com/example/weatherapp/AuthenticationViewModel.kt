package com.example.weatherapp

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

public class AuthenticationViewModel : ViewModel {

    companion object {
        private val TAG : String = AuthenticationViewModel::class.java.getSimpleName()
    }

    private val repository : Repository
    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }
    private val email : MutableLiveData<String> = MutableLiveData<String>("")
    private val password : MutableLiveData<String> = MutableLiveData<String>("")
    private val _confirmPassword : MutableLiveData<String> = MutableLiveData<String>("Admin123")
    val confirmPassword: LiveData<String> = _confirmPassword
    private val _message = mutableStateOf("")
    val message: State<String> = _message

    constructor() : super() {
        Log.d(TAG, "constructor")
        repository = Repository()
    }

    public fun setEmail(updatedValue : String) {
        email.setValue(updatedValue)
    }

    public fun setPassword(updatedValue : String) {
        password.setValue(updatedValue)
    }

    public fun setConfirmPassword(updatedValue : String) {
        _confirmPassword.setValue(updatedValue)
    }

    public fun observeEmail() : MutableLiveData<String> = email
    public fun observePassword() : MutableLiveData<String> = password

    public fun checkCredential(activity : Activity, onSuccess : (FirebaseUser?) -> Unit, onFailure : (Throwable?) -> Unit) {
        try {
            Log.d(TAG, "checkCredential 0")
            if (email.getValue()?.isBlank() == true || password.getValue()?.isBlank() == true) onFailure(throw Exception("Email or Password is Blank"))
            Log.d(TAG, "checkCredential 1")
            firebaseAuth.signInWithEmailAndPassword(email.getValue() ?: "", password.getValue() ?: "").addOnCompleteListener(activity) { task ->
                Log.d(TAG, "checkCredential 2")
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success. User: ${task.result.user?.email}")
                    _message.value = "Login Successful 🎉"
                    onSuccess(task.result.user)
                } else if (task.isSuccessful.not()) {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _message.value = "Invalid credentials ❌"
                    onFailure(task.exception)
                }
            }
        } catch (exception : Exception) {
            onFailure(exception)
        }
    }

    public fun registerCredential(activity : Activity, onSuccess : (FirebaseUser?) -> Unit, onFailure : (Throwable?) -> Unit) {
        try {
            if (email.getValue()?.isBlank() == true || password.getValue()?.isBlank() == true) {
                _message.value = "All fields are required ❌"
                onFailure(throw Exception("Email or Password is Blank"))
            }
            if (password.getValue() != confirmPassword.getValue()) {
                _message.value = "Passwords do not match ❌"
                onFailure(throw Exception("Passwords do not match"))
            }
            firebaseAuth.createUserWithEmailAndPassword (email.getValue() ?: "", password.getValue() ?: "").addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success. User: ${task.result.user?.email}")
                    _message.value = "Registration Successful 🎉"
                    onSuccess(task.result.user)
                } else if (task.isSuccessful.not()) {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _message.value = "Invalid credentials ❌"
                    onFailure(task.exception)
                }
            }
        } catch (exception : Exception) {
            onFailure(exception)
        }
    }
}