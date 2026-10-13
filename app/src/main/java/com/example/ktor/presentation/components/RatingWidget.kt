package com.example.ktor.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ktor.R
import com.example.ktor.ui.theme.StarColor
import com.example.ktor.util.EXTRA_SMALL_PADDING

@Composable
fun RatingWidget(
    rating: Double,
    modifier: Modifier = Modifier,
    scaledFactor: Float = 3F,
    spaceBetween: Dp = EXTRA_SMALL_PADDING
) {
    val starPathString = stringResource(R.string.star_path)

    val result = calculateStars(rating)

    val starPath = remember {
        PathParser()
            .parsePathString(starPathString)
            .toPath()
    }

    val starPathBounds = remember {
        starPath.getBounds()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        result["filledStars"]?.let {
            repeat(it) {
                FilledStar(
                    starPath = starPath,
                    starPathBounds = starPathBounds,
                    scaledFactor = scaledFactor
                )
            }
        }
        result["halfStars"]?.let {
            repeat(it) {
                HalfFilledStar(
                    starPath = starPath,
                    starPathBounds = starPathBounds,
                    scaledFactor = scaledFactor
                )
            }
        }
        result["emptyStars"]?.let {
            repeat(it) {
                EmptyStar(
                    starPath = starPath,
                    starPathBounds = starPathBounds,
                    scaledFactor = scaledFactor
                )
            }
        }

    }


}


@Composable
fun calculateStars(
    rating: Double,
): Map<String, Int> {

    val maxStars by remember { mutableStateOf(5) }
    var filledStars by remember { mutableStateOf(0) }
    var halfStars by remember { mutableStateOf(0) }
    var emptyStars by remember { mutableStateOf(0) }

    LaunchedEffect(rating) {

        val (firstNUmber, lastNumber) = rating.toString().split(".").map { it.toInt() }

        if (firstNUmber in 0..5 && lastNumber in 0..9) {
            filledStars = firstNUmber
            if (lastNumber in 1..5) {
                halfStars++
            }
            if (lastNumber in 6..9) {
                filledStars++
            }
            if (firstNUmber == 5 && lastNumber > 0) {
                emptyStars = 5
                filledStars = 0
                halfStars = 0
            }
        } else {
            Log.d("RatingWidget", "Invalid Rating Number")
        }
    }


    emptyStars = maxStars - (filledStars + halfStars)

    return mapOf(
        "filledStars" to filledStars,
        "halfStars" to halfStars,
        "emptyStars" to emptyStars
    )

}

@Composable
fun EmptyStar(
    starPath: Path,
    starPathBounds: Rect,
    scaledFactor: Float
) {

    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        val canvasSize = this.size

        scale(scaledFactor) {
            val pathWith = starPathBounds.width
            val pathHeight = starPathBounds.height

            val left = (canvasSize.width / 2) - (pathWith / 1.7f)
            val top = (canvasSize.height / 2) - (pathHeight / 1.7f)

            translate(
                left = left,
                top = top
            ) {
                drawPath(
                    path = starPath,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }

}

@Composable
fun HalfFilledStar(
    starPath: Path,
    starPathBounds: Rect,
    scaledFactor: Float
) {
    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        val canvasSize = this.size

        scale(scaledFactor) {
            val pathWith = starPathBounds.width
            val pathHeight = starPathBounds.height

            val left = (canvasSize.width / 2) - (pathWith / 1.7f)
            val top = (canvasSize.height / 2) - (pathHeight / 1.7f)

            translate(
                left = left,
                top = top
            ) {
                drawPath(
                    path = starPath,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                clipPath(
                    path = starPath
                ) {
                    drawRect(
                        size = Size(
                            width = starPathBounds.maxDimension / 1.7f,
                            height = starPathBounds.maxDimension * scaledFactor
                        ),
                        color = StarColor
                    )

                }


            }

        }
    }

}


@Composable
fun FilledStar(
    starPath: Path,
    starPathBounds: Rect,
    scaledFactor: Float = 2F
) {

    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        val canvasSize = this.size

        scale(scaledFactor) {
            val pathWith = starPathBounds.width
            val pathHeight = starPathBounds.height

            val left = (canvasSize.width / 2) - (pathWith / 1.7f)
            val top = (canvasSize.height / 2) - (pathHeight / 1.7f)

            translate(
                left = left,
                top = top
            ) {
                drawPath(
                    path = starPath,
                    color = StarColor
                )
            }
        }
    }

}

@Composable
@Preview
fun FilledStarPreview() {
    RatingWidget(
        rating = 1.0
    )
}
