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
 * The {@code Username} annotation is used to mark a field in a user entity
 * class that represents the username of the user.
 * This annotation helps identify the field that stores the user's login
 * identifier, typically used during the authentication process.
 *
 * <p>When applied to a field, the {@code Username} annotation signifies
 * that the field holds the user's username, which may be used to locate the
 * user in the system, validate login credentials, or associate the user with
 * various security-related processes.
 *
 * <p>This annotation is primarily used by the {@link SecurityUtil} class
 * to reflectively identify the field that contains the username in the
 * user entity, ensuring that the correct information is used when
 * authenticating users or fetching user details from the system.
 *
 * <p>It is expected that the annotated field will hold a unique, user-specific
 * identifier, often required during login or user lookup in the system.
 *
 * @see SecurityUtil
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {}
