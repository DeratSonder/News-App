package com.example.news.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.news.R
import com.example.news.compose.NewsListScreen


@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class NewsListFragment : Fragment(R.layout.newslist_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ComposeView>(R.id.newslist_compose_view).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {

                NewsListScreen(
                    onClickToTopBar = {
                        findNavController().navigate(
                            R.id.action_newsListFragment_to_favouriteNewsFragment
                        )
                    },

                    onClick = {
                        val bundle = Bundle().apply {
                            putSerializable("article", it)
                        }
                        findNavController().navigate(
                            R.id.action_newsListFragment_to_newsFragment,
                            bundle
                        )

                    }
                )
            }
        }
    }

}

