package com.abm.login.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users = repository.users
    var id : Long = 0
    lateinit var email : String
    lateinit var password : String
    var loginType : LoginType = LoginType.LOCAL


    fun insertUser(){
        viewModelScope.launch (Dispatchers.IO){
            repository.insert(User(id,email,password, loginType))
        }
    }

    fun getUser() : LiveData<List<User>> {
        viewModelScope.launch ( Dispatchers.IO ){
            repository.users
        }
        return repository.users
    }

    fun deleteAll(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAll()
        }
    }


}