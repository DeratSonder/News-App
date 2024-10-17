package com.example.news.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news.viewmodel.NewsListViewModel
import timber.log.Timber

@Composable
fun DeleteItemDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: Painter,
) {
    AlertDialog(
        icon = {
            Icon(
                icon,
                contentDescription = "Example Icon",
                tint = Color(0xff0b86e7)
            )
        },
        title = {
            Text(
                text = dialogTitle,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = dialogText,
                color = Color.Black,
                fontSize = 15.sp
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm", color = Color(0xff0b86e7))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss", color = Color(0xff0b86e7))
            }
        },
        containerColor = Color.White,
        tonalElevation = AlertDialogDefaults.TonalElevation,
        modifier = Modifier.shadow(elevation = 10.dp, shape = RoundedCornerShape(10.dp))
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryListDialog(
    onClick: (String) -> Unit,
    countryList: List<Map<String, String>>,
    viewModel: NewsListViewModel = hiltViewModel()
) {

    val currentCountryCode = viewModel.countryCode.value

    Dialog(
        onDismissRequest = {
            onClick(viewModel.countryCode.value)
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier
                .height(400.dp)
                .width(300.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.White)

        ) {
            Column {

                Text(
                    "Country",
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 25.dp)
                        .padding(vertical = 25.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(10.dp)

                ) {
                    items(countryList) { country ->
                        country.entries.firstOrNull()?.let { (countryName, countryCode) ->
                            ListItem(

                                headlineText = {
                                    Text(
                                        text = countryName,
                                        color = if (countryCode != currentCountryCode) Color(
                                            0xff0b86e7
                                        )
                                        else Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = if(countryCode != currentCountryCode) Color.White
                                    else Color(0xff0b86e7)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onClick(countryCode)
                                    }
                            )
                        }

                    }
                }
            }
        }

    }
}
