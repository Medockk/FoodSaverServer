package com.foodback.feature.users.impl.mapper

import com.foodback.core.coreMedia.api.service.MediaUriMapperService
import com.foodback.core.coreSecurity.api.dto.UserRole
import com.foodback.feature.users.api.dto.proifle.CreateProfileRequest
import com.foodback.feature.users.api.dto.proifle.ProfileResponse
import com.foodback.feature.users.impl.entity.AuthEntity
import com.foodback.feature.users.impl.entity.ProfileEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Autowired

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
internal abstract class ProfileMapper {

    @Autowired
    lateinit var mediaUriMapperService: MediaUriMapperService

    @Mapping(target = "id", source = "profile.userId")
    @Mapping(target = "fullName", source = "profile.fullName")
    @Mapping(target = "email", source = "auth.email")
    @Mapping(target = "restaurantId", source = "auth.restaurantId")
    @Mapping(target = "authorities", source = "auth.roles", qualifiedByName = ["rolesToAuthorities"])
    @Mapping(target = "imageUri", source = "profile.imageUri", qualifiedByName = ["mapUri"])
    abstract fun toResponse(profile: ProfileEntity, auth: AuthEntity): ProfileResponse

    @Mapping(target = "currentAddressId", ignore = true)
    @Mapping(target = "currentPaymentMethodId", ignore = true)
    @Mapping(target = "addressIds", ignore = true)
    abstract fun toEntity(request: CreateProfileRequest): ProfileEntity

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

    @Named("mapUri")
    fun relativeUriToAbsoluteUri(uri: String?): String? {
        val absoluteUris = uri?.let { uri ->
            if (uri.startsWith("http")) {
                uri
            } else {
                mediaUriMapperService.toAbsoluteUri(uri)
            }
        }

        return absoluteUris
    }
}