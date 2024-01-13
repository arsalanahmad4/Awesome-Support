package com.help.branchsupport

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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

            getInstalledPackageNames(this)
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

