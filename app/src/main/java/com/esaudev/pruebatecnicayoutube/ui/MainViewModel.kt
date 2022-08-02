package com.esaudev.pruebatecnicayoutube.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    private val _randomUserState = MutableLiveData<Resource<RandomUser>>()
    val randomUserState: LiveData<Resource<RandomUser>>
        get() = _randomUserState

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val eventChannel = Channel<MainViewEvent>()
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        getRandomUser()
    }

    fun getRandomUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true
            )
            _uiState.value = when(val result = repository.fetchRandomUser()){
                is Resource.Success -> {
                    _uiState.value.copy(
                        randomUserData = result.data!!,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    eventChannel.send(MainViewEvent.DisplayError)
                    _uiState.value.copy(
                        hasError = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    data class MainViewState(
        val randomUserData: RandomUser? = null,
        val hasError: Boolean = false,
        val isLoading: Boolean = true
    )

    /*
    sealed class MainViewState {
        data class Success(
            val randomUserData: RandomUser
        ): MainViewState()

        object Loading: MainViewState()

        object Error: MainViewState()
    }
    */

    sealed class MainViewEvent {
        object DisplayError: MainViewEvent()
    }
}