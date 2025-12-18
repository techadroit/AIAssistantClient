package org.dev.assistant.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

open class BaseViewModel : ViewModel() {

    val scope = viewModelScope
}