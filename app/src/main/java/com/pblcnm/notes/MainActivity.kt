package com.pblcnm.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.pblcnm.notes.ui.screen.create.CreateNoteScreen
import com.pblcnm.notes.ui.theme.RickNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickNotesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A1C29),
                                        Color(0xFF0C0F1C),
                                        Color(0xFF2A1E36)
                                    )
                                )
                            )
                    ) {
                        CreateNoteScreen(
                            onBackToDimension = {},
                            modifier = Modifier.fillMaxSize()
                        )
                    }
//                    NotesListScreen(
//                        modifier =  Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}
