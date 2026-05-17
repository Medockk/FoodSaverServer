package com.foodback.feature.users.impl.mapper

import com.foodback.core.coreSecurity.api.dto.UserRole
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.impl.entity.UserEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Named

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal interface ProfileMapper {

    @Mapping(target = "id", source = "uid")
    @Mapping(target = "authorities", source = "roles", qualifiedByName = ["rolesToAuthorities"])
    fun toResponse(entity: UserEntity): ProfileResponse

    @Named(value = "rolesToAuthorities")
    fun rolesToAuthorities(roles: MutableList<UserRole>): List<String> {
        val authorities = roles.flatMap { role ->
            role.getAllAuthorities()
        }.mapNotNull {
            val authority = it.authority
                ?: return@mapNotNull null
            try {
                val role = UserRole.valueOf(authority)
                "ROLE_${role.name}"
            } catch (_: Exception) {
                authority
            }
        }

        return authorities
    }
}