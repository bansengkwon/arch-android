package com.psq.myjetpck.model

data class TestBean(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)