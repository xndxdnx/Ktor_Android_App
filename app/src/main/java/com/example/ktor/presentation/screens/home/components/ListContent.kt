package com.example.ktor.presentation.screens.home.components


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.ktor.R
import com.example.ktor.domain.model.Hero
import com.example.ktor.navigation.Screens
import com.example.ktor.presentation.components.RatingWidget
import com.example.ktor.ui.theme.topAppContentColor
import com.example.ktor.util.Constants.BASE_URL
import com.example.ktor.util.LARGE_PADDING
import com.example.ktor.util.MEDIUM_PADDING
import com.example.ktor.util.SMALL_PADDING

@Composable
fun ListContent(
    heroes: LazyPagingItems<Hero>,
    navHostController: NavHostController
) {


    LazyColumn(
        contentPadding = PaddingValues(SMALL_PADDING),
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        items(
            count = heroes.itemCount,
            key = { index -> heroes[index]?.id ?: index }
        ) { index ->

            val hero = heroes[index]

            hero?.let { hero ->
                HeroItem(
                    hero = hero,
                    navHostController = navHostController
                )
            }


        }

    }


}


@Composable
fun HeroItem(
    hero: Hero,
    navHostController: NavHostController
) {

    val context = LocalContext.current

    val image = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data("$BASE_URL${hero.image}")
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .build()
    )

    Box(
        modifier = Modifier
            .height(400.dp)
            .clickable { navHostController.navigate(Screens.DetailsScreen.passHeroId(hero.id)) },
        contentAlignment = Alignment.BottomStart
    ) {

        Surface(
            shape = MaterialTheme.shapes.large,
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth(),
            color = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(
                bottomStart = LARGE_PADDING,
                bottomEnd = LARGE_PADDING
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MEDIUM_PADDING)
            ) {
                Text(
                    text = hero.name,
                    color = topAppContentColor(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = hero.about,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Row(

                ) {
                    RatingWidget(
                        modifier = Modifier
                            .padding(SMALL_PADDING),
                        rating = hero.rating
                    )
                    Text(
                        text = "(${hero.rating})",
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.6f),
                    )
                }
            }

        }
    }


}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HeroItemPreview(

) {
    val hero = Hero(
        id = 1,
        name = "Vlad",
        about = "Blabla",
        rating = 3.6,
        day = "OK",
        month = "Jan",
        family = listOf(),
        abilities = listOf(),
        natureTypes = listOf(),
        power = 4,
        image = "jkljk;j;kkl;lk;"

    )
    HeroItem(
        hero,
        navHostController = rememberNavController()
    )

}
