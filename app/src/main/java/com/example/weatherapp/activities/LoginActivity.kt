package com.example.weatherapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.weatherapp.AuthenticationViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme

public class LoginActivity : ComponentActivity() {
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

    private val viewModel : AuthenticationViewModel by viewModels<AuthenticationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginComposable(
                        modifier = Modifier.padding(innerPadding),
                        onLoginClicked = {
                            viewModel.checkCredential(
                                activity = this@LoginActivity,
                                onSuccess = { user ->
                                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                },
                                onFailure = { exception ->
                                    Toast.makeText(this, "Login failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        onSignUpClicked = {
                            startActivity(Intent(this, RegisterActivity::class.java))
                        }

                    )
                }
            }
        }
    }

    @Composable
    fun LoginComposable(
        modifier: Modifier = Modifier,
        onLoginClicked: () -> Unit,
        onSignUpClicked: () -> Unit
    ) {
        val email : String by viewModel.observeEmail().observeAsState("")
        val password : String by viewModel.observePassword().observeAsState("")
        val message by viewModel.message
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.aether),
                contentDescription = "Aether Logo",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail -> viewModel.setEmail(newEmail) },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword -> viewModel.setPassword(newPassword) },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onLoginClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account?")
                Spacer(modifier = Modifier.width(5.dp))
                TextButton(onClick = onSignUpClicked) {
                    Text(text = "Register")
                }
            }
            if (message.isNotEmpty()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}