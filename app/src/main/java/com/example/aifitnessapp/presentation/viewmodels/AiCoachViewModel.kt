package com.example.aifitnessapp.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class AiCoachViewModel(
    private val apiKey: String,
    private val context: Context
) : ViewModel() {

    private val client = OkHttpClient()
    private val chatDao = DatabaseModule.getDatabase(context).chatDao()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming

    init {
        loadChat()
    }

    private fun loadChat() {
        viewModelScope.launch {
            chatDao.getAll().collect { list ->
                _messages.value = list
            }
        }
    }

    // --------------------------------------------------------
    // 🔥 USER MESSAGE HANDLER (Logs + Store + Call API)
    // --------------------------------------------------------
    fun sendMessage(userText: String) {
        Log.d("AiCoachLog", "USER_MESSAGE: $userText")
        addMessage("user", userText)
        callGroqApi(userText)
    }

    private fun addMessage(role: String, text: String) {
        viewModelScope.launch {
            chatDao.insert(ChatMessage(role = role, content = text))
        }
    }

    // --------------------------------------------------------
    // 🔥 CALL GROQ API — MODEL: openai/gpt-oss-120b
    // --------------------------------------------------------
    private fun callGroqApi(prompt: String) {
        viewModelScope.launch {
            _isStreaming.value = true

            try {
                // Build request messages
                val messagesJson = JSONArray().apply {
                    put(
                        JSONObject()
                            .put("role", "user")
                            .put("content", prompt)
                    )
                }

                // Full API body
                val bodyJson = JSONObject().apply {
                    put("model", "openai/gpt-oss-120b")
                    put("messages", messagesJson)
                    put("temperature", 0.7)
                }

                Log.d("AiCoachLog", "REQUEST_BODY: $bodyJson")

                // Create request
                val requestBody = bodyJson
                    .toString()
                    .toRequestBody("application/json".toMediaType())

                val request = Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build()

                // Execute API call
                val response = client.newCall(request).execute()
                val raw = response.body?.string() ?: ""

                Log.d("AiCoachLog", "RAW_RESPONSE: $raw")

                val json = JSONObject(raw)

                val reply = json
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")

                Log.d("AiCoachLog", "ASSISTANT_REPLY: $reply")

                addMessage("assistant", reply)

            } catch (e: Exception) {
                Log.e("AiCoachLog", "ERROR: ${e.message}", e)
                addMessage("assistant", "⚠ Error: ${e.message ?: "Unknown Error"}")
            }

            _isStreaming.value = false
        }
    }
}
