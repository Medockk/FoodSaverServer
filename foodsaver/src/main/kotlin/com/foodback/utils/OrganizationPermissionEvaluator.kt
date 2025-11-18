package com.foodback.utils

import com.foodback.repository.OrganizationRepository
import com.foodback.repository.UserRepository
import com.foodback.security.auth.UserDetailsImpl
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*

@Component
class OrganizationPermissionEvaluator(
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository
) : PermissionEvaluator {

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

        return when (targetType) {
            EvaluatorTargetType.PRODUCT_TYPE.type -> {
                isMemberOfOrganization(user, organizationId, permission)
            }

            else -> false
        }
    }

    fun isMemberOfOrganization(user: UserDetailsImpl, organizationId: UUID, permission: Any): Boolean {
        if (user.organizationId == null) return false

        return user.organizationId == organizationId && hasPermission(user, permission)
    }

    fun hasPermission(user: UserDetailsImpl, permission: Any): Boolean {
        return user.authorities.any { authority ->
            authority.authority == permission
        }
    }
}