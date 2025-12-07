package com.example.aifitnessapp.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aifitnessapp.data.local.dao.SavedDietDao
import com.example.aifitnessapp.domain.model.DietPlanJson
import com.example.aifitnessapp.domain.model.SavedDietPlan
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class DietPlanViewModel(
    private val apiKey: String,
    private val savedDao: SavedDietDao,
    private val profileId: Int,
    private val appContext: Context
) : ViewModel() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val _dietJson = MutableStateFlow<DietPlanJson?>(null)
    val dietJson: StateFlow<DietPlanJson?> = _dietJson

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadLastSavedDiet()
    }

    // ------------------------------------------------------
    // LOAD LAST SAVED DIET (SAFE DB CALL)
    // ------------------------------------------------------
    fun loadLastSavedDiet() {
        viewModelScope.launch {
            val saved = withContext(Dispatchers.IO) {
                savedDao.getLatestPlan(profileId)
            }

            saved?.let {
                try {
                    val parsed = gson.fromJson(it.json, DietPlanJson::class.java)
                    _dietJson.value = parsed
                    Log.d("DietVM", "Loaded saved diet")
                } catch (e: Exception) {
                    Log.e("DietVM", "Failed to parse saved diet: ${e.message}")
                }
            }
        }
    }

    // ------------------------------------------------------
    // SAVE TODAY’S DIET PLAN
    // ------------------------------------------------------
    fun saveCurrentDietPlan() {
        val plan = _dietJson.value ?: return
        val json = gson.toJson(plan)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                savedDao.savePlan(
                    SavedDietPlan(
                        profileId = profileId,
                        timestamp = System.currentTimeMillis(),
                        json = json
                    )
                )
            }
            Log.d("DietVM", "Diet saved successfully")
        }
    }

    // ------------------------------------------------------
    // GENERATE DIET USING GROQ
    // ------------------------------------------------------
    fun generateDiet(
        name: String,
        age: Int,
        weight: Float,
        target: Float,
        height: Float
    ) {
        viewModelScope.launch {
            _isLoading.value = true

            val prompt = """
Generate an Indian vegetarian diet plan in STRICT JSON ONLY with schema:

{
  "breakfast": { "time": "", "items": [], "calories": 0, "details": [] },
  "mid_morning_snack": { "time": "", "items": [], "calories": 0, "details": [] },
  "lunch": { "time": "", "items": [], "calories": 0, "details": [] },
  "evening_snack": { "time": "", "items": [], "calories": 0, "details": [] },
  "dinner": { "time": "", "items": [], "calories": 0, "details": [] },
  "summary": { "total_calories": 0, "nutrients": [], "tips": [] }
}

Rules:
- ALL bullet points must be arrays
- No paragraphs
- JSON only

User:
Name: $name
Age: $age
Weight: $weight
Target: $target
Height: $height
""".trimIndent()

            try {
                val messages = JSONArray().apply {
                    put(JSONObject().put("role", "user").put("content", prompt))
                }

                val bodyJson = JSONObject().apply {
                    put("model", "openai/gpt-oss-120b")
                    put("messages", messages)
                    put("temperature", 0.2)
                }

                val request = Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(bodyJson.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }

                val body = response.body?.string() ?: ""
                Log.d("DietVM", "Groq Response: $body")

                val json = JSONObject(body)
                val content = json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim()

                val parsed = gson.fromJson(content, DietPlanJson::class.java)
                _dietJson.value = parsed

            } catch (e: Exception) {
                Log.e("DietVM", "ERROR: ${e.message}", e)
            }

            _isLoading.value = false
        }
    }

    fun loadSavedDiet(plan: DietPlanJson) {
        _dietJson.value = plan
    }
}
