package com.example.data.model

data class TrendItem(
    val id: String,
    val title: String,
    val channel: String,
    val channelSubscribers: String,
    val viewsVelocity: String,
    val totalViews: String,
    val publishedTime: String,
    val category: String,
    val velocityScore: Int, // 0 - 100
    val trajectoryBadge: String, // "SUPER SPIKE 🔥", "EARLY BREAKOUT ⚡", "EVERGREEN 🌲", "SATURATING ⚠️"
    val searchVolume: String,
    val competitionLevel: String, // "Low", "Medium", "High"
    val sweetSpotDuration: String,
    val thumbnailKeywords: List<String>,
    val deepInsightSummary: String,
    val isSaved: Boolean = false
)

enum class TrendCategory(val displayName: String, val iconName: String) {
    ALL("🔥 All Trends", "all"),
    TECH_AI("🤖 Tech & AI", "tech"),
    SHORTS("⚡ Viral Shorts", "shorts"),
    GAMING("🎮 Gaming", "gaming"),
    FINANCE("📈 Finance & Crypto", "finance"),
    POP_CULTURE("🎬 Entertainment", "pop"),
    LIFESTYLE("🌿 Lifestyle & Fit", "life"),
    EDUCATION("🧠 Edu & Mindset", "edu")
}

data class BreakoutCreator(
    val id: String,
    val channelName: String,
    val handle: String,
    val subscriberCount: String,
    val baselineViews: String,
    val breakoutVideoTitle: String,
    val breakoutViews: String,
    val growthMultiplier: String, // e.g. "14.8x baseline"
    val niche: String,
    val secretSauceFormula: String,
    val topKeyword: String
)

data class KeywordItem(
    val id: String,
    val keyword: String,
    val searchVolumeScore: Int, // 0-100
    val competitionLevel: String, // "LOW", "MEDIUM", "HIGH"
    val goldenRatioScore: Int, // 0-100
    val monthlyGrowthRate: String,
    val relatedTags: List<String>,
    val category: String
)

data class ViralHookResult(
    val hookType: String,
    val hookText: String,
    val titleVariations: List<String>,
    val estimatedCtr: String,
    val retentionTip: String,
    val fullScript: ScriptOutline? = null
)

data class ScriptOutline(
    val topic: String,
    val hookSection: String, // 0-3s
    val bridgeSection: String, // 3-15s
    val coreContentSection: String, // 15-45s
    val climaxAndCta: String, // 45-60s
    val visualBrollCues: List<String>,
    val estimatedWords: Int
)
