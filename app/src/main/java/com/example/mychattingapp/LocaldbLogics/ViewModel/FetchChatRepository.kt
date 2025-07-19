package com.example.mychattingapp.LocaldbLogics.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.mychattingapp.FireBaseLogics.FireBseSetings.FirestoreHelper
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.Repositories.UserDocIdRepo
import com.example.mychattingapp.Utils.DateUtils.convertToUserTimeZone
import com.example.mychattingapp.notification.sendChatNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class FetchChatsRepository @Inject constructor(
    private val userDocIdRepo: UserDocIdRepo,
) {

    val userDocIdsLiveData = userDocIdRepo.getAllUserDocIds().asLiveData()
    val auth: FirebaseAuth = Firebase.auth
    private val db = FirestoreHelper.instance

    private val _userDocIdList = MutableLiveData<List<String>>()
    val userDocIdListGlobal: LiveData<List<String>> get() = _userDocIdList

    private val _chatLoading = MutableStateFlow(false)
    val chatLoading = _chatLoading.asStateFlow()

    private val _allChats = MutableStateFlow<List<Message>>(emptyList())
    val allChats: StateFlow<List<Message>> = _allChats

    fun updateMessageItem(messageId: String?, updateMap: Map<String, Any>?) {
        // Validate inputs
        if (messageId.isNullOrEmpty()) {
            Log.e("Firestore", "Invalid messageId: Cannot be null or empty")
            return
        }
        if (updateMap.isNullOrEmpty()) {
            Log.e("Firestore", "Invalid updateMap: Cannot be null or empty")
            return
        }

        // Attempt to update the Firestore document
        db.collection("messages")
            .document(messageId)
            .update(updateMap)
            .addOnSuccessListener {
                Log.d("Firestore", "Document updated successfully!")
            }
            .addOnFailureListener { e ->
                // Log the error and ensure it doesn't crash the app
                Log.e("Firestore", "Error updating document: ${e.message}", e)
//                Log.e("Firestore", "Error updating document: ${e.message}. Retrying...", e)
//                retryUpdate(messageId, updateMap) // Implement retry logic here
            }
    }

    fun fetchChatsInRealTime(userDocIdList: List<String>) {
        val db = FirestoreHelper.instance
        val userId = auth.currentUser?.uid ?: return
        _chatLoading.value = true // Indicate that chats are loading

        try {
            // Query 1: Messages where the current user is the receiver
            val receiverQuery =
                db.collection("messages")
                    .whereEqualTo("receiver", userId)
                    .whereIn("sender", userDocIdList)


            // Query 2: Messages where the current user is the sender
            val senderQuery = db.collection("messages")
                .whereEqualTo("sender", userId)
                .whereIn("receiver", userDocIdList)

            val combinedChats = mutableMapOf<String, Message>()

            // Listen for receiver query
            receiverQuery?.addSnapshotListener { receiverSnapshots, error ->
                if (error != null) {
                    Log.e("ChatAppViewModel", "Error fetching messages: ${error.message}")
                    _chatLoading.value = false
                    return@addSnapshotListener
                }

                receiverSnapshots?.let {
                    for (doc in it.documentChanges) {
                        processDocumentChange(doc, combinedChats)
                    }
                    updateChatList(combinedChats)
                }
            }

            // Listen for sender query
            senderQuery.addSnapshotListener { senderSnapshots, error ->
                if (error != null) {
                    Log.e("ChatAppViewModel", "Error fetching messages: ${error.message}")
                    _chatLoading.value = false
                    return@addSnapshotListener
                }

                senderSnapshots?.let {
                    for (doc in it.documentChanges) {
                        processDocumentChange(doc, combinedChats)
                    }
                    updateChatList(combinedChats)
                }
            }
        } catch (e: Exception) {
            Log.e("ChatAppViewModel", "Error fetching messages: ${e.message}", e)
        } finally {
            _chatLoading.value = false // Indicate that chats are no longer loading
        }
    }

    fun fetchChatsFunctionInit() {
        // Automatically observe the LiveData and update Firestore query when the data changes
        userDocIdsLiveData.observeForever { userDocIds ->
            // Extract the userdocid values and update LiveData
            Log.d("ViewModelUserDocIds", ": $userDocIds")
            val userDocIdList = userDocIds.map { it.userdocid }
            _userDocIdList.value = _userDocIdList.value?.plus(userDocIdList)

            // Perform Firestore query with the updated list
            userDocIdListGlobal.value?.let { fetchChatsInRealTime(it) }
        }
    }


    // Helper function to process document changes
    private fun processDocumentChange(
        docChange: DocumentChange,
        combinedChats: MutableMap<String, Message>
    ) {
        val doc = docChange.document
        val message = Message(
            id = doc.getLong("id")?.toInt() ?: 0,
            chatId = doc.getLong("chatId")?.toInt() ?: 0,
            sender = doc.getString("sender") ?: "",
            receiver = doc.getString("receiver") ?: "",
            text = doc.getString("text") ?: "",
            timestamp = convertToUserTimeZone(doc.getString("timestamp") ?: ""),
            reaction = doc.getString("reaction") ?: "",
            icons = doc.getString("icons") ?: "",
            messageId = doc.id,
            password = doc.getString("password") ?: "",
            viewOnce = doc.getString("viewOnce") ?: "",
        )

//        // Notification logic for the current user
//        if (message.receiver == currentUserId.value && message.icons == "singleTick") {
//            if (uidForNotification.value != "" && uidForNotification.value == message.sender) {
//                filterTargetUser(message.sender)?.let {
//                    sendChatNotification(
//                        context = context,
//                        message = message.text,
//                        title = it.userName
//                    )
//                }
//            } else if (uidForNotification.value == "") {
//                filterTargetUser(message.sender)?.let {
//                    sendChatNotification(
//                        context = context,
//                        message = message.text,
//                        title = it.userName
//                    )
//                }
//            }
//        }

        // Update message status for "doubleTick"
        if (message.receiver == auth.currentUser?.uid.toString() && message.icons != "doubleTick" && message.icons != "doubleTickGreen") {
            val update = mapOf("icons" to "doubleTick")
            updateMessageItem(message.messageId, update)
        }

        // Handle Live Document Changes..............
        when (docChange.type) {
            DocumentChange.Type.ADDED -> combinedChats[message.messageId] = message
            DocumentChange.Type.MODIFIED -> combinedChats[message.messageId] = message
            DocumentChange.Type.REMOVED -> combinedChats.remove(message.messageId)
        }
    }

    // Helper function to update the chat list
    private fun updateChatList(combinedChats: MutableMap<String, Message>) {
        _allChats.value =
            combinedChats.values.sortedBy { it.timestamp } // Sort messages by timestamp
    }

}