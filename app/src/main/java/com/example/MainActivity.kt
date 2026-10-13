package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.StudyViewModel
import com.example.ui.components.StudyGenieTopBar
import com.example.ui.screens.ChatTutorScreen
import com.example.ui.screens.FlashcardDeckScreen
import com.example.ui.screens.ResourceLibraryScreen
import com.example.ui.screens.StepSolverScreen
import com.example.ui.screens.StudyNotesScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                StudyGenieApp()
            }
        }
    }
}

@Composable
fun StudyGenieApp(
    viewModel: StudyViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedMode by viewModel.selectedMode.collectAsState()

    // Chat State
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val chatInputText by viewModel.chatInputText.collectAsState()

    // Solver State
    val solverInputText by viewModel.solverInputText.collectAsState()
    val solverResult by viewModel.solverResult.collectAsState()
    val isSolverLoading by viewModel.isSolverLoading.collectAsState()

    // Library State
    val filteredResources by viewModel.filteredResources.collectAsState()
    val savedSolutions by viewModel.savedSolutions.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val libraryFilterSubject by viewModel.libraryFilterSubject.collectAsState()
    val selectedResource by viewModel.selectedResource.collectAsState()

    // Flashcards State
    val allDecks by viewModel.allDecks.collectAsState()
    val activeDeck by viewModel.activeDeck.collectAsState()
    val activeCardIndex by viewModel.activeCardIndex.collectAsState()
    val isCardFlipped by viewModel.isCardFlipped.collectAsState()

    // Notes State
    val studyNotes by viewModel.studyNotes.collectAsState()

    // Back handling
    BackHandler(enabled = currentTab != AppTab.CHAT_TUTOR || selectedResource != null || activeDeck != null) {
        if (selectedResource != null) {
            viewModel.selectResource(null)
        } else if (activeDeck != null) {
            viewModel.closeDeck()
        } else if (currentTab != AppTab.CHAT_TUTOR) {
            viewModel.selectTab(AppTab.CHAT_TUTOR)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            StudyGenieTopBar(
                currentMode = selectedMode,
                onModeClick = { /* Can cycle modes or show modal */ },
                onClearChatClick = { viewModel.clearChat() }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_navigation_bar"),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.CHAT_TUTOR,
                    onClick = { viewModel.selectTab(AppTab.CHAT_TUTOR) },
                    icon = { Icon(imageVector = Icons.Default.Forum, contentDescription = "AI Tutor") },
                    label = { Text("AI Tutor") },
                    modifier = Modifier.testTag("nav_tab_tutor")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.STEP_SOLVER,
                    onClick = { viewModel.selectTab(AppTab.STEP_SOLVER) },
                    icon = { Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Solver") },
                    label = { Text("Step Solver") },
                    modifier = Modifier.testTag("nav_tab_solver")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.LIBRARY,
                    onClick = { viewModel.selectTab(AppTab.LIBRARY) },
                    icon = { Icon(imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Library") },
                    label = { Text("Library") },
                    modifier = Modifier.testTag("nav_tab_library")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.FLASHCARDS,
                    onClick = { viewModel.selectTab(AppTab.FLASHCARDS) },
                    icon = { Icon(imageVector = Icons.Default.Style, contentDescription = "Cards") },
                    label = { Text("Flashcards") },
                    modifier = Modifier.testTag("nav_tab_flashcards")
                )
                NavigationBarItem(
                    selected = currentTab == AppTab.NOTES,
                    onClick = { viewModel.selectTab(AppTab.NOTES) },
                    icon = { Icon(imageVector = Icons.Default.EditNote, contentDescription = "Notes") },
                    label = { Text("Notes") },
                    modifier = Modifier.testTag("nav_tab_notes")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.CHAT_TUTOR -> {
                    ChatTutorScreen(
                        messages = chatMessages,
                        selectedSubject = selectedSubject,
                        selectedMode = selectedMode,
                        inputText = chatInputText,
                        isGenerating = isGenerating,
                        onSubjectSelect = { viewModel.selectSubject(it) },
                        onModeSelect = { viewModel.selectMode(it) },
                        onInputTextChange = { viewModel.setChatInputText(it) },
                        onSendMessage = { prompt -> viewModel.sendChatMessage(prompt) },
                        onSaveSolution = { msg ->
                            val promptTitle = chatMessages.findLast { it.sender == com.example.model.MessageSender.USER }?.content.orEmpty()
                            viewModel.saveSolution(promptTitle, msg)
                        }
                    )
                }

                AppTab.STEP_SOLVER -> {
                    StepSolverScreen(
                        inputText = solverInputText,
                        onInputTextChange = { viewModel.setSolverInputText(it) },
                        selectedSubject = selectedSubject,
                        onSelectSubject = { viewModel.selectSubject(it) },
                        selectedMode = selectedMode,
                        onSelectMode = { viewModel.selectMode(it) },
                        solverResult = solverResult,
                        isLoading = isSolverLoading,
                        onSolveClick = { viewModel.solveWithStepSolver() },
                        onSaveResult = { result ->
                            viewModel.saveSolution(solverInputText, result)
                        },
                        onAskInChat = { question ->
                            viewModel.selectTab(AppTab.CHAT_TUTOR)
                            viewModel.sendChatMessage(question)
                        }
                    )
                }

                AppTab.LIBRARY -> {
                    ResourceLibraryScreen(
                        resources = filteredResources,
                        savedSolutions = savedSolutions,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        selectedFilterSubject = libraryFilterSubject,
                        onSelectFilterSubject = { viewModel.setLibraryFilterSubject(it) },
                        selectedResource = selectedResource,
                        onSelectResource = { viewModel.selectResource(it) },
                        onDeleteSolution = { viewModel.deleteSavedSolution(it) },
                        onToggleFavorite = { id, fav -> viewModel.toggleFavoriteSolution(id, fav) }
                    )
                }

                AppTab.FLASHCARDS -> {
                    FlashcardDeckScreen(
                        decks = allDecks,
                        activeDeck = activeDeck,
                        activeCardIndex = activeCardIndex,
                        isCardFlipped = isCardFlipped,
                        onOpenDeck = { viewModel.openDeck(it) },
                        onCloseDeck = { viewModel.closeDeck() },
                        onFlipCard = { viewModel.flipActiveCard() },
                        onMarkMastery = { viewModel.markCurrentCardMastery(it) },
                        onCreateDeck = { title, subject, cards ->
                            viewModel.createFlashcardDeckFromTopic(title, subject, cards)
                        }
                    )
                }

                AppTab.NOTES -> {
                    StudyNotesScreen(
                        notes = studyNotes,
                        onAddNote = { title, content, subject ->
                            viewModel.addNote(title, content, subject)
                        },
                        onDeleteNote = { viewModel.deleteNote(it) }
                    )
                }
            }
        }
    }
}
