package tech.lastbox.security.core.annotations;

import tech.lastbox.security.core.SecurityUtil;

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
 * user-related security operations.</p>
 *
 * <p>This annotation is used by the {@link SecurityUtil} class to dynamically
 * locate and instantiate the appropriate user handler for operations related
 * to user authentication.</p>
 *
 * <p>The annotation can be applied to either a class (e.g., a user repository
 * or service), enabling flexible design patterns such as dependency injection
 * and component discovery in the context of security.</p>
 *
 * @see SecurityUtil
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserHandler {}
