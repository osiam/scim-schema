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
import spock.lang.Unroll

class UserSpec extends Specification {

    static def EXTENSION_URN = 'urn:org.osiam:schemas:test:1.0:Test'
    static def EXTENSION_EMPTY = new Extension(EXTENSION_URN)
    static def CORE_SCHEMA_SET = [Constants.USER_CORE_SCHEMA] as Set

    def 'default constructor should be present due to json mappings'() {
        when:
        def user = new User()
        then:
        user != null
    }

    def 'should contain core schemas as default'() {
        when:
        def user = new User.Builder('username').build()
        then:
        user.schemas == CORE_SCHEMA_SET
    }

    def 'should be possible to create an user without name for PATCH'() {
        when:
        def user = new User.Builder().build()
        then:
        user.schemas == CORE_SCHEMA_SET
        user.name == null
    }


    def 'should be able to contain schemas'() {
        def schemas = ['urn:wtf', 'urn:hajo'] as Set
        when:
        User user = new User.Builder('username').setSchemas(schemas).build()
        then:
        user.schemas == schemas
    }

    @Unroll
    def 'using #parameter for userName raises exception'() {
        when:
        new User.Builder(parameter)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'userName must not be null or empty.'

        where:
        parameter << [null, '']
    }

    def 'should generate a user based on builder'() {
        given:

        Extension extension = getExtension('extension')
        Meta meta = new Meta.Builder().build()
        Address address = getAddress()
        Email email = getEmail()
        Entitlement entitlement = getEntitlement()
        Im im = getIm()
        Name name = getName()
        PhoneNumber phoneNumber = getPhoneNumber()
        Photo photo = getPhoto()
        Role role = getRole()
        X509Certificate x509Certificat = getX509Certificat()

        def builder = new User.Builder('username')
                .setId('id')
                .setActive(true)
                .setAddresses([address] as Set)
                .setDisplayName('displayName')
                .setEmails([email] as Set)
                .setEntitlements([entitlement] as Set)
                .setExternalId('externalId')
                .setIms([im] as Set)
                .setLocale('de_DE')
                .setName(name)
                .setNickName('nickname')
                .setPassword('password')
                .setPhoneNumbers([phoneNumber] as Set)
                .setPhotos([photo] as Set)
                .setPreferredLanguage('german')
                .setProfileUrl('/user/username')
                .setRoles([role] as Set)
                .setTimezone('MEZ')
                .setTitle('title')
                .setSchemas(['schema'] as Set)
                .setX509Certificates([x509Certificat] as Set)
                .setMeta(meta)
                .setUserType('userType')
                .addExtension(extension);

        when:
        User user = builder.build()

        then:
        user.active == builder.active
        user.addresses.first() == address
        user.displayName == 'displayName'
        user.emails == builder.emails
        user.entitlements.first() == entitlement
        user.ims.first() == im
        user.locale == 'de_DE'
        user.name == builder.name
        user.nickName == 'nickname'
        user.password == 'password'
        user.phoneNumbers.first() == phoneNumber
        user.photos.first() == photo
        user.preferredLanguage == 'german'
        user.profileUrl == '/user/username'
        user.roles.first() == role
        user.timezone == 'MEZ'
        user.title == 'title'
        user.userType == 'userType'
        user.x509Certificates.first() == x509Certificat
        user.userName == 'username'
        user.id == 'id'
        user.meta == meta
        user.externalId == 'externalId'
        user.schemas.first() == 'schema'
        user.getAllExtensions().get('extension').getField('gender', ExtensionFieldType.STRING) == extension.getField('gender', ExtensionFieldType.STRING);
    }

    @Unroll
    def 'creating a user with copy builder copies #field field if present'() {
        given:
        User user = new User.Builder('user').build()

        when:
        user[(field)] = value
        def copiedUser = new User.Builder(user).build()

        then:
        copiedUser[(field)] == value

        where:
        field              | value
        'emails'           | [new Email.Builder().build()] as Set
        'phoneNumbers'     | [new PhoneNumber.Builder().build()] as Set
        'ims'              | [new Im.Builder().build()] as Set
        'photos'           | [new Photo.Builder().build()] as Set
        'addresses'        | [new Address.Builder().build()] as Set
        'groups'           | [new GroupRef.Builder().build()] as Set
        'entitlements'     | [new Entitlement.Builder().build()] as Set
        'roles'            | [new Role.Builder().build()] as Set
        'x509Certificates' | [new X509Certificate.Builder().build()] as Set
        'extensions'       | [(EXTENSION_URN): (EXTENSION_EMPTY)]
    }

    @Unroll
    def 'creating a user with copy builder initializes #field empty if missing in original'() {
        given:
        def user = new User.Builder('user').build()

        when:
        user[(field)] = value
        def copiedUser = new User.Builder(user).build()

        then:
        copiedUser[(field)] == value

        where:
        field              | value
        'emails'           | [] as Set
        'phoneNumbers'     | [] as Set
        'ims'              | [] as Set
        'photos'           | [] as Set
        'addresses'        | [] as Set
        'groups'           | [] as Set
        'entitlements'     | [] as Set
        'roles'            | [] as Set
        'x509Certificates' | [] as Set
        'extensions'       | [:]
    }

