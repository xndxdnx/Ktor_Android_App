package com.example.ktor.presentation.screens.details.components

import android.app.Activity
import android.graphics.Color.parseColor
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ktor.R
import com.example.ktor.domain.model.Hero
import com.example.ktor.ui.theme.applyDefaultSystemBars
import com.example.ktor.ui.theme.welcomeScreenTitleColor
import com.example.ktor.util.Constants.BASE_URL
import com.example.ktor.util.Constants.MIN_BACKGROUND_IMAGE_HEIGHT
import com.example.ktor.util.EXTRA_LARGE_PADDING
import com.example.ktor.util.LARGE_PADDING
import com.example.ktor.util.MEDIUM_PADDING
import com.example.ktor.util.SMALL_PADDING

@OptIn(ExperimentalMaterialApi::class)
val BottomSheetScaffoldState.currentSheetFractions : Float
    get() {
        val fraction = bottomSheetState.progress
        val targetValue = bottomSheetState.targetValue
        
        val currentValue = bottomSheetState.currentValue
        
        return  when  {
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Collapsed -> 1F
            currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Expanded -> 0F
            currentValue == BottomSheetValue.Collapsed && targetValue == BottomSheetValue.Expanded -> 1F - fraction
            currentValue == BottomSheetValue.Expanded && targetValue == BottomSheetValue.Collapsed -> 0F + fraction
            else -> fraction
        }
    }


@Composable
fun DetailsContent(
    navHostController: NavHostController,
    selectedHero: Hero?,
    colors: Map<String, String>
) {
    val activity = LocalContext.current as Activity
    
    val darkTheme = isSystemInDarkTheme()

    val vibrant = colors["vibrant"]!!
    val darkVibrant = colors["darkVibrant"]!!
    val onDarkVibrant = colors["onDarkVibrant"]!!

    val sheetColor = Color(parseColor(darkVibrant))

    DisposableEffect(darkVibrant, darkTheme) {
        val window = activity.window
        val insetsController =
            WindowCompat.getInsetsController(window, window.decorView)

        window.statusBarColor = sheetColor.toArgb()
        window.navigationBarColor = sheetColor.toArgb()

        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        
        onDispose {
            applyDefaultSystemBars(
                activity = activity,
                darkTheme = darkTheme
            )
        }
    }
    
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    
    val currentSheetFraction = scaffoldState.currentSheetFractions
    
    val radiusAnim by animateDpAsState(
        targetValue = if (currentSheetFraction == 1F) EXTRA_LARGE_PADDING else 0.dp,
        label = "radius_animation"
    )


    BottomSheetScaffold(
        modifier = Modifier 
            .fillMaxSize(),
        backgroundColor = sheetColor,
        sheetBackgroundColor = sheetColor,
        sheetShape = RoundedCornerShape(topStart = radiusAnim.value, topEnd = radiusAnim.value),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 140.dp,
        sheetContent = {
            selectedHero?.let {hero ->
                BottomSheetContent(
                    selectedHero = hero,
                    sheetBackgroundColor = Color(parseColor(darkVibrant)),
                    infoBoxIconColor = Color(parseColor(vibrant)),
                    contentColor = Color(parseColor(onDarkVibrant)),
                )
            }
            
        },
        content = {
            selectedHero?.let {hero ->
                BackgroundContent(
                    heroImage = hero.image,
                    imageFraction = currentSheetFraction,
                    backgroundColor = Color(parseColor(onDarkVibrant)),
                    onCloseClick = { navHostController.popBackStack() }
                )
            }
        }
    )
    
    

}

@Composable
fun BackgroundContent(
    heroImage: String,
    imageFraction: Float = 1F,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onCloseClick: () -> Unit = {}
) {

    val imageUrl = remember { "$BASE_URL$heroImage" }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor),

        ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(
                    fraction = (imageFraction + MIN_BACKGROUND_IMAGE_HEIGHT)
                        // ограничивает итоговое значение максимально 
                        .coerceAtMost(maximumValue = 1.0F)
                )
                .align(Alignment.TopCenter),
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(imageUrl)
                .error(drawableResId = R.drawable.ic_placeholder)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .padding(SMALL_PADDING)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun BottomSheetContent(
    infoBoxIconColor: Color = MaterialTheme.colorScheme.primary,
    sheetBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    selectedHero: Hero
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = sheetBackgroundColor)
            .padding(LARGE_PADDING)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = LARGE_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.icon_logo),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .padding(end = SMALL_PADDING)
                    .size(32.dp)
            )
            Text(
                text = selectedHero.name,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .weight(1f)
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MEDIUM_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoBox(
                icon = painterResource(R.drawable.icon_bolt2),
                iconColor = infoBoxIconColor,
                title = "${selectedHero.power}",
                description = "Power",
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(R.drawable.icon_calendar),
                iconColor = infoBoxIconColor,
                title = "${selectedHero.month}",
                description = "Month",
                textColor = contentColor
            )
            InfoBox(
                icon = painterResource(R.drawable.icon_cake),
                iconColor = infoBoxIconColor,
                title = "${selectedHero.day}",
                description = "Birthday",
                textColor = contentColor
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "About",
            color = contentColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MEDIUM_PADDING),
            text = selectedHero.about,
            color = contentColor.copy(alpha = ContentAlpha.medium),
            fontSize = 14.sp,
            maxLines = 7,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING)
        ) {
            OrderList(
                modifier = Modifier.weight(1f),
                textColor = contentColor,
                items = selectedHero.family,
                title = "Family"
            )
            OrderList(
                modifier = Modifier.weight(1f),
                textColor = contentColor,
                items = selectedHero.abilities,
                title = "Abilities"
            )
            OrderList(
                modifier = Modifier.weight(1f),
                textColor = contentColor,
                items = selectedHero.natureTypes,
                title = "Nature Types"
            )
        }
    }
}

@Composable
fun OrderList(
    title: String,
    items: List<String>,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = SMALL_PADDING),
            fontWeight = FontWeight.Bold,
            text = title,
            fontSize = 22.sp,
            color = textColor
        )
        items.forEachIndexed { index, item ->
            Text(
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .alpha(ContentAlpha.medium),
                text = "${index + 1}. $item"
            )
        }
    }
}


@Composable
fun InfoBox(
    icon: Painter,
    iconColor: Color,
    title: String,
    description: String,
    textColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(end = SMALL_PADDING)
                .size(32.dp),
            tint = iconColor,
        )
        Column(

        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = textColor
            )
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = textColor.copy(alpha = ContentAlpha.medium)
            )
        }
    }
}


@Composable
@Preview
fun InfoBox(
) {
    InfoBox(
        icon = painterResource(R.drawable.icon_calendar),
        iconColor = welcomeScreenTitleColor(),
        title = "HEllo",
        description = "hello",
        textColor = welcomeScreenTitleColor(),
    )
}


@Composable
@Preview
fun BottomSheetContentPreview() {
    BottomSheetContent(
        selectedHero = Hero(
            id = 1,
            name = "Gojo",
            image = "",
            about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            rating = 4.5,
            power = 100,
            month = "Dec",
            day = "7th",
            family = listOf("Minato", "Kushina"),
            abilities = listOf("Infinity", "Hollow Purple"),
            natureTypes = listOf("Space", "Infinity")
        )
    )
}