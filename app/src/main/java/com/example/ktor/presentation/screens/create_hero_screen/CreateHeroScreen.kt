package com.example.ktor.presentation.screens.create_hero_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ktor.R
import com.example.ktor.presentation.screens.create_hero_screen.components.FormCard
import com.example.ktor.ui.theme.DarkGray
import com.example.ktor.ui.theme.Purple40
import com.example.ktor.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHeroScreen(
    viewModel: CreateHeroViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var imageFileName by remember { mutableStateOf("hero_image.jpg") }
    var name by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("4.5") }
    var power by remember { mutableStateOf("80") }
    var month by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var family by remember { mutableStateOf("") }
    var abilities by remember { mutableStateOf("") }
    var natureTypes by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imageUri = uri
        imageBytes = null
    }

    val context = LocalContext.current
    val darkTheme = isSystemInDarkTheme()
    val pageColor = if (darkTheme) Color.Black else Color(0xFFF4F1FA)
    val cardColor = if (darkTheme) DarkGray else Color.White
    val accent = if (darkTheme) Purple80 else Purple40
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = accent,
        focusedLabelColor = accent,
        cursorColor = accent
    )

    LaunchedEffect(imageUri) {
        val uri = imageUri ?: return@LaunchedEffect

        val loaded = viewModel.loadImageFromUri(context = context, uri = uri)

        if (loaded == null) {
            viewModel.errorMessage.value = context.getString(R.string.image_read_failed)
            imageUri = null
        } else {
            imageBytes = loaded.first
            imageFileName = loaded.second
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = pageColor,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = pageColor

                ), title = {
                    Column() {
                        Text(
                            text = "New Hero", fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Add a corcerer to the archive",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
                    }
                }, navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,

                            )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImagePicker(
                image = imageUri, accent = accent, onClick = {
                    imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                })

            FormCard(
                color = cardColor,
            ) {
                SectionTitle("Profile")
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = "Name") },
                    colors = fieldColors,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = about,
                    onValueChange = { about = it },
                    label = { Text(text = "About") },
                    colors = fieldColors,
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            
            FormCard(
                color = cardColor,
            ) {
                SectionTitle("About")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = rating,
                        onValueChange = { rating = it },
                        label = { Text(text = "Rating") },
                        colors = fieldColors,
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                    )
                    OutlinedTextField(
                        value = power,
                        onValueChange = { power = it },
                        label = { Text(text = "Power") },
                        colors = fieldColors,
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = month,
                        onValueChange = { month = it },
                        label = { Text(text = "month") },
                        colors = fieldColors,
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                    )
                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it },
                        label = { Text(text = "birthday") },
                        colors = fieldColors,
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
            FormCard(
                color = cardColor,
            ) {
                SectionTitle("Connections")
                OutlinedTextField(
                    value = family,
                    onValueChange = { family = it },
                    label = { Text(text = "Family (comma separated)") },
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = abilities,
                    onValueChange = { abilities = it },
                    label = { Text(text = "Abilities (comma separated)") },
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = natureTypes,
                    onValueChange = { natureTypes = it },
                    label = { Text(text = "Nature Types (comma separated)") },
                    colors = fieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            viewModel.errorMessage.value?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Button(
                onClick = {
                    val bytes = imageBytes
                    
                    if (bytes == null) {
                        viewModel.errorMessage.value = if (imageUri == null ) {
                            context.getString(R.string.image_required)
                        }else{
                            context.getString(R.string.image_read_failed)
                        }
                        return@Button
                    }
                    if (name.isNullOrBlank()) {
                        viewModel.errorMessage.value = context.getString(R.string.name_required)
                        return@Button
                    }
                    
                    viewModel.createHero(
                        imageBytes = bytes,
                        fileName = imageFileName,
                        name = name,
                        about = about,
                        rating = rating,
                        power = power,
                        month = month,
                        day = day,
                        family = family,
                        abilities = abilities,
                        natureTypes = natureTypes,
                        onSuccess = { navHostController.popBackStack() }
                    )
                },
                enabled = viewModel.isLoading.value,
                colors = ButtonDefaults.buttonColors(accent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (viewModel.isLoading.value){
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                }else{ Text(text = "Create Hero",) }
            }
            
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun ImagePicker(
    onClick: () -> Unit = {}, accent: Color, image: Uri?
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        accent.copy(alpha = 0.35f), accent.copy(alpha = 0.01f)
                    )
                )
            )
            .border(
                width = 1.dp, color = accent.copy(alpha = 0.45f), shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                onClick = onClick
            ), contentAlignment = Alignment.Center
    ) {
        if (image != null) {
            AsyncImage(
                model = image,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = Color.Black.copy(alpha = 0.45f))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.tab_to_chose_a_portrait), color = Color.White
                )

            }

        } else {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = accent,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Pick Hero Image",
                    color = accent,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.tab_to_chose_a_portrait),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }


        }

    }

}