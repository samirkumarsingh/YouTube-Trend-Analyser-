package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedItemEntity
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldSurge
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VelocityHigh
import com.example.ui.theme.YoutubeRed

@Composable
fun SavedVaultView(
    savedItems: List<SavedItemEntity>,
    onDeleteItem: (Long) -> Unit,
    onUpdateStatus: (Long, String) -> Unit,
    onCopyContent: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    val filterTypes = listOf("ALL", "TREND", "HOOK", "SCRIPT", "KEYWORD")

    val filteredItems = if (selectedFilter == "ALL") {
        savedItems
    } else {
        savedItems.filter { it.type.equals(selectedFilter, ignoreCase = true) }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Vault Header & Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filterTypes.forEach { type ->
                val label = when (type) {
                    "ALL" -> "All Saved (${savedItems.size})"
                    "TREND" -> "Trends"
                    "HOOK" -> "Viral Hooks"
                    "SCRIPT" -> "Scripts"
                    "KEYWORD" -> "Keywords"
                    else -> type
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (selectedFilter == type) YoutubeRed.copy(alpha = 0.2f) else Color(0xFF1A1D27),
                    border = BorderStroke(
                        1.dp,
                        if (selectedFilter == type) YoutubeRed else DarkCardBorder
                    ),
                    modifier = Modifier.clickable { selectedFilter = type }
                ) {
                    Text(
                        text = label,
                        color = if (selectedFilter == type) YoutubeRed else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredItems.isEmpty()) {
            // Empty State
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                border = BorderStroke(1.dp, DarkCardBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderOpen,
                        contentDescription = "Empty Vault",
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Your Creator Vault is Empty",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Bookmark explosive trends, generated hooks, and scripts to organize your production pipeline here.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredItems.forEach { item ->
                    SavedVaultItemCard(
                        item = item,
                        onDelete = { onDeleteItem(item.id) },
                        onStatusChange = { newStatus -> onUpdateStatus(item.id, newStatus) },
                        onCopy = {
                            onCopyContent(
                                "${item.title}\n${item.subtitle}\n\n${item.detailsJson}",
                                "Saved item copied to clipboard!"
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SavedVaultItemCard(
    item: SavedItemEntity,
    onDelete: () -> Unit,
    onStatusChange: (String) -> Unit,
    onCopy: () -> Unit
) {
    val statuses = listOf("Saved", "In Production", "Scripted", "Published")

    val typeColor = when (item.type) {
        "TREND" -> VelocityHigh
        "HOOK" -> ElectricViolet
        "SCRIPT" -> NeonCyan
        "KEYWORD" -> AmberGlow
        else -> EmeraldSurge
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("saved_vault_item_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        border = BorderStroke(1.dp, DarkCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Type tag, Category, and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = typeColor.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, typeColor.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = item.type,
                            color = typeColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.category,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Content",
                            tint = TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = VelocityHigh.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title & Subtitle
            Text(
                text = item.title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            if (item.subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details Box
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF10121A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.detailsJson,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Workflow Status Pipeline Chips
            Text(
                text = "Production Status:",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                statuses.forEach { statusOption ->
                    val isSelected = item.status == statusOption
                    val statusColor = when (statusOption) {
                        "Saved" -> TextSecondary
                        "In Production" -> AmberGlow
                        "Scripted" -> ElectricViolet
                        "Published" -> EmeraldSurge
                        else -> TextSecondary
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) statusColor.copy(alpha = 0.2f) else Color(0xFF171922),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) statusColor else Color(0xFF262B3B)
                        ),
                        modifier = Modifier.clickable { onStatusChange(statusOption) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = statusColor,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = statusOption,
                                color = if (isSelected) statusColor else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
