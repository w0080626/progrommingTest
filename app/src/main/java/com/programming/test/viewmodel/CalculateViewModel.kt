package com.programming.test.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author Chenglong.Lu
 * @email w490576578@gmail.com
 * @date 2023/04/28
 * Don't contact before modify.
 */
class CalculateViewModel : ViewModel() {
    val amount = MutableStateFlow("")
    val time = MutableStateFlow("")
    val result = MutableStateFlow("")
    val items = MutableStateFlow(mutableListOf<String>())

    fun calculateResult(): String {
        val amt = amount.value.toDoubleOrNull() ?: return ""
        val t = time.value.toDoubleOrNull() ?: return ""
        result.value = String.format("%.2f", amt * t)
        return result.value
    }
}