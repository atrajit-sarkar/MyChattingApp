package com.example.mychattingapp.ProfilePicUploadLogics

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

data class GitHubFileResponse(
    val content: ContentDetails?,
    val commit: CommitDetails?
)

data class ContentDetails(
    val name: String,
    val path: String,
    val sha: String,
    val size: Int,
    val url: String
)

data class CommitDetails(
    val sha: String,
    val message: String
)

data class GitHubFile(
    val message: String, // Commit message for the file upload
    val content: String, // Base64-encoded file content
    val sha: String? = null // Optional SHA for updating existing files
)

data class GitHubFileMetadata(
    val sha: String
)

interface GitHubApi {
    @PUT("repos/{owner}/{repo}/contents/{path}")
    fun uploadFile(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") filePath: String,
        @Body file: GitHubFile
    ): Call<GitHubFileResponse>

    @GET("repos/{owner}/{repo}/contents/{path}")
    fun getFileMetadata(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") filePath: String
    ): Call<GitHubFileMetadata>
}

