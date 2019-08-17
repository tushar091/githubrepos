package com.example.githubrepos.network

import android.util.Log
import com.example.githubrepos.model.BaseRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


fun <T> createRequest(request: BaseRequest, classOfT: Class<T>): Observable<Optional<T>> {
    return Observable.fromCallable {
        val url = URL("https://api.github.com/repositories")
        val response = getResponseFromHttpUrl(url)
        Optional.ofNullable(parseResponse(response, classOfT))
    }
}

fun <T> createListRequest(request: BaseRequest, classOfT: Class<T>): Observable<Optional<Array<T>>> {
    return Observable.fromCallable {
        val response = getResponseFromHttpUrl(request.url)
        Optional.ofNullable(parseListResponse(response, classOfT))
    }
}

@Throws(IOException::class)
fun getResponseFromHttpUrl(url: URL): String? {
    val urlConnection = url.openConnection() as HttpURLConnection
    try {
        val `in` = urlConnection.getInputStream()

        val scanner = Scanner(`in`)
        scanner.useDelimiter("\\A")

        val hasInput = scanner.hasNext()
        var response: String? = null
        if (hasInput) {
            response = scanner.next()
        }
        scanner.close()
        return response
    } finally {
        urlConnection.disconnect()
    }
}

fun <T> parseResponse(response: String?, classOfT: Class<T>): T? {
    val gson = Gson()
    try {
        return gson.fromJson(response, classOfT)
    } catch (e: JsonSyntaxException) {
        Log.e("json exception", e.message)
    } catch (e: Exception) {
        Log.e("json exception", e.message)
    }
    return null
}

fun <T> parseListResponse(response: String?, classOfT: Class<T>): Array<T>? {
    val gson = Gson()
    try {
        val listType = TypeToken.getArray(classOfT)
        return gson.fromJson(response, listType.type)
    } catch (e: JsonSyntaxException) {
        Log.e("json exception", e.message)
    } catch (e: Exception) {
        Log.e("json exception", e.message)
    }
    return null
}
