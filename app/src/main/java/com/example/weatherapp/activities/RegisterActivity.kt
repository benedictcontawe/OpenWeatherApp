package com.example.weatherapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.AuthenticationViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme

public class RegisterActivity : ComponentActivity() {
    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }

    private val viewModel : AuthenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegistrationComposable(
                        modifier = Modifier.padding(innerPadding),
                        onRegisterClicked = {
                            viewModel.registerCredential(
                                activity = this@RegisterActivity,
                                onSuccess = { user ->
                                    Toast.makeText(this, "Register successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                },
                                onFailure = { exception ->
                                    Toast.makeText(this, "Register failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun RegistrationComposable(
        modifier: Modifier = Modifier,
        onRegisterClicked: () -> Unit
    ) {
        val email : String by viewModel.observeEmail().observeAsState("")
        val password : String by viewModel.observePassword().observeAsState("")
        val confirmPassword : String by viewModel.confirmPassword.observeAsState("")
        val message by viewModel.message
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.aether), // replace with your actual logo
                contentDescription = "Aether Logo",
                modifier = Modifier
                    .size(300.dp) // adjust the size as needed
                    .padding(bottom = 32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail -> viewModel.setEmail(newEmail) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword -> viewModel.setPassword(newPassword) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { newPassword -> viewModel.setConfirmPassword(newPassword) },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { onRegisterClicked() },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Register", fontSize = 18.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Already have an Account?")
                Spacer(modifier = Modifier.width(5.dp))
                TextButton(onClick = { finish() }) {
                    Text(text = "Login")
                }
            }
            if (message.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = message)
            }
        }
    }
}