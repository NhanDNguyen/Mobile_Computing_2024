package com.example.mobilecomputing

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.data.Message

// Homework1
@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    message: Message,
    onClick: (Message) -> Unit,
) {
    Row(
        modifier = modifier.padding(all = 8.dp),
    ) {

        var isImageExpanded by remember { mutableStateOf(false) }
        Image(
            painter = painterResource(id = message.imageId),
            contentDescription = message.contentDescription,
            modifier = Modifier
                .clickable { isImageExpanded = !isImageExpanded }
                //.fillMaxWidth(fraction = if (isImageExpanded) 1.0f else 0.4f)
                .size( if (isImageExpanded) 160.dp else 80.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
            //.clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = message.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.clickable { onClick( message ) }
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = message.body,
                    modifier = Modifier.padding(all = 4.dp).clickable { isExpanded = !isExpanded },
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
}

@Composable
fun ImageContainerScreen(
    messages: List<Message>,
    onClick: (Message) -> Unit
) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message = message, onClick = onClick)
        }
    }
}