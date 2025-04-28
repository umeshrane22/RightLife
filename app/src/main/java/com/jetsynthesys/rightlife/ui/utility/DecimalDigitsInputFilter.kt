package com.jetsynthesys.rightlife.ui.utility

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?, 
        start: Int, 
        end: Int, 
        dest: Spanned?, 
        dstart: Int, 
        dend: Int
    ): CharSequence? {
        
        val result = (dest?.substring(0, dstart) ?: "") +
                (source?.subSequence(start, end) ?: "") +
                (dest?.substring(dend) ?: "")
        
        if (result.contains(".")) {
            // If there's a decimal, allow up to 4 characters total (including decimal part)
            if (result.length > 4) { // Example: 12.34 = 5 characters including dot
                return ""
            }
            val parts = result.split(".")
            if (parts[0].length > 2) { // Integer part max 2 digits
                return ""
            }
            if (parts[1].length > 2) { // Decimal part max 2 digits
                return ""
            }
        } else {
            // No decimal: allow only up to 2 digits
            if (result.length > 2) {
                return ""
            }
        }
        return null // Accept the input
    }
}
