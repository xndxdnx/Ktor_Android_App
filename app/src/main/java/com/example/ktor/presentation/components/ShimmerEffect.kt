package com.example.ktor.presentation.components

import android.view.Surface
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ktor.ui.theme.ShimmerDarkGrey
import com.example.ktor.ui.theme.ShimmerMediumGrey
import com.example.ktor.ui.theme.shimmerItemColor
import com.example.ktor.util.EXTRA_SMALL_PADDING
import com.example.ktor.util.LARGE_PADDING
import com.example.ktor.util.MEDIUM_PADDING
import com.example.ktor.util.SMALL_PADDING

@Composable
fun ShimmerEffect() {
    LazyColumn(
        contentPadding = PaddingValues(SMALL_PADDING),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
    ) {
        items(count = 3,) {
            AnimatedShimmerItem()
        }
    }
    
}


@Composable
fun AnimatedShimmerItem() {
    // состояния анимации
    val transaction = rememberInfiniteTransition()

    // 
    val alphaAnim by transaction.animateFloat(
        initialValue = 1F,
        targetValue = 0F,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                // определяет скорость изменения значений 
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    ShimmerItem(alpha = alphaAnim)

}

@Composable
fun ShimmerItem(
    alpha: Float
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        color = shimmerItemColor(),
        shape = RoundedCornerShape(LARGE_PADDING)
    ) {
        Column(
            modifier = Modifier
                .padding(MEDIUM_PADDING),
            verticalArrangement = Arrangement.Bottom
        ) {

            Surface(
                modifier = Modifier
                    .alpha(alpha = alpha)
                    .fillMaxWidth(0.5f)
                    .height(30.dp),
                color = if (isSystemInDarkTheme())
                    ShimmerDarkGrey else ShimmerMediumGrey,
                shape = RoundedCornerShape(size = SMALL_PADDING)
            ) { }
            Spacer(modifier = Modifier.padding(SMALL_PADDING))
            repeat(3) {
                Surface(
                    modifier = Modifier
                        .alpha(alpha = alpha)
                        .fillMaxWidth()
                        .height(15.dp),
                    color = if (isSystemInDarkTheme())
                        ShimmerDarkGrey else ShimmerMediumGrey,
                    shape = RoundedCornerShape(size = SMALL_PADDING)
                ) { }
                Spacer(modifier = Modifier.padding(EXTRA_SMALL_PADDING))
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(5) {
                    Surface(
                        modifier = Modifier
                            .alpha(alpha = alpha)
                            .size(20.dp),
                        color = if (isSystemInDarkTheme())
                            ShimmerDarkGrey else ShimmerMediumGrey,
                        shape = RoundedCornerShape(size = SMALL_PADDING)
                    ) { }
                    Spacer(modifier = Modifier.padding(SMALL_PADDING))
                }
            }
        }

    }
}
@Preview
@Composable
fun AnimatedShimmerItemPreview(

) {
    AnimatedShimmerItem()
}