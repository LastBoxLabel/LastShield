/*
 * Copyright 2024 LastBox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.lastbox.lastshield.security.core.annotations;

import tech.lastbox.lastshield.security.core.SecurityUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code UserHandler} annotation is used to mark a class or method
 * as a handler for user-related operations within the security context.
 * It is intended to identify specific components responsible for managing
 * user entities (such as user repositories or services) in a security-focused
 * application.
 *
 * <p>When applied to a class, this annotation indicates that the
 * associated class is responsible for handling user data, including
 * actions like fetching user information, authenticating users, or managing
 * user-related security operations.
 *
 * <p>This annotation is used by the {@link SecurityUtil} class to dynamically
 * locate and instantiate the appropriate user handler for operations related
 * to user authentication.
 *
 * <p>The annotation can be applied to either a class (e.g., a user repository
 * or service), enabling flexible design patterns such as dependency injection
 * and component discovery in the context of security.
 *
 * @see SecurityUtil
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserHandler {}
