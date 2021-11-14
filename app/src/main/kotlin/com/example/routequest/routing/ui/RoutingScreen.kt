package com.example.routequest.routing.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.example.routequest.R
import com.example.routequest.routing.domain.Distance
import com.example.routequest.routing.domain.Route
import com.example.routequest.routing.domain.foreach
import com.example.routequest.ui.states.ErrorState
import com.example.routequest.ui.states.LoadingState
import com.example.routequest.ui.states.OfflineState
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun RoutingScreen() {
    val viewModel: RoutingViewModel = mavericksViewModel()
    val route by viewModel.collectAsState { it.route }
    val distance by viewModel.collectAsState { it.distance }
    val errorMessage by viewModel.collectAsState { it.errorMessage }
    val isLoading by viewModel.collectAsState { it.isLoading }
    val isOnline by viewModel.collectAsState { it.isOnline }

    if (isLoading) LoadingState()
    when {
        !isOnline -> OfflineState()
        errorMessage != null -> ErrorState(message = errorMessage!!)
        else -> RouteMap(route, distance)
    }
}

@Composable
fun RouteMap(route: Route?, distance: Distance?) {
    val mapView = rememberMapViewWithLifecycle()
    AndroidView(
        factory = { mapView },
        update = { googleMap ->
            CoroutineScope(Dispatchers.Main).launch {
                val map = googleMap.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                if (route != null) {
                    val pickUp = route.fold(route) { _, next -> next }.let { start ->
                        LatLng(start.coordinates.latitude, start.coordinates.longitude)
                    }
                    val destination = LatLng(
                        route.coordinates.latitude,
                        route.coordinates.longitude
                    )

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 6f))

                    val markerOptions = MarkerOptions()
                        .position(pickUp)
                    map.addMarker(markerOptions)

                    val markerOptionsDestination = MarkerOptions()
                        .apply {
                            if (distance != null) title(distance.toString())
                            position(destination)
                        }
                    map
                        .addMarker(markerOptionsDestination)
                        ?.showInfoWindow()

                    val polylineRoute = PolylineOptions().apply {
                        route.foreach { current ->
                            add(
                                LatLng(
                                    current.coordinates.latitude,
                                    current.coordinates.longitude
                                )
                            )
                        }
                    }
                    map.addPolyline(polylineRoute)
                }
            }
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

@Preview(
    name = "RoutingScreenPreview",
    showBackground = true
)
@Composable
fun RoutingScreenPreview() {
    TODO()
}