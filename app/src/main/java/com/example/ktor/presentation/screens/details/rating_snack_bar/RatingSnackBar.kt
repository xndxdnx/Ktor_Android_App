package com.example.ktor.presentation.screens.details.rating_snack_bar


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.ktor.R
import com.example.ktor.ui.theme.StarColor
import com.example.ktor.util.Constants.MAX_RATING
import kotlinx.coroutines.delay

@Composable
fun RatingSnackBar (
    errorText: String?,
    heroName: String,
    stars: Int,
    isError: Boolean,
    modifier: Modifier = Modifier
) {


    var revealed by remember(stars, isError) { mutableIntStateOf(0) }


    LaunchedEffect(stars,isError) {
        
        revealed = 0
        
        if (isError) return@LaunchedEffect

//        val checked =  displayStarCount(revealed.toDouble())
        
        for (index in 1..stars.coerceIn(0, MAX_RATING) ) {
            delay(70)
            revealed = index
        }
        
    }
    
    
    val accent = if (isError) {
        Color(0xFFFF8A80)
    } else StarColor

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = accent.copy(alpha = 0.55f),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xF2181412),
        shadowElevation = 8.dp
    ) {


        Row(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = accent,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = accent.copy(alpha = 0.16f),
                        CircleShape
                    )
                    .padding(8.dp)
            )
            Column(
                modifier = modifier
                    .weight(1f),
            ) {
                Text(
                    text = stringResource(
                        if (isError) R.string.rating_failed_title else R.string.rating_saved_title
                    ),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = if (isError) {
                        errorText ?: stringResource(R.string.rating_failed_title)
                    } else {
                        stringResource(R.string.rating_saved_body, heroName, stars)
                    },
                    color = Color.White.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
                if (!isError) {
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        for (star in 1..MAX_RATING) {
                            val filled = star <= revealed
                            val scale by animateFloatAsState(
                                targetValue = if (filled) 1f else 0.72f,
                                animationSpec = tween(
                                    durationMillis = 220,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "snackStar$star"
                            )
                            Icon(
                                imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = if (filled) StarColor else Color.White.copy(alpha = 0.35f),
                                modifier = Modifier
                                    .size(16.dp)
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}