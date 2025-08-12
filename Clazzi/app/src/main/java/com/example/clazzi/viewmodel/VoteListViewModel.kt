package com.example.clazzi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clazzi.model.Vote
import com.example.clazzi.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VoteListViewModel(
    val voteRepository: VoteRepository
) : ViewModel() {
    private val _voteList = MutableStateFlow<List<Vote>>((emptyList()))
    val voteList: StateFlow<List<Vote>> = _voteList

    init {
        viewModelScope.launch {
            voteRepository.observeVotes().collect { votes ->
                _voteList.value = votes
            }
        }
    }

    fun addVote(vote: Vote, context: Context, imageUri: Uri) {
        _voteList.value += vote
        viewModelScope.launch {
            voteRepository.addVote(vote, context, imageUri)
        }
    }

    fun setVote(vote: Vote) {
        viewModelScope.launch {
            voteRepository.setVote(vote)
        }
    }
}