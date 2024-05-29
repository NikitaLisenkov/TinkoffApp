package com.example.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.TypedValue
import com.example.app.TinkoffApp
import kotlinx.coroutines.CancellationException
import kotlin.math.roundToInt

fun Float.sp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics
)

fun Float.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)

fun Int.dp(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
).roundToInt()

fun Context.getApp(): TinkoffApp = this.applicationContext as TinkoffApp

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}

suspend fun <T> runSuspendCatching(
    onSuccess: suspend (T) -> Unit = {},
    onError: () -> Unit = {},
    action: suspend () -> T,
): T? = try {
    val result = action()
    onSuccess.invoke(result)
    result
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    onError.invoke()
    null
}