package com.example.routequest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airbnb.mvrx.Mavericks
import com.example.routequest.R
import com.example.routequest.routing.ui.RoutingScreen
import com.example.routequest.ui.theme.RouteQuestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Mavericks.initialize(this)
        super.onCreate(savedInstanceState)
        setContent {
            RouteQuestTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            }
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colors.background
                    ) {
                        RoutingScreen()
                    }
                }
            }
        }
    }
}
