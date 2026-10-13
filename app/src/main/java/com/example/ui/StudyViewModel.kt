package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ResourceLibraryData
import com.example.data.db.CustomFlashcardEntity
import com.example.data.db.SavedSolutionEntity
import com.example.data.db.StudyGenieDatabase
import com.example.data.db.StudyNoteEntity
import com.example.model.ChatMessage
import com.example.model.FlashcardDeck
import com.example.model.FlashcardItem
import com.example.model.MessageSender
import com.example.model.StudyMode
import com.example.model.StudyResource
import com.example.model.Subject
import com.example.model.SubjectCategory
import com.example.network.GeminiApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val label: String, val iconName: String) {
    CHAT_TUTOR("AI Tutor", "forum"),
    STEP_SOLVER("Step Solver", "auto_fix_high"),
    LIBRARY("Library", "menu_book"),
    FLASHCARDS("Cards", "style"),
    NOTES("Notes", "edit_note")
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val db = StudyGenieDatabase.getInstance(application)
    private val savedSolutionDao = db.savedSolutionDao()
    private val studyNoteDao = db.studyNoteDao()
    private val customFlashcardDao = db.customFlashcardDao()

    // Navigation & Context State
    private val _currentTab = MutableStateFlow(AppTab.CHAT_TUTOR)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedSubject = MutableStateFlow(SubjectCategory.MATH)
    val selectedSubject: StateFlow<SubjectCategory> = _selectedSubject.asStateFlow()

    private val _selectedMode = MutableStateFlow(StudyMode.STEP_BY_STEP)
    val selectedMode: StateFlow<StudyMode> = _selectedMode.asStateFlow()

    // Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.GENIE,
                content = "👋 Welcome to **StudyGenie**! I'm your multi-subject AI study tutor.\n\nAsk me any problem or concept across **Math, Science, History, Literature, or Computer Science**. I'll provide clear step-by-step solutions, formulas, and intuitive explanations.\n\nTry tapping one of the suggested prompts below to get started!",
                subject = SubjectCategory.MATH,
                suggestedFollowUps = listOf(
                    "Solve for x: 3x² - 12x + 9 = 0",
                    "Explain photosynthesis light reactions step by step",
                    "What were the 4 main causes of World War I?"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _chatInputText = MutableStateFlow("")
    val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

    // Dedicated Step-by-Step Solver Screen State
    private val _solverInputText = MutableStateFlow("")
    val solverInputText: StateFlow<String> = _solverInputText.asStateFlow()

    private val _solverResult = MutableStateFlow<ChatMessage?>(null)
    val solverResult: StateFlow<ChatMessage?> = _solverResult.asStateFlow()

    private val _isSolverLoading = MutableStateFlow(false)
    val isSolverLoading: StateFlow<Boolean> = _isSolverLoading.asStateFlow()

    // Resource Library State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _libraryFilterSubject = MutableStateFlow(SubjectCategory.ALL)
    val libraryFilterSubject: StateFlow<SubjectCategory> = _libraryFilterSubject.asStateFlow()

    private val _selectedResource = MutableStateFlow<StudyResource?>(null)
    val selectedResource: StateFlow<StudyResource?> = _selectedResource.asStateFlow()

    val savedSolutions: StateFlow<List<SavedSolutionEntity>> =
        savedSolutionDao.getAllSolutions().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val studyNotes: StateFlow<List<StudyNoteEntity>> =
        studyNoteDao.getAllNotes().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // Flashcards State
    private val _activeDeck = MutableStateFlow<FlashcardDeck?>(null)
    val activeDeck: StateFlow<FlashcardDeck?> = _activeDeck.asStateFlow()

    private val _activeCardIndex = MutableStateFlow(0)
    val activeCardIndex: StateFlow<Int> = _activeCardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    // Deck lists
    private val _allDecks = MutableStateFlow<List<FlashcardDeck>>(ResourceLibraryData.BUILT_IN_DECKS)
    val allDecks: StateFlow<List<FlashcardDeck>> = _allDecks.asStateFlow()

    // Filtered resources derived from search & subject
    val filteredResources: StateFlow<List<StudyResource>> =
        combine(_searchQuery, _libraryFilterSubject) { query, subject ->
            ResourceLibraryData.RESOURCES.filter { resource ->
                val matchesSubject = subject == SubjectCategory.ALL || resource.subject == subject
                val matchesQuery = query.isBlank() ||
                        resource.title.contains(query, ignoreCase = true) ||
                        resource.summary.contains(query, ignoreCase = true) ||
                        resource.sections.any { it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true) }
                matchesSubject && matchesQuery
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResourceLibraryData.RESOURCES)

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun selectSubject(subject: SubjectCategory) {
        _selectedSubject.value = subject
    }

    fun selectMode(mode: StudyMode) {
        _selectedMode.value = mode
    }

    fun setChatInputText(text: String) {
        _chatInputText.value = text
    }

    fun setSolverInputText(text: String) {
        _solverInputText.value = text
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setLibraryFilterSubject(subject: SubjectCategory) {
        _libraryFilterSubject.value = subject
    }

    fun selectResource(resource: StudyResource?) {
        _selectedResource.value = resource
    }

    fun sendChatMessage(customPrompt: String? = null) {
        val promptToSend = customPrompt ?: _chatInputText.value.trim()
        if (promptToSend.isBlank() || _isGenerating.value) return

        if (customPrompt == null) {
            _chatInputText.value = ""
        }

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            content = promptToSend,
            subject = _selectedSubject.value
        )

        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userMessage)

        val placeholderMessage = ChatMessage(
            sender = MessageSender.GENIE,
            content = "StudyGenie is thinking through step-by-step...",
            subject = _selectedSubject.value,
            isLoading = true
        )
        currentList.add(placeholderMessage)
        _chatMessages.value = currentList
        _isGenerating.value = true

        viewModelScope.launch {
            val result = GeminiApiClient.generateStudySolution(
                prompt = promptToSend,
                subject = _selectedSubject.value,
                mode = _selectedMode.value,
                conversationHistory = currentList.filter { !it.isLoading }
            )

            _isGenerating.value = false
            val updated = _chatMessages.value.toMutableList()
            updated.removeAll { it.isLoading }

            result.onSuccess { response ->
                updated.add(response)
                _chatMessages.value = updated
            }.onFailure { err ->
                updated.add(
                    ChatMessage(
                        sender = MessageSender.GENIE,
                        content = "Sorry, couldn't generate response: ${err.message}",
                        isError = true
                    )
                )
                _chatMessages.value = updated
            }
        }
    }

    fun solveWithStepSolver(prompt: String? = null) {
        val question = prompt ?: _solverInputText.value.trim()
        if (question.isBlank() || _isSolverLoading.value) return

        _isSolverLoading.value = true
        _solverResult.value = null

        viewModelScope.launch {
            val result = GeminiApiClient.generateStudySolution(
                prompt = question,
                subject = _selectedSubject.value,
                mode = _selectedMode.value
            )
            _isSolverLoading.value = false
            result.onSuccess { response ->
                _solverResult.value = response
            }.onFailure {
                _solverResult.value = ChatMessage(
                    sender = MessageSender.GENIE,
                    content = "Could not solve problem. Please try again.",
                    isError = true
                )
            }
        }
    }

    fun saveSolution(question: String, message: ChatMessage) {
        viewModelScope.launch {
            val entity = SavedSolutionEntity(
                id = message.id,
                question = question.ifBlank { message.content.take(60) },
                subject = message.subject.name,
                mode = _selectedMode.value.name,
                answer = message.content,
                proTip = message.proTip.orEmpty(),
                timestamp = System.currentTimeMillis()
            )
            savedSolutionDao.insertSolution(entity)

            // Mark message as saved
            val updated = _chatMessages.value.map {
                if (it.id == message.id) it.copy(isSaved = true) else it
            }
            _chatMessages.value = updated
        }
    }

    fun deleteSavedSolution(id: String) {
        viewModelScope.launch {
            savedSolutionDao.deleteById(id)
        }
    }

    fun toggleFavoriteSolution(id: String, currentFav: Boolean) {
        viewModelScope.launch {
            savedSolutionDao.updateFavorite(id, !currentFav)
        }
    }

    // Flashcard Deck Player
    fun openDeck(deck: FlashcardDeck) {
        _activeDeck.value = deck
        _activeCardIndex.value = 0
        _isCardFlipped.value = false
    }

    fun closeDeck() {
        _activeDeck.value = null
        _activeCardIndex.value = 0
        _isCardFlipped.value = false
    }

    fun flipActiveCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun nextCard() {
        val deck = _activeDeck.value ?: return
        if (_activeCardIndex.value < deck.cards.size - 1) {
            _activeCardIndex.value += 1
            _isCardFlipped.value = false
        }
    }

    fun prevCard() {
        if (_activeCardIndex.value > 0) {
            _activeCardIndex.value -= 1
            _isCardFlipped.value = false
        }
    }

    fun markCurrentCardMastery(isMastered: Boolean) {
        val deck = _activeDeck.value ?: return
        val currentIndex = _activeCardIndex.value
        val card = deck.cards.getOrNull(currentIndex) ?: return

        val updatedCards = deck.cards.toMutableList()
        updatedCards[currentIndex] = card.copy(isMastered = isMastered)
        val updatedDeck = deck.copy(cards = updatedCards)
        _activeDeck.value = updatedDeck

        // Update in decks list
        _allDecks.value = _allDecks.value.map {
            if (it.id == deck.id) updatedDeck else it
        }

        // Advance to next card
        if (currentIndex < deck.cards.size - 1) {
            _activeCardIndex.value = currentIndex + 1
            _isCardFlipped.value = false
        }
    }

    fun createFlashcardDeckFromTopic(title: String, subject: SubjectCategory, cards: List<Pair<String, String>>) {
        val newDeck = FlashcardDeck(
            id = "custom_" + System.currentTimeMillis(),
            title = title,
            subject = subject,
            description = "Custom study deck on $title",
            cards = cards.map { (q, a) ->
                FlashcardItem(question = q, answer = a)
            }
        )
        _allDecks.value = listOf(newDeck) + _allDecks.value
        openDeck(newDeck)
    }

    // Notes
    fun addNote(title: String, content: String, subject: SubjectCategory) {
        if (title.isBlank() && content.isBlank()) return
        viewModelScope.launch {
            val note = StudyNoteEntity(
                title = title.ifBlank { "Study Note" },
                content = content,
                subject = subject.name
            )
            studyNoteDao.insertNote(note)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            studyNoteDao.deleteById(id)
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                sender = MessageSender.GENIE,
                content = "Chat cleared! How can StudyGenie help you study today?",
                subject = _selectedSubject.value,
                suggestedFollowUps = Subject.find(_selectedSubject.value).samplePrompts.take(3)
            )
        )
    }
}
