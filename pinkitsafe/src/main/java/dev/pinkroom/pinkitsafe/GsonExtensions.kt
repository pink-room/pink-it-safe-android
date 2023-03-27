package dev.pinkroom.pinkitsafe

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@PublishedApi
internal inline fun <reified T> Gson.fromJson(value: String): T = fromJson(value, typeToken<T>())

@PublishedApi
internal inline fun <reified T> typeToken(): Type = object : TypeToken<T>() {}.type