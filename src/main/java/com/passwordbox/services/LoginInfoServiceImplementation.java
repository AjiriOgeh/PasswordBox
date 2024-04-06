package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Vault;
import com.passwordbox.data.repositories.LoginInfoRepository;
import com.passwordbox.dataTransferObjects.requests.DeleteLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.EditLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.SaveNewLoginInfoRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;
import com.passwordbox.exceptions.LoginInfoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.passwordbox.utilities.Mappers.*;
import static com.passwordbox.utilities.ValidateInputs.validateLoginInfoTitle;

@Service
public class LoginInfoServiceImplementation implements LoginInfoService {

    @Autowired
    private LoginInfoRepository loginInfoRepository;
    @Override
    public LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault) {
        validateLoginInfoTitle(saveNewLoginInfoRequest.getTitle(), vault);
        LoginInfo loginInfo = saveNewLoginInfoRequestMap(saveNewLoginInfoRequest);
        loginInfoRepository.save(loginInfo);
        return loginInfo;
    }

    @Override
    public LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault) {
        //refactor this part of the code make sure it matches existing object(The title)
        // change the validateLoginInformation
        LoginInfo loginInfo = findLoginInformationInVault(editLoginInfoRequest.getTitle(), vault);
        validateLoginInfoTitle(editLoginInfoRequest.getEditedTitle(), vault);
        editLoginInfoRequestMap(editLoginInfoRequest, loginInfo);
        loginInfoRepository.save(loginInfo);
        return loginInfo;

    }

    @Override
    public DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault) {
        LoginInfo loginInfo = findLoginInformationInVault(deleteLoginInfoRequest.getTitle(), vault);
        DeleteLoginInfoResponse deleteLoginInfoResponse = deleteLoginInfoResponseMap(loginInfo);
        loginInfoRepository.delete(loginInfo);
        return deleteLoginInfoResponse;
    }


    private LoginInfo findLoginInformationInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getLoginInfos().size(); count++){
            if (vault.getLoginInfos().get(count).getTitle().equals(title))
                return vault.getLoginInfos().get(count);
        }
        throw new LoginInfoNotFoundException("Login Info does not Exist. Please Try Again");
    }

}
