package com.example.mychattingapp.FireBaseLogics

import android.util.Log
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.google.firebase.firestore.FirebaseFirestore

fun collectUsersFromFirestore(): List<User> {
    val db = FirebaseFirestore.getInstance()
    val users = mutableListOf<User>()
    db.collection("users")
//        .orderBy("timestamp")
        .addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("Firestore", "Listen failed.", error)
                return@addSnapshotListener
            }
            snapshots?.documents?.map { it.toObject(User::class.java)!! }?.let { users.addAll(it) }

        }
    return users
}
