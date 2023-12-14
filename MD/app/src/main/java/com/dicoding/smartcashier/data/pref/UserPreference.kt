package com.dicoding.smartcashier.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preference ->
            UserModel(
                preference[NAME_KEY] ?: "",
                preference[EMAIL_KEY] ?: "",
                preference[PASSWORD_KEY] ?: "",
                preference[IS_LOGIN_KEY] ?: false
            )
        }
    }
    suspend fun saveUser(userModel: UserModel) {
        dataStore.edit { preference->
            preference[NAME_KEY] = userModel.name
            preference[EMAIL_KEY] = userModel.email
            preference[PASSWORD_KEY] = userModel.password
            preference[IS_LOGIN_KEY] = true
        }
    }

    suspend fun login(userModel: UserModel) {
        dataStore.edit {preference->
            preference[IS_LOGIN_KEY] = userModel.isLogin
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}