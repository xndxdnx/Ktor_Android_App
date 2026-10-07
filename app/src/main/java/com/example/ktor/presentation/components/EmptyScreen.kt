package com.example.ktor.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import com.example.ktor.R
import com.example.ktor.ui.theme.DarkGray
import com.example.ktor.ui.theme.LightGray
import com.example.ktor.util.SMALL_PADDING
import java.net.SocketTimeoutException

@Composable
fun EmptyScreen(
    error: LoadState.Error
) {
    val message by remember { mutableStateOf(parseErrorMessage(error.toString())) }

    val icon by remember { mutableStateOf(R.drawable.icon_error) }

    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) {
            ContentAlpha.disabled
        } else 0F,
        animationSpec = tween(durationMillis = 1000),
    )

    LaunchedEffect(true) {
        startAnimation = true

    }
    
    EmptyContent(
        message = message,
        icon = icon,
        alphaAnim = alphaAnim
    )
    
}

@Composable
fun EmptyContent(
    alphaAnim: Float,
    message: String,
    icon: Int
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (isDarkTheme()) Modifier.background(color = Color.Black) 
                else Modifier.background(color = Color.White)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { 
        
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .alpha(alphaAnim),
            tint = if (isDarkTheme()) LightGray 
            else DarkGray
        )

        Text(
            text = message,
            color = if (isDarkTheme()) LightGray
            else DarkGray,
            modifier = Modifier
                .padding(SMALL_PADDING)
                .alpha(alphaAnim),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }


}


@Composable
private fun isDarkTheme(): Boolean {
    // конфигурация устройства
    return (LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}

fun parseErrorMessage(message: String): String {

    return when {
        message.contains("SocketTimeoutException") -> {
            "Сервер недоступен"
        }

        message.contains("ConnectException") -> {
            "Нет доступа к интернету"
        }

        else -> "Неизвестная ошибка"
    }

}

@Composable
@Preview
fun EmptyScreenPreview(
) {
    EmptyScreen(
        error = LoadState.Error(SocketTimeoutException())
    )
    
    
}