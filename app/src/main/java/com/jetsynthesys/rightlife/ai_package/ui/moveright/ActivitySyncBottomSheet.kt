package com.jetsynthesys.rightlife.ai_package.ui.moveright

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jetsynthesys.rightlife.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActivitySyncBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_activity_sync, container, false)
    }

}