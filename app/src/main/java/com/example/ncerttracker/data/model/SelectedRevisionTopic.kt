package com.example.ncerttracker.data.model

data class SelectedRevisionTopic(
    val chapterId: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val subject: Subject,
    val topicTitle: String,
    val isSubtopic: Boolean = false,
    val subtopicIndex: Int? = null
) {
    val uniqueKey: String
        get() = if (isSubtopic) "${chapterId}_sub_$subtopicIndex" else "${chapterId}_full"
}
