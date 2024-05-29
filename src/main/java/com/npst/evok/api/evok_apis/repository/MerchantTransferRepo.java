package com.npst.evok.api.evok_apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npst.evok.api.evok_apis.entity.MerchantTransfer;
@Repository
public interface MerchantTransferRepo extends JpaRepository<MerchantTransfer, Integer>{

}
