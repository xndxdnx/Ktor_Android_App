package com.example.ktor.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val LightGray = Color(0xFFD8D8D8)
val DarkGray = Color(0xFF2A2A2A)


@Composable
fun activeIndicatorColor() : Color = if (isSystemInDarkTheme()) Purple40 else Purple80

@Composable
fun inactiveIndicatorColor() : Color = if (isSystemInDarkTheme()) LightGray.copy(alpha = 0.3F) else DarkGray.copy(alpha = 0.3F)

@Composable
fun welcomeScreenBackgroundColor() : Color = if (isSystemInDarkTheme()) Color.Black else Color.White

@Composable
fun welcomeScreenTitleColor() : Color = if (isSystemInDarkTheme()) DarkGray else LightGray

@Composable
fun welcomeScreenDescriptionColor() : Color = if (isSystemInDarkTheme()) DarkGray.copy(alpha = 0.5F) else LightGray.copy(alpha = 0.5F)

@Composable
fun buttonWelcomeColor() : Color = if (isSystemInDarkTheme()) Purple40 else Purple80

@Composable
fun topAppBarBackgroundColor() : Color = if (isSystemInDarkTheme()) Color.Black else Purple40

@Composable
fun topAppBarContentColor() : Color = if (isSystemInDarkTheme()) LightGray else Color.White