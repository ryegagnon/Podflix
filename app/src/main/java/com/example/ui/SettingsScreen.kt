package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsScreen(
    onAddFeedClick: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var urlText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .padding(48.dp)
    ) {
        Text(
            text = "Settings",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.colors(
                containerColor = Color.DarkGray,
                contentColor = Color.White,
                focusedContainerColor = Color.LightGray,
                focusedContentColor = Color.Black
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text("Add Feed via RSS URL", fontSize = 18.sp)
            }
        }
    }

    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(Color.DarkGray, RoundedCornerShape(8.dp))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Enter RSS Feed URL", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                // Using standard Compose TextField for input since tv-material doesn't have one yet
                androidx.compose.material3.OutlinedTextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Uri
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
                    modifier = Modifier.background(Color.White, RoundedCornerShape(4.dp)).width(400.dp),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row {
                    androidx.tv.material3.Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.colors(containerColor = Color.Gray)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    androidx.tv.material3.Button(
                        onClick = { 
                            if (urlText.isNotBlank()) {
                                onAddFeedClick(urlText)
                                showDialog = false
                                urlText = ""
                            }
                        },
                        colors = ButtonDefaults.colors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            focusedContainerColor = Color(0xFF00C853),
                            focusedContentColor = Color.White
                        )
                    ) {
                        Text("Add")
                    }
                }
            }
        }
    }
}
