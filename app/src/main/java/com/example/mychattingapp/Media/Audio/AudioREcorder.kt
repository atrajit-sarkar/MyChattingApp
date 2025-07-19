package com.example.mychattingapp.Media.Audio

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.IOException

class AudioRecorder(private val context: Context, private val onDataReady: (File) -> Unit) {
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null

    // Start recording and notify when the file is ready for uploading in real-time.
    fun startRecording() {
        // Create the output file (can be cached or stored elsewhere depending on needs)
        outputFile = File(context.cacheDir, "temp_audio_${System.currentTimeMillis()}.m4a")

        mediaRecorder = MediaRecorder().apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile?.absolutePath)
                prepare()
                start()
            } catch (e: IOException) {
                // Handle exception (e.g., microphone is not available)
                e.printStackTrace()
            }
        }
    }

    // Stop the recording and return the file for uploading
    fun stopRecording(): File? {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            outputFile?.let {
                onDataReady(it) // Notify that the file is ready for upload
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outputFile
    }

    // Cancel recording and delete the file
    fun cancelRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            outputFile?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Optionally: You can implement a method to check the recording status, e.g., while recording.
    fun isRecording(): Boolean {
        return mediaRecorder != null
    }
}

