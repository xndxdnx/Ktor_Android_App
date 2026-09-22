package com.example.ktor.presentation.screens.welcome.weclome_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ktor.domain.model.OnBoardingPage
import com.example.ktor.navigation.Screens
import com.example.ktor.ui.theme.activeIndicatorColor
import com.example.ktor.ui.theme.buttonWelcomeColor
import com.example.ktor.ui.theme.inactiveIndicatorColor
import com.example.ktor.ui.theme.welcomeScreenBackgroundColor
import com.example.ktor.ui.theme.welcomeScreenDescriptionColor
import com.example.ktor.ui.theme.welcomeScreenTitleColor
import com.example.ktor.util.Constants.ONBOARDING_PAGE_COUNT
import com.example.ktor.util.EXTRA_LAGE_PADDING
import com.example.ktor.util.PAGING_INDICATOR_SPACING
import com.example.ktor.util.PAGING_INDICATOR_WIDTH
import com.example.ktor.util.SMALL_PADDING

@Composable
fun WelcomeScreen(
    navHostController: NavHostController
) {

    val pages = listOf<OnBoardingPage>(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)

    val pagesState = rememberPagerState(pageCount = { ONBOARDING_PAGE_COUNT })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(welcomeScreenBackgroundColor())
    ) {
        HorizontalPager(
            state = pagesState,
            modifier = Modifier
                .weight(10f),
            verticalAlignment = Alignment.CenterVertically
        ) { position ->
            PagerScreen(pages[position])
        }
        HorizontalPagerIndicator(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .padding(SMALL_PADDING),
            state = pagesState,
            pageCount = ONBOARDING_PAGE_COUNT,
            activeColor = activeIndicatorColor(),
            inactiveColor = inactiveIndicatorColor(),
        )
        FinishButton(
            modifier = Modifier
                .weight(1f),
            pagerState = pagesState,
            onClick = {
                navHostController.popBackStack()
                navHostController.navigate(route = Screens.HomeScreen.route)
            }
        )
    }
}

@Composable
fun PagerScreen(
    onBoardingPage: OnBoardingPage,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(onBoardingPage.images),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.7f)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onBoardingPage.title,
            color = welcomeScreenTitleColor(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = EXTRA_LAGE_PADDING)
                .padding(top = SMALL_PADDING),
            text = onBoardingPage.description,
            color = welcomeScreenDescriptionColor(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun HorizontalPagerIndicator(
    modifier: Modifier = Modifier,
    state: PagerState,
    pageCount: Int,
    activeColor: androidx.compose.ui.graphics.Color,
    inactiveColor: androidx.compose.ui.graphics.Color,
    indicatorWidth: Dp = PAGING_INDICATOR_WIDTH,
    spacing: Dp = PAGING_INDICATOR_SPACING
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(indicatorWidth)
                    .clip(CircleShape)
                    .background(
                        color = if (state.currentPage == index) {
                            activeColor
                        } else {
                            inactiveColor
                        }
                    ),
            )

        }

    }


}

@Composable
fun FinishButton(
    pagerState: PagerState,
    onClick:() -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .padding(EXTRA_LAGE_PADDING),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonWelcomeColor()
                )
            ) { 
                Text(text ="Start")
            }
        }
    }
    
}


@Composable
@Preview
fun FirstOnboardingScreenPreview() {
    
  Column(
      modifier = Modifier.fillMaxSize()
  ) { 
      PagerScreen(
          onBoardingPage = OnBoardingPage.Second
      )
  }
    
    
}