package com.example.ktor.presentation.screens.search


import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ktor.R
import com.example.ktor.ui.theme.KTorTheme
import com.example.ktor.ui.theme.searchWidgetBackgroundColor
import com.example.ktor.ui.theme.searchWidgetContentColor
import kotlin.time.Clock


@Composable
fun SearchTopBar(
    query: String,
    onClose: () -> Unit,
    onSearchClick: (String) -> Unit,
    onTextChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) { 
        SearchWidget(
            query = query,
            onClose = onClose,
            onSearchClick = onSearchClick,
            onTextChange = onTextChange
        )
    }
    
}



@Composable
fun SearchWidget(
    query: String,
    onClose: () -> Unit,
    onSearchClick: (String) -> Unit,
    onTextChange: (String) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = searchWidgetBackgroundColor(),
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = query,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = searchWidgetBackgroundColor(),
            ),
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    text = "search...",
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    color = Color.White
                )
            },
            textStyle = TextStyle(color = searchWidgetContentColor()),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {
                       onSearchClick(query)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_search_ic),
                        contentDescription = null,
                        tint = searchWidgetContentColor()
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (query.isNotBlank()) {
                            onTextChange("")
                        } else {
                            onClose()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_close_ic),
                        contentDescription = null,
                        tint = searchWidgetContentColor(),
                    )
                }
            },
            // говорит что поле для поиска и спец клаву просит с поиском
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

            keyboardActions = KeyboardActions(onSearch = { onSearchClick(query) })


        )

    }

}

@Preview
@Composable
fun SearchWidgetPreview(
) {

    KTorTheme(

    ) {
        SearchWidget(
            query = "",
            onClose = {},
            onSearchClick = {},
            onTextChange = {}
        )
    }

}