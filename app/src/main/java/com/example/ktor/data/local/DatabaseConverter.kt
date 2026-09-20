package com.example.ktor.data.local

import androidx.room.TypeConverter


// Нужен для того чтобы рум не умеет работать с листами напрямую
// мапперы это не единственное решение для работы в коде
// нам нужно сделать тоже-самое как маппер через аннотацию 
class DatabaseConverter () {
    
    private val separator = ","
    
    @TypeConverter
    fun converterListToString (list: List<String>) : String {
        val stringBuilder = StringBuilder()
        for (item in list) {
            stringBuilder.append(item).append(separator)
        }
        stringBuilder.setLength(stringBuilder.length - separator.length)
        return stringBuilder.toString()
    }
    
    @TypeConverter
    fun converterStringToList(string: String) : List<String> {
        return string.split(separator)
    }
    
    
    
}