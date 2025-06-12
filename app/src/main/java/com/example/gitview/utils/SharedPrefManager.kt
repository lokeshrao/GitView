package com.example.gitview.utils

import android.content.Context

class BookmarkPrefs(context: Context) {
    private val prefs = context.getSharedPreferences("bookmarks", Context.MODE_PRIVATE)

    fun saveBookmark(username: String, avatarUrl: String) {
        val editor = prefs.edit()
        editor.putString(username, avatarUrl)
        editor.apply()
    }

    fun removeBookmark(username: String) {
        prefs.edit().remove(username).apply()
    }

    fun getAllBookmarks(): Map<String, String> {
        return prefs.all.mapValues { it.value as String }
    }

    fun isBookmarked(username: String): Boolean {
        return prefs.contains(username)
    }
}

