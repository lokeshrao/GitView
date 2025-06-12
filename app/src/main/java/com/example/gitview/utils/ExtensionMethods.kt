package com.example.gitview.utils

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun TextInputEditText.onTextChangedDebounce(
    lifecycleScope: CoroutineScope,
    delayMillis: Long = 300,
    onDebounce: (String) -> Unit
) {
    var debounceJob: Job? = null
    this.addTextChangedListener {
        debounceJob?.cancel()
        debounceJob = lifecycleScope.launch {
            delay(delayMillis)
            onDebounce(it.toString())
        }
    }
}