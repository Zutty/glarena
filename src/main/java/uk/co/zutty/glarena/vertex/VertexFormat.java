package uk.co.zutty.glarena.vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Zutty
 * Date: 16/06/2013
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
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
        private List<Attribute> attributes = new ArrayList<Attribute>();

        private Builder() {}

        public static Builder format() {
            return new Builder();
        }

        public Builder withAttribute(int elements) {
            Attribute attr = new Attribute();
            attr.setElements(elements);
            attr.setOffset(stride);
            attributes.add(attr);
            stride += elements * FLOAT_BYTES;
            elements += elements;
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
