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

package org.osiam.resources.scim

import spock.lang.Specification

class GroupSpec extends Specification {

    def 'should be able to generate a group'() {
        given:
        
        User user = new User.Builder('userName').build()
        
        MemberRef memberRef = new MemberRef.Builder(user).build()
        
        Meta meta = new Meta.Builder().build()
        
        Group.Builder builder = new Group.Builder('display')
                .setExternalId('externalId')
                .setId('id')
                .setMeta(meta)
                .setSchemas(['schema'] as Set)
                .setMembers([memberRef] as Set)

        when:
        Group group = builder.build()

        then:
        group.members.iterator().next().value == memberRef.value
        group.displayName == 'display'
        group.externalId == 'externalId'
        group.schemas.first() == 'schema'
        group.id == 'id'
    }

    def 'should contain empty public constructor for json serializing'() {
        when:
        Group result = new Group()
        then:
        result
    }

    def 'should be able to clone a group'() {
        given:
        Group group = new Group.Builder('display').
                setId('id').build()
        when:
        Group result = new Group.Builder(group).build()

        then:
        group.displayName == result.displayName
        group.members == result.members
        group.id == result.id
    }

    def 'using the copy-of builder with null as parameter raises exception'() {
        when:
        new Group.Builder(null)

        then:
        thrown(IllegalArgumentException)
    }

}
