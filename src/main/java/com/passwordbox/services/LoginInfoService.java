package com.passwordbox.services;

import com.passwordbox.data.models.LoginInfo;
import com.passwordbox.data.models.Vault;
import com.passwordbox.dataTransferObjects.requests.DeleteLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.EditLoginInfoRequest;
import com.passwordbox.dataTransferObjects.requests.SaveNewLoginInfoRequest;
import com.passwordbox.dataTransferObjects.responses.DeleteLoginInfoResponse;

public interface LoginInfoService {
    LoginInfo saveNewLoginInfo(SaveNewLoginInfoRequest saveNewLoginInfoRequest, Vault vault);

    LoginInfo editLoginInfo(EditLoginInfoRequest editLoginInfoRequest, Vault vault);

    DeleteLoginInfoResponse deleteLoginInfo(DeleteLoginInfoRequest deleteLoginInfoRequest, Vault vault);
}
