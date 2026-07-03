package com.icodeforyou.sqleditor.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.icodeforyou.sqleditor.SqlQueryEditor

private const val TAG = "SqlEditorSample"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                SampleScreen()
            }
        }
    }
}

@Composable
private fun SampleScreen() {
    var editorVisible by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable {
        mutableStateOf("SELECT *\nFROM users\nWHERE active = 1;")
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Button(onClick = { editorVisible = true }) {
                Text(text = "Edit SQL")
            }
        }
    }

    if (editorVisible) {
        SqlQueryEditor(
            initialQuery = query,
            onAccept = { accepted ->
                Log.d(TAG, "ACCEPT -> $accepted")
                query = accepted
                editorVisible = false
            },
            onCancel = {
                Log.d(TAG, "CANCEL -> query unchanged: $query")
                editorVisible = false
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview() {
    MaterialTheme {
        SampleScreen()
    }
}