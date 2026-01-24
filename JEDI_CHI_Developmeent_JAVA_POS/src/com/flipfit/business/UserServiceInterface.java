package com.flipfit.business;

/**
 * Service for user authentication and role resolution.
 */
public interface UserServiceInterface {
    /**
     * Resolve user by identifier (email or adminId) and password.
     * @return "ADMIN", "OWNER", or "CUSTOMER" if valid; null otherwise
     */
    String authenticate(String identifier, String password);

    /**
     * Get the entity id after successful login (adminId, owner email, or customer email).
     */
    String getLoggedInUserId();
}
