package com.demo.android_mvi_clean_architecture.ui.navigatiobar

import com.demo.android_mvi_clean_architecture.ui.base.BaseViewModel
import com.demo.android_mvi_clean_architecture.util.singleSharedFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class NavigationBarSharedViewModel @Inject constructor() : BaseViewModel() {

    private val _bottomItem = singleSharedFlow<BottomNavigationItem>()
    val bottomItem = _bottomItem.asSharedFlow()

    fun onBottomItemClicked(bottomNavigationItem: BottomNavigationItem) = launch {
        _bottomItem.emit(bottomNavigationItem)
    }
}