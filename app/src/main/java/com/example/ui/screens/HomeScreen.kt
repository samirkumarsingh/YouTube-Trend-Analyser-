package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TrendCategory
import com.example.ui.components.BreakoutCreatorCard
import com.example.ui.components.DeepAnalysisDialog
import com.example.ui.components.HookStudioView
import com.example.ui.components.KeywordMatrixCard
import com.example.ui.components.SavedVaultView
import com.example.ui.components.ScriptOutlineSheet
import com.example.ui.components.TrendVelocityCard
import com.example.ui.theme.AmberGlow
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.EmeraldSurge
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VelocityHigh
import com.example.ui.theme.YoutubeRed
import com.example.ui.viewmodel.TrendViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TrendViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val allTrends by viewModel.allTrends.collectAsStateWithLifecycle()
    val breakoutCreators by viewModel.breakoutCreators.collectAsStateWithLifecycle()
    val goldenKeywords by viewModel.goldenKeywords.collectAsStateWithLifecycle()
    val savedItems by viewModel.savedItems.collectAsStateWithLifecycle()

    val selectedTrendForDeconstruction by viewModel.selectedTrendForDeconstruction.collectAsStateWithLifecycle()
    val deepAnalysisText by viewModel.deepAnalysisText.collectAsStateWithLifecycle()
    val isAnalyzingTrend by viewModel.isAnalyzingTrend.collectAsStateWithLifecycle()

    val hookTopicInput by viewModel.hookTopicInput.collectAsStateWithLifecycle()
    val hookStyle by viewModel.hookStyle.collectAsStateWithLifecycle()
    val generatedHooks by viewModel.generatedHooks.collectAsStateWithLifecycle()
    val isGeneratingHooks by viewModel.isGeneratingHooks.collectAsStateWithLifecycle()

    val activeScript by viewModel.activeScript.collectAsStateWithLifecycle()
    val isGeneratingScript by viewModel.isGeneratingScript.collectAsStateWithLifecycle()

    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    // Helper for clipboard copy
    val copyToClipboard: (String, String) -> Unit = { text, label ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("YouTube Trend Analyzer", text)
        clipboard.setPrimaryClip(clip)
        viewModel.showSnackbar(label)
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(YoutubeRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Logo",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "YouTube Trend Analyzer",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSurge)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "LIVE VELOCITY RADAR • REAL-TIME",
                                    color = EmeraldSurge,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = TextSecondary,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                val navItems = listOf(
                    Triple(0, "Radar", Icons.Default.Whatshot),
                    Triple(1, "Hook Studio", Icons.Default.Bolt),
                    Triple(2, "Creators", Icons.Default.ElectricBolt),
                    Triple(3, "Keywords", Icons.Default.Key),
                    Triple(4, "Vault", Icons.Default.Bookmark)
                )

                navItems.forEach { (index, label, icon) ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(index) },
                        modifier = Modifier.testTag("nav_tab_$index"),
                        icon = {
                            if (index == 4 && savedItems.isNotEmpty()) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = YoutubeRed,
                                            contentColor = Color.White
                                        ) {
                                            Text(text = "${savedItems.size}")
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (isSelected) YoutubeRed else TextSecondary
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) YoutubeRed else TextSecondary
                                )
                            }
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) TextPrimary else TextMuted
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = YoutubeRed,
                            selectedTextColor = TextPrimary,
                            indicatorColor = YoutubeRed.copy(alpha = 0.15f),
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        )
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // High-level Real-time Analytics Banner
            item {
                Spacer(modifier = Modifier.height(4.dp))
                QuickMetricsBanner(
                    totalSpikes = allTrends.count { it.trajectoryBadge.contains("SPIKE") },
                    topVelocity = "+210K/hr",
                    breakoutMultiplier = "196x"
                )
            }

            // Tab 0: Trends Radar
            if (selectedTab == 0) {
                item {
                    Column {
                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Filter trends by topic, title, channel...", fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("trend_search_bar"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = YoutubeRed,
                                unfocusedBorderColor = DarkCardBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedContainerColor = DarkCard,
                                unfocusedContainerColor = DarkCard
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Category Pills
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            TrendCategory.entries.forEach { category ->
                                val isSelected = selectedCategory == category
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) YoutubeRed else DarkCard,
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) YoutubeRed else DarkCardBorder
                                    ),
                                    modifier = Modifier.clickable { viewModel.setCategory(category) }
                                ) {
                                    Text(
                                        text = category.displayName,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Filtered Trend List
                val filteredTrends = allTrends.filter { trend ->
                    val matchesCategory = selectedCategory == TrendCategory.ALL ||
                        trend.category.contains(selectedCategory.name.replace("_", " "), ignoreCase = true) ||
                        (selectedCategory == TrendCategory.SHORTS && trend.category.contains("Shorts", ignoreCase = true)) ||
                        (selectedCategory == TrendCategory.TECH_AI && trend.category.contains("Tech", ignoreCase = true)) ||
                        (selectedCategory == TrendCategory.FINANCE && trend.category.contains("Finance", ignoreCase = true)) ||
                        (selectedCategory == TrendCategory.GAMING && trend.category.contains("Gaming", ignoreCase = true)) ||
                        (selectedCategory == TrendCategory.POP_CULTURE && trend.category.contains("Entertainment", ignoreCase = true)) ||
                        (selectedCategory == TrendCategory.LIFESTYLE && trend.category.contains("Lifestyle", ignoreCase = true)) ||
                        (selectedCategory == TrendCategory.EDUCATION && trend.category.contains("Edu", ignoreCase = true))

                    val matchesQuery = searchQuery.isBlank() ||
                        trend.title.contains(searchQuery, ignoreCase = true) ||
                        trend.channel.contains(searchQuery, ignoreCase = true) ||
                        trend.thumbnailKeywords.any { it.contains(searchQuery, ignoreCase = true) }

                    matchesCategory && matchesQuery
                }

                if (filteredTrends.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCard)
                        ) {
                            Text(
                                text = "No trend spikes found matching \"$searchQuery\". Try searching for AI, Shorts, or Gaming.",
                                color = TextMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(24.dp)
                            )
                        }
                    }
                } else {
                    items(filteredTrends, key = { it.id }) { trend ->
                        TrendVelocityCard(
                            trend = trend,
                            onAnalyzeClick = { viewModel.analyzeTrend(it) },
                            onSaveClick = { viewModel.saveTrendItem(it) }
                        )
                    }
                }
            }

            // Tab 1: Viral Hook Studio
            if (selectedTab == 1) {
                item {
                    HookStudioView(
                        topicInput = hookTopicInput,
                        onTopicChange = { viewModel.setHookTopic(it) },
                        hookStyle = hookStyle,
                        onStyleChange = { viewModel.setHookStyle(it) },
                        isGenerating = isGeneratingHooks,
                        hooks = generatedHooks,
                        onGenerateClick = { viewModel.generateHooks() },
                        onGenerateScriptClick = { topic, hookType ->
                            viewModel.generateScriptForHook(topic, hookType)
                        },
                        onSaveHookClick = { viewModel.saveHookItem(it) },
                        onCopyText = copyToClipboard
                    )
                }
            }

            // Tab 2: Breakout Creators
            if (selectedTab == 2) {
                item {
                    Column {
                        Text(
                            text = "⚡ Emerging Channel Radar",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Channels under 50k subscribers achieving 100x baseline views with innovative format templates.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                items(breakoutCreators, key = { it.id }) { creator ->
                    BreakoutCreatorCard(
                        creator = creator,
                        onTopicClick = { topic ->
                            viewModel.setHookTopic(topic)
                            viewModel.setTab(1)
                            viewModel.generateHooks(topic, "High CTR / Pattern Interrupt")
                        }
                    )
                }
            }

            // Tab 3: Golden Keywords Matrix
            if (selectedTab == 3) {
                item {
                    Column {
                        Text(
                            text = "🎯 Golden Keywords Matrix",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "High search volume + Low creator competition = Fast organic rank opportunities.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                items(goldenKeywords, key = { it.id }) { keyword ->
                    KeywordMatrixCard(
                        keyword = keyword,
                        onCopyTagsClick = { kw ->
                            val tagsString = kw.relatedTags.joinToString(", ")
                            copyToClipboard(tagsString, "Tags copied for YouTube Studio!")
                        },
                        onSaveClick = { viewModel.saveKeywordItem(it) }
                    )
                }
            }

            // Tab 4: Saved Vault
            if (selectedTab == 4) {
                item {
                    SavedVaultView(
                        savedItems = savedItems,
                        onDeleteItem = { viewModel.deleteSavedItem(it) },
                        onUpdateStatus = { id, status -> viewModel.updateSavedItemStatus(id, status) },
                        onCopyContent = copyToClipboard
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Deep Analysis Dialog
    if (selectedTrendForDeconstruction != null) {
        DeepAnalysisDialog(
            trend = selectedTrendForDeconstruction!!,
            analysisText = deepAnalysisText,
            isAnalyzing = isAnalyzingTrend,
            onDismiss = { viewModel.closeDeconstructionDialog() },
            onCopyText = copyToClipboard
        )
    }

    // Script Outline Modal
    if (activeScript != null) {
        ScriptOutlineSheet(
            script = activeScript!!,
            onDismiss = { viewModel.closeScriptSheet() },
            onSaveToVault = {
                viewModel.saveScriptItem(it)
                viewModel.closeScriptSheet()
            },
            onCopyText = copyToClipboard
        )
    }
}

@Composable
fun QuickMetricsBanner(
    totalSpikes: Int,
    topVelocity: String,
    breakoutMultiplier: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141722)),
        border = BorderStroke(1.dp, DarkCardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "ACTIVE SPIKES", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "$totalSpikes Videos", color = VelocityHigh, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(26.dp)
                    .background(DarkCardBorder)
            )

            Column {
                Text(text = "PEAK VELOCITY", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = topVelocity, color = AmberGlow, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(26.dp)
                    .background(DarkCardBorder)
            )

            Column {
                Text(text = "TOP OUTLIER", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = breakoutMultiplier, color = NeonCyan, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