    def 'enriching extension using the getter raises exception'() {
        given:
        def user = new User.Builder('test2').build()
        
        when:
        user.getAllExtensions().put(EXTENSION_URN, EXTENSION_EMPTY)
        
        then:
        thrown(UnsupportedOperationException)
    }

    def 'builder should add a schema to the schema Set for each added extension'() {
        given:
        def extension1Urn = 'urn:org.osiam:schemas:test:1.0:Test1'
        def extension1 = new Extension(extension1Urn)
        def extension2Urn = 'urn:org.osiam:schemas:test:1.0:Test2'
        def extension2 = new Extension(extension2Urn)
        when:
        def user = new User.Builder('test2')
                .addExtension(extension1)
                .addExtension(extension2)
                .build()
        then:
        user.schemas.contains(extension1Urn)
        user.schemas.contains(extension2Urn)
    }

    def 'scim core schema must always be present in schema set when adding extensions'() {
        given:
        def extension1Urn = 'urn:org.osiam:schemas:test:1.0:Test1'
        def extension1 = new Extension(extension1Urn)
        def extension2Urn = 'urn:org.osiam:schemas:test:1.0:Test2'
        def extension2 = new Extension(extension2Urn)
        def coreSchemaUrn = Constants.USER_CORE_SCHEMA

        when:
        def user = new User.Builder('test2')
                .addExtension(extension1)
                .addExtension(extension2)
                .build()
        then:
        user.schemas.contains(coreSchemaUrn)
    }

    def 'an added extension can be retrieved'() {
        given:
        def user = new User.Builder('test2')
                .addExtension(EXTENSION_EMPTY)
                .build()
        expect:
        user.getExtension(EXTENSION_URN) == EXTENSION_EMPTY
    }

    def 'extensions can be added in bulk'() {
        given:
        def extensions = new HashSet<Extension>()
        extensions.add(EXTENSION_EMPTY)
        def user = new User.Builder('test')
                //.addExtensions([(EXTENSION_URN): EXTENSION_EMPTY])
                .addExtensions(extensions)
                .build()

        expect:
        user.getExtension(EXTENSION_URN) == EXTENSION_EMPTY

    }

    @Unroll
    def 'retrieving extension with #testCase urn raises exception'() {
        given:
        def user = new User.Builder('test2')
                .build()
        when:
        user.getExtension(urn)
        then:
        thrown(expectedException)

        where:
        testCase  | urn           | expectedException
        'null'    | null          | IllegalArgumentException
        'empty'   | ''            | IllegalArgumentException
        'invalid' | EXTENSION_URN | NoSuchElementException
    }

    def 'using the copy-of builder with null as parameter raises exception'() {
        when:
        new User.Builder(null)

        then:
        thrown(IllegalArgumentException)
    }

    Email getEmail() {
        new Email.Builder()
                .setPrimary(true)
                .setValue('test@tarent.de')
                .setType(Email.Type.WORK)
                .build();
    }

    Address getAddress() {
        new Address.Builder()
                .setCountry('Germany')
                .setFormatted('formatted')
                .setLocality('Berlin')
                .setPostalCode('12345')
                .setPrimary(true)
                .setRegion('Berlin')
                .setStreetAddress('Voltastr. 5')
                .setType(Address.Type.WORK)
                .build();
    }

    Extension getExtension(urn) {
        Extension extension = new Extension(urn);
        extension.addOrUpdateField('gender', 'male')
        return extension
    }

    Entitlement getEntitlement() {
        new Entitlement.Builder()
                .setPrimary(true)
                .setType(new Entitlement.Type('irrelevant'))
                .setValue('entitlement')
                .build();
    }

    Im getIm() {
        new Im.Builder()
                .setPrimary(true)
                .setType(Im.Type.AIM)
                .setValue('aim')
                .build();
    }

    Name getName() {
        new Name.Builder()
                .setFamilyName('test')
                .setFormatted('formatted')
                .setGivenName('test')
                .setHonorificPrefix('Dr.')
                .setHonorificSuffix('Mr.')
                .setMiddleName('test').build();
    }

    PhoneNumber getPhoneNumber() {
        new PhoneNumber.Builder()
                .setPrimary(true)
                .setType(PhoneNumber.Type.WORK)
                .setValue('03012345678')
                .build();
    }

    Photo getPhoto() {
        new Photo.Builder()
                .setPrimary(true)
                .setType(Photo.Type.PHOTO)
                .setValue('username.jpg')
                .build();
    }

    Role getRole() {
        new Role.Builder()
                .setPrimary(true)
                .setValue('user_role')
                .build();
    }

    X509Certificate getX509Certificat() {
        new X509Certificate.Builder()
                .setPrimary(true)
                .setValue('x509Certificat')
                .build();
    }
}
