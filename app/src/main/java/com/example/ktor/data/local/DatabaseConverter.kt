package com.example.ktor.data.local

import androidx.room.TypeConverter


// Нужен для того чтобы рум не умеет работать с листами напрямую
// мапперы это не единственное решение для работы в коде
// нам нужно сделать тоже-самое как маппер через аннотацию 
class DatabaseConverter () {
    
    private val separator = ","
    
    @TypeConverter
    fun converterListToString (list: List<String>) : String {
        
        if (list.isEmpty()) {
            return ""
        }
        
        return list.joinToString(separator)
    }
    
    @TypeConverter
    fun converterStringToList(string: String) : List<String> {
        
        if (string.isBlank()) {
            return emptyList()
        }
        return string.split(separator)
    }
    
    
    
}