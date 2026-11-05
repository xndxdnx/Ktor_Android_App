package com.example.ktor.presentation.screens.details

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.model.isSelectableStarRating
import com.example.ktor.domain.usecase.UseCases
import com.example.ktor.util.Constants.DETAILS_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel  @Inject constructor(
    private val useCases: UseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel(){
    
    private val _selectedHero = MutableStateFlow<Hero?>(null)
    val selectedHero = _selectedHero.asStateFlow()
    
    
    private val _uiEvents = MutableSharedFlow<DetailsUIEvents>()
    val uiEvents = _uiEvents.asSharedFlow()
    
    
    private val ratingMutex  = Mutex()  
    
    
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
    
    
     fun updateHeroRating (rating: Int) {
        
        if (isSelectableStarRating(rating)) return
        
        
        val current = _selectedHero.value ?: return
        
        val previous = current
        
        _selectedHero.value = current.copy(
            rating = rating.toDouble()
        )
        
        viewModelScope.launch { 
            // получает muteсs смотрит чтобы он не был занят потом работает 
            ratingMutex.withLock { 
                
                try {
                
                    val updated = useCases.updateHeroRatingUseCase(rating = rating, heroId = current.id)
                    
                    val latest = _selectedHero.value
                    
                    
                    if (latest?.id == updated.id && latest.rating == updated.rating) {
                        _selectedHero.value = updated
                    }
                    
                    
                    
                }catch (e: Exception) {
                    
                    val latest = _selectedHero.value

                    if (latest?.id == current.id && latest.rating == rating.toDouble()) {
                        _selectedHero.value = previous
                    }
                    
                    
                }
                
                
            }
            
            
        }
        
    }
}