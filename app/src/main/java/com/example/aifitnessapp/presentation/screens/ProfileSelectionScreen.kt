package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aifitnessapp.data.local.ActiveProfileManager
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.domain.model.UserProfile
import com.example.aifitnessapp.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileSelectionScreen(
    onAddNewUser: () -> Unit,
    onSelectProfile: (UserProfile) -> Unit
) {
    val context = LocalContext.current
    val dao = DatabaseModule.getDatabase(context).profileDao()

    // Load profiles
    val profiles by dao.getAllProfiles().collectAsState(initial = emptyList())

    PremiumLightBackground {
        ScreenContainer {

            PremiumTitle("Select Profile")
            Spacer(Modifier.height(20.dp))

            if (profiles.isEmpty()) {
                GlassCard {
                    Text(
                        "No profiles found.\nAdd your first profile!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(Modifier.height(20.dp))
            }

            // ---------------- PROFILE LIST ----------------
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(profiles) { profile ->

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        ActiveProfileManager(context).setActiveProfile(profile.id)
                                    }
                                    onSelectProfile(profile)
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column(Modifier.weight(1f)) {
                                Text(
                                    text = profile.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Age: ${profile.age}, Weight: ${profile.weight} kg",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            IconButton(
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        dao.deleteProfile(profile)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Profile",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // ---------------- ADD NEW USER ----------------
            Button(
                onClick = { onAddNewUser() },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(14.dp)
            ) {
                Text("Add New User", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
