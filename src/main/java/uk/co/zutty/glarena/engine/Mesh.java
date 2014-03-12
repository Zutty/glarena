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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the raw data for a 3D mesh
 */
public class Mesh {

    private List<Vertex> vertices;
    private List<Short> indices;

    public Mesh() {
        vertices = new ArrayList<>();
        indices = new ArrayList<>();
    }

    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    public void addIndex(int index) {
        indices.add((short) index);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Short> getIndices() {
        return indices;
    }
}
