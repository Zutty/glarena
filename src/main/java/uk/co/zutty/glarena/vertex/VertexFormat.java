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

package uk.co.zutty.glarena.vertex;

import java.util.ArrayList;
import java.util.List;

public class VertexFormat {

    private static final int FLOAT_BYTES = 4;

    private int stride;
    private int elements;
    private List<Attribute> attributes;

    public int getStride() {
        return stride;
    }

    public int getElements() {
        return elements;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public static class Builder {

        private int stride = 0;
        private int elements = 0;
        private List<Attribute> attributes = new ArrayList<>();

        private Builder() {
        }

        public static Builder format() {
            return new Builder();
        }

        public Builder withAttribute(int elements) {
            Attribute attr = new Attribute();
            attr.setElements(elements);
            attr.setOffset(stride);
            attributes.add(attr);
            stride += elements * FLOAT_BYTES;
            this.elements += elements;
            return this;
        }

        public VertexFormat build() {
            VertexFormat format = new VertexFormat();
            format.stride = stride;
            format.elements = elements;
            format.attributes = attributes;
            return format;
        }
    }
}
