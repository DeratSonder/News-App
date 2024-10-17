package com.example.news.compose

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.news.R
import com.example.news.data.remote.response.Article
import com.example.news.util.Constants.countryList
import com.example.news.util.Constants.headerTabRow
import com.example.news.viewmodel.NewsListViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NewsListScreen(
    onClickToTopBar: () -> Unit,
    onClick: (Article) -> Unit,
    viewModel: NewsListViewModel = hiltViewModel(),
) {
    Surface(
        color = Color(0xff1c1c27),
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {

            TopBar(viewModel = viewModel, onClick = onClickToTopBar)

            TabBar(titles = headerTabRow)

            NewsList(onClick = onClick)

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun TopBar(
    onClick: () -> Unit,
    viewModel: NewsListViewModel = hiltViewModel()
) {

    var openDialog by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()

    ) {

        Icon(
            painter = painterResource(id = R.drawable.earth_svgrepo_com),
            contentDescription = "Country",
            tint = Color.White,
            modifier = Modifier
                .size(45.dp)
                .clickable {
                    openDialog = true
                }
        )

        SearchBar {
            viewModel.searchNewsList(it)
        }

        Icon(
            painter = painterResource(id = R.drawable.heart_svgrepo_com),
            contentDescription = "Announcement",
            modifier = Modifier
                .size(45.dp)
                .clickable {
                    onClick()
                },
            tint = Color.White
        )

    }

    if (openDialog) {

        CountryListDialog(
            onClick = {
                viewModel.clearNewsList()
                viewModel.setCountryCode(it)
                openDialog = false
            },
            countryList = countryList
        )

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit = {}
) {

    var text by remember {
        mutableStateOf("")
    }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onSearch(it)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "Search Icon",
                tint = Color.White
            )

        },
        placeholder = {
            Text("Search...", color = Color.White)
        },
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(color = Color.White),
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 12.dp)
            .clip(CircleShape),
        colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xff2e2e38))
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun TabBar(
    titles: List<String>,
    viewModel: NewsListViewModel = hiltViewModel()
) {

    var state by remember {
        mutableIntStateOf(viewModel.stateOfTabRow.intValue)
    }

    ScrollableTabRow(
        selectedTabIndex = state,
        containerColor = Color(0xff1c1c27),
        contentColor = Color.White,
        edgePadding = 0.dp,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[state]),
                color = Color(0xff0b86e7) // Change this to your desired color
            )
        }

    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                onClick = {
                    state = index
                    viewModel.setStateOfTabRow(index)
                    viewModel.setCategory(title.lowercase(Locale.ROOT))
                    viewModel.clearNewsList()
                },
                selected = (index == state),
                unselectedContentColor = Color(0xff6c6e7c),

                text = {
                    if (state == index)
                        Text(title, fontWeight = FontWeight.Bold)
                    else Text(text = title)
                },

                )
        }

    }

}

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NewsList(
    onClick: (Article) -> Unit,
    viewModel: NewsListViewModel = hiltViewModel()
) {

    val newList by viewModel.newsList.collectAsState()
    val category by remember { viewModel.categoryValue }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {

        if (newList.isEmpty()) {
            viewModel.setCurPage()
            viewModel.loadCategoryNewsPaginated(category)
        }

        items(newList.size) {

            if (it >= newList.size - 1 && !endReached && !isLoading) {
                viewModel.loadCategoryNewsPaginated(category)

            }

            NewsCard(
                article = newList[it],
                width = 400.dp,
                height = 300.dp,
                onClick = onClick

            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.Green)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadCategoryNewsPaginated(category)
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NewsCard(
    article: Article,
    width: Dp,
    height: Dp,
    onClick: (Article) -> Unit,
    viewModel: NewsListViewModel = hiltViewModel()
) {

    ElevatedCard(
        modifier = Modifier
            .padding(12.dp)
            .width(width)
            .height(height)
            .clickable {
                onClick(article)
            },
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 20f
                        )

                    )
            )
            Box(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.BottomStart)
            ) {
                Column {
                    Row {
                        Text(
                            text = article.source?.name.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)

                        )
                        Text(
                            text = viewModel.defineTimePublish(article.publishedAt.toString()),
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Text(
                        text = article.title.toString(),
                        maxLines = 2,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Left

                    )
                }
            }
        }
    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.White),

        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Oops!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = error,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff0b86e7)
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(10.dp),
                onClick = { onRetry() }
            ) {
                Text("Retry")
            }
        }
    }
}