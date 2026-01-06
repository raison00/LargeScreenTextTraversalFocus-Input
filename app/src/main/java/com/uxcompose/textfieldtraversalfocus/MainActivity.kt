@file:OptIn(ExperimentalMaterial3Api::class)

package com.uxcompose.textfieldtraversalfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uxcompose.textfieldtraversalfocus.ui.theme.TextFieldTraversalFocusTheme

//var showBottomSheet: Boolean

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TextFieldTraversalFocusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Main Container: 24.dp padding and vertical scrolling
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(24.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        FocusTraversalSection()
                        Section3InputActions()
                        InputActionsSection()

                    }
                }
            }
        }
    }
}

@Composable
fun FocusTraversalSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Non-Working Focus Traversal within TextFields",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Try to use the 'TAB' key on the keyboard to move between the TextFields. This is using a colum instead of a grid for mobile row behavior improvements.",
            style = MaterialTheme.typography.bodyMedium
        )

        // Using a simple Column instead of a Grid for better mobile row behavior
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { index ->
                FocusableTextField(label = "TextField $index")
            }
        }
    }
}

@Composable
fun FocusableTextField(label: String) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        label = { Text(label) },
        leadingIcon = {
            IconButton(onClick = { /* Search Action */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            }
        },
        trailingIcon = {
            if (text.isNotEmpty() || isFocused) {
                IconButton(onClick = { text = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun InputActionsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    var textValue1 by remember { mutableStateOf("") }
    var textValue2 by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryFixed)
                Spacer(Modifier.width(8.dp))
                Text("TextField Focus Trap", style = MaterialTheme.typography.titleLarge)
            }

            Text("Try the dialogs and modals below to see TextField focus trapping issues somewhat resolved. The issue is the complexity of the TextField focus trap within a common modal trap across Android form factors.")

            Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Create, null)
                Spacer(Modifier.width(8.dp))
                Text("Open Dialog")
            }

            ElevatedButton(onClick = { showBottomSheet = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Build, null)
                Spacer(Modifier.width(8.dp))
                Text("Open Bottom Sheet")
            }
        }
    }

    // --- Overlays ---

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.titleLarge)
                    Text("Using the keyboard to tab or return to navigate through the UI is not working here. The focus remains within the TextField.", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(value = textValue, onValueChange = { textValue = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = textValue1, onValueChange = { textValue1 = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                        Button(onClick = { showDialog = false }) { Text("Save") }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.titleLarge)
                Text("Using the keyboard to tab or return to navigate through the UI is not working here. The focus remains within the TextField.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(value = textValue2, onValueChange = { textValue2 = it }, label = { Text("This TextField will allow for multi-line entries.") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}

@Composable
fun Section3InputActions() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var showModalWithTextField by remember { mutableStateOf(false) }
    var showBottomlWithTextField by remember { mutableStateOf(false) }
    var dialogTextValue by remember { mutableStateOf("") }
    var modalTextValue by remember { mutableStateOf("") }
    var bottomTextValue by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.tertiary

                )
                Text(
                    //Section 3:
                    text = "Issue: Focus & Input with Trapping ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
            }

            Text(
                text = "Issue:  Dialogs and modals with elevated text fields for user input lose focus and get trapped in first TextField.  ",
                style = MaterialTheme.typography.bodyMedium
            )

            // Dialog with TextField Button
            ElevatedButton(
                onClick = { showDialogWithTextField = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Dialog with TextField")
            }

            // Modal with TextField Button
            ElevatedButton(
                onClick = { showModalWithTextField = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Modal with TextField")
            }
            ElevatedButton(onClick = { showBottomlWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Build, null)
                Spacer(Modifier.width(8.dp))
                Text("Open Bottom Sheet")
            }

            // Action Buttons Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                OutlinedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                OutlinedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Dialog with TextField
    if (showDialogWithTextField) {
        Dialog(onDismissRequest = { showDialogWithTextField = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Enter Information",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "The issue here is the Focus Trapping of the TextField prevents the text to be entered into the email field. ",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = dialogTextValue,
                        onValueChange = { dialogTextValue = it },
                        label = { Text("Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { showDialogWithTextField = false }) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { showDialogWithTextField = false }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet with TextField
    if (showModalWithTextField) {
        ModalBottomSheet(
            onDismissRequest = { showModalWithTextField = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Quick Entry Form",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "The issue here is the Focus Trapping of the TextField prevents the text to be entered into the description field. ",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = modalTextValue,
                    onValueChange = { modalTextValue = it },
                    label = { Text("Title") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Description") },
                    leadingIcon = {
                        Icon(Icons.Default.List, contentDescription = null)
                    },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { showModalWithTextField = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = { showModalWithTextField = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
