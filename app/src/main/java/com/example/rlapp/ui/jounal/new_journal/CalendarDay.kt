package com.example.rlapp.ui.jounal.new_journal

data class CalendarDay(
    val day: String,      // Mon, Tue, Wed...
    val date: Int,        // 3, 4, 5...
    var isSelected: Boolean = false,  // If selected (yellow highlight)
    var dateString: String,
    var isChecked: Boolean = false,    // If the green checkmark should be shown

)
