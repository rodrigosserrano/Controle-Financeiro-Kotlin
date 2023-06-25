package com.example.controlefinanceiro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controlefinanceiro.data.domain.Transaction
import com.example.controlefinanceiro.ui.theme.toCurrency

@Composable
fun AverageCard(transaction: Transaction, viewModel: OverviewViewModel = viewModel()) {
    Card (
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ){
        Column (Modifier.padding(top= 18.dp, start = 32.dp, end = 32.dp, bottom = 18.dp)) {
            Text(
                text = transaction.category
            )
            Text(
                text = transaction.value.toCurrency(),
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium
            )
        }
    }

    Spacer(modifier = Modifier.padding(8.dp))
}