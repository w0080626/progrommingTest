package com.programming.test.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.programming.test.viewmodel.CalculateViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<CalculateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CalculateView(viewModel = viewModel)
            }
        }
    }

    @Composable
    fun CalculateView(viewModel: CalculateViewModel) {
        val navController = rememberNavController()
        val amount by viewModel.amount.collectAsState("")
        val time by viewModel.time.collectAsState("")
        val items by viewModel.items.collectAsState(mutableListOf())

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Amount: ${formatAmount(amount)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = amount,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.amount.value = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Time: ${formatTime(time)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = time,
                onValueChange = { viewModel.time.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Time (in seconds)") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    val result = viewModel.calculateResult()
                    items.add(result)
                    startActivity(Intent(this@MainActivity, TableActivity::class.java).apply {
                        putStringArrayListExtra("tableItems", ArrayList(items))
                    })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }

    private fun formatAmount(amount: String): String {
        try {
            if (amount.isEmpty()) return ""
            val formatter = NumberFormat.getNumberInstance(Locale.US)
            return formatter.format(amount.toDouble())
        } catch (ex: NumberFormatException) {
            return ""
        }
    }

    private fun formatTime(secondString: String): String {
        try {
            if (secondString.isEmpty()) return ""
            val seconds = secondString.toLong()
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60
            return when {
                hours > 0 -> "${hours}h${minutes}m${remainingSeconds}s"
                else -> "${minutes}m${remainingSeconds}s"
            }
        } catch (ex: NumberFormatException) {
            return ""
        }
    }
}
