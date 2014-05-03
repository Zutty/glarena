/*
 * Copyright (c) 2013 George Weller
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

package uk.co.zutty.glarena.engine;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import uk.co.zutty.glarena.gl.Texture;
import uk.co.zutty.glarena.gl.Texture2D;
import uk.co.zutty.glarena.gl.TextureCube;
import uk.co.zutty.glarena.gl.enums.TextureFormat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static uk.co.zutty.glarena.gl.enums.CubeMapTarget.*;

/**
 * Loader for PNG format textures.
 */
public class TextureLoader {
    public static Texture loadTexture(String location) {
        return loadTexture(location, TextureFormat.RGBA);
    }

    public static Texture loadTexture(String location, TextureFormat format) {
        return new Texture2D(loadPNG(location, format.getDecoderFormat()), format);
    }

    public static TextureCube loadCubemap(String location) {
        TextureCube texture = new TextureCube();

        texture.setImage(POSITIVE_X, loadPNG(location + "_right1.png", PNGDecoder.Format.RGBA));
        texture.setImage(NEGATIVE_X, loadPNG(location + "_left2.png", PNGDecoder.Format.RGBA));
        texture.setImage(POSITIVE_Y, loadPNG(location + "_top3.png", PNGDecoder.Format.RGBA));
        texture.setImage(NEGATIVE_Y, loadPNG(location + "_bottom4.png", PNGDecoder.Format.RGBA));
        texture.setImage(POSITIVE_Z, loadPNG(location + "_front5.png", PNGDecoder.Format.RGBA));
        texture.setImage(NEGATIVE_Z, loadPNG(location + "_back6.png", PNGDecoder.Format.RGBA));

        return texture;
    }

    public static Image loadPNG(String filename, PNGDecoder.Format format) {
        Image data = new Image();

        try (InputStream in = TextureLoader.class.getResourceAsStream(filename)) {
            PNGDecoder decoder = new PNGDecoder(in);
            data.setWidth(decoder.getWidth());
            data.setHeight(decoder.getHeight());

            ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, format);
            buffer.flip();
            data.setBuffer(buffer);

            return data;
        } catch (IOException e) {
            throw new GameException(e);
        }
    }
}
