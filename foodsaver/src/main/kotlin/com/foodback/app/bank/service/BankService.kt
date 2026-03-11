package com.foodback.app.bank.service

import com.foodback.app.bank.entity.BankEntity
import com.foodback.app.bank.repository.BankRepository
import com.foodback.app.user.repository.UserRepository
import com.foodback.exception.bank.BankException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.random.Random

@Service
class BankService(
    private val bankRepository: BankRepository,
    private val userRepository: UserRepository
) {

    fun getCards(uid: UUID): List<BankEntity> {
        val cards = bankRepository.findAllByUser_Uid(uid)
        return cards
    }

    fun getSelectedCard(uid: UUID): BankEntity? {
        val card = bankRepository.findByUser_UidAndIsSelected(uid)
        return card
    }

    @Transactional
    fun selectedCard(uid: UUID, cardId: UUID): BankEntity {
        val bank = bankRepository.findById(cardId)
            .orElseThrow {
                BankException("Card not found")
            }

        bankRepository.unselectedAllCards(uid)

        bank.isSelected = true

        return bank
    }

    // создать FoodSaverCard
    @Transactional
    fun addCard(uid: UUID, isSelected: Boolean): BankEntity {
        while (true) {
            val cardNumber = generateRandomCardNumber()
            bankRepository.findByCardNumber(cardNumber) ?: run {
                val existingBank = bankRepository.findAllByUser_Uid(uid)
                val user = userRepository.findUserById(uid)
                val bank = bankRepository.save(
                    BankEntity(
                        user = user,
                        cardNumber = cardNumber,
                        isSelected = if (existingBank.isEmpty()) true
                        else isSelected
                    )
                )
                println("Bank is $bank")

                return bank
            }
        }
    }

    @Transactional
    fun deleteCard(uid: UUID, cardId: UUID) {
        try {
            bankRepository.deleteByIdAndUser_Uid(cardId, uid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateRandomCardNumber(): String {
        val first = Random.nextInt(8000, 9000).toString()
        var second = ""
        (1 until 4).forEach { _ ->
            second += Random.nextInt(1000, 10000).toString()
        }

        val number = first + second
        println("Generated card number $number")
        return number
    }
}