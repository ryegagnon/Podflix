package com.example.data

object SampleData {
    val podcasts = listOf(
        Podcast(
            id = "pod_1",
            title = "Big Buck Bunny HLS",
            author = "Blender Foundation",
            description = "A large and lovable rabbit deals with three bullying rodents.",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c5/Big_buck_bunny_poster_big.jpg",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            category = "Top this week"
        ),
        Podcast(
            id = "pod_2",
            title = "Elephants Dream HD",
            author = "Blender Foundation",
            description = "The first computer-generated short film produced entirely with free software.",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/4b/Elephants_Dream_poster.jpg",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            category = "Top this month"
        ),
        Podcast(
            id = "pod_3",
            title = "Sintel High Definition",
            author = "Blender Foundation",
            description = "A lonely young woman, Sintel, helps and befriends a dragon.",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/27/Sintel_poster.jpg",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
            category = "Top this year"
        ),
        Podcast(
            id = "pod_4",
            title = "Tears of Steel HD",
            author = "Blender Foundation",
            description = "A group of warriors and scientists try to save the world from destructive robots.",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/2d/Tears_of_Steel_poster.jpg",
            videoUrl = "https://storage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            category = "Top this year"
        )
    )
}
