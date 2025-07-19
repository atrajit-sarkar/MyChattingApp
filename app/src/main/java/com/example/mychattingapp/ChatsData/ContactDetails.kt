package com.example.mychattingapp.ChatsData

data class ContactDetails(
    val contactName:String,
    val recentMessage:String,
    val messageSentTime:String


)

fun loadContacts():List<ContactDetails>{
    return listOf(
        ContactDetails(contactName = "Atrajit", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "Gb TUKUN", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "GB NOBITA", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "GB SOUTAM", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "GB LITTLE GIRL", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "Atrajit2.0", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "Atrajit3.0", recentMessage = "Hi", messageSentTime = "12:02"),
        ContactDetails(contactName = "Atrajit4.0", recentMessage = "Hi", messageSentTime = "12:02"),
    )
}


