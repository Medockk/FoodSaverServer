package com.foodback.service

import com.foodback.dto.request.payment.PaymentRequestModel
import com.foodback.dto.response.payment.PaymentResponseModel
import com.foodback.entity.PaymentMethodEntity
import com.foodback.exception.auth.UserException
import com.foodback.exception.general.ErrorCode.RequestError
import com.foodback.exception.payment.PaymentMethodException
import com.foodback.mappers.PaymentMethodMapper
import com.foodback.repository.PaymentMethodRepository
import com.foodback.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import kotlin.jvm.optionals.getOrElse

@Service
class PaymentMethodService(
    private val paymentMethodRepository: PaymentMethodRepository,
    private val userRepository: UserRepository,
    private val paymentMethodMapper: PaymentMethodMapper
) {

    @Transactional
    fun getAllPaymentMethod(user: UUID): List<PaymentResponseModel> {
        val methods = paymentMethodRepository.findAllByUser_Uid(user)
            .getOrElse { emptyList() }

        println(methods)
        return paymentMethodMapper.toResponseModel(methods)
    }

    @Transactional
    fun getCurrentPaymentMethod(user: UUID): PaymentResponseModel? {
        val method = paymentMethodRepository.findByIsSelectedAndUser_uid(true, user)
            .getOrElse { null }

        return method?.let { paymentMethodMapper.toResponseModel(it) }
    }

    @Transactional
    fun addPaymentMethod(
        userUid: UUID,
        request: PaymentRequestModel
    ): PaymentResponseModel {
        val user = userRepository.findById(userUid)
            .orElseThrow { UserException("User not found!", RequestError.UserRequest.USER_NOT_FOUND) }

        if (request.isSelected) {
            paymentMethodRepository.disableCurrentPaymentMethod(userUid)
        }

        val paymentMethodEntity = PaymentMethodEntity(
            user = user,
            bank = request.bank,
            cardNumber = request.cardNumber,
            isSelected = request.isSelected
        )
        val savedEntity = paymentMethodRepository.save(paymentMethodEntity)
        return paymentMethodMapper.toResponseModel(savedEntity)
    }

    @Transactional
    fun removePaymentMethod(methodId: UUID) {
        try {
            paymentMethodRepository.deleteById(methodId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}