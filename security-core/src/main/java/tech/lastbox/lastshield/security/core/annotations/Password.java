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
 * The {@code Password} annotation is used to mark a field in a user entity class
 * as containing the user's password. It is specifically utilized by the {@link SecurityUtil} class
 * to locate and extract password data from user objects during authentication or authorization processes using reflection.
 *
 * <p>This annotation serves a very specific purpose in the security layer of the application.
 * When applied to a field, it allows {@link SecurityUtil} to identify which field contains the password
 * for the user, enabling secure handling of password data (e.g., encryption, validation)
 * without exposing the entire user object.</p>
 *
 * <p>The annotation is intended to be used on a single field in user-related entities (e.g., User, Admin)
 * to indicate that this field holds sensitive information, ensuring that it is treated appropriately within
 * the context of authentication workflows.</p>
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {}
