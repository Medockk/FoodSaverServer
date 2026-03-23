package com.foodback.utils

import com.foodback.security.auth.UserDetailsImpl
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

/**
 * Special class to process permission evaluator.
 * This class check: can user do CRUD operation.
 * If user belong to an organization, to user can do CRUD-operation with current product.
 * Otherwise, user receive 403 Http Status code (Forbidden)
 */
@Component
class OrganizationPermissionEvaluator() : PermissionEvaluator {

    override fun hasPermission(
        authentication: Authentication,
        targetDomainObject: Any,
        permission: Any
    ): Boolean {
        return false
    }

    override fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        targetType: String,
        permission: Any
    ): Boolean {

        val user = authentication.principal as? UserDetailsImpl ?: return false
        val organizationId = user.organizationId ?: return false

        val isMemberOfOrganization = when (targetType) {
            EvaluatorTargetType.PRODUCT_TYPE_DELETE.type,
            EvaluatorTargetType.PRODUCT_TYPE_UPDATE.type -> {
                isMemberOfOrganization(user, organizationId, permission)
            }

            EvaluatorTargetType.PRODUCT_TYPE_ADD.type -> {
                hasPermission(user, permission)
            }

            else -> false
        }

        return isMemberOfOrganization
    }

    /**
     * Method check: user belongs to an organization or not
     * @return True, if user belongs to an organization, otherwise false
     */
    fun isMemberOfOrganization(user: UserDetailsImpl, organizationId: UUID, permission: Any): Boolean {
        if (user.organizationId == null) return false

        return user.organizationId == organizationId && hasPermission(user, permission)
    }

    /**
     * Method to check: have current user permission or not
     * @return True, if user have permission, otherwise false
     */
    private fun hasPermission(user: UserDetailsImpl, permission: Any): Boolean {
        return user.authorities.any { authority ->
            authority.authority == permission
        }
    }
}