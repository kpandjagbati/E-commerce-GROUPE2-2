package com.esgis2026.assigame.security;

import com.esgis2026.assigame.entity.Utilisateur;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AccessDeniedException("Utilisateur non authentifie");
        }

        return userDetails;
    }

    public static Utilisateur getCurrentUtilisateur() {
        return getCurrentUserDetails().getUtilisateur();
    }

    public static boolean isAdmin() {
        return getCurrentUserDetails().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public static boolean isVendeur() {
        return getCurrentUserDetails().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_VENDEUR"));
    }
}
