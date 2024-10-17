package com.example.news.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.news.R
import com.example.news.viewmodel.FavouriteNewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment(R.layout.news_fragment) {

    private val viewModel: FavouriteNewsViewModel by activityViewModels()

    private val args: NewsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.article

        view.findViewById<WebView>(R.id.webView).apply {
            webViewClient = WebViewClient()
            loadUrl(article.url.toString())
        }

        view.findViewById<ComposeView>(R.id.newsfragment_compose_view).setContent {

            val articlesFoundByTitle =
                viewModel.getFavouriteArticleByTitle(article.title.toString())
                    .collectAsState(
                        initial = emptyList()
                    ).value

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xff1c1c27)
                    )

            ) {

                var isFavourite by remember { mutableStateOf(false) }

                Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 20.dp)
                        .clickable {
                            findNavController().navigateUp()
                        }

                )
                Box(modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 20.dp)
                    .clickable {
                        isFavourite = true
                    }) {

                    if (isFavourite) {

                        val context = LocalContext.current

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_favorite_24),
                            contentDescription = "Added to Favourite News",
                            tint = Color.Red,
                            modifier = Modifier.size(30.dp)
                        )

                        if (articlesFoundByTitle.isEmpty()) {
                            viewModel.insertFavouriteArticle(article)
                            Toast.makeText(
                                context,
                                "The article is added to favourite news",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                            contentDescription = "Add to Favourite News",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )

                    }
                }
            }
        }
    }
}