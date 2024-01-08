package com.example.mobilecomputing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.ui.theme.MobileComputingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var messages = listOf<Message>(
                Message(
                    author = "Nathan Cima",
                    body = "Ornamented doors of Saint Pierre Cathedral in Montpellier, France",
                    imageId = R.drawable.cathedral,
                    contentDescription = "Ornamented doors of Saint Pierre Cathedral",
                ),

                Message(
                    author = "Dennis Schrader",
                    body = "San Miguel de Allende Cathedral in Guanajuato, Mexico",
                    imageId = R.drawable.cathedral2,
                    contentDescription = "San Miguel de Allende Cathedral",
                ),

                Message(
                    author = "Brandon Morgan",
                    body = "Saint Mary's Cathedral in Natchez, United States",
                    imageId = R.drawable.cathedral3,
                    contentDescription = "Saint Mary's Cathedral",
                ),

                Message(
                    author = "Annie Spratt",
                    body = "Basilique Notre Dame de Montréal, Canada",
                    imageId = R.drawable.cathedral4,
                    contentDescription = "Basilique Notre Dame",
                ),

                Message(
                    author = "K. Mitch Hodge",
                    body = "The altar in Durham Cathedral in Durham, England",
                    imageId = R.drawable.cathedral5,
                    contentDescription = "The altar in Durham Cathedral",
                ),
            )

            ImageContainer(messages = messages)

        }
    }
}

data class Message(
    val author: String,
    val body: String,
    val imageId: Int = -1,
    val contentDescription: String = ""
)

// Homework1
@Composable
fun MessageCard(
    modifier: Modifier= Modifier,
    message: Message
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
                .fillMaxWidth(fraction = if (isImageExpanded) 1.0f else 0.5f)
                .border(3.0.dp, MaterialTheme.colorScheme.primary)
        )
        
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = message.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
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
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
}

@Composable
fun ImageContainer(
    messages: List<Message>
) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message = message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileComputingTheme {

    }
}