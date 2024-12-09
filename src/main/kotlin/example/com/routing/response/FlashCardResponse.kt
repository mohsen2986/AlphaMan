package example.com.routing.response

import kotlinx.serialization.Serializable

@Serializable
data class FlashCardResponse(
    val title: String,
    val description: String
)