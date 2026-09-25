package com.example.ktor.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktor.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val useCases: UseCases 
) : ViewModel(){
    
    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted : StateFlow<Boolean> = _onboardingCompleted
    
    init {
        viewModelScope.launch(Dispatchers.IO)  {
            _onboardingCompleted.value = useCases.readOnboardingUseCase().stateIn(viewModelScope).value
        }
    }
}