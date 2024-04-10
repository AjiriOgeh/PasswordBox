package com.passwordbox.data.repositories;

import com.passwordbox.data.models.Vault;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VaultRepositoryTest {

    @Autowired
    VaultRepository vaultRepository;

    @Test
    public void vaultIsCreated_VaultIsSavedTest(){
        Vault vault = new Vault();
        vaultRepository.save(vault);
        assertEquals(1, vaultRepository.count());
    }

}