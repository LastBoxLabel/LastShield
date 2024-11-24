package tech.lastbox.security.core.annotations;

import tech.lastbox.security.core.SecurityUtil;

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
 * various security-related processes.</p>
 *
 * <p>This annotation is primarily used by the {@link SecurityUtil} class
 * to reflectively identify the field that contains the username in the
 * user entity, ensuring that the correct information is used when
 * authenticating users or fetching user details from the system.</p>
 *
 * <p>It is expected that the annotated field will hold a unique, user-specific
 * identifier, often required during login or user lookup in the system.</p>
 *
 * @see SecurityUtil
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {}
