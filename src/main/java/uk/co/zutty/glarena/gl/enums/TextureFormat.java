/*
 * Copyright (c) 2014 George Weller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package uk.co.zutty.glarena.gl.enums;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public enum TextureFormat {
    RGBA(GL11.GL_RGBA8, GL11.GL_RGBA, PNGDecoder.Format.RGBA),
    RED(GL30.GL_R8, GL11.GL_RGBA, PNGDecoder.Format.RGBA);

    private final int glInternalFormat;
    private final int glUploadFormat;
    private final PNGDecoder.Format decoderFormat;

    private TextureFormat(int glInternalFormat, int glUploadFormat, PNGDecoder.Format decoderFormat) {
        this.glInternalFormat = glInternalFormat;
        this.glUploadFormat = glUploadFormat;
        this.decoderFormat = decoderFormat;
    }

    public int getGlInternalFormat() {
        return glInternalFormat;
    }

    public int getGlUploadFormat() {
        return glUploadFormat;
    }

    public PNGDecoder.Format getDecoderFormat() {
        return decoderFormat;
    }
}
