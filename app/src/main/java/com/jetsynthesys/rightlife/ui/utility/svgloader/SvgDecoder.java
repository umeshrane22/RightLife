package com.jetsynthesys.rightlife.ui.utility.svgloader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.caverock.androidsvg.SVG;

import java.io.IOException;
import java.io.InputStream;

public class SvgDecoder implements ResourceDecoder<InputStream, SVG> {

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull com.bumptech.glide.load.Options options) {
        return true; // Assume all InputStreams are SVGs
    }

    @Override
    public Resource<SVG> decode(@NonNull InputStream source, int width, int height, @NonNull com.bumptech.glide.load.Options options) throws IOException {
        try {
            SVG svg = SVG.getFromInputStream(source);
            return new SimpleResource<>(svg);
        } catch (Exception e) {
            throw new IOException("Failed to load SVG", e);
        }
    }
}
