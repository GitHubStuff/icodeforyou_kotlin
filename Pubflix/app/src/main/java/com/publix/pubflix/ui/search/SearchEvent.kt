package com.publix.pubflix.ui.search

sealed interface SearchEvent {
    data object SearchStarted : SearchEvent
}