package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.SavedItemEntity
import com.example.data.model.BreakoutCreator
import com.example.data.model.KeywordItem
import com.example.data.model.ScriptOutline
import com.example.data.model.TrendCategory
import com.example.data.model.TrendItem
import com.example.data.model.ViralHookResult
import com.example.data.remote.GeminiService
import com.example.data.repository.TrendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrendViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TrendRepository

    init {
        val db = AppDatabase.getDatabase(application)
        val geminiService = GeminiService()
        repository = TrendRepository(db.savedItemDao(), geminiService)
    }

    val savedItems: StateFlow<List<SavedItemEntity>> = repository.savedItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow(TrendCategory.ALL)
    val selectedCategory: StateFlow<TrendCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allTrends = MutableStateFlow(repository.getLiveTrends())
    val allTrends: StateFlow<List<TrendItem>> = _allTrends.asStateFlow()

    private val _breakoutCreators = MutableStateFlow(repository.getBreakoutCreators())
    val breakoutCreators: StateFlow<List<BreakoutCreator>> = _breakoutCreators.asStateFlow()

    private val _goldenKeywords = MutableStateFlow(repository.getGoldenKeywords())
    val goldenKeywords: StateFlow<List<KeywordItem>> = _goldenKeywords.asStateFlow()

    // Deep Trend Deconstruction State
    private val _selectedTrendForDeconstruction = MutableStateFlow<TrendItem?>(null)
    val selectedTrendForDeconstruction: StateFlow<TrendItem?> = _selectedTrendForDeconstruction.asStateFlow()

    private val _deepAnalysisText = MutableStateFlow<String?>(null)
    val deepAnalysisText: StateFlow<String?> = _deepAnalysisText.asStateFlow()

    private val _isAnalyzingTrend = MutableStateFlow(false)
    val isAnalyzingTrend: StateFlow<Boolean> = _isAnalyzingTrend.asStateFlow()

    // Viral Hook Studio State
    private val _hookTopicInput = MutableStateFlow("AI Automation Tools")
    val hookTopicInput: StateFlow<String> = _hookTopicInput.asStateFlow()

    private val _hookStyle = MutableStateFlow("High CTR / Pattern Interrupt")
    val hookStyle: StateFlow<String> = _hookStyle.asStateFlow()

    private val _generatedHooks = MutableStateFlow<List<ViralHookResult>>(emptyList())
    val generatedHooks: StateFlow<List<ViralHookResult>> = _generatedHooks.asStateFlow()

    private val _isGeneratingHooks = MutableStateFlow(false)
    val isGeneratingHooks: StateFlow<Boolean> = _isGeneratingHooks.asStateFlow()

    // Script Generator State
    private val _activeScript = MutableStateFlow<ScriptOutline?>(null)
    val activeScript: StateFlow<ScriptOutline?> = _activeScript.asStateFlow()

    private val _isGeneratingScript = MutableStateFlow(false)
    val isGeneratingScript: StateFlow<Boolean> = _isGeneratingScript.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        // Pre-generate sample hooks for instant rich experience
        generateHooks(topic = "AI Automation Tools", style = "High CTR / Pattern Interrupt")
    }

    fun setTab(index: Int) {
        _selectedTab.value = index
    }

    fun setCategory(category: TrendCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setHookTopic(topic: String) {
        _hookTopicInput.value = topic
    }

    fun setHookStyle(style: String) {
        _hookStyle.value = style
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun analyzeTrend(trend: TrendItem) {
        _selectedTrendForDeconstruction.value = trend
        _isAnalyzingTrend.value = true
        _deepAnalysisText.value = null

        viewModelScope.launch {
            val result = repository.getAiTrendDeconstruction(
                title = trend.title,
                channel = trend.channel,
                category = trend.category
            )
            _deepAnalysisText.value = result
            _isAnalyzingTrend.value = false
        }
    }

    fun closeDeconstructionDialog() {
        _selectedTrendForDeconstruction.value = null
        _deepAnalysisText.value = null
    }

    fun generateHooks(topic: String = _hookTopicInput.value, style: String = _hookStyle.value) {
        if (topic.isBlank()) {
            showSnackbar("Please enter a topic or video niche")
            return
        }
        _isGeneratingHooks.value = true
        viewModelScope.launch {
            val hooks = repository.generateAiHooks(topic, style)
            _generatedHooks.value = hooks
            _isGeneratingHooks.value = false
        }
    }

    fun generateScriptForHook(topic: String, hookType: String) {
        _isGeneratingScript.value = true
        viewModelScope.launch {
            val script = repository.generateAiScript(topic, hookType)
            _activeScript.value = script
            _isGeneratingScript.value = false
        }
    }

    fun closeScriptSheet() {
        _activeScript.value = null
    }

    fun saveTrendItem(trend: TrendItem) {
        viewModelScope.launch {
            repository.saveTrend(trend)
            showSnackbar("Saved \"${trend.title.take(30)}...\" to Vault")
        }
    }

    fun saveHookItem(hook: ViralHookResult) {
        viewModelScope.launch {
            repository.saveHook(hook, _hookTopicInput.value)
            showSnackbar("Saved Hook \"${hook.hookType}\" to Vault")
        }
    }

    fun saveScriptItem(script: ScriptOutline) {
        viewModelScope.launch {
            repository.saveScript(script)
            showSnackbar("Saved 60s Script for \"${script.topic}\" to Vault")
        }
    }

    fun saveKeywordItem(keyword: KeywordItem) {
        viewModelScope.launch {
            repository.saveKeyword(keyword)
            showSnackbar("Saved Keyword \"${keyword.keyword}\" to Vault")
        }
    }

    fun deleteSavedItem(id: Long) {
        viewModelScope.launch {
            repository.deleteSavedItem(id)
            showSnackbar("Item removed from Vault")
        }
    }

    fun updateSavedItemStatus(id: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updateSavedItemStatus(id, newStatus)
            showSnackbar("Status updated to $newStatus")
        }
    }
}
