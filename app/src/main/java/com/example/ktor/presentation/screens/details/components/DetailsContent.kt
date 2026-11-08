package com.example.ktor.presentation.screens.details.components

import android.app.Activity
import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ktor.R
import com.example.ktor.domain.model.Hero
import com.example.ktor.presentation.screens.details.rating_snack_bar.RatingNotice
import com.example.ktor.presentation.screens.details.rating_snack_bar.RatingSnackBar
import com.example.ktor.ui.theme.applyDefaultSystemBars
import com.example.ktor.util.Constants.ABOUT_TEXT_MAX_LINES
import com.example.ktor.util.Constants.BASE_URL
import com.example.ktor.util.Constants.EXPANDED_RADIUS_LEVEL
import com.example.ktor.util.Constants.INFO_ICON_SIZE
import com.example.ktor.util.Constants.MIN_BACKGROUND_IMAGE_HEIGHT
import com.example.ktor.util.EXTRA_LARGE_PADDING
import com.example.ktor.util.LARGE_PADDING
import com.example.ktor.util.MEDIUM_PADDING
import com.example.ktor.util.SMALL_PADDING
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

private enum class SheetAnchor {
    Bottom,
    Middle,
    Top
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class)
@Composable
fun DetailsContent(
    onRatingSelected: (Int) -> Unit,
    navController: NavHostController,
    selectedHero: Hero?,
    colors: Map<String, String>,
    ratingNotice: RatingNotice? = null,
    onRatingNoticeConsume:(Long) -> Unit
) {
    
    var showNotice by remember { mutableStateOf<RatingNotice?>(null) }
    
    var noticeVisible by remember { mutableStateOf(false) }


    LaunchedEffect(ratingNotice?.token) {
        
        val notice = ratingNotice ?: return@LaunchedEffect
        
        showNotice = notice
        noticeVisible = true
        delay(2600L.milliseconds)
        noticeVisible= false
        delay(320L.milliseconds)
        onRatingNoticeConsume(notice.token)
    }
    
    
    val activity = LocalContext.current as Activity
    val darkTheme = isSystemInDarkTheme()

    val vibrant = colors["vibrant"] ?: "#6650A4"
    val darkVibrant = colors["darkVibrant"] ?: "#2A2A2A"
    val onDarkVibrant = colors["onDarkVibrant"] ?: "#FFFFFF"

    fun String?.toSafeColor(fallback: Color = Color.Gray): Color {
        val value = this?.trim()

        if (value.isNullOrEmpty() ||
            value.equals("null", ignoreCase = true)
        ) {
            return fallback
        }

        return try {
            Color(parseColor(value))
        } catch (e: IllegalArgumentException) {
            fallback
        }
    }

    val sheetColor = darkVibrant.toSafeColor()
    
    
//    val sheetColor = Color(parseColor(darkVibrant)) 

    DisposableEffect(darkVibrant, darkTheme) {
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        window.statusBarColor = sheetColor.toArgb()
        window.navigationBarColor = sheetColor.toArgb()
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        onDispose {
            applyDefaultSystemBars(activity = activity, darkTheme = darkTheme)
        }
    }

    val density = LocalDensity.current
    
    var layoutHeight by remember { mutableStateOf(0) }
    
    val sheetState = remember {
        AnchoredDraggableState(
            initialValue = SheetAnchor.Bottom,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            snapAnimationSpec = tween(durationMillis = 300),
            decayAnimationSpec = exponentialDecay()
        )
    }
    val peekPx = with(density) {140.dp.toPx() }
    
    if (layoutHeight > 0) {
        val anchors = DraggableAnchors {
            SheetAnchor.Bottom at (layoutHeight - peekPx)
            SheetAnchor.Middle at layoutHeight * 0.46f
            SheetAnchor.Top at 0f
        }
        SideEffect {
            sheetState.updateAnchors(anchors)
        }
    }

    val sheetOffset = sheetState.offset
    val middleOffset = layoutHeight * 0.46f
    val progressToTop = if (sheetOffset.isNaN() || middleOffset == 0f) {
        0f
    } else {
        ((middleOffset - sheetOffset) / middleOffset).coerceIn(0f, 1f)
    }
    val radiusAnim by animateDpAsState(
        targetValue = EXTRA_LARGE_PADDING * (1f - progressToTop) + EXPANDED_RADIUS_LEVEL * progressToTop,
        label = "Radius Animation"
    )
    val imageFraction = 1f - progressToTop

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { layoutHeight = it.height }
            .background(sheetColor)
    ) {
        selectedHero?.let { hero ->
            BackgroundContent(
                heroImage = hero.image,
                imageFraction = imageFraction,
                backgroundColor = sheetColor
            )
        }
        if (layoutHeight > 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        val y = if (sheetOffset.isNaN()) {
                            (layoutHeight - peekPx).roundToInt()
                        } else {
                            sheetOffset.roundToInt()
                        }
                        IntOffset(x = 0, y = y)
                    }
                    .anchoredDraggable(
                        state = sheetState,
                        orientation = Orientation.Vertical
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = radiusAnim,
                            topEnd = radiusAnim
                        )
                    )
                    .background(sheetColor)
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(width = 48.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.45f))
                        .align(Alignment.CenterHorizontally)
                )
                selectedHero?.let {
                    BottomSheetContent(
                        onRatingSelected = onRatingSelected,
                        selectedHero = it,
                        infoBoxIconColor = vibrant.toSafeColor(
                            fallback = Color(0xFF6650A4)
                        ),

                        sheetBackgroundColor = darkVibrant.toSafeColor(
                            fallback = Color(0xFF2A2A2A)
                        ),

                        contentColor = onDarkVibrant.toSafeColor(
                            fallback = Color.White
                        )
                    )
                }
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(all = SMALL_PADDING)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f)),
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = Icons.Default.Close,
                contentDescription =null,
                tint = Color.White
            )
        }

        AnimatedVisibility(
            visible = noticeVisible,
            modifier = Modifier
                .align (Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 18.dp),
            
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight / 2 + 48 },
                animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
            ) + fadeIn(tween(durationMillis = 280)) 
                + scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 280)
            ) + fadeOut(tween(durationMillis = 220))
            
        ) {
            
            showNotice?.let { notice ->
                RatingSnackBar(
                    errorText = notice.errorText,
                    heroName = notice.heroName,
                    stars = notice.stars,
                    isError = notice.isError,
                )
            }
            
        }
    }
}

