package com.passwordbox.services;

import com.passwordbox.data.models.CreditCard;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.CreditCardRepository;
import com.passwordbox.dataTransferObjects.requests.DeleteCreditCardRequest;
import com.passwordbox.dataTransferObjects.requests.EditCreditCardRequest;
import com.passwordbox.dataTransferObjects.requests.SaveCreditCardRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteCreditCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.passwordbox.utilities.FindDetails.findCreditCardInVault;
import static com.passwordbox.utilities.Mappers.*;

@Service
public class CreditCardServiceImplementation implements CreditCardService{

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    public CreditCard saveCreditCard(SaveCreditCardRequest saveCreditCardRequest) {
        CreditCard creditCard = saveCreditCardRequestMap(saveCreditCardRequest);
        creditCardRepository.save(creditCard);
        return creditCard;
    }

    @Override
    public CreditCard editCreditCard(EditCreditCardRequest editCreditCardRequest, Vault vault) {
        CreditCard creditCard = findCreditCardInVault(editCreditCardRequest.getTitle().toLowerCase(), vault);
        CreditCard updatedCreditCard = editCreditCardRequestMap(editCreditCardRequest, creditCard);
        creditCardRepository.save(updatedCreditCard);
        return updatedCreditCard;
    }

    @Override
    public DeleteCreditCardResponse deleteCreditCard(DeleteCreditCardRequest deleteCreditCardRequest, Vault vault) {
        CreditCard creditCard = findCreditCardInVault(deleteCreditCardRequest.getTitle(), vault);
        DeleteCreditCardResponse deleteCreditCardResponse = deleteCreditCardResponseMap(creditCard);
        creditCardRepository.delete(creditCard);
        return deleteCreditCardResponse;
    }
}