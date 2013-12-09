/*
 * Copyright (C) 2013 tarent AG
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.osiam.resources.scim;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java class for Resource complex type.
 */
public abstract class Resource {

    private String id;
    private Meta meta;
    @JsonProperty(required = true)
    private Set<String> schemas;

    protected Resource(){}

    protected Resource(Builder builder) {
        this.id = builder.id;
        this.meta = builder.meta;
        this.schemas = builder.schemas;
    }

    public abstract static class Builder {
        protected String id; // NOSONAR - fields are needed in child classes
        protected Meta meta; // NOSONAR - fields are needed in child classes
        protected Set<String> schemas = new HashSet<>(); // NOSONAR - fields are needed in child classes

        public Builder(Resource resource) {
            if(resource == null){
                throw new IllegalArgumentException("The given ressource can't be null");
            }
            this.id = resource.id;
            this.meta = resource.meta;
            this.schemas = resource.schemas;
        }

        public Builder() {
        }

        public Builder setSchemas(Set<String> schemas) {
            this.schemas = schemas;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setMeta(Meta meta) {
            this.meta = meta;
            return this;
        }

        public abstract <T> T build();
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the value of the meta property.
     *
     * @return possible object is
     *         {@link Meta }
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * Gets the value of the schemas property.
     *
     * @return possible object is
     *         {@link Set<String> }
     */
    public Set<String> getSchemas() {
    	return schemas;
    }
}
