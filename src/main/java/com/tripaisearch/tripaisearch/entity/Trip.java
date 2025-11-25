package com.tripaisearch.tripaisearch.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "trip")
public final class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VENDOR_ID", length = 10)
    private String vendorId;

    @Column(name = "PICKUP_DATETIME", nullable = false)
    private LocalDateTime pickupDatetime;

    @Column(name = "DROPOFF_DATETIME", nullable = false)
    private LocalDateTime dropoffDatetime;

    @Column(name = "PASSENGER_COUNT", nullable = false)
    private Integer passengerCount;

    @Column(name = "TRIP_DISTANCE", nullable = false, precision = 10, scale = 2)
    private BigDecimal tripDistance;

    @Column(name = "RATE_CODE")
    private Integer rateCode;

    @Column(name = "STORE_AND_FWD_FLAG", length = 1)
    private String storeAndFwdFlag;

    @Column(name = "PAYMENT_TYPE", length = 20)
    private String paymentType;

    @Column(name = "FARE_AMOUNT", precision = 10, scale = 2)
    private BigDecimal fareAmount;

    @Column(name = "EXTRA", precision = 10, scale = 2)
    private BigDecimal extra;

    @Column(name = "MTA_TAX", precision = 10, scale = 2)
    private BigDecimal mtaTax;

    @Column(name = "TIP_AMOUNT", precision = 10, scale = 2)
    private BigDecimal tipAmount;

    @Column(name = "TOLLS_AMOUNT", precision = 10, scale = 2)
    private BigDecimal tollsAmount;

    @Column(name = "IMP_SURCHARGE", precision = 10, scale = 2)
    private BigDecimal impSurcharge;

    @Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "PICKUP_LOCATION_ID")
    private Integer pickupLocationId;

    @Column(name = "DROPOFF_LOCATION_ID")
    private Integer dropoffLocationId;

    // Getters e setters
    public Long getId() {
        return id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public LocalDateTime getPickupDatetime() {
        return pickupDatetime;
    }

    public LocalDateTime getDropoffDatetime() {
        return dropoffDatetime;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public BigDecimal getTripDistance() {
        return tripDistance;
    }

    public Integer getRateCode() {
        return rateCode;
    }

    public String getStoreAndFwdFlag() {
        return storeAndFwdFlag;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public BigDecimal getFareAmount() {
        return fareAmount;
    }

    public BigDecimal getExtra() {
        return extra;
    }

    public BigDecimal getMtaTax() {
        return mtaTax;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public BigDecimal getTollsAmount() {
        return tollsAmount;
    }

    public BigDecimal getImpSurcharge() {
        return impSurcharge;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Integer getPickupLocationId() {
        return pickupLocationId;
    }

    public Integer getDropoffLocationId() {
        return dropoffLocationId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public void setPickupDatetime(LocalDateTime pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public void setDropoffDatetime(LocalDateTime dropoffDatetime) {
        this.dropoffDatetime = dropoffDatetime;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public void setTripDistance(BigDecimal tripDistance) {
        this.tripDistance = tripDistance;
    }

    public void setRateCode(Integer rateCode) {
        this.rateCode = rateCode;
    }

    public void setStoreAndFwdFlag(String storeAndFwdFlag) {
        this.storeAndFwdFlag = storeAndFwdFlag;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setFareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
    }

    public void setExtra(BigDecimal extra) {
        this.extra = extra;
    }

    public void setMtaTax(BigDecimal mtaTax) {
        this.mtaTax = mtaTax;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
    }

    public void setTollsAmount(BigDecimal tollsAmount) {
        this.tollsAmount = tollsAmount;
    }

    public void setImpSurcharge(BigDecimal impSurcharge) {
        this.impSurcharge = impSurcharge;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPickupLocationId(Integer pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public void setDropoffLocationId(Integer dropoffLocationId) {
        this.dropoffLocationId = dropoffLocationId;
    }
}
