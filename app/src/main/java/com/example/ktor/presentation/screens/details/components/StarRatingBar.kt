package com.example.ktor.presentation.screens.details.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ktor.domain.model.displayStarCount
import com.example.ktor.ui.theme.StarColor
import com.example.ktor.util.Constants.MAX_RATING
import com.example.ktor.util.SMALL_PADDING
import kotlinx.coroutines.delay

@Composable
fun StarRatingBar(
    rating: Double,
    onRatingSelected: (Int) -> Unit,
    contentColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    val selectedStars = displayStarCount(rating)

    var shownStars by remember { mutableIntStateOf(selectedStars) }

    LaunchedEffect(selectedStars) {

        val target = selectedStars

        if (target == shownStars) return@LaunchedEffect

        val step = if (target > shownStars) 1 else -1

        var cursor = shownStars

        while (cursor != target) {
            cursor += step

            shownStars = cursor
            delay(70)
        }

    }

    Column(
        modifier = Modifier
            .padding(top = 20.dp)

    ) {
        Text(
            text = "RatingHero",
            fontWeight = FontWeight.Bold,
            color = contentColor,
            fontSize = 12.sp
        )

        Row(
            modifier = Modifier
                .padding(top = SMALL_PADDING),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (star in 1..MAX_RATING) {
                RatingStar(
                    star = star,
                    enabled = enabled,
                    color = contentColor,
                    onSelected = {onRatingSelected(star)},
                    filled = star <= shownStars,
                    modifier = modifier
                )
            }
        }

    }

}

@Composable
fun RatingStar(
    modifier: Modifier = Modifier,
    color: Color,
    onSelected: () -> Unit,
    filled: Boolean,
    star: Int,
    enabled: Boolean
) {


    val scale = remember { Animatable(1f) }

    var vasFilled by remember { mutableStateOf(filled) }


    val tint by animateColorAsState(
        targetValue = if (filled) StarColor else color.copy(alpha = 0.4f),
        animationSpec = tween(durationMillis = 180),
        label = "Star tint $star"
    )
    
    
    
    LaunchedEffect(filled) {

        val becomeFilled = filled && !vasFilled

        val becomeEmpty = !filled && vasFilled
        
        vasFilled = filled
        
        when{
            becomeFilled -> {
                scale.snapTo(0.45f)
                scale.animateTo(targetValue = 1f, animationSpec = spring(Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow))
            }
            becomeEmpty -> {
                scale.animateTo(targetValue =  0.82f , animationSpec = tween(durationMillis = 120))
                scale.animateTo(targetValue =  1f , animationSpec = tween(durationMillis = 140))
            }
        }
    }

    Icon(
        imageVector = if (filled)Icons.Filled.Star else Icons.Outlined.StarBorder,
        contentDescription = "",
        tint = tint,
        modifier = modifier
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value
            )
            .clip(CircleShape)
            .size(44.dp)
            .clickable(enabled) {onSelected()},
        
    )
    
    
    


}