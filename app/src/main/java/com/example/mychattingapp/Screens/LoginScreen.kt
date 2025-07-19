package com.example.mychattingapp.Screens


//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.auth
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

//@Preview(showBackground = true)
@Composable
fun LoginSignupScreen(
    navController: NavHostController,
    viewModel: ChatAppViewModel
) {
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val userError = remember { mutableStateOf(false) }
    val userExistsError = remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()
    val authError by viewModel.authError.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {}
    ) { innerPadding ->


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = 10.dp,
                shape = RoundedCornerShape(12.dp),
                backgroundColor = Color(0xFF037067)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Loading indicator
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    } else {

                        // Title
                        Text(
                            text = if (isLogin) "Login" else "Sign Up",
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Username field
                    TextField(
                        isError = userError.value || userExistsError.value,
                        value = username,
                        onValueChange = {
                            username = it
                            userError.value = false
                            userExistsError.value = false
                        },
                        label = { Text("Username") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(Color(0xFF4EEEEE)),
                    )

                    // Password field
                    TextField(
                        colors = TextFieldDefaults.colors(Color(0xFF4EEEEE)),
                        isError = userError.value || userExistsError.value,
                        value = password,
                        onValueChange = {
                            password = it
                            userError.value = false
                            userExistsError.value = false
                        },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                FaIcon(
                                    faIcon = if (passwordVisible) FaIcons.Eye else FaIcons.EyeSlash,
                                    tint = Color.White
                                )
                            }
                        },
                        supportingText = {
                            if (userError.value) {
                                Text(text = authError, color = Color.Red)

                            } else if (userExistsError.value) {
                                Text(text = authError, color = Color.Red)
                            }
                        }
                    )

                    // Submit Button
                    Button(
                        border = BorderStroke(width = 1.dp, color = Color.LightGray),
                        enabled = username != "" && password != "" && !isLoading,
                        onClick = {
                            if (isLogin) {
                                viewModel.changeLoadingState(true)
                                viewModel.singInWithEmailAndPassword(
                                    email = username,
                                    password = password,
                                    onloginsuccess = {
                                        val updateSignOut = mapOf(
                                            "activeStatus" to "*LoggedIn & Online \uD83E\uDD29"
                                        )
                                        viewModel.updateUserItem(viewModel.currentUserId.value, updateSignOut)

                                        navigateIfNotFast {

                                            if (auth.currentUser?.isEmailVerified == true) {
                                                // Allow access to the app
                                                navController.navigate("home_screen") {
                                                    popUpTo("login_screen") {
                                                        inclusive = true
                                                    }
                                                }
                                            } else {
                                                // Prompt user to verify email with a toast
                                                Toast.makeText(
                                                    navController.context,
                                                    "Please verify your email",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        }
                                        viewModel.changeLoadingState(false)
                                    },
                                    onloginfail = {
                                        userError.value = true
                                        viewModel.changeLoadingState(false)
                                    }
                                )
                                Log.d("Loading", "LoginSignupScreen: $isLoading")


                            } else {
                                viewModel.changeLoadingState(true)
                                viewModel.createUserWithEmailAndPassword(
                                    email = username,
                                    password = password,
                                    onSignUpSuccess = {

                                        viewModel.changeLoadingState(false)
                                    },
                                    onSignUpFail = {
                                        userExistsError.value = true
                                        viewModel.changeLoadingState(false)
                                    },
                                    onVerificationSent = {
                                        Toast.makeText(
                                            navController.context,
                                            "Verification Email has been Sent",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = if (isLogin) "Login" else "Sign Up")
                    }

                    // Toggle Button
                    TextButton(
                        onClick = {
                            isLogin = !isLogin
                            userError.value = false
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (isLogin) "Don't have an account? Sign Up" else "Already have an account? Login",
                            color = Color(0xFF2EF5E2)
                        )
                    }
                    if (auth.currentUser?.isEmailVerified == false) {
                        TextButton(
                            onClick = {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("Resend", "Verification email resent.")
                                        } else {
                                            Log.e("Resend", "Failed to resend verification email.")
                                        }
                                    }

                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Resend Verification Email",
                                color = Color(0xFF2EF5E2)
                            )
                        }
                    }
                }
            }

        }


    }

}

