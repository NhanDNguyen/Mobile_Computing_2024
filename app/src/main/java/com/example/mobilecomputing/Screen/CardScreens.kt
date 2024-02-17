package com.example.mobilecomputing.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.NoteDetails
import com.example.mobilecomputing.util.getDateAsString

@Composable
fun TextNoteCard(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
    onNoteClick: (NoteDetails) -> Unit,
    widthInDp: Dp,
    heightInDp: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .size(width = widthInDp, height = heightInDp)
                .clickable { onNoteClick(noteDetails) }
                .padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Text(
                text = noteDetails.body,
                modifier = Modifier.padding(8.dp),
            )
        }
        Text(text = noteDetails.title, fontWeight = FontWeight.Bold)
        Text(text = getDateAsString(noteDetails.date), color = Color.Gray)
    }

}

@Composable
fun ImageNoteCard(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
    onNoteClick: (NoteDetails) -> Unit,
    widthInDp: Dp,
    heightInDp: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .size(width = widthInDp, height = heightInDp)
                .clickable { onNoteClick(noteDetails) }
                .padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            noteDetails.imageData?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Text(text = noteDetails.title, fontWeight = FontWeight.Bold)
        Text(text = getDateAsString(noteDetails.date), color = Color.Gray)
    }

}

@Composable
fun AudioNoteCard(
    modifier: Modifier = Modifier,
    noteDetails: NoteDetails,
    onNoteClick: (NoteDetails) -> Unit,
    widthInDp: Dp,
    heightInDp: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .size(width = widthInDp, height = heightInDp)
                .clickable { onNoteClick(noteDetails) }
                .padding(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Default.Audiotrack, contentDescription = "audio")
        }
        Text(text = noteDetails.title, fontWeight = FontWeight.Bold)
        Text(text = getDateAsString(noteDetails.date), color = Color.Gray)
    }

}