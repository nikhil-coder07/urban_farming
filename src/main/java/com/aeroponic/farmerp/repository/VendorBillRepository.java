package com.aeroponic.farmerp.repository;

import com.aeroponic.farmerp.model.PaymentStatus;
import com.aeroponic.farmerp.model.VendorBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorBillRepository extends JpaRepository<VendorBill, Long> {
    List<VendorBill> findByPaidStatus(PaymentStatus paidStatus);
}
