package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Vault;
import com.passwordbox.dataTransferObjects.requests.DeleteLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.EditLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.SaveNewLoginInfoRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;

public interface LoginInfoService {
    LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault) throws Exception;

    LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault) throws Exception;

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault);
}
