package com.example.data.repository

import com.example.data.local.SavedItemDao
import com.example.data.local.SavedItemEntity
import com.example.data.model.BreakoutCreator
import com.example.data.model.KeywordItem
import com.example.data.model.ScriptOutline
import com.example.data.model.TrendCategory
import com.example.data.model.TrendItem
import com.example.data.model.ViralHookResult
import com.example.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrendRepository(
    private val savedItemDao: SavedItemDao,
    private val geminiService: GeminiService
) {

    val savedItems: Flow<List<SavedItemEntity>> = savedItemDao.getAllSavedItems()

    suspend fun saveTrend(item: TrendItem) {
        val entity = SavedItemEntity(
            type = "TREND",
            title = item.title,
            subtitle = "${item.channel} • ${item.viewsVelocity}",
            detailsJson = item.deepInsightSummary,
            category = item.category,
            status = "Saved"
        )
        savedItemDao.insertItem(entity)
    }

    suspend fun saveHook(hook: ViralHookResult, topic: String) {
        val titlesFormatted = hook.titleVariations.joinToString("\n• ")
        val entity = SavedItemEntity(
            type = "HOOK",
            title = hook.hookType,
            subtitle = "Topic: $topic • CTR: ${hook.estimatedCtr}",
            detailsJson = "HOOK:\n${hook.hookText}\n\nTITLES:\n• $titlesFormatted\n\nTIP:\n${hook.retentionTip}",
            category = "Hook Studio",
            status = "Scripted"
        )
        savedItemDao.insertItem(entity)
    }

    suspend fun saveScript(script: ScriptOutline) {
        val entity = SavedItemEntity(
            type = "SCRIPT",
            title = script.topic,
            subtitle = "60s Short Script • ~${script.estimatedWords} words",
            detailsJson = "HOOK (0-3s):\n${script.hookSection}\n\nBRIDGE (3-15s):\n${script.bridgeSection}\n\nCORE VALUE (15-45s):\n${script.coreContentSection}\n\nCLIMAX & CTA (45-60s):\n${script.climaxAndCta}",
            category = "Shorts Script",
            status = "In Production"
        )
        savedItemDao.insertItem(entity)
    }

    suspend fun saveKeyword(keyword: KeywordItem) {
        val entity = SavedItemEntity(
            type = "KEYWORD",
            title = keyword.keyword,
            subtitle = "Score: ${keyword.goldenRatioScore}/100 • Vol: ${keyword.searchVolumeScore} • Comp: ${keyword.competitionLevel}",
            detailsJson = "Related Tags: ${keyword.relatedTags.joinToString(", ")}\nGrowth: ${keyword.monthlyGrowthRate}",
            category = keyword.category,
            status = "Saved"
        )
        savedItemDao.insertItem(entity)
    }

    suspend fun deleteSavedItem(id: Long) {
        savedItemDao.deleteById(id)
    }

    suspend fun updateSavedItemStatus(id: Long, newStatus: String) {
        savedItemDao.updateStatus(id, newStatus)
    }

    suspend fun generateAiHooks(topic: String, style: String): List<ViralHookResult> {
        return geminiService.generateViralHooks(topic, style)
    }

    suspend fun generateAiScript(topic: String, hookType: String): ScriptOutline {
        return geminiService.generateViralScript(topic, hookType)
    }

    suspend fun getAiTrendDeconstruction(title: String, channel: String, category: String): String {
        return geminiService.analyzeTrendDeconstruction(title, channel, category)
    }

    fun getLiveTrends(): List<TrendItem> {
        return listOf(
            TrendItem(
                id = "trend-1",
                title = "Gemini 3 Flash Changed Everything: Full Real-World Benchmark Test",
                channel = "Matt Wolfe AI",
                channelSubscribers = "740K subs",
                viewsVelocity = "+182K / hour",
                totalViews = "1.4M views",
                publishedTime = "14 hours ago",
                category = "Tech & AI",
                velocityScore = 98,
                trajectoryBadge = "SUPER SPIKE 🔥",
                searchVolume = "High (94/100)",
                competitionLevel = "Medium",
                sweetSpotDuration = "11:45 min",
                thumbnailKeywords = listOf("GEMINI 3", "10X FASTER", "BENCHMARK"),
                deepInsightSummary = "Massive algorithm push across developer and general tech viewers due to side-by-side speed tests. The split-screen thumbnail showing response latency drove a 14.2% CTR."
            ),
            TrendItem(
                id = "trend-2",
                title = "I Built a \$10,000/Mo Automation Empire with Only Free Tools",
                channel = "Income Overload",
                channelSubscribers = "185K subs",
                viewsVelocity = "+94K / hour",
                totalViews = "620K views",
                publishedTime = "1 day ago",
                category = "Finance & Crypto",
                velocityScore = 92,
                trajectoryBadge = "EARLY BREAKOUT ⚡",
                searchVolume = "High (89/100)",
                competitionLevel = "Low",
                sweetSpotDuration = "14:20 min",
                thumbnailKeywords = listOf("\$10,000/MO", "0 CODE", "PROOF"),
                deepInsightSummary = "High retention achieved by showing bank statement receipts in the first 4 seconds. Solves immediate financial anxiety without gatekeeping."
            ),
            TrendItem(
                id = "trend-3",
                title = "This 30-Second Dopamine Reset Fixes Brain Fog Instantly",
                channel = "NeuroPeak Hacks",
                channelSubscribers = "92K subs",
                viewsVelocity = "+210K / hour",
                totalViews = "3.8M views",
                publishedTime = "2 days ago",
                category = "Viral Shorts",
                velocityScore = 99,
                trajectoryBadge = "SUPER SPIKE 🔥",
                searchVolume = "Very High (98/100)",
                competitionLevel = "Medium",
                sweetSpotDuration = "0:42 sec",
                thumbnailKeywords = listOf("DOPAMINE", "30 SECONDS", "FIX NOW"),
                deepInsightSummary = "Shorts algorithm looping multiplier of 1.48x! Uses a seamless continuous audio loop where the end sentence finishes the start sentence."
            ),
            TrendItem(
                id = "trend-4",
                title = "GTA 6 New Physics Leak Confirms Unreal Feature Nobody Expected",
                channel = "CyberGamer Realm",
                channelSubscribers = "1.2M subs",
                viewsVelocity = "+145K / hour",
                totalViews = "2.1M views",
                publishedTime = "8 hours ago",
                category = "Gaming",
                velocityScore = 95,
                trajectoryBadge = "SUPER SPIKE 🔥",
                searchVolume = "Extreme (99/100)",
                competitionLevel = "High",
                sweetSpotDuration = "8:30 min",
                thumbnailKeywords = listOf("GTA 6", "PHYSICS LEAK", "CONFIRMED"),
                deepInsightSummary = "High search intent around GTA 6 leaks. Video capitalizes on frame-by-frame breakdown psychology."
            ),
            TrendItem(
                id = "trend-5",
                title = "The 5-Minute Morning Mobility Routine Every Creator Needs",
                channel = "Posture & Flow",
                channelSubscribers = "310K subs",
                viewsVelocity = "+48K / hour",
                totalViews = "890K views",
                publishedTime = "3 days ago",
                category = "Lifestyle & Fit",
                velocityScore = 84,
                trajectoryBadge = "EVERGREEN 🌲",
                searchVolume = "Medium (76/100)",
                competitionLevel = "Low",
                sweetSpotDuration = "7:10 min",
                thumbnailKeywords = listOf("NO GEAR", "5 MIN", "FIX BACK"),
                deepInsightSummary = "Evergreen search traffic compounder. Saves and shares to playlist rate is 4x the channel benchmark."
            ),
            TrendItem(
                id = "trend-6",
                title = "Quantum Computing Breakthrough in Simple English (Why It Matters)",
                channel = "Everyday Cosmos",
                channelSubscribers = "420K subs",
                viewsVelocity = "+62K / hour",
                totalViews = "1.1M views",
                publishedTime = "1 day ago",
                category = "Edu & Mindset",
                velocityScore = 88,
                trajectoryBadge = "EARLY BREAKOUT ⚡",
                searchVolume = "High (82/100)",
                competitionLevel = "Low",
                sweetSpotDuration = "16:00 min",
                thumbnailKeywords = listOf("QUANTUM", "SIMPLIFIED", "THE TRUTH"),
                deepInsightSummary = "Visual 3D metaphors explain intricate concepts without academic jargon. Average view duration 9:40 min."
            ),
            TrendItem(
                id = "trend-7",
                title = "Why Hollywood Blockbusters Are Failing at the Box Office in 2026",
                channel = "Cinematic Lens",
                channelSubscribers = "680K subs",
                viewsVelocity = "+77K / hour",
                totalViews = "1.7M views",
                publishedTime = "2 days ago",
                category = "Entertainment",
                velocityScore = 89,
                trajectoryBadge = "EARLY BREAKOUT ⚡",
                searchVolume = "High (88/100)",
                competitionLevel = "Medium",
                sweetSpotDuration = "18:45 min",
                thumbnailKeywords = listOf("THE COLLAPSE", "HOLLYWOOD", "EXPLAINED"),
                deepInsightSummary = "Cultural commentary with deep analytical storytelling. Strong comment engagement with 9,400+ comments."
            ),
            TrendItem(
                id = "trend-8",
                title = "This 1 AI Setting in DaVinci Resolve Saves 10 Hours of Editing",
                channel = "CutLab Pro",
                channelSubscribers = "140K subs",
                viewsVelocity = "+55K / hour",
                totalViews = "430K views",
                publishedTime = "18 hours ago",
                category = "Tech & AI",
                velocityScore = 87,
                trajectoryBadge = "EARLY BREAKOUT ⚡",
                searchVolume = "High (85/100)",
                competitionLevel = "Low",
                sweetSpotDuration = "6:50 min",
                thumbnailKeywords = listOf("DAVINCI", "1 CLICK", "SECRET"),
                deepInsightSummary = "High utility workflow hack. 85% click-to-save ratio."
            )
        )
    }

    fun getBreakoutCreators(): List<BreakoutCreator> {
        return listOf(
            BreakoutCreator(
                id = "creator-1",
                channelName = "Minimalist Automation",
                handle = "@minimalautomation",
                subscriberCount = "12.4K subs",
                baselineViews = "3.2K views/video",
                breakoutVideoTitle = "I Replaced My 9-5 Marketing Team with 3 Python Scripts",
                breakoutViews = "480K views",
                growthMultiplier = "150x baseline",
                niche = "Tech / Automation",
                secretSauceFormula = "B-roll of clean code terminal + open source GitHub repo in pinned comment + deadpan confident narration.",
                topKeyword = "Python AI Workflow"
            ),
            BreakoutCreator(
                id = "creator-2",
                channelName = "StoryArc Cinematic",
                handle = "@storyarclab",
                subscriberCount = "28.5K subs",
                baselineViews = "6.1K views/video",
                breakoutVideoTitle = "The Psychological Trick Behind Nolan's Best Scenes",
                breakoutViews = "1.2M views",
                growthMultiplier = "196x baseline",
                niche = "Film & Editing",
                secretSauceFormula = "Sound design cues matched precisely with color grading shifts + 0s intro straight into the climax sound.",
                topKeyword = "Cinematic Editing"
            ),
            BreakoutCreator(
                id = "creator-3",
                channelName = "Micro Habits Daily",
                handle = "@microhabitsdaily",
                subscriberCount = "8.9K subs",
                baselineViews = "1.8K views/video",
                breakoutVideoTitle = "The 20-Minute Protocol That Fixed My Sleep in 3 Days",
                breakoutViews = "310K views",
                growthMultiplier = "172x baseline",
                niche = "Health & Routine",
                secretSauceFormula = "Hand-drawn paper notebook diagrams + timestamps + no background music for raw authentic vibe.",
                topKeyword = "Sleep Optimization"
            ),
            BreakoutCreator(
                id = "creator-4",
                channelName = "Indie Game Craft",
                handle = "@indiegamecraft",
                subscriberCount = "45.1K subs",
                baselineViews = "9.5K views/video",
                breakoutVideoTitle = "Making a Game in 48 Hours with Unreal Engine 5.6",
                breakoutViews = "890K views",
                growthMultiplier = "93x baseline",
                niche = "Game Dev",
                secretSauceFormula = "Live timer on screen + genuine panic moments cut fast + satisfying final physics reveal.",
                topKeyword = "UE5 Devlog"
            )
        )
    }

    fun getGoldenKeywords(): List<KeywordItem> {
        return listOf(
            KeywordItem(
                id = "kw-1",
                keyword = "Gemini 3 Flash automation workflow",
                searchVolumeScore = 92,
                competitionLevel = "LOW",
                goldenRatioScore = 96,
                monthlyGrowthRate = "+340% this week",
                relatedTags = listOf("gemini 3 flash", "ai workflows 2026", "free ai automation", "gemini api python"),
                category = "Tech & AI"
            ),
            KeywordItem(
                id = "kw-2",
                keyword = "Shorts retention loop tutorial",
                searchVolumeScore = 88,
                competitionLevel = "LOW",
                goldenRatioScore = 94,
                monthlyGrowthRate = "+280% this month",
                relatedTags = listOf("youtube shorts algorithm", "viral shorts hook", "shorts retention hack", "audio loop short"),
                category = "Viral Shorts"
            ),
            KeywordItem(
                id = "kw-3",
                keyword = "Sora 2 alternative free",
                searchVolumeScore = 95,
                competitionLevel = "MEDIUM",
                goldenRatioScore = 91,
                monthlyGrowthRate = "+420% this month",
                relatedTags = listOf("free ai video generator", "sora 2 open source", "text to video 2026", "runway gen3 free"),
                category = "Tech & AI"
            ),
            KeywordItem(
                id = "kw-4",
                keyword = "Solana micro-trading bot setup",
                searchVolumeScore = 84,
                competitionLevel = "LOW",
                goldenRatioScore = 93,
                monthlyGrowthRate = "+195% this month",
                relatedTags = listOf("crypto trading bot", "solana automation", "passive income crypto 2026", "dex trading setup"),
                category = "Finance & Crypto"
            ),
            KeywordItem(
                id = "kw-5",
                keyword = "Desk setup productivity ergonomics 2026",
                searchVolumeScore = 79,
                competitionLevel = "LOW",
                goldenRatioScore = 89,
                monthlyGrowthRate = "+140% this month",
                relatedTags = listOf("minimal desk setup", "creator workspace", "standing desk cable management", "ergonomic chair review"),
                category = "Lifestyle & Fit"
            ),
            KeywordItem(
                id = "kw-6",
                keyword = "Unreal Engine 5.6 beginner roadmap",
                searchVolumeScore = 86,
                competitionLevel = "MEDIUM",
                goldenRatioScore = 88,
                monthlyGrowthRate = "+210% this month",
                relatedTags = listOf("ue5 game dev tutorial", "learn unreal engine fast", "indie game dev guide", "ue5 blueprints"),
                category = "Gaming"
            )
        )
    }
}
