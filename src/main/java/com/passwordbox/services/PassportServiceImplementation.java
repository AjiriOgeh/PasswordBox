package com.passwordbox.services;

import com.passwordbox.data.models.Passport;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.PassportRepository;
import com.passwordbox.dataTransferObjects.requests.DeletePassportRequest;
import com.passwordbox.dataTransferObjects.requests.SavePassportRequest;
import com.passwordbox.dataTransferObjects.responses.DeletePassportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.passwordbox.utilities.FindDetails.findPassportInVault;
import static com.passwordbox.utilities.Mappers.deletePassportResponseMap;
import static com.passwordbox.utilities.Mappers.savePassportRequestMap;

@Service
public class PassportServiceImplementation implements PassportService{

    @Autowired
    private PassportRepository passportRepository;

    @Override
    public Passport savePassport(SavePassportRequest savePassportRequest) {
        Passport passport = savePassportRequestMap(savePassportRequest);
        passportRepository.save(passport);
        return passport;
    }

    @Override
    public DeletePassportResponse deletePassport(DeletePassportRequest deletePassportRequest, Vault vault) {
        Passport passport = findPassportInVault(deletePassportRequest.getTitle(), vault);
        DeletePassportResponse deletePassportResponse = deletePassportResponseMap(passport);
        passportRepository.delete(passport);
        return deletePassportResponse;
    }
}
