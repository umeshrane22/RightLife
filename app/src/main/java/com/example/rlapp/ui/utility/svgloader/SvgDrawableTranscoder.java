package com.example.rlapp.ui.utility.svgloader;

import android.graphics.drawable.PictureDrawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.SVG;

public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, PictureDrawable> {

    @Override
    public Resource<PictureDrawable> transcode(@NonNull Resource<SVG> toTranscode, @NonNull com.bumptech.glide.load.Options options) {
        SVG svg = toTranscode.get();
        PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
        return new SvgDrawableResource(drawable);
    }
}
