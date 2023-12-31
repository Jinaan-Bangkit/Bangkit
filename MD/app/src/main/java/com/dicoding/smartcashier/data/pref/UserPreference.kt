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
                preference[TOKEN] ?: "",
                preference[IS_LOGIN_KEY] ?: false
            )
        }
    }
    suspend fun saveUser(userModel: UserModel) {
        dataStore.edit { preference->
            preference[TOKEN] = userModel.token
            preference[IS_LOGIN_KEY] = true
        }
    }

    suspend fun login(userModel: UserModel) {
        dataStore.edit {preference->
            preference[IS_LOGIN_KEY] = userModel.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit {preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN = stringPreferencesKey("token")
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