package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BaseCard
import com.example.ui.theme.*

@Composable
fun ShopScreen() {
    val scrollState = rememberScrollState()
    var showBuyDialog by remember { mutableStateOf<String?>(null) }
    var showToast by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = "SHOP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Upgrade penambangmu agar menghasilkan KV Coin lebih cepat.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        ShopBalanceCard()
        
        Spacer(modifier = Modifier.height(8.dp))
        
        MinerShopItem("Basic Miner", "1", "+0.00000010 KV/mnt", "0.001 KV", Color(0xFF64748B), { showBuyDialog = it })
        MinerShopItem("Iron Miner", "1", "+0.00000050 KV/mnt", "0.005 KV", Color(0xFF94A3B8), { showBuyDialog = it })
        MinerShopItem("Silver Miner", "1", "+0.00000150 KV/mnt", "0.015 KV", Color(0xFFCBD5E1), { showBuyDialog = it })
        MinerShopItem("Gold Miner", "1", "+0.00000500 KV/mnt", "0.050 KV", Color(0xFFFBBF24), { showBuyDialog = it })
        MinerShopItem("Diamond Miner", "1", "+0.00001000 KV/mnt", "0.100 KV", Color(0xFF38BDF8), { showBuyDialog = it })
        MinerShopItem("Quantum Miner", "1", "+0.00002000 KV/mnt", "0.200 KV", Color(0xFFA78BFA), { showBuyDialog = it })
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showBuyDialog != null) {
        AlertDialog(
            onDismissRequest = { showBuyDialog = null },
            title = { Text("Konfirmasi Pembelian", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Apakah Anda yakin ingin membeli ${showBuyDialog}?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showBuyDialog = null
                        showToast = true
                    }
                ) {
                    Text("Beli", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBuyDialog = null }) {
                    Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }
    
    if (showToast) {
        AlertDialog(
            onDismissRequest = { showToast = false },
            title = { Text("Transaksi Gagal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) },
            text = { Text("KV Coin tidak mencukupi.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = { showToast = false }) {
                    Text("Tutup", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
fun ShopBalanceCard() {
    BaseCard(borderColor = Color(0x4D06B6D4)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x3306B6D4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = "Coin",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "KV COIN",
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "0.00000082 KV",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun MinerShopItem(name: String, level: String, rate: String, price: String, colorHint: Color, onBuyClick: (String) -> Unit) {
    BaseCard(borderColor = colorHint.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorHint.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⛏️", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Level $level • Belum Dimiliki",
                        style = MaterialTheme.typography.labelSmall,
                        color = colorHint,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onBuyClick(name) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text("BELI", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
