package com.help.branchsupport

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.help.branchsupport.ui.theme.BranchSupportTheme
import java.security.MessageDigest


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BranchSupportTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }

            //getInstalledPackageNames(this)
            getallapps(this)
            printSampleSha1List(this)
        }
    }

    fun getInstalledPackageNames(context: Context): List<String> {
        val packageNames = mutableListOf<String>()
        val packageManager: PackageManager = context.packageManager

        // Get the list of installed applications
        val installedPackages = packageManager.getInstalledApplications(0) // Remove GET_META_DATA

        // Iterate through the installed applications and extract package names
        for (applicationInfo in installedPackages) {
            packageNames.add(applicationInfo.packageName)
            Log.e("Package Name", applicationInfo.packageName)
        }

        return packageNames
    }

    fun getallapps() {
        // get list of all the apps installed
        val infos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        // create a list with size of total number of apps
        val apps = arrayOfNulls<String>(infos.size)
        var i = 0
        // add all the app name in string list
        for (info in infos) {
            apps[i] = info.packageName
            Log.e("Package Name", " -> " + apps[i])
            i++
        }
        // set all the apps name in list view
        Toast.makeText(this, infos.size.toString() + " Apps are installed", Toast.LENGTH_SHORT)
            .show()
    }

    // https://tomas-repcik.medium.com/listing-all-installed-apps-in-android-13-via-packagemanager-3b04771dc73
    fun getallapps(context: Context) {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }
    }

    //https://stackoverflow.com/questions/46390522/android-get-list-of-installed-apps-signatures
    fun printSampleSha1List(ctx: Context) {
        val packages: List<ApplicationInfo> =
            ctx.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (pkg in packages) {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = ctx.packageManager.getPackageInfo(
                    pkg.packageName,
                    PackageManager.GET_SIGNATURES
                )
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            packageInfo?.signatures?.forEach { signature ->
                // SHA1 the signature
                val sha1 = getSHA1(signature.toByteArray())

                Log.i("Sha1", "name:${pkg.packageName}, $sha1")
                // Note: sample just checks the first signature
                return@forEach
            }
        }
    }

    fun getSHA1(sig: ByteArray): String {
        val digest: MessageDigest
        try {
            digest = MessageDigest.getInstance("SHA1")
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
        digest.update(sig)
        val hashtext: ByteArray = digest.digest()
        return bytesToHex(hashtext)
    }

    // Util method to convert byte array to hex string
    fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F'
        )
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    override fun onStart() {
        super.onStart()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BranchSupportTheme {
        Greeting("Android")
    }
}

