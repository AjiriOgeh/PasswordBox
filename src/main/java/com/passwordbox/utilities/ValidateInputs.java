package com.passwordbox.utilities;

import com.passwordbox.data.models.Vault;

public class ValidateInputs {

    public static  void validateLoginInfoTitle(String title, Vault vault) {
        if (title == null) throw new IllegalArgumentException("Title field cannot be null. Please Enter a valid title.");
        if (title.isEmpty()) throw new IllegalArgumentException("Title field cannot be empty. Please Enter a valid title.");
        if (doesLoginInfoTitleExist(title, vault)) throw new IllegalArgumentException("Title Already Exists. Please Enter a different title");
    }

    private static boolean doesLoginInfoTitleExist(String title, Vault vault) {
        for(int count = 0; count < vault.getLoginInfos().size(); count++){
            if (vault.getLoginInfos().get(count).getTitle().equals(title))
                return true;
        }
        return false;
    }

    public static void validateNoteTitle(String title, Vault vault) {
        if (title == null) throw new IllegalArgumentException("Title field cannot be null. Please Enter a valid title.");
        if (title.isEmpty()) throw new IllegalArgumentException("Title field cannot be empty. Please Enter a valid title.");
        if (doesNoteTitleExist(title, vault)) throw new IllegalArgumentException("Title Already Exists. Please Enter a different title");
    }

    private static boolean doesNoteTitleExist(String title, Vault vault) {
        for(int count = 0; count < vault.getNotes().size(); count++){
            if (vault.getNotes().get(count).getTitle().equals(title))
                return true;
        }
        return false;
    }
}
