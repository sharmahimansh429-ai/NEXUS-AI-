package com.example

import com.example.data.update.AppRelease
import com.example.data.update.ReleaseAsset
import com.example.data.update.UpdateState
import com.example.data.update.VersionComparator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateSystemTest {

    @Test
    fun versionComparator_detectsNewerVersionsCorrectly() {
        // v1.1 is newer than 1.0
        assertTrue(VersionComparator.isRemoteNewer("v1.1", "1.0"))
        // 1.0.1 is newer than 1.0
        assertTrue(VersionComparator.isRemoteNewer("1.0.1", "1.0"))
        // 2.0.0 is newer than 1.9.9
        assertTrue(VersionComparator.isRemoteNewer("2.0.0", "1.9.9"))
        // v1.2-beta is newer than 1.1
        assertTrue(VersionComparator.isRemoteNewer("v1.2-beta", "1.1"))

        // Same version is not newer
        assertFalse(VersionComparator.isRemoteNewer("1.0", "1.0"))
        assertFalse(VersionComparator.isRemoteNewer("v1.0", "1.0"))
        assertFalse(VersionComparator.isRemoteNewer("v1.0.0", "1.0.0"))

        // Older version is not newer
        assertFalse(VersionComparator.isRemoteNewer("0.9", "1.0"))
        assertFalse(VersionComparator.isRemoteNewer("v0.9.5", "1.0"))
    }

    @Test
    fun versionComparator_cleansVersionString() {
        assertEquals("1.0", VersionComparator.cleanVersionString("v1.0"))
        assertEquals("2.3.4", VersionComparator.cleanVersionString("V2.3.4"))
        assertEquals("1.0.0", VersionComparator.cleanVersionString("1.0.0"))
    }

    @Test
    fun appRelease_modelIntegrity() {
        val asset = ReleaseAsset(
            name = "nexus-ai-v1.1.apk",
            downloadUrl = "https://github.com/sharmahimansh429-ai/NEXUS-AI-/releases/download/v1.1/nexus-ai-v1.1.apk",
            sizeBytes = 25000000L
        )

        val release = AppRelease(
            tagName = "v1.1",
            cleanVersion = "1.1",
            releaseTitle = "NEXUS AI v1.1",
            releaseNotes = "Added APK updater",
            publishedAt = "2026-10-27T12:00:00Z",
            htmlUrl = "https://github.com/sharmahimansh429-ai/NEXUS-AI-/releases/tag/v1.1",
            apkAsset = asset
        )

        assertEquals("v1.1", release.tagName)
        assertEquals("1.1", release.cleanVersion)
        assertEquals("nexus-ai-v1.1.apk", release.apkAsset?.name)
        assertEquals(25000000L, release.apkAsset?.sizeBytes)

        val updateAvailable = UpdateState.UpdateAvailable(release, "1.0")
        assertEquals("1.0", updateAvailable.currentVersion)
        assertEquals(release, updateAvailable.release)
    }
}
