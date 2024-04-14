package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.LoginInfoRepository;
import com.passwordbox.dataTransferObjects.requests.DeleteLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.EditLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.SaveNewLoginInfoRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.passwordbox.utilities.FindDetails.findLoginInfoInVault;
import static com.passwordbox.utilities.Mappers.*;
import static com.passwordbox.utilities.ValidateInputs.validateLoginInfoTitle;

@Service
public class LoginInfoServiceImplementation implements LoginInfoService {

    @Autowired
    private LoginInfoRepository loginInfoRepository;
    @Override
    public LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault) throws Exception {
        validateLoginInfoTitle(saveNewLoginInfoRequest.getTitle(), vault);
        LoginInfo loginInfo = saveNewLoginInfoRequestMap(saveNewLoginInfoRequest);
        loginInfoRepository.save(loginInfo);
        return loginInfo;
    }

    @Override
    public LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault) throws Exception {
        LoginInfo loginInfo = findLoginInfoInVault(editLoginInfoRequest.getTitle().toLowerCase(), vault);
        validateLoginInfoTitle(editLoginInfoRequest.getEditedTitle(), vault);
        LoginInfo updatedLoginInfo = editLoginInfoRequestMap(editLoginInfoRequest, loginInfo);
        loginInfoRepository.save(updatedLoginInfo);
        return updatedLoginInfo;
    }

    @Override
    public DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = findLoginInfoInVault(deleteLoginInfoRequest.getTitle().toLowerCase(), vault);
        DeleteLoginInfoResponse deleteLoginInfoResponse = deleteLoginInfoResponseMap(loginInfo);
        loginInfoRepository.delete(loginInfo);
        return deleteLoginInfoResponse;
    }

}
