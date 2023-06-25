package com.example.controlefinanceiro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controlefinanceiro.ui.theme.categories
import com.example.controlefinanceiro.ui.theme.toCurrency
import java.math.BigDecimal

@Composable
fun TransactionCard(
    uuid: String,
    value: BigDecimal,
    category: String,
    date: String,
) {
    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        TransactionInfoRow(uuid, category, date, value)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun TransactionInfoRow(uuid: String, category: String, date: String, value: BigDecimal, viewModel: OverviewViewModel = viewModel()) {
    Column(Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = categories.firstOrNull { it.first == category }?.second ?: Icons.Filled.Lightbulb,
                contentDescription = ""
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(2.dp))

            Text(
                text = value.toCurrency(),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = {
                viewModel.deleteTransaction(uuid)
            }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    TransactionCard(
        uuid = "",
        date = "jun. 24",
        category = "Restaurant",
        value = BigDecimal.valueOf(23.23)
    )
}