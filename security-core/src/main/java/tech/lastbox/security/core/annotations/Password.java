package tech.lastbox.security.core.annotations;

import tech.lastbox.security.core.SecurityUtil;

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