@Composable
fun BottomSheetContent(
    onRatingSelected: (Int) -> Unit = {},
    selectedHero: Hero,
    infoBoxIconColor: Color = MaterialTheme.colorScheme.primary,
    sheetBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(sheetBackgroundColor)
            .padding(all = LARGE_PADDING)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LARGE_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = SMALL_PADDING)
                    .size(INFO_ICON_SIZE),
                painter = painterResource(id = R.drawable.icon_logo),
                contentDescription = null,
                tint = contentColor
            )
            Text(
                modifier = Modifier
                    .weight(1f),
                text = selectedHero.name,
                color = contentColor,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MEDIUM_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(
                icon = painterResource(id = R.drawable.icon_bolt2),
                iconColor = infoBoxIconColor,
                bigText = "${selectedHero.power}",
                smallText = "power",
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(id = R.drawable.icon_calendar),
                iconColor = infoBoxIconColor,
                bigText = selectedHero.month,
                smallText = "month",
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(id = R.drawable.icon_cake),
                iconColor = infoBoxIconColor,
                bigText = selectedHero.day,
                smallText = "birthday",
                textColor = contentColor
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "About",
            color = contentColor,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .padding(bottom = MEDIUM_PADDING),
            text = selectedHero.about,
            color = contentColor,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            maxLines = ABOUT_TEXT_MAX_LINES
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING)
        ) {
            OrderedList(
                modifier = Modifier.weight(1f),
                title = "Family",
                items = selectedHero.family,
                textColor = contentColor
            )
            OrderedList(
                modifier = Modifier.weight(1f),
                title = "Abilities",
                items = selectedHero.abilities,
                textColor = contentColor
            )
            OrderedList(
                modifier = Modifier.weight(1f),
                title = "Nature Types",
                items = selectedHero.natureTypes,
                textColor = contentColor
            )
        }
        
        StarRatingBar(
            onRatingSelected = onRatingSelected,
            contentColor = contentColor,
            rating = selectedHero.rating
        )
    }
}


@ExperimentalCoilApi
@Composable
fun BackgroundContent(
    heroImage: String,
    imageFraction: Float = 1f,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    val imageUrl = remember { "$BASE_URL$heroImage" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(
                    fraction = (imageFraction + MIN_BACKGROUND_IMAGE_HEIGHT)
                        .coerceAtMost(maximumValue = 1.0f)
                )
                .align(Alignment.TopCenter),
            model = ImageRequest.Builder(LocalContext.current)
                .data(data = imageUrl)
                .error(drawableResId = R.drawable.ic_placeholder)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun InfoBox(
    icon: Painter,
    iconColor: Color,
    bigText: String,
    smallText: String,
    textColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier
                .padding(end = SMALL_PADDING)
                .size(INFO_ICON_SIZE),
            painter = icon,
            contentDescription = "",
            tint = iconColor
        )
        Column {
            Text(
                text = bigText,
                color = textColor,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Black
            )
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text = smallText,
                color = textColor,
                fontSize = MaterialTheme.typography.labelLarge.fontSize
            )
        }
    }
}

@Composable
fun OrderedList(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    textColor: Color
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(bottom = SMALL_PADDING),
            text = title,
            color = textColor,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        items.forEachIndexed { index, item ->
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text = "${index + 1}. $item",
                color = textColor,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
    }
}
