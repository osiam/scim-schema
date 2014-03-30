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

package org.osiam.resources.helper

import java.util.Formatter.DateTime

import org.osiam.resources.helper.ScimHelper.StringQueryBuilder
import org.osiam.resources.scim.Email
import org.osiam.resources.scim.User

import spock.lang.Specification

import com.google.common.base.Optional

class ScimHelperSpec  extends Specification{

    def 'getSendToEmail returnes the correct email if user has primary email'(){
        given:
        Email email01 = new Email.Builder().setValue('noPrimary@test.com').build()
        Email email02 = new Email.Builder().setValue('primary@test.com').setPrimary(true).build()
        User user = new User.Builder('irrelevant').setEmails([email01, email02] as List).build()
        
        when:
        Optional<Email> sentEmail = ScimHelper.getSendToEmail(user)
        
        then:
        sentEmail.isPresent() == true
        sentEmail.get().getValue() == 'primary@test.com'
    }
    
    def 'getSendToEmail returnes the correct email if user has no primary email'(){
        given:
        Email email01 = new Email.Builder().setValue('noPrimary01@test.com').build()
        Email email02 = new Email.Builder().setValue('noPrimary02@test.com').build()
        User user = new User.Builder('irrelevant').setEmails([email01, email02] as List).build()
        
        when:
        Optional<Email> sentEmail = ScimHelper.getSendToEmail(user)
        
        then:
        sentEmail.isPresent() == true
        sentEmail.get().getValue() == 'noPrimary01@test.com'
    }
    
    def 'stringQueryBuilder works as aspected'(){
        given:
        StringQueryBuilder queryBuilder = new StringQueryBuilder()
        .setFilter('userName eq  \"marissa\"')
        .setSortBy('userName')
        .setCount(50)
        .setSortOrder('ascending')
        .setStartIndex(3)
        String query
        
        when:
        query = queryBuilder.build()
        
        then:
        query == '&filter=userName+eq++%22marissa%22&sortBy=userName&sortOrder=ascending&count=50&startIndex=3'
    }
    
    def 'stringQueryBuilder works as aspected while two attributes are set'(){
        given:
        StringQueryBuilder queryBuilder = new StringQueryBuilder()
        .setFilter('userName eq  \"marissa\"')
        .setCount(50)
        String query
        
        when:
        query = queryBuilder.build()
        
        then:
        query == '&filter=userName+eq++%22marissa%22&count=50'
    }

}
