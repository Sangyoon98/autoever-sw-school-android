package com.sangyoon.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangyoon.mvvm.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow(User(name = "채상윤"))
    val user: StateFlow<User> = _user

    fun changeName(name: String) {
        _user.value = User(name = name)
    }
}