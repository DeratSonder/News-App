package com.example.news.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.compose.DeleteItemDialog
import com.example.news.compose.NewsCard
import com.example.news.data.remote.response.Article
import com.example.news.viewmodel.FavouriteNewsViewModel

class FavouriteNewsFragment : Fragment(R.layout.favouritenews_fragment) {

    private val viewModel: FavouriteNewsViewModel by activityViewModels()

    private lateinit var articleDeleted: Article

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.favourite_compose_view).setContent {

            val favouriteArticles by viewModel.favouriteNewsList.collectAsState()
            var openAlertDialog by remember { mutableStateOf(false) }

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(75.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xff1c1c27)
                        )

                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                            .clickable {
                                findNavController().navigateUp()
                            }
                    )

                    Text("Favourite News", color = Color.White, fontSize = 20.sp)

                }

                LazyColumn {

                    items(favouriteArticles.size) {

                        Box {

                            val article = favouriteArticles[it]

                            NewsCard(
                                article = article,
                                width = 400.dp,
                                height = 300.dp,
                                onClick = {
                                    val bundle = Bundle().apply {
                                        putSerializable("article", it)
                                    }
                                    findNavController().navigate(
                                        R.id.action_favouriteNewsFragment_to_newsFragment,
                                        bundle
                                    )
                                }
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .alpha(0.8f)
                                    .size(50.dp)
                                    .padding(top = 20.dp, end = 20.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable {
                                        openAlertDialog = true
                                        articleDeleted = article
                                    },
                                tint = Color.LightGray
                            )
                        }
                    }
                }
            }

            if (openAlertDialog) {

                val context = LocalContext.current

                DeleteItemDialog(
                    onDismissRequest = {
                        openAlertDialog = false
                    },
                    onConfirmation = {

                        viewModel.deleteFavouriteArticle(articleDeleted)

                        Toast.makeText(context, "The article is deleted", Toast.LENGTH_SHORT).show()

                        openAlertDialog = false
                    },
                    dialogTitle = "Delete Selected Article ?",
                    dialogText = "Are you sure, you want to permanently delete the selected article ?",
                    icon = painterResource(id = R.drawable.baseline_delete_24)
                )
            }
        }
    }
}