package com.example.controlefinanceiro

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.controlefinanceiro.ui.theme.formatDate
import com.example.controlefinanceiro.ui.theme.randomTransaction
import com.example.controlefinanceiro.ui.theme.toCurrency
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(viewModel: OverviewViewModel = viewModel()) {
    val listState = rememberLazyListState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    Scaffold(
        topBar = {
            TopAppBar (
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = "Welcome back, \nRodrigo Serrano",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.clearTransaction()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ClearAll,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.addTransaction(randomTransaction())
            }) {
                Row (
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                    AnimatedVisibility(visible = !showButton) {
                        Text(
                            text = "Add a Transaction",
                            modifier = Modifier.padding(
                                start = 8.dp
                            )
                        )
                    }
                }
            }
        }
    ) {
        SpendingCard(viewModel, it)
        AveragePerCategory(viewModel)
        TransactionsList(viewModel, it, showButton, listState)
    }
}

@Composable
private fun SpendingCard(
    viewModel: OverviewViewModel,
    it: PaddingValues
)
{
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(it)
    ) {
        Text(
            text = "Spending",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp
            )
        )
        TotalSpendingCard(uiState.total.toCurrency())
    }
}

@Composable
fun AveragePerCategory(viewModel: OverviewViewModel = viewModel()){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(top = 225.dp)
    ) {
        LazyRow(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp
            ),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 16.dp
            ),
        ) {
            items(uiState.totalTransactions) {transaction ->
                AverageCard(transaction)
            }
        }
    }
}

@Composable
private fun TransactionsList(
    viewModel: OverviewViewModel,
    it: PaddingValues,
    showButton: Boolean,
    listState: LazyListState
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(top= 370.dp)
    ) {
        Text(
            text = "Transactions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp
            )
        )
        LazyColumn(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp
            ),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 16.dp
            ),
            state = listState
        ) {
            items(uiState.transactions) { transaction ->
                TransactionCard(
                    uuid = transaction.uuid,
                    value = transaction.value,
                    category = transaction.category,
                    date = transaction.date.formatDate(),
                )
            }
        }
    }
    AnimatedVisibility(
        visible = showButton,
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            IconButton(
                modifier = Modifier
                    .padding(18.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.shapes.medium
                    ),
                onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowUpward,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentDescription = ""
                )
            }
        }
    }
}