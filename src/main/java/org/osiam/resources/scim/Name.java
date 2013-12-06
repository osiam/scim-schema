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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class represents the User's real name.
 *
 * <p>
 * For more detailed information please look at the <a
 * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
 * </p>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class Name {

    private String formatted;
    private String familyName;
    private String givenName;
    private String middleName;
    private String honorificPrefix;
    private String honorificSuffix;

    /**
     * needed for json serializing
     */
    private Name() {
    }

    private Name(Builder builder) {
        this.formatted = builder.formatted;
        this.familyName = builder.familyName;
        this.givenName = builder.givenName;
        this.middleName = builder.middleName;
        this.honorificPrefix = builder.honorificPrefix;
        this.honorificSuffix = builder.honorificSuffix;

    }

    /**
     * Gets the full name, including all middle names, titles, and suffixes as appropriate, formatted for display.
     *
     * <p>
     * For more detailed information please look at the <a
     * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
     * </p>
     *
     * @return the formatted name
     */
    public String getFormatted() {
        return formatted;
    }

    /**
     * Gets the family name of the User.
     *
     * <p>
     * For more detailed information please look at the <a
     * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
     * </p>
     *
     * @return the fami8ly name
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Gets the given (first) name of the User.
     *
     * <p>
     * For more detailed information please look at the <a
     * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
     * </p>
     *
     * @return the given name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Gets the middle name(s) of the User.
     *
     * <p>
     * For more detailed information please look at the <a
     * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
     * </p>
     *
     * @return the middle name
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Gets the honorific prefix(es) of the User.
     *
     * <p>
     * For more detailed information please look at the <a
     * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
     * </p>
     *
     * @return the honorific prefix
     */
    public String getHonorificPrefix() {
        return honorificPrefix;
    }

    /**
     * Gets the honorific suffix(es) of the User.
     *
     * <p>
     * For more detailed information please look at the <a
     * href="http://tools.ietf.org/html/draft-ietf-scim-core-schema-02#section-6">SCIM core schema 2.0, section 6</a>
     * </p>
     *
     * @return the honorific sufix
     */
    public String getHonorificSuffix() {
        return honorificSuffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Name other = (Name) o;

        if (familyName != null ? !familyName.equals(other.familyName) : other.familyName != null) {
            return false;
        }
        if (formatted != null ? !formatted.equals(other.formatted) : other.formatted != null) {
            return false;
        }
        if (givenName != null ? !givenName.equals(other.givenName) : other.givenName != null) {
            return false;
        }
        if (honorificPrefix != null ? !honorificPrefix.equals(other.honorificPrefix) : other.honorificPrefix != null) {
            return false;
        }
        if (honorificSuffix != null ? !honorificSuffix.equals(other.honorificSuffix) : other.honorificSuffix != null) {
            return false;
        }
        if (middleName != null ? !middleName.equals(other.middleName) : other.middleName != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
        result = prime * result + ((formatted == null) ? 0 : formatted.hashCode());
        result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
        result = prime * result + ((honorificPrefix == null) ? 0 : honorificPrefix.hashCode());
        result = prime * result + ((honorificSuffix == null) ? 0 : honorificSuffix.hashCode());
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
        return result;
    }

    /**
     * Builder class that is used to build {@link Name} instances
     */
    public static class Builder {
        private String formatted;
        private String familyName;
        private String givenName;
        private String middleName;
        private String honorificPrefix;
        private String honorificSuffix;

        /**
         * Sets the full name (See {@link Name#getFormatted()}).
         *
         * @param formatted
         *            the formatted name
         * @return the builder itself
         */
        public Builder setFormatted(String formatted) {
            this.formatted = formatted;
            return this;
        }

        /**
         * Sets the family name of the User (See {@link Name#getFamilyName()}).
         *
         * @param familyName
         *            the family name
         * @return the builder itself
         */
        public Builder setFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        /**
         * Sets the given name of the User (See {@link Name#getGivenName()}).
         *
         * @param givenName
         *            the given name
         * @return the builder itself
         */
        public Builder setGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        /**
         * Sets the middle name(s) of the User (See {@link Name#getMiddleName()}).
         *
         * @param middleName
         *            the middle name
         * @return the builder itself
         */
        public Builder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        /**
         * Sets the honorific prefix(es) of the User (See {@link Name#getHonorificPrefix()}).
         *
         * @param honorificPrefix
         *            the honorific prefix
         * @return the builder itself
         */
        public Builder setHonorificPrefix(String honorificPrefix) {
            this.honorificPrefix = honorificPrefix;
            return this;
        }

        /**
         * Sets the honorific suffix(es) of the User (See {@link Name#getHonorificSuffix()}).
         *
         * @param honorificSuffix
         *            the honorific suffix
         * @return the builder itself
         */
        public Builder setHonorificSuffix(String honorificSuffix) {
            this.honorificSuffix = honorificSuffix;
            return this;
        }

        /**
         * Builds a Name Object with the given parameters
         *
         * @return The complete Name Object
         */
        public Name build() {
            return new Name(this);
        }
    }
}
