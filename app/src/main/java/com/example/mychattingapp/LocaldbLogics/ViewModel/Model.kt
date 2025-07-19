package com.example.mychattingapp.LocaldbLogics.ViewModel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.Color
import androidx.core.os.postDelayed
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mychattingapp.ChatsData.ChannelsData
import com.example.mychattingapp.ChatsData.UsersStatusData
import com.example.mychattingapp.FireBaseLogics.FireBseSetings.FirestoreHelper
import com.example.mychattingapp.FireBaseLogics.addUserToFirestore
import com.example.mychattingapp.FireBaseLogics.deleteMessageFromFirestore
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.CustomChatTheme
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.SavedSettings
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.UserDocId
import com.example.mychattingapp.LocaldbLogics.Repositories.ChatRepository
import com.example.mychattingapp.LocaldbLogics.Repositories.CustomChatThemeRepo
import com.example.mychattingapp.LocaldbLogics.Repositories.SavedSettingsRepo
import com.example.mychattingapp.LocaldbLogics.Repositories.UserDocIdRepo
import com.example.mychattingapp.LocaldbLogics.Repositories.UserRepository
import com.example.mychattingapp.Utils.DateUtils.convertToUserTimeZone
import com.example.mychattingapp.notification.sendChatNotification
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.DefaultTheme
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.Theme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.logging.Handler
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ChatAppViewModel @Inject constructor(
    private val chatrepository: ChatRepository,
    private val userRepository: UserRepository,
    private val userDocIdRepo: UserDocIdRepo,
    private val savedSettingsRepo: SavedSettingsRepo,
    private val customChatThemeRepo: CustomChatThemeRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {


    // Observe userDocIdRepo to get updated data whenever the local database changes
    val userDocIdsLiveData = userDocIdRepo.getAllUserDocIds().asLiveData()

    // Function to add userDocId to the database
    fun addUserDocId(userDocId: UserDocId) {
        viewModelScope.launch(Dispatchers.IO) {
            userDocIdRepo.insertUserDocId(userDocId)
        }
    }

    // Function to remove userDocId from the database
    fun deleteUserDocId(userdocid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userDocIdRepo.deleteUserDocId(userdocid)
        }
    }

    // UpdateScreen sortedStatusList and sorted Channel list.......
    private val _sortedStatusList = MutableStateFlow<List<UsersStatusData>>(emptyList())
    val sortedStatusList: StateFlow<List<UsersStatusData>> = _sortedStatusList

    private val _sortedChannelList = MutableStateFlow<List<ChannelsData>>(emptyList())
    val sortedChannelList: StateFlow<List<ChannelsData>> = _sortedChannelList

    fun updateSortedStatusList(newList: List<UsersStatusData>) {
        _sortedStatusList.value = newList
    }

    fun updateSortedChannelList(newList: List<ChannelsData>) {
        _sortedChannelList.value = newList
    }

    // isUpdateScreenSearchBarActive........
    private val _isUpdateScreenSearchBarActive = MutableStateFlow(false)
    val isUpdateScreenSearchBarActive: StateFlow<Boolean> = _isUpdateScreenSearchBarActive

    fun changeUpdateScreenSearchBarActiveState(value: Boolean) {
        _isUpdateScreenSearchBarActive.value = value
    }

    //isUpdateScreenSearchBarFocused........
    private val _isUpdateScreenSearchBarFocused = MutableStateFlow(false)
    val isUpdateScreenSearchBarFocused: StateFlow<Boolean> = _isUpdateScreenSearchBarFocused

    fun changeUpdateScreenSearchBarFocusedState(value: Boolean) {
        _isUpdateScreenSearchBarFocused.value = value
    }


    val allLocalChats = chatrepository.getAllMessages().asLiveData()

    // creat a variable to store all chats
    val allUsers = userRepository.getAllUsers().asLiveData()

    //Create a list of selected users and a function to add user to the list
    private val _selectedUsersHomeScreen = MutableStateFlow<List<User>>(emptyList())
    val selectedUsersHomeScreen: StateFlow<List<User>> = _selectedUsersHomeScreen
    fun selectUserForHomeScreen(user: User) {
        _selectedUsersHomeScreen.value += user
    }

    private val _homeScreenSearchBarActiveState = MutableStateFlow(false)
    val homeScreenSearchBarActiveState: StateFlow<Boolean> = _homeScreenSearchBarActiveState

    fun changeHomeScreenSearchBarActiveState(value: Boolean) {
        _homeScreenSearchBarActiveState.value = value
    }

    private val _selectedMessage = MutableStateFlow<Message?>(null)
    val selectedMessage: StateFlow<Message?> = _selectedMessage

    fun selectMessage(message: Message) {
        _selectedMessage.value = message
    }

    fun clearSelectedMessage() {
        _selectedMessage.value = null
    }


    //___________________________________________________________________________________________

    // 1. Boolean to track if message selection is initiated
    private val _messageSelectInitiated = MutableStateFlow(false)
    val messageSelectInitiated: StateFlow<Boolean> = _messageSelectInitiated

    // Boolean to initiate editing Messages...............
    private val _messageEditingInitiated = MutableStateFlow(false)
    val messageEditingInitiated: StateFlow<Boolean> = _messageEditingInitiated

    // Function to change the value of editing message initiated..........
    fun isEditingInitiated(value: Boolean) {
        _messageEditingInitiated.value = value
    }

    // 2. List to hold selected messages
    private val _selectedMessages = MutableStateFlow<List<LocalMessage>>(emptyList())
    val selectedMessages: StateFlow<List<LocalMessage>> = _selectedMessages

    // 3. Function to select (add) a message
    fun selectAMessage(message: LocalMessage) {
        _selectedMessages.value += message
    }

    // 4. Function to deselect (remove) a message
    fun deselectMessage(message: LocalMessage) {
        _selectedMessages.value -= message

    }

    // 5. Function to clear all selected messages
    fun clearSelectedMessages() {
        _selectedMessages.value = emptyList()

    }

    // 6. Function to check if a message is already selected
    fun isMessageSelected(message: LocalMessage): Boolean {
        return _selectedMessages.value.any { it.messageId == message.messageId }
    }

    // 7. Function to change the value of messageSelectInitiated......
    fun isMessageSelectInitiated(value: Boolean) {
        _messageSelectInitiated.value = value
    }

    //............................................

    // Chat repo Functions.........
    fun addMessage(message: LocalMessage) {
        viewModelScope.launch {
            chatrepository.insertMessage(message)
        }
    }

    fun deleteMessage(message: LocalMessage) {
        viewModelScope.launch {
            chatrepository.deleteMessage(message)
        }
    }

    fun updateMessage(message: LocalMessage) {
        viewModelScope.launch {
            chatrepository.updateMessage(message)
        }
    }

    fun getMessageById(uid: String): LiveData<List<LocalMessage>> =
        chatrepository.getMessageById(uid, currentUserId.value.toString()).asLiveData()


    fun deleteAllMessage(uid: String) {
        viewModelScope.launch {
            chatrepository.deleteAllMessage(uid, currentUserId.value.toString())
        }
        changeDeleteMessageFromFirestore(false)
    }


    //Userrepo functions..............
    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

    fun getUserById(userId: Int): LiveData<List<User>> =
        userRepository.getUserById(userId).asLiveData()


    fun deleteAllUser() {
        viewModelScope.launch {
            userRepository.deleteAllUser()
        }
    }


    // FireBase Logics.................

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Create a fucntion to change isLoading value
    fun changeLoadingState(value: Boolean) {
        _isLoading.value = value
    }

    // StateFlow to hold the list of users
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList as StateFlow<List<User>> // Expose it as immutable

    // Create a variable to hold a string that can be called in any composable
    private val _currentUser = MutableStateFlow<String>(
        FirebaseAuth.getInstance().currentUser?.email.toString().split("@")[0]
    )
    val currentUser = _currentUser.asStateFlow()


    // Firebase Auth instance
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // MutableStateFlow for the current user ID
    private val _currentUserId = MutableStateFlow<String?>(null) // Allow null values initially
    val currentUserId = _currentUserId.asStateFlow()

    // LiveData for observing user doc ID list
    private val _userDocIdList = MutableLiveData<List<String>>()
    val userDocIdListGlobal: LiveData<List<String>> get() = _userDocIdList

    init {
        // Listen to Firebase Auth state changes
        firebaseAuth.addAuthStateListener { auth ->
            val newUserId = auth.currentUser?.uid
            _currentUserId.value = newUserId
            updateUserDocIdList(newUserId)
        }
    }

    // Function to update the user document ID list
    private fun updateUserDocIdList(newUserId: String?) {
        // Ensure the list contains a non-null value or handle empty state
        _userDocIdList.value = if (newUserId != null) {
            listOf(newUserId)
        } else {
            emptyList()
        }
    }


    //Variable to store authetication issues
    private val _authError = MutableStateFlow<String>("")
    val authError = _authError.asStateFlow()


    // Function to sign in users from Firestore
    private val auth: FirebaseAuth = Firebase.auth

    fun singInWithEmailAndPassword(
        email: String,
        password: String,
        onloginsuccess: () -> Unit,
        onloginfail: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            onloginsuccess()
                            _currentUser.value = auth.currentUser?.email.toString().split("@")[0]


                        } else {
                            onloginfail()
                            _authError.value = it.exception?.message.toString()


                        }

                    }
            } catch (e: Exception) {
                // Log or handle the error
                Log.d("", "singinwithemailandpassword: ")
            }
        }
    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onSignUpSuccess: () -> Unit,
        onSignUpFail: () -> Unit,
        onVerificationSent: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null) {
                                user.sendEmailVerification()
                                    .addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            onVerificationSent() // Notify verification email is sent
                                            _currentUser.value =
                                                user.email.toString()
                                                    .split("@")[0] // Update current user state

                                            // Add user to Firestore
                                            val newUser = User(
                                                uid = user.uid,
                                                userName = email.split("@")[0],
                                                password = password,
                                                messageCounter = "0",
                                                messageSentTime = "",
                                                recentMessage = "",
                                                activeStatus = ""
                                            )
                                            addUserToFirestore(newUser)
                                        } else {
                                            onSignUpFail() // Handle failure to send verification email
                                            _authError.value =
                                                verificationTask.exception?.message.toString()
                                        }
                                    }
                            }
                            onSignUpSuccess() // Call success callback
                        } else {
                            onSignUpFail() // Handle sign-up failure
                            _authError.value = task.exception?.message.toString()
                        }
                    }
            } catch (e: Exception) {
                Log.d("Error", "Error during sign-up: ${e.message}")
            }
        }
    }


    //Create a contactLoading variable
    private val _contactLoading = MutableStateFlow(false)
    val contactLoading = _contactLoading.asStateFlow()

    // Create a list of userDocId:String and a function to add a function to add a userDocId to the list
    private val _userDocId = MutableStateFlow<List<String>>(emptyList())
    val userDocId = _userDocId.asStateFlow()


//    fun addUserDocId() {
//        // Create a new list by adding the new item to the existing list
//        for (userId in userDocIds.value!!) {
//            _userDocId.value += userId.userdocid
//        }
//    }

    private var firestoreListener: ListenerRegistration? = null

    init {
        // Observe changes in userDocIdsLiveData
        userDocIdsLiveData.observeForever { userDocIds ->
            Log.d("ViewModelUserDocIds", ": $userDocIds")
            val userDocIdList = userDocIds.map { it.userdocid }
            _userDocIdList.value = _userDocIdList.value?.plus(userDocIdList)

            // Reinitialize Firestore listener with the updated list
            userDocIdListGlobal.value?.let { fetchUsersInRealTime(it) }
        }
    }

    private fun fetchUsersInRealTime(userDocIdList: List<String>) {
        // Remove the old listener
        firestoreListener?.remove()

        // Do nothing if the list is empty
        if (userDocIdList.isEmpty()) {
            _userList.value = emptyList()
            return
        }

        val db = FirestoreHelper.instance
        _contactLoading.value = true

        try {
            firestoreListener = db.collection("users")
                .whereIn("uid", userDocIdList)
                .addSnapshotListener { snapshots, error ->
                    if (error != null) {
                        Log.e("ChatAppViewModel", "Error fetching users: ${error.message}")
                        _contactLoading.value = false
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        val currentUsers = _userList.value?.toMutableList() ?: mutableListOf()
                        val userMap = currentUsers.associateBy { it.uid }.toMutableMap()

                        for (docChange in snapshots.documentChanges) {
                            val doc = docChange.document
                            val user = User(
                                uid = doc.getString("uid") ?: "",
                                userName = doc.getString("userName") ?: "",
                                password = doc.getString("password") ?: "",
                                messageSentTime = doc.getString("messageSentTime") ?: "",
                                recentMessage = doc.getString("recentMessage") ?: "",
                                messageCounter = doc.getString("messageCounter") ?: "",
                                activeStatus = doc.getString("activeStatus") ?: "",
                                userDocId = doc.getString("userDocId") ?: "",
                                chattingTo = doc.getString("chattingTo") ?: "",
                                profilePicUri = doc.getString("profilePicUri") ?: ""
                            )

                            when (docChange.type) {
                                DocumentChange.Type.ADDED -> {
                                    userMap[user.uid] = user
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    userMap[user.uid] = user
                                }

                                DocumentChange.Type.REMOVED -> {
                                    userMap.remove(user.uid)
                                }
                            }
                        }

                        _userList.value = userMap.values.sortedBy { it.userName }
                    }
                }
        } catch (e: Exception) {
            Log.e("ChatAppViewModel", "Error fetching users: ${e.message}", e)
        } finally {
            _contactLoading.value = false
        }
    }


    private val _allChats = MutableStateFlow<List<Message>>(emptyList())
    val allChats: StateFlow<List<Message>> = _allChats

    fun addAMessageToAllChats(message: Message) {
        _allChats.value += message
    }

    fun replaceLastMessage(message: Message) {
        val index = _allChats.value.indexOfFirst { it.chatId == message.chatId }
        if (index != -1) {
            val updatedList = _allChats.value.toMutableList()
            updatedList[index] = message
            _allChats.value = updatedList
        }
    }

    fun removeAMessageFromAllChats(message: Message) {
        _allChats.value -= message
    }

    private val _chatLoading = MutableStateFlow(false)
    val chatLoading = _chatLoading.asStateFlow()
//    var lastVisible: DocumentSnapshot? = null

    // Create a variable to store messageCounter value in Int
    private val _messageCounter = MutableStateFlow(0)
    val messageCounter = _messageCounter.asStateFlow()

    init {
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

    //Create a variable to store a uid tye string
    private val _uidForNotification = MutableStateFlow<String>("")
    val uidForNotification = _uidForNotification.asStateFlow()

    // Function to change the value of the uid variable
    fun changeUid(value: String) {
        _uidForNotification.value = value
    }

    //    Fetch chats in real-time
    // Function to fetch chats in real-time and append new data in order
    fun fetchChatsInRealTime(userDocIdList: List<String>) {
        val db = FirestoreHelper.instance
        val userId = currentUserId.value ?: return
        _chatLoading.value = true // Indicate that chats are loading

        try {
            // Query 1: Messages where the current user is the receiver
            val receiverQuery =
                db.collection("messages")
                    .whereEqualTo("receiver", userId)
                    .whereIn("sender", userDocIdList)
                    .whereIn("deletedForMe", userDocIdList.minus(userId).plus(""))


            // Query 2: Messages where the current user is the sender
            val senderQuery = db.collection("messages")
                .whereEqualTo("sender", userId)
                .whereIn("receiver", userDocIdList)
                .whereIn("deletedForMe", userDocIdList.minus(userId).plus(""))

//            val combinedChats = mutableMapOf<String, Message>()

            // Listen for receiver query
            receiverQuery?.addSnapshotListener { receiverSnapshots, error ->
                if (error != null) {
                    Log.e("ChatAppViewModel", "Error fetching messages: ${error.message}")
                    _chatLoading.value = false
                    return@addSnapshotListener
                }

                receiverSnapshots?.let {
                    for (doc in it.documentChanges) {
                        processDocumentChange(doc)
                    }
//                    updateChatList(combinedChats)
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
                        processDocumentChange(doc)
                    }
//                    updateChatList(combinedChats)
                }
            }
        } catch (e: Exception) {
            Log.e("ChatAppViewModel", "Error fetching messages: ${e.message}", e)
        } finally {
            _chatLoading.value = false // Indicate that chats are no longer loading
        }
    }

    val loggedUser = filterUser(auth.currentUser?.uid.toString())
    private val _chatItemVisible = MutableStateFlow(false)
    val chatItemVisible = _chatItemVisible.asStateFlow()

    // Helper function to process document changes
    @OptIn(DelicateCoroutinesApi::class)
    private fun processDocumentChange(
        docChange: DocumentChange,
//        combinedChats: MutableMap<String, Message>
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
            password = doc.getString("password") ?: "",
            viewOnce = doc.getString("viewOnce") ?: "",
            editIcon = doc.getString("editIcon") ?: "",
            messageId = doc.id,
            deletedForEveryone = doc.getString("deletedForEveryone") ?: "",
            fileType = doc.getString("fileType") ?: "",
            fileUri = doc.getString("fileUri") ?: ""
        )
        Log.d("messageId1729", "processDocumentChange: ${message.messageId}")

        val localMessage = LocalMessage(
            sender = message.sender,
            receiver = message.receiver,
            text = message.text,
            timestamp = message.timestamp,
            reaction = message.reaction,
            icons = message.icons,
            password = message.password,
            viewOnce = message.viewOnce,
            editIcon = message.editIcon,
            messageId = message.deletedForEveryone,
            id = message.messageId,
            fileType = message.fileType,
            fileUri = message.fileUri

        )


        // Notification logic for the current user
        if (message.receiver == currentUserId.value && message.icons == "singleTick") {
            if (uidForNotification.value != "" && uidForNotification.value != message.sender) {
                filterTargetUser(message.sender)?.let {
                    sendChatNotification(
                        context = context,
                        message = message.text,
                        title = it.userName
                    )
                }
            } else if (uidForNotification.value == "") {
                filterTargetUser(message.sender)?.let {
                    sendChatNotification(
                        context = context,
                        message = message.text,
                        title = it.userName
                    )
                }
            }
        }

        // Update message status for "doubleTick"
        if (message.receiver == currentUserId.value && message.icons != "doubleTick" && message.icons != "doubleTickGreen") {
            val update = mapOf("icons" to "doubleTick")
            updateMessageItem(message.messageId, update)
        }

        viewModelScope.launch {
            // Handle Live Document Changes..............
            when (docChange.type) {
                DocumentChange.Type.ADDED -> {
//                    combinedChats[message.messageId] = message
                    _chatItemVisible.value = true

                    try {

                        if (message.receiver == currentUserId.value && loggedUser.value[0].chattingTo == message.sender && !isAppMinimized.value) {
                            changeIsPlayingReceived(true)
                        }
                    } catch (e: Exception) {
                        Log.d("Exception", "Exception: $e")
                    }

                    addMessage(localMessage)
                }

                DocumentChange.Type.MODIFIED -> {
//                    combinedChats[message.messageId] = message
                    updateMessage(localMessage)
                }

                DocumentChange.Type.REMOVED -> {
                    _chatItemVisible.value = false

//                    combinedChats.remove(message.messageId)

//                deleteMessage(localMessage)
                }
            }

            delay(2000)
            if (message.icons == "doubleTickGreen") {

                deleteMessageFromFirestore(message.messageId)
            }
        }
    }

    // Helper function to update the chat list
//    private fun updateChatList(combinedChats: MutableMap<String, Message>) {
//        _allChats.value =
//            combinedChats.values.sortedBy { it.timestamp } // Sort messages by timestamp
//    }


    // Create variable to store a user
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    //    create a filterfunction to filter user given a uid and store it into the variable
    fun filterUser(uid: String): StateFlow<List<User>> {
        return _userList.map { users ->
            users.filter {
                it.uid == uid
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    }


    //filter from userList a user that's uid matches given a uid.... Create a function....
    fun filterTargetUser(uid: String): User? {
        val targetUser = userList.value?.find { it.uid == uid }
        return targetUser

    }

    //filter from allChats list of messages whose sender_id is either current user uid or given uid and reciever_id is current user uid or give uid and return the filtered list

    fun filterTargetMessages(uid: String): StateFlow<List<Message>> {
        return _allChats.map { messages ->
            messages.filter {
                (it.sender == uid && it.receiver == (auth.currentUser?.uid
                    ?: "")) || (it.receiver == uid && it.sender == (auth.currentUser?.uid ?: ""))
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    }

    fun filterTargetLocalMessages(uid: String): StateFlow<List<LocalMessage>> {
        // Convert LiveData to StateFlow
        val localChatsStateFlow =
            allLocalChats.asFlow().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        return localChatsStateFlow.map { messages ->
            messages.filter {
                (it.sender == uid && it.receiver == (auth.currentUser?.uid ?: "")) ||
                        (it.receiver == uid && it.sender == (auth.currentUser?.uid ?: ""))
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    }


    private val db = FirestoreHelper.instance

    // Update MessageItem in dataBase
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

    //Message Delivery Status....
    private val _messageSentStatus = MutableStateFlow<Boolean>(false)
    val messageSentStatus: StateFlow<Boolean> = _messageSentStatus
    fun changeMessageSentStatus(value: Boolean) {
        _messageSentStatus.value = value
    }

    //Update UserItem in dataBase (We will update this to directly query using documentId implementation to optimize the function).............
    fun updateUserItem(uid: String?, updateMap: Map<String, Any>?) {
        // Validate inputs
        if (uid.isNullOrEmpty()) {
            Log.e("Firestore", "Invalid uid: Cannot be null or empty")
            return
        }
        if (updateMap.isNullOrEmpty()) {
            Log.e("Firestore", "Invalid updateMap: Cannot be null or empty")
            return
        }

        // Query to find the document with matching uid field
        db.collection("users")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Log.e("Firestore", "No user found with the specified uid")
                    return@addOnSuccessListener
                }

                // Assume there's only one matching document
                for (document in querySnapshot.documents) {
                    db.collection("users")
                        .document(document.id)
                        .update(updateMap)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Document updated successfully!")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error updating document: ${e.message}", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error finding user: ${e.message}", e)
            }
    }

    // Create a variable isAppMinimized to store boolean type
    private val _isAppMinimized = MutableStateFlow(false)
    val isAppMinimized: StateFlow<Boolean> = _isAppMinimized

    // Create a function to change the value of isAppMinimized
    fun changeAppMinimizedState(value: Boolean) {
        _isAppMinimized.value = value
    }

    // Temporary uid
    private val _tempUid = MutableStateFlow("")
    val tempUid: StateFlow<String> = _tempUid
    fun changeTempUid(value: String) {
        _tempUid.value = value
    }

    // Create a isPlaying variable to store boolean
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun changeIsPlaying(state: Boolean) {
        _isPlaying.value = state
    }

    private val _isPlayingReceived = MutableStateFlow(false)
    val isPlayingReceived: StateFlow<Boolean> = _isPlayingReceived

    fun changeIsPlayingReceived(state: Boolean) {
        _isPlayingReceived.value = state
    }

    // detecting UserTyping or not...
    private val _userTyping = MutableStateFlow(false)
    val userTyping: StateFlow<Boolean> = _userTyping
    fun changeUserTyping(state: Boolean) {
        _userTyping.value = state
    }

    //......................................................................
    // SavedSettings Update function
    val savedSettings = savedSettingsRepo.getSettings().asLiveData()

    // Update Settings
    fun updateSettings(newSettings: SavedSettings) {
        viewModelScope.launch {
            savedSettingsRepo.updateSavedSettings(newSettings)
        }
    }


    init {
        viewModelScope.launch {
            savedSettingsRepo.insertSavedSettings()
        }
    }

    //ThemeVariable....
    private val _themeName = MutableStateFlow<Theme>(DefaultTheme)
    val themeName: StateFlow<Theme> = _themeName

    // Change themeName
    fun changeThemeName(theme: Theme) {
        _themeName.value = theme
    }

    //WallPaper Id variable.....
    private val _wallPaperId = MutableStateFlow<Int>(0)
    val wallPaperId: StateFlow<Int> = _wallPaperId

    //Change wallPaperId
    fun changeWallPaperId(id: Int) {
        _wallPaperId.value = id
    }

    // CustomChatTheme CRUD Logics....
    val customChatTheme = customChatThemeRepo.getAllCustomChatThemes().asLiveData()

    // Insert a custom Chat theme
    fun insertCustomChatTheme(customChatTheme: CustomChatTheme) {
        viewModelScope.launch {
            customChatThemeRepo.insertCustomChatTheme(customChatTheme)
        }
    }

    // Update a customChatTheme
    fun updateCustomChatTheme(customChatTheme: CustomChatTheme) {
        viewModelScope.launch {
            customChatThemeRepo.updateCustomChatTheme(customChatTheme)
        }
    }

    // Delete a customChatTheme
    fun deleteCustomChatTheme(customChatTheme: CustomChatTheme) {
        viewModelScope.launch {
            customChatThemeRepo.deleteCustomChatTheme(customChatTheme)
        }
    }

    // Get custom Chat Theme by id
    fun getCustomChatThemeById(id: Int) =
        customChatThemeRepo.getCustomChatThemeById(id).asLiveData()

    // Custom wallpaper variables..........
    private val _ownMessageColor = MutableStateFlow<Color>(Color.Black)
    val ownMessageColor: StateFlow<Color> = _ownMessageColor
    fun changeOwnMessageColor(color: Color) {
        _ownMessageColor.value = color
    }

    private val _notOwnMessageColor = MutableStateFlow<Color>(Color.Black)
    val notOwnMessageColor: StateFlow<Color> = _notOwnMessageColor
    fun changeNotOwnMessageColor(color: Color) {
        _notOwnMessageColor.value = color
    }

    private val _ownBorderColor = MutableStateFlow<Color>(Color.Black)
    val ownBorderColor: StateFlow<Color> = _ownBorderColor
    fun changeOwnBorderColor(color: Color) {
        _ownBorderColor.value = color
    }

    private val _notOwnBorderColor = MutableStateFlow<Color>(Color.Black)
    val notOwnBorderColor: StateFlow<Color> = _notOwnBorderColor
    fun changeNotOwnBorderColor(color: Color) {
        _notOwnBorderColor.value = color
    }

    private val _lockColor = MutableStateFlow<Color>(Color.Black)
    val lockColor: StateFlow<Color> = _lockColor
    fun changeLockColor(color: Color) {
        _lockColor.value = color
    }

    private val _viewOnceColor = MutableStateFlow<Color>(Color.Black)
    val viewOnceColor: StateFlow<Color> = _viewOnceColor
    fun changeViewOnceColor(color: Color) {
        _viewOnceColor.value = color
    }

    private val _lockOpenColor = MutableStateFlow<Color>(Color.Black)
    val lockOpenColor: StateFlow<Color> = _lockOpenColor
    fun changeLockOpenColor(color: Color) {
        _lockOpenColor.value = color
    }

    private val _openedColor = MutableStateFlow<Color>(Color.Black)
    val openedColor: StateFlow<Color> = _openedColor
    fun changeOpenedColor(color: Color) {
        _openedColor.value = color
    }

    private val _deleteMessageFromFirestore = MutableStateFlow<Boolean>(false)
    val deleteMessageFromFirestore: StateFlow<Boolean> = _deleteMessageFromFirestore

    fun changeDeleteMessageFromFirestore(value: Boolean) {
        _deleteMessageFromFirestore.value = value
    }

    private val _uploadingProfilePhoto = MutableStateFlow<Boolean>(false)
    val uploadingProfilePhoto: StateFlow<Boolean> = _uploadingProfilePhoto

    fun changeUploadingProfilePhotoState(value: Boolean) {
        _uploadingProfilePhoto.value = value
    }

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    fun setToastMessage(message: String) {
        _toastMessage.value = message
    }

    private val _tempImageLocalUri = MutableStateFlow("")
    val tempImageLocalUri: StateFlow<String> = _tempImageLocalUri
    fun changeTempImageLocalUri(value: String) {
        _tempImageLocalUri.value = value
    }

    private val _isImageSelected = MutableStateFlow(false)
    val isImageSelected: StateFlow<Boolean> = _isImageSelected
    fun changeIsImageSelected(value: Boolean) {
        _isImageSelected.value = value
    }


}