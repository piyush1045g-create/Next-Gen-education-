package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Subject
import com.example.model.SubjectCategory

@Composable
fun SubjectChipRow(
    selectedCategory: SubjectCategory,
    onSelectSubject: (SubjectCategory) -> Unit,
    includeAll: Boolean = false,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (includeAll) {
            val isAllSelected = selectedCategory == SubjectCategory.ALL
            SubjectChipItem(
                label = "All Subjects",
                emoji = "✨",
                isSelected = isAllSelected,
                accentColor = MaterialTheme.colorScheme.primary,
                onClick = { onSelectSubject(SubjectCategory.ALL) },
                testTag = "subject_chip_all"
            )
        }

        Subject.ALL_SUBJECTS.forEach { subject ->
            val isSelected = selectedCategory == subject.category
            SubjectChipItem(
                label = subject.title,
                emoji = subject.iconEmoji,
                isSelected = isSelected,
                accentColor = subject.accentColor,
                onClick = { onSelectSubject(subject.category) },
                testTag = "subject_chip_${subject.category.name.lowercase()}"
            )
        }
    }
}

@Composable
private fun SubjectChipItem(
    label: String,
    emoji: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    val backgroundColor = if (isSelected) {
        accentColor
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (isSelected) {
        Color.White
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = contentColor
            )
        }
    }
}
