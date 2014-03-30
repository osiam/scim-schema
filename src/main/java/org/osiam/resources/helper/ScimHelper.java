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

package org.osiam.resources.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.Email;
import org.osiam.resources.scim.Group;
import org.osiam.resources.scim.User;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * This class is a collection of different helper methods around the scim schema context
 */
public class ScimHelper {

    private ScimHelper() {
    }

    /**
     * try to extract an email from the User. If the User has a primary email address this email will be returned. If
     * not the first email address found will be returned. If no Email has been found email.isPresent() == false
     * 
     * @param user
     *        a {@link User} with a possible email
     * @return an email if found
     */
    public static Optional<Email> getSendToEmail(User user) {
        for (Email email : user.getEmails()) {
            if (email.isPrimary()) {
                return Optional.of(email);
            }
        }

        if (user.getEmails().size() > 0) {
            return Optional.of(user.getEmails().get(0));
        }
        return Optional.absent();
    }
    
    /**
     * formats a given DateTime to the scim needed string form with the pattern 
     * "yyyy-MM-dd'T'HH:mm:ss.SSS"
     * @param dateTime dateTime to be converted
     * @return dateTime as scim conform String
     */
    public static String getScimConformFormatedDateTime(DateTime dateTime){
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return dateFormat.print(dateTime);
    }
    
    /**
     * Builds a query string out of the given attributes. 
     */
    public static class StringQueryBuilder {
        
        private String filter;
        private String attributes;
        private String sortBy;
        private String sortOrder;
        private Integer count;
        private Integer startIndex;
        
        public StringQueryBuilder(){
            
        }
        
        /**
         * the general filter of the query
         * @param filter the filter value
         * @return the Builder itself
         */
        public StringQueryBuilder setFilter(String filter){
            this.filter = filter;
            return this;
        }
        
        /**
         * all wanted fields of the user or group as , separated string
         * @param attributes the attribtue value
         * @return  the Builder itself
         */
        public StringQueryBuilder setAttributes(String attributes){
            this.attributes = attributes;
            return this;
        }
        
        /**
         * the field where the the query should be sorted by
         * @param sortBy the sortBy value
         * @return  the Builder itself
         */
        public StringQueryBuilder setSortBy(String sortBy){
            this.sortBy = sortBy;
            return this;
        }

        /**
         * ascending or descending
         * @param sortOrder ascending or descending
         * @return  the Builder itself
         */
        public StringQueryBuilder setSortOrder(String sortOrder){
            this.sortOrder = sortOrder;
            return this;
        }
        
        /**
         * the number of wanted results per page
         * @param count the count value
         * @return  the Builder itself
         */
        public StringQueryBuilder setCount(int count){
            this.count = count;
            return this;
        }
        
        /**
         * the page start index
         * @param startIndex the startIndex value
         * @return  the Builder itself
         */
        public StringQueryBuilder setStartIndex(int startIndex){
            this.startIndex = startIndex;
            return this;
        }
        
        /**
         * 
         * @return a query string that can be sued to query for {@link User}ser or {@link Group}
         * based on the set values
         */
        public String build() {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder = appendQueryPart(queryBuilder, filter, "filter");
            queryBuilder = appendQueryPart(queryBuilder, attributes, "attributes");
            queryBuilder = appendQueryPart(queryBuilder, sortBy, "sortBy");
            

            if (!Strings.isNullOrEmpty(sortOrder)) {
                if (!sortOrder.equals("ascending") && !sortOrder.equals("descending")) {
                    throw new SCIMDataValidationException("Can't create a Query String. the given sortOrder is '"
                            + sortOrder + "' and not ascending or descending.");
                }
                queryBuilder = appendQueryPart(queryBuilder, sortOrder, "sortOrder");
            }
            if (count != null) {
                queryBuilder.append("&count=").append(count.toString());
            }
            if (startIndex != null) {
                queryBuilder.append("&startIndex=").append(startIndex.toString());
            }

            return queryBuilder.toString();
        }
        
        private StringBuilder appendQueryPart(StringBuilder queryBuilder, String value, String queryPart){
            if (!Strings.isNullOrEmpty(value)) {
                queryBuilder.append("&").append(queryPart).append("=");
                String utf8Encoded = null;
                try {
                    utf8Encoded = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new SCIMDataValidationException("The given " + queryPart + " '" + value + "' could not UTF-8 encoded", e);
                }
                queryBuilder.append(utf8Encoded);
            }
            return queryBuilder;
        }
    }

}


