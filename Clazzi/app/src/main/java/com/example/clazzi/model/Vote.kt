package com.example.clazzi.model

import java.util.Date

data class Vote(
    val id: String = "",
    val title: String = "",
    val createdAt: Date? = null,
    val voteOptions: List<VoteOption> = emptyList(),
    val imageUrl: String? = null
) {
    val optionCount: Int
        get() = voteOptions.size
}

data class VoteOption(
    val id: String = "",
    val optionText: String = "",
    val voters: List<String> = emptyList()
) {
    val voteCount: Int
        get() = voters.size
}