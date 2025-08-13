package com.example.clazzi.ui.screens

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.clazzi.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ChatRoomScreen() {
    val firestore = FirebaseFirestore.getInstance()
    val messages = remember { mutableStateListOf<Message>() }
    val messageText = remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    LaunchedEffect(chatRoomId) {
        // 채팅방 문서 생성 또는 확인
        val chatDocRef = firestore.collection("chats").document(chatRoomId)
        chatDocRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                // 채팅방이 없으면 문서 생성
                chatDocRef.set(hashMapOf("createdAt" to System.currentTimeMillis()))
            }
        }
    }

    Column {
        // 채팅방 타이틀
        Text(
            text = "채팅방 제목",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        // 메시지 목록
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

        }

        // 메시지 입력창
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(1f),
                placeholder = { Text("메시지를 입력하세요") }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {

                }
            ) {
                Text("전송")
            }
        }
    }
}