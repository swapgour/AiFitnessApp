package com.example.aifitnessapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aifitnessapp.BuildConfig
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.data.local.ActiveProfileManager
import com.example.aifitnessapp.ui.theme.PremiumLightBackground
import com.example.aifitnessapp.ui.theme.GlassCard
import com.example.aifitnessapp.ui.theme.PremiumTitle
import com.example.aifitnessapp.presentation.viewmodels.DietPlanViewModel
import com.example.aifitnessapp.presentation.viewmodels.DietViewModelFactory
import com.example.aifitnessapp.domain.model.Meal
import com.example.aifitnessapp.domain.model.Summary
import kotlinx.coroutines.launch

@Composable
fun DietScreen() {

    PremiumLightBackground {

        val context = LocalContext.current
        val apiKey = BuildConfig.GROQ_API_KEY
        val snackbar = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // 1️⃣ Load Active Profile ID
        val activeProfileManager = remember { ActiveProfileManager(context) }
        var activeProfileId by remember { mutableStateOf<Int?>(null) }

        LaunchedEffect(Unit) {
            activeProfileManager.activeProfileId.collect { id ->
                activeProfileId = id
            }
        }

        if (activeProfileId == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            return@PremiumLightBackground
        }

        // 2️⃣ Load Profile from Room
        val db = DatabaseModule.getDatabase(context)
        val profileDao = db.profileDao()

        var profile by remember { mutableStateOf<com.example.aifitnessapp.domain.model.UserProfile?>(null) }

        LaunchedEffect(activeProfileId) {
            if (activeProfileId != null) {
                profileDao.observeProfile(activeProfileId!!).collect { loaded ->
                    profile = loaded
                }
            }
        }

        println("DEBUG >>> activeProfileId = $activeProfileId")
        println("DEBUG >>> profile from Room = $profile")

        LaunchedEffect(activeProfileId) {
            println("DEBUG >>> observeProfile FLOW started with ID = $activeProfileId")
        }


        if (profile == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Loading profile…") }
            return@PremiumLightBackground
        }

        // 3️⃣ ViewModel Initialization
        val savedDao = db.savedDietDao()
        val vm: DietPlanViewModel = viewModel(
            factory = DietViewModelFactory(
                apiKey = apiKey,
                savedDao = savedDao,
                profileId = profile!!.id,
                appContext = context.applicationContext
            )
        )

        val diet by vm.dietJson.collectAsState()
        val loading by vm.isLoading.collectAsState()

        // 4️⃣ UI Layout
        Scaffold(
            snackbarHost = { SnackbarHost(snackbar) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.generateDiet(
                            name = profile!!.name,
                            age = profile!!.age,
                            weight = profile!!.weight,
                            target = profile!!.targetWeight,
                            height = profile!!.heightCm
                        )
                    }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Generate Diet")
                }
            },
            containerColor = androidx.compose.ui.graphics.Color.Transparent
        ) { pad ->

            Column(
                modifier = Modifier
                    .padding(pad)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                PremiumTitle("AI Diet Plan")
                Spacer(Modifier.height(20.dp))

                if (loading) {
                    Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                diet?.let { plan ->

                    // Save button
                    Button(
                        onClick = {
                            vm.saveCurrentDietPlan()
                            scope.launch { snackbar.showSnackbar("Diet Plan Saved Successfully!") }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Save Today’s Diet Plan")
                    }

                    Spacer(Modifier.height(20.dp))

                    MealCard("Breakfast", plan.breakfast)
                    MealCard("Mid-Morning Snack", plan.mid_morning_snack)
                    MealCard("Lunch", plan.lunch)
                    MealCard("Evening Snack", plan.evening_snack)
                    MealCard("Dinner", plan.dinner)
                    SummaryCard(plan.summary)
                }
            }
        }
    }
}

@Composable
fun MealCard(title: String, meal: Meal) {
    GlassCard {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$title (${meal.time})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))
            Text("Items:", style = MaterialTheme.typography.bodyLarge)

            meal.items.forEach { Text("• $it") }

            Spacer(Modifier.height(8.dp))
            Text("Calories: ${meal.calories}")

            Spacer(Modifier.height(8.dp))
            Text("Details:", style = MaterialTheme.typography.bodyLarge)
            meal.details.forEach { Text("• $it") }
        }
    }

    Spacer(Modifier.height(20.dp))
}

@Composable
fun SummaryCard(summary: Summary) {
    GlassCard {
        Column(Modifier.padding(16.dp)) {

            Text("Daily Summary", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Text("Total Calories: ${summary.total_calories}")
            Spacer(Modifier.height(8.dp))

            Text("Nutrients:", style = MaterialTheme.typography.bodyLarge)
            summary.nutrients.forEach { Text("• $it") }

            Spacer(Modifier.height(8.dp))
            Text("Tips:", style = MaterialTheme.typography.bodyLarge)
            summary.tips.forEach { Text("• $it") }
        }
    }

    Spacer(Modifier.height(40.dp))
}
