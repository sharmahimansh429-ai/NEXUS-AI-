package com.example.data.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class UpdateManager(
    private val context: Context,
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()
) {

    companion object {
        const val REPO_OWNER = "sharmahimansh429-ai"
        const val REPO_NAME = "NEXUS-AI-"
        const val RELEASES_API_URL = "https://api.github.com/repos/$REPO_OWNER/$REPO_NAME/releases/latest"
        const val GITHUB_REPO_URL = "https://github.com/$REPO_OWNER/$REPO_NAME"
    }

    /**
     * Checks the latest published GitHub release and compares it with installed version.
     */
    suspend fun checkLatestRelease(): Result<AppRelease> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(RELEASES_API_URL)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("User-Agent", "NEXUS-AI-App/${BuildConfig.VERSION_NAME}")
                .build()

            val response = okHttpClient.newCall(request).execute()
            val code = response.code
            val bodyString = response.body?.string()

            if (code == 404) {
                return@withContext Result.failure(
                    IOException("No published releases found on GitHub ($REPO_OWNER/$REPO_NAME) yet.")
                )
            }

            if (code == 403) {
                return@withContext Result.failure(
                    IOException("GitHub API rate limit reached. Please wait a minute and retry.")
                )
            }

            if (!response.isSuccessful || bodyString.isNullOrBlank()) {
                return@withContext Result.failure(
                    IOException("GitHub release check failed (HTTP $code).")
                )
            }

            val json = JSONObject(bodyString)
            val tagName = json.optString("tag_name", "").trim()
            val releaseTitle = json.optString("name", tagName).ifBlank { tagName }
            val releaseNotes = json.optString("body", "No release notes provided.").trim()
            val publishedAt = json.optString("published_at", "")
            val htmlUrl = json.optString("html_url", "$GITHUB_REPO_URL/releases")

            val assetsArray = json.optJSONArray("assets")
            var apkAsset: ReleaseAsset? = null

            if (assetsArray != null) {
                for (i in 0 until assetsArray.length()) {
                    val assetObj = assetsArray.optJSONObject(i) ?: continue
                    val assetName = assetObj.optString("name", "")
                    if (assetName.endsWith(".apk", ignoreCase = true)) {
                        val downloadUrl = assetObj.optString("browser_download_url", "")
                        val size = assetObj.optLong("size", 0L)
                        apkAsset = ReleaseAsset(
                            name = assetName,
                            downloadUrl = downloadUrl,
                            sizeBytes = size
                        )
                        break
                    }
                }
            }

            val cleanVer = VersionComparator.cleanVersionString(tagName)
            val release = AppRelease(
                tagName = tagName,
                cleanVersion = cleanVer,
                releaseTitle = releaseTitle,
                releaseNotes = releaseNotes,
                publishedAt = publishedAt,
                htmlUrl = htmlUrl,
                apkAsset = apkAsset
            )
            Result.success(release)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Streams the APK download to internal cache with periodic progress callbacks.
     */
    suspend fun downloadApk(
        release: AppRelease,
        onProgress: (percent: Int, downloadedBytes: Long, totalBytes: Long) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        val apkAsset = release.apkAsset
            ?: return@withContext Result.failure(
                IllegalStateException("Release '${release.tagName}' does not have an attached .apk asset.")
            )

        try {
            val updatesDir = File(context.cacheDir, "updates")
            if (!updatesDir.exists()) {
                updatesDir.mkdirs()
            }

            val destinationFile = File(updatesDir, "nexus-ai-${release.cleanVersion}.apk")
            if (destinationFile.exists()) {
                destinationFile.delete()
            }

            val request = Request.Builder()
                .url(apkAsset.downloadUrl)
                .addHeader("User-Agent", "NEXUS-AI-App/${BuildConfig.VERSION_NAME}")
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    IOException("Download failed with HTTP ${response.code}")
                )
            }

            val body = response.body
                ?: return@withContext Result.failure(IOException("Empty response body from release asset server."))

            val totalBytes = if (apkAsset.sizeBytes > 0) apkAsset.sizeBytes else body.contentLength()
            var downloadedBytes = 0L

            body.byteStream().use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(32 * 1024)
                    var read: Int
                    var lastPercent = -1

                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                        downloadedBytes += read

                        val percent = if (totalBytes > 0) {
                            ((downloadedBytes * 100) / totalBytes).toInt().coerceIn(0, 100)
                        } else 0

                        if (percent != lastPercent) {
                            lastPercent = percent
                            onProgress(percent, downloadedBytes, totalBytes)
                        }
                    }
                    output.flush()
                }
            }

            Result.success(destinationFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks if this app has permission to install unknown APKs (Android 8.0+).
     */
    fun canRequestPackageInstalls(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }

    /**
     * Returns the intent to direct user to the Android settings to enable installing unknown apps.
     */
    fun getManageUnknownSourcesIntent(): Intent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    /**
     * Prompts the official Android PackageInstaller to install the downloaded APK.
     */
    fun installApk(apkFile: File): Result<Unit> {
        return try {
            if (!apkFile.exists() || apkFile.length() == 0L) {
                return Result.failure(IOException("APK file is missing or corrupted."))
            }

            val authority = "${context.packageName}.fileprovider"
            val apkUri = FileProvider.getUriForFile(context, authority, apkFile)

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(installIntent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
