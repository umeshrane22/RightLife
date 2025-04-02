package com.jetsynthesys.rightlife.ui.utility.svgloader;

import android.graphics.drawable.PictureDrawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.resource.drawable.DrawableResource;

public class SvgDrawableResource extends DrawableResource<PictureDrawable> {

    public SvgDrawableResource(PictureDrawable drawable) {
        super(drawable);
    }

    @NonNull
    @Override
    public Class<PictureDrawable> getResourceClass() {
        return PictureDrawable.class;
    }

    @Override
    public int getSize() {
        // Size can be determined if necessary, set to 0 for now
        return 0;
    }

    @Override
    public void recycle() {
        // No specific recycling needed for PictureDrawable
    }
}
