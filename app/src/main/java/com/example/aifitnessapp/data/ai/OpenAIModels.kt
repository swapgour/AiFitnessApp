package com.example.aifitnessapp.data.ai

data class OpenAIRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
