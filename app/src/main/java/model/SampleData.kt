package com.carlos.movilwordkapp.model

object SampleData {
    val profiles = listOf(
        UserProfile(
            id = "1", name = "Daniela", age = 26, bio = "...",
            imageUrls = listOf(
                "https://picsum.photos/seed/1/400/600",
                "https://picsum.photos/seed/1a/400/600",
                "https://picsum.photos/seed/1b/400/600"
            )
        ),
        UserProfile(
            id = "2", name = "Javier", age = 30, bio = "...",
            imageUrls = listOf(
                "https://picsum.photos/seed/2/400/600",
                "https://picsum.photos/seed/2a/400/600"
            )
        ),
        UserProfile(
            id = "3", name = "Valeria", age = 24, bio = "...",
            imageUrls = listOf(
                "https://picsum.photos/seed/3/400/600",
                "https://picsum.photos/seed/3a/400/600",
                "https://picsum.photos/seed/3b/400/600",
                "https://picsum.photos/seed/3c/400/600"
            )
        ),
        UserProfile(
            id = "4", name = "Marcos", age = 29, bio = "...",
            imageUrls = listOf(
                "https://picsum.photos/seed/4/400/600"
            )
        )
    )

    // Simula que los perfiles con ID "2" (Javier) y "3" (Valeria) ya te han dado like.
    val profilesWhoLikedYou = setOf("2", "3")

    // Conversación de ejemplo para la pantalla de chat.
    val sampleConversation = listOf(
        ChatMessage("¡Hola! Así que también te gusta el rock indie, ¿eh?", isSentByMe = false),
        ChatMessage("¡Hola, Javier! Sí, totalmente. ¿Cuál es tu banda favorita del momento?", isSentByMe = true),
        ChatMessage("Difícil elegir, pero he estado escuchando mucho a 'The War on Drugs'. ¿Y tú?", isSentByMe = false),
        ChatMessage("¡Excelente elección! Yo no me canso de 'Arctic Monkeys', aunque no son tan nuevos jaja", isSentByMe = true),
        ChatMessage("Jaja, un clásico nunca falla. Deberíamos ir a un concierto algún día.", isSentByMe = false),
        ChatMessage("¡Me encantaría! 😊", isSentByMe = true)
    )
}