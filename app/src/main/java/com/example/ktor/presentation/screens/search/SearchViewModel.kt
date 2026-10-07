package com.example.ktor.presentation.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ktor.domain.model.Hero
import com.example.ktor.domain.usecase.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _searchedHeroes = MutableStateFlow<PagingData<Hero>>(PagingData.empty())
    val searchedHeroes = _searchedHeroes.asStateFlow()

    private val _hasSearched = mutableStateOf(false)
    val hasSearched = _hasSearched

    private var searchJob: Job? = null
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            searchJob?.cancel()
            _hasSearched.value
            _searchedHeroes.value = PagingData.empty()
        }
    }
    
    fun searchHeroes(query: String){
        val trimmed = query.trim()
        
        _searchQuery.value = trimmed
        
        if (trimmed.isBlank()) {
            updateSearchQuery(query = "")
            return
        }
        
        _hasSearched.value = true 
        searchJob?.cancel()
        
        searchJob = viewModelScope.launch { 
            useCases.searchHeroesUseCase(query = trimmed)
                .cachedIn(viewModelScope)
                .collect { pagingData -> 
                    _searchedHeroes.value = pagingData  
                }
            
        }
    }
    
    
    


}