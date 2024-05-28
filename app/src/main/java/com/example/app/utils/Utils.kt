package com.example.app.utils

import android.content.Context
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