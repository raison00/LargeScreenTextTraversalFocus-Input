# Large Screen Text Traversal Focus & Input TextField Trappings and Solutions 
Working through Jetpack Compose Large Screen Focus and Input traps within TextFields using popular methods.  Using the original TextField (part of the Compose Material/Material3 libraries) to the newer TextFieldState ecosystem (introduced with BasicTextField2, now just called BasicTextField in newer versions), the API shift is significant.

The Old Way: You had to manually "hoist" the string and update it. If you did any heavy processing in onValueChange, you could cause typing lag.
The New Way: You pass a state object. The text field updates the state internally, and you observe it when you need it.

In the old API, if you wanted to force uppercase or limit length, you had to write logic inside onValueChange. In the new API, this is handled by InputTransformation.  The new API uses a synchronous edit buffer within TextFieldState. This ensures that the cursor position and the text content are always in sync, even if the UI takes a moment to catch up.

## Version and Milestone
Compose 1.6,BasicTextField2 introduced as an experiment.
Compose 1.7,API renamed to BasicTextField; TextFieldState becomes the new standard.
Compose 1.8,Stabilization of Transformations and introduction of SecureTextField.
Compose 1.10 (Current),"onValueChange is officially considered ""legacy"" for new feature development."

In Material 3 version 1.4.0 (December 2025), the high-level components (TextField and OutlinedTextField) finally received official overloads for TextFieldState. This allows developers to get the Material design visuals while using the high-performance state-based engine under the hood.

This respository is for creating a Focus Input, TextField, and Traversal Demonstration on Android 16, Jetpack Compose 2025.12.01, Material 3 1.4.0.   
minSdk = 24
targetSdk = 36
Java + jvmTarget = 11  
targetCompatibility = JavaVersion.VERSION_11



A common issue for developers is getting stuck in a focus trap, especially within a TextField.
This solution shows working Focus Traversal within TextFields using the TAB key for keyboard navigation within and between TextFields.  

There are changes to the Compose API for the TextFields that should be considered.
The 2026 Checklist
When you build a text input today, ask yourself:

[ ] Is the state hoisted? (Use TextFieldState in the ViewModel).

[ ] Are visuals custom? (Use BasicTextField with a decorator).

[ ] Is there logic in onValueChange? (Move it to InputTransformation).

[ ] Is it saving to a DB? (Use snapshotFlow + debounce).



<img width="1035" height="325" alt="image" src="https://github.com/user-attachments/assets/5a743802-4927-4119-8b06-1057e2e5c686" />

<video src="https://github.com/raison00/LargeScreenTextTraversalFocus-Input/blob/main/DesktopChromebookFocusTrappings.mp4?raw=true" width="100%" autoplay loop muted playsinline>
</video>

[Focus Trapping Video](https://youtu.be/KXpY0NdfFn4)

<video src="https://youtu.be/KXpY0NdfFn4" width="100%" autoplay loop muted playsinline>
</video>


[link to video file](https://github.com/raison00/LargeScreenTextTraversalFocus-Input/blob/main/DesktopChromebookFocusTrappings.mp4)

# Overview of a mixed implementation of API versions that works, but is not the correct practice in 2026:
This code snippet demonstrates the implementation of a scrollable vertical list of input fields in Jetpack Compose. It leverages LocalFocusManager to provide programmatic control over keyboard focus and uses a repeat loop to generate a series of FocusableTextField components with indexed labeling.

This code implementation is an example of Accessible and State-Aware Input Design. By explicitly managing the focus state, there is a "Conditional UI" pattern—where the clear button only appears when relevant—which reduces visual clutter for the user.

<img width="605" height="169" alt="kotlin code image" src="https://github.com/user-attachments/assets/8d346d7e-c0af-4497-a21e-72deb992a57d" />


# Composable updates isFocused whenever the focus state changes
<img width="687" height="860" alt="kotlin code image" src="https://github.com/user-attachments/assets/d1aa3a44-6ee6-4ab6-b1ce-f3d94145fb9c" />


#  Triggers the onNext callback for various keyboard "Enter" actions + ensures the TextField doesn't get trapped within creating multilines
<img width="653" height="420" alt="KeyboardOption kotlin code image" src="https://github.com/user-attachments/assets/433694bd-b7fe-4e18-ad27-965e6aba80b9" />


# Component Breakdown
LocalFocusManager.current: Retrieves the FocusManager instance from the current Composition. This allows the parent or child components to programmatically move focus (e.g., moveFocus(FocusDirection.Down)) or clear focus when an action is completed.

Column: Acts as the layout container, organizing the children vertically. The spacedBy(16.dp) parameter ensures a consistent visual rhythm and "touch target" clearance between fields.

repeat(times = 4): A standard Kotlin control flow used here to instantiate multiple fields. This approach is ideal for form prototyping or generating lists where the number of fields is predetermined.

FocusableTextField: A custom (or wrapped) composable that handles the internal state and focus request logic for each individual input line.


# Focus Traversal Description
This implementation uses a vertical Column which, by default, establishes a linear focus traversal path. 
In a compliant system, the FocusManager should intercept the IME_ACTION_NEXT event from TextField $index and trigger a moveFocus request to TextField $index + 1.

# Compliance WCAG AA Considerations
Form Factor Consistency: Because this uses a Column, the focus order is Top-to-Bottom.

Accessibility: Each field is dynamically labeled ("TextField 0", "TextField 1"), which provides the necessary context for Screen Readers (TalkBack) to identify the user's current position within the list.

Commented code to make TextFields function with icons and CTAs within the field while allowing for input and focus traversal:


1. Obtain the FocusManager to handle programmatic navigation (Tab/Next/Enter)

val focusManager = LocalFocusManager.current

2. Vertical layout with standardized 16dp spacing for accessibility compliance
   
Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

3. Dynamically generate 4 focusable input fields
   
    repeat(times = 4) { index ->
        FocusableTextField(
            label = "TextField $index" // Indexed labeling for clear UX and A11y
        )
    }
}

<img width="605" height="169" alt="kotlin code image" src="https://github.com/user-attachments/assets/8d346d7e-c0af-4497-a21e-72deb992a57d" />


## Specific Details:
[versions]
agp = "8.13.2" 

kotlin = "2.0.21"

coreKtx = "1.17.0"

junit = "4.13.2"

junitVersion = "1.3.0"

espressoCore = "3.7.0"

lifecycleRuntimeKtx = "2.10.0"

activityCompose = "1.12.2"

composeBom = "2025.12.01"

## The Old Way: You had to manually "hoist" the string and update it. If you did any heavy processing in onValueChange, you could cause typing lag.
# Compostable
```kotlin
@Composable
fun FocusableTextField(
    label: String,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {}
) {
    // State for the text input
    var text by remember { mutableStateOf("") }

    // State to track if this specific field has focus
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            // Updates isFocused whenever the focus state changes
            .onFocusChanged { state ->
                isFocused = state.isFocused
            },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            // Show clear button only if there's text AND the field is active
            if (text.isNotEmpty() && isFocused) {
                IconButton(onClick = { text = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear text"
                    )
                }
            }
        },
        // KeyboardOptions only contains IME and Type info
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        // Triggers the onNext callback for various keyboard "Enter" actions
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onNext() },
            onGo = { onNext() }
        ),
        //  singleLine and maxLines stop the trapped textfield issue, will not work within KeyboardOptions
        singleLine = true,
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}



