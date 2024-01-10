package com.example.mobilecomputing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobilecomputing.data.Message

@Composable
fun BioScreen(
    modifier: Modifier=Modifier,
    message: Message,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(all = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = message.imageId),
            contentDescription = message.contentDescription,
            modifier = Modifier
                .size(160.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message.author,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message.body,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}