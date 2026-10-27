package com.example.data.update

import java.io.File

data class ReleaseAsset(
    val name: String,
    val downloadUrl: String,
    val sizeBytes: Long
)

data class AppRelease(
    val tagName: String,
    val cleanVersion: String,
    val releaseTitle: String,
    val releaseNotes: String,
    val publishedAt: String,
    val htmlUrl: String,
    val apkAsset: ReleaseAsset?
)

sealed class UpdateState {
    object Idle : UpdateState()
    object Checking : UpdateState()
    data class UpdateAvailable(
        val release: AppRelease,
        val currentVersion: String
    ) : UpdateState()

    data class UpToDate(
        val currentVersion: String,
        val latestTag: String,
        val checkedAt: Long = System.currentTimeMillis()
    ) : UpdateState()

    data class Downloading(
        val progressPercent: Int,
        val bytesDownloaded: Long,
        val totalBytes: Long,
        val release: AppRelease
    ) : UpdateState()

    data class ReadyToInstall(
        val apkFile: File,
        val release: AppRelease
    ) : UpdateState()

    data class Error(
        val message: String,
        val isNetworkIssue: Boolean = false,
        val releaseUrl: String? = null
    ) : UpdateState()
}

object VersionComparator {
    fun normalizeVersion(version: String): List<Int> {
        val clean = version.trim()
            .removePrefix("v")
            .removePrefix("V")
        return clean.split(".", "-", "_").mapNotNull { part ->
            val digits = part.takeWhile { it.isDigit() }
            if (digits.isNotEmpty()) digits.toIntOrNull() else null
        }
    }

    fun isRemoteNewer(remoteVersion: String, currentVersion: String): Boolean {
        val remoteParts = normalizeVersion(remoteVersion)
        val currentParts = normalizeVersion(currentVersion)
        val maxLen = maxOf(remoteParts.size, currentParts.size)
        for (i in 0 until maxLen) {
            val r = remoteParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (r > c) return true
            if (r < c) return false
        }
        return false
    }

    fun cleanVersionString(tag: String): String {
        return tag.trim().removePrefix("v").removePrefix("V")
    }
}
