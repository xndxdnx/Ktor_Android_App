package com.example.ktor.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import com.example.ktor.R
import kotlinx.serialization.descriptors.SerialDescriptor

sealed class OnBoardingPage(
    val title: String,
    @DrawableRes
    val images: Int,
    val description: String
) {
    object First: OnBoardingPage(
        images = R.drawable.greetings,
        title = "Добро пожаловать",
        description = "Забудь о догадках и фанатских теориях. Здесь только проверенная информация о каждом персонаже и их силах!"
    )
    object Second: OnBoardingPage(
        images = R.drawable.explore,
        title = "Иследуй",
        description = "Отправляйся в путешествие по миру JJK. Найди ответы на все вопросы о персонажах, их способностях и мире проклятий."
    )
    object Third: OnBoardingPage(
        images = R.drawable.power,
        title = "Сила",
        description = "Сравни мощь персонажей, анализируй их техники и пойми, кто действительно сильнейший в мире Jujutsu."
    )
    
}