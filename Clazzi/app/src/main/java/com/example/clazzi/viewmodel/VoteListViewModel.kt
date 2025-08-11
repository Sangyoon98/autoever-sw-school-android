package com.example.clazzi.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clazzi.model.Vote
import com.example.clazzi.model.VoteOption
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class VoteListViewModel : ViewModel() {
    val db = Firebase.firestore
    private val _voteList = MutableStateFlow<List<Vote>>((emptyList()))
    val voteList: StateFlow<List<Vote>> = _voteList

    init {
        db.collection("votes")
            .orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firebase", "Error getting documents: ", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _voteList.value = snapshot.toObjects(Vote::class.java)
                }
            }
    }

    /*init {
        _voteList.value = listOf(
            Vote(
                id = "1", title = "오늘 점심 뭐 먹을까요?", voteOptions = listOf(
                    VoteOption(id = "1", optionText = "삼겹살"),
                    VoteOption(id = "2", optionText = "치킨"),
                    VoteOption(id = "3", optionText = "피자"),
                )
            ),
            Vote(
                id = "2", title = "우리 반에서 제일 잘 생긴 사람은?", voteOptions = listOf(
                    VoteOption(id = "1", optionText = "김한수"),
                    VoteOption(id = "2", optionText = "박명수"),
                    VoteOption(id = "3", optionText = "유재석"),
                )
            ),
            Vote(
                id = "3", title = "서핑 같이 가고 싶은 사람은?", voteOptions = listOf(
                    VoteOption(id = "1", optionText = "정준하"),
                    VoteOption(id = "2", optionText = "하하"),
                )
            )
        )
    }*/

    fun getVoteById(voteId: String): Vote? {
        return _voteList.value.find { vote ->
            vote.id == voteId
        }
    }

    fun addVote(vote: Vote, context: Context, imageUri: Uri) {
        _voteList.value += vote
        viewModelScope.launch {
            try {
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

                val inputStream = context.contentResolver.openInputStream(imageUri)
                val uploadTask = inputStream?.let {
                    imageRef.putStream(it).await()
                }

                val downloadUrl = imageRef.downloadUrl.await().toString()

                val voteMap = hashMapOf(
                    "id" to vote.id,
                    "title" to vote.title,
                    "imageUrl" to downloadUrl,
                    "createAt" to FieldValue.serverTimestamp(),
                    "voteOptions" to vote.voteOptions.map {
                        hashMapOf(
                            "id" to it.id,
                            "optionText" to it.optionText
                        )
                    },
                    "deadline" to vote.deadline
                )

                db.collection("votes")
                    .document(vote.id)
                    .set(voteMap)
                    .await()
            } catch (e: Exception) {
                Log.e("Firestore", "Error adding vote", e)
            }
        }
    }

    fun setVote(vote: Vote) {
        viewModelScope.launch {
            try {
                db.collection("votes")
                    .document(vote.id)
                    .set(vote)
                    .await()
                Log.d("Firestore", "투표가 성공적으로 되었습니다.")
            } catch (e: Exception) {
                Log.e("Firestore", "투표 업데이트 중 오류가 발생했습니다.",e)
            }
        }
    }
}