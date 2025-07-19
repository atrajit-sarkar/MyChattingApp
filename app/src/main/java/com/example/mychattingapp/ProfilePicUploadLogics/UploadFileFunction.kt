package com.example.mychattingapp.ProfilePicUploadLogics

import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

fun uploadToGitHub(
    token: String,
    owner: String,
    repo: String,
    filePath: String,
    file: File,
    specialUri: String = "",
    viewModel: ChatAppViewModel
) {
    val auth = Firebase.auth
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(
            OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(GitHubApi::class.java)

    val encodedContent = encodeFileToBase64(file)

    // Fetch file metadata first
    api.getFileMetadata(owner, repo, filePath)
        .enqueue(object : retrofit2.Callback<GitHubFileMetadata> {
            override fun onResponse(
                call: retrofit2.Call<GitHubFileMetadata>,
                response: retrofit2.Response<GitHubFileMetadata>
            ) {
                val sha = if (response.isSuccessful) response.body()?.sha else null

                // Prepare file data
                val fileData = GitHubFile(
                    message = "Upload by User: ${auth.currentUser?.uid.toString()}",
                    content = encodedContent,
                    sha = sha // Include sha if file exists
                )

                // Upload the file
                api.uploadFile(owner, repo, filePath, fileData)
                    .enqueue(object : retrofit2.Callback<GitHubFileResponse> {
                        override fun onResponse(
                            call: retrofit2.Call<GitHubFileResponse>,
                            response: retrofit2.Response<GitHubFileResponse>
                        ) {
                            if (response.isSuccessful) {
                                println("File uploaded successfully!")
                                if (specialUri != "") {
                                    val profilePicUpdate = mapOf<String, Any>(
                                        "profilePicUri" to "${specialUri}.jpg"
                                    )
                                    viewModel.updateUserItem(
                                        viewModel.currentUserId.value,
                                        profilePicUpdate
                                    )
                                }
                                viewModel.setToastMessage("File uploaded successfully!")
                            } else {
                                println("Failed to upload: ${response.errorBody()?.string()}")
                                viewModel.setToastMessage(
                                    "Upload failed: ${
                                        response.errorBody()?.string()
                                    }"
                                )
                            }
                            viewModel.changeUploadingProfilePhotoState(false)
                        }

                        override fun onFailure(
                            call: retrofit2.Call<GitHubFileResponse>,
                            t: Throwable
                        ) {
                            t.printStackTrace()
                            viewModel.changeUploadingProfilePhotoState(false)
                            viewModel.setToastMessage("Upload failed: ${t.message}")
                        }
                    })
            }

            override fun onFailure(call: retrofit2.Call<GitHubFileMetadata>, t: Throwable) {
                t.printStackTrace()
                viewModel.changeUploadingProfilePhotoState(false)
            }
        })
}


