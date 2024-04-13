package com.passwordbox.services;

import com.passwordbox.data.models.Passport;
import com.passwordbox.data.models.Vault;
import com.passwordbox.dataTransferObjects.requests.DeletePassportRequest;
import com.passwordbox.dataTransferObjects.requests.SavePassportRequest;
import com.passwordbox.dataTransferObjects.responses.DeletePassportResponse;

public interface PassportService {
    Passport savePassport(SavePassportRequest savePassportRequest);

    DeletePassportResponse deletePassport(DeletePassportRequest deletePassportRequest, Vault vault);
}
