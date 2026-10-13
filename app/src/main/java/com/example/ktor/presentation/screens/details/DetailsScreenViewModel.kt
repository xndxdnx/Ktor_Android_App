package com.example.ktor.presentation.screens.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.usecase.UseCases
import com.example.ktor.util.Constants.DETAILS_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel  @Inject constructor(
    useCases: UseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel(){
    
    private val _selectedHero = MutableStateFlow<Hero?>(null)
    val selectedHero = _selectedHero.asStateFlow()
    
    private val _uiEvents = MutableSharedFlow<DetailsUIEvents>()
    val uiEvents = _uiEvents.asSharedFlow()
    
    private val _colorPalette: MutableState<Map<String, String>> = mutableStateOf(mapOf()) 
    val colorPalette = _colorPalette 
    
    fun generateColorPalette() {
        viewModelScope.launch {
            _uiEvents.emit(DetailsUIEvents.GenerateColorPalette)
        }
    }
    
    fun setColorPalette(colors: Map<String, String>) {
        _colorPalette.value = colors
    }
    
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val heroId = savedStateHandle.get<Int>(DETAILS_ARGUMENT_KEY)
            
            _selectedHero.value = heroId?.let { id->
                useCases.getSelectedHeroUseCase(id)
            }
            
        }
    }
}