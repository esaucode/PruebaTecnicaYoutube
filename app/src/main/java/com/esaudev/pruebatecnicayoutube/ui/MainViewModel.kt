package com.esaudev.pruebatecnicayoutube.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaudev.pruebatecnicayoutube.domain.model.RandomUser
import com.esaudev.pruebatecnicayoutube.domain.repository.RandomUserRepository
import com.esaudev.pruebatecnicayoutube.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RandomUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainViewState>(MainViewState())
    val uiState: StateFlow<MainViewState> = _uiState

    private val eventChannel = Channel<MainViewEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        getRandomUser()
    }

    fun getRandomUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when(val result = repository.fetchRandomUser()){
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        randomUser = result.data,
                        isLoading = false,
                        hasError = false
                    )
                }
                is Resource.Error -> {
                    eventChannel.send(MainViewEvent.DisplayError)
                    _uiState.value = _uiState.value.copy(
                        hasError = true,
                        isLoading = false
                    )
                }
            }
        }
    }


    data class MainViewState(
        val randomUser: RandomUser? = null,
        val isLoading: Boolean = true,
        val hasError: Boolean = false
    )

    sealed class MainViewEvent {
        object DisplayError: MainViewEvent()
    }

}