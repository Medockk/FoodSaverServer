package com.foodback.app.bank.controller

import com.foodback.app.bank.dto.response.v1.BankResponseDtoV1
import com.foodback.app.bank.mapper.BankMappersV1
import com.foodback.app.bank.service.BankService
import com.foodback.security.auth.UserDetailsImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/bank")
class BankControllerV1(
    private val bankService: BankService,
    private val bankMappersV1: BankMappersV1
) {

    @GetMapping("/all")
    fun getCards(@AuthenticationPrincipal principal: UserDetailsImpl): ResponseEntity<List<BankResponseDtoV1>> {
        val cards = bankService.getCards(principal.uid)
        return if (cards.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(cards.map { bankMappersV1.mapToResponse(it) })
    }

    @GetMapping("/selected")
    fun getSelectedCard(@AuthenticationPrincipal principal: UserDetailsImpl): ResponseEntity<BankResponseDtoV1> {
        val card = bankService.getSelectedCard(principal.uid)
        return if (card == null) ResponseEntity.noContent().build()
        else ResponseEntity.ok(bankMappersV1.mapToResponse(card))
    }

    @PostMapping("/add")
    fun addCard(
        @AuthenticationPrincipal principal: UserDetailsImpl,
        @RequestParam(required = false, defaultValue = "false")
        isSelected: Boolean = false
    ): ResponseEntity<BankResponseDtoV1> {
        val bankEntity = bankService.addCard(principal.uid, isSelected)

        return ResponseEntity.ok(bankMappersV1.mapToResponse(bankEntity))
    }

    @DeleteMapping("/delete")
    fun deleteCard(@AuthenticationPrincipal principal: UserDetailsImpl, @RequestParam cardId: UUID) {
        bankService.deleteCard(principal.uid, cardId)
    }

    @PutMapping("/select")
    fun selectCard(@AuthenticationPrincipal principal: UserDetailsImpl, @RequestParam cardId: UUID): ResponseEntity<BankResponseDtoV1> {
        val bankEntity = bankService.selectedCard(principal.uid, cardId)
        return ResponseEntity.ok(bankMappersV1.mapToResponse(bankEntity))
    }
}