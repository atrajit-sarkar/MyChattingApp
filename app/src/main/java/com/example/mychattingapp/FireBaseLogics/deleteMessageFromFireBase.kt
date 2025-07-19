package com.example.mychattingapp.FireBaseLogics

import android.util.Log
import com.example.mychattingapp.FireBaseLogics.FireBseSetings.FirestoreHelper
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase


fun deleteMessageFromFirestore(messageId: String) {

    val db = FirestoreHelper.instance

    Log.d("Firebase", "addUserToFirestore:Fuckoff ")

    db.collection("messages") // "users" is the Firestore collection name
        .document(messageId).delete()
        .addOnSuccessListener { documentReference ->

        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error adding user", e)
        }

}

fun deleteAllMessageOfUid(givenUid: String, viewModel: ChatAppViewModel) {
    val firestore = FirestoreHelper.instance
    val auth = Firebase.auth
    val currentUserUid = auth.currentUser?.uid
    var successOfDeletionCount = 0
    val messagesCollection = firestore.collection("messages")  // Adjust this to your collection name

    // Check if both deletions have been completed successfully or failed
    fun checkAndUpdateDeleteStatus() {
        if (successOfDeletionCount == -1) {
            // Failure case: if any deletion failed
            viewModel.changeDeleteMessageFromFirestore(false)
        } else if (successOfDeletionCount == 2) {
            // Success case: if both deletions were successful
            viewModel.changeDeleteMessageFromFirestore(false)
        } else if (successOfDeletionCount == 0) {
            // No messages to delete or partial success
            viewModel.changeDeleteMessageFromFirestore(false)
        }
    }

    // Function to handle message deletion and update success count
    fun deleteMessages(query: Query) {
        query.get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // No messages to delete
                    checkAndUpdateDeleteStatus()
                    return@addOnSuccessListener
                }

                var deletedCount = 0
                for (document in documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            deletedCount++
                            if (deletedCount == documents.size()) {
                                successOfDeletionCount++
                                checkAndUpdateDeleteStatus()
                            }
                        }
                        .addOnFailureListener {
                            successOfDeletionCount = -1  // Mark as failure if any deletion fails
                            checkAndUpdateDeleteStatus()
                        }
                }
            }
            .addOnFailureListener {
                successOfDeletionCount = -1  // Mark as failure if query fails
                checkAndUpdateDeleteStatus()
            }
    }


    // Delete messages where sender is current user and receiver is given UID
    val query1 = messagesCollection
        .whereEqualTo("sender", currentUserUid)
        .whereEqualTo("receiver", givenUid)

    // Delete messages where sender is given UID and receiver is current user
    val query2 = messagesCollection
        .whereEqualTo("sender", givenUid)
        .whereEqualTo("receiver", currentUserUid)

    // Start deleting both sets of messages
    deleteMessages(query1)
    deleteMessages(query2)
}

