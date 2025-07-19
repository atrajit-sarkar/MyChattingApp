package com.example.mychattingapp.FireBaseLogics

import android.util.Log
import com.example.mychattingapp.FireBaseLogics.FireBseSetings.FirestoreHelper
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel

fun addMessageToFirestore(message: Message,viewModel: ChatAppViewModel) {

    val db = FirestoreHelper.instance

    Log.d("Firebase", "addUserToFirestore:Fuckoff ")

    db.collection("messages") // "users" is the Firestore collection name
        .add(message)
        .addOnSuccessListener { documentReference ->

            // After the document is added, update the `id` field with the generated Firestore ID
            val updatedMessage = message.copy(messageId = documentReference.id)
            // Update the message with the Firestore generated ID
            documentReference.set(updatedMessage)
                .addOnSuccessListener {
                    Log.d("Firestore", "Message added with ID: ${documentReference.id}")
                    viewModel.changeMessageSentStatus(true)
                    viewModel.changeIsPlaying(state = true)

                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }
        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error adding user", e)
        }


}

fun addMessageToFirestoreMeta(message: Message,viewModel: ChatAppViewModel) {

    val db = FirestoreHelper.instance

    Log.d("Firebase", "addUserToFirestore:Fuckoff ")

    db.collection("messages") // "users" is the Firestore collection name
        .add(message)
        .addOnSuccessListener {

            viewModel.changeMessageSentStatus(true)
            viewModel.changeIsPlaying(state = true)

        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Error adding user", e)
        }



}