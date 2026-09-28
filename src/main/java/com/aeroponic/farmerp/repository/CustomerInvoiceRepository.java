package com.aeroponic.farmerp.repository;

import com.aeroponic.farmerp.model.CustomerInvoice;
import com.aeroponic.farmerp.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {
    List<CustomerInvoice> findByPaidStatus(PaymentStatus paidStatus);
}
