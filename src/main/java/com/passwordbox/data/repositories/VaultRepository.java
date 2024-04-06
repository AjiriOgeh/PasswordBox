package com.passwordbox.data.repositories;

import com.passwordbox.data.models.Vault;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VaultRepository extends MongoRepository<Vault, String> {
}
