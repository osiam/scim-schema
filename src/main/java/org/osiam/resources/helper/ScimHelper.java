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

import org.osiam.resources.exception.SCIMDataValidationException;
import org.osiam.resources.scim.Email;
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
     * Builds a query string out of the given filter.
     * 
     * @param filter
     *        the general filter of the query
     * @throws UnsupportedEncodingException
     *         if one of the given strings could not be utf8 encoded
     */
    public static String formatQueryString(String filter)
            throws UnsupportedEncodingException {
        return formatQueryString(filter, null, null, null, null, null);
    }

    /**
     * Builds a query string out of the given attributes. If a value is null it will not be part of the query string
     * 
     * @param filter
     *        the general filter of the query
     * @param attributes
     *        all wanted fields of the user or group
     * @param sortBy
     *        a sort by field
     * @param sortOrder
     *        ascending or descending
     * @param count
     *        the results per page
     * @param startIndex
     *        the page start index
     * @return a build query string
     * @throws UnsupportedEncodingException
     *         if one of the given strings could not be utf8 encoded
     * @throws SCIMDataValidationException
     *         if sortOrder is not null and not ascending or descending
     */
    public static String formatQueryString(String filter, String attributes, String sortBy, String sortOrder,
            Integer count, Integer startIndex)
            throws UnsupportedEncodingException {
        StringBuilder queryBuilder = new StringBuilder();
        String utf8Encoded;
        if (!Strings.isNullOrEmpty(filter)) {
            queryBuilder.append("&filter=");
            utf8Encoded = URLEncoder.encode(filter, "UTF-8");
            queryBuilder.append(utf8Encoded);
        }
        if (!Strings.isNullOrEmpty(attributes)) {
            queryBuilder.append("&attributes=");
            utf8Encoded = URLEncoder.encode(attributes, "UTF-8");
            queryBuilder.append(utf8Encoded);
        }
        if (!Strings.isNullOrEmpty(sortBy)) {
            queryBuilder.append("&sortBy=");
            utf8Encoded = URLEncoder.encode(sortBy, "UTF-8");
            queryBuilder.append(utf8Encoded);
        }
        if (!Strings.isNullOrEmpty(sortOrder)) {
            if (!sortOrder.equals("&ascending") && !sortOrder.equals("descending")) {
                throw new SCIMDataValidationException("Can't create a Query String. the given sortOrder is '"
                        + sortOrder + "' and not ascending or descending.");
            }
            queryBuilder.append("&sortOrder=");
            utf8Encoded = URLEncoder.encode(sortOrder, "UTF-8");
            queryBuilder.append(utf8Encoded);
        }
        if (count != null) {
            queryBuilder.append("&count=").append(count.toString());
        }
        if (startIndex != null) {
            queryBuilder.append("&startIndex=").append(startIndex.toString());
        }

        return queryBuilder.toString();
    }
}
