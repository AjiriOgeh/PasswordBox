package com.passwordbox.utilities;

import com.passwordbox.data.models.*;
import com.passwordbox.exceptions.CreditCardNotFoundException;
import com.passwordbox.exceptions.LoginInfoNotFoundException;
import com.passwordbox.exceptions.NoteNotFoundException;

public class FindDetails {

    public static LoginInfo findLoginInfoInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getLoginInfos().size(); count++){
            if (vault.getLoginInfos().get(count).getTitle().equals(title))
                return vault.getLoginInfos().get(count);
        }
        throw new LoginInfoNotFoundException("Login Info does not Exist. Please Try Again");
    }

    public static Note findNoteInVault(String title, Vault vault) {
        for(int count = 0; count < vault.getNotes().size(); count++){
            if (vault.getNotes().get(count).getTitle().equals(title))
                return vault.getNotes().get(count);
        }
        throw new NoteNotFoundException("Note does not Exist. Please Try Again");
    }

    public static CreditCard findCreditCardInVault(String title, Vault vault) {
        for (int count = 0; count < vault.getCreditCards().size(); count++) {
            if (vault.getCreditCards().get(count).getTitle().equals(title))
                return vault.getCreditCards().get(count);
        }
        throw new CreditCardNotFoundException("CreditCard does not Exist. Please Try Again");
    }




}
