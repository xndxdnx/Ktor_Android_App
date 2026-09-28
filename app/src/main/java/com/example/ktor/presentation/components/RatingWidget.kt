package com.example.ktor.presentation.components

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ktor.R
import com.example.ktor.ui.theme.StarColor


@Composable
fun RatingWidget(
    rating: Double,
    modifier: Modifier = Modifier
) {
    val starPathString = stringResource(R.string.star_path)

    val starPath = remember {
        PathParser()
            .parsePathString(starPathString)
            .toPath()
    }

    val starPathBounds = remember {
        starPath.getBounds()
    }

    FilledStar(
        starPath = starPath,
        starPathBounds = starPathBounds,
    )
    

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
