package com.aeroponic.farmerp.dto;

import java.math.BigDecimal;
import java.util.List;

public class BalanceSheetReportDTO {
    private List<AccountBalanceDTO> assets;
    private BigDecimal totalAssets;
    private List<AccountBalanceDTO> liabilities;
    private BigDecimal totalLiabilities;
    private BigDecimal netPosition;

    public BalanceSheetReportDTO() {}

    public BalanceSheetReportDTO(List<AccountBalanceDTO> assets, BigDecimal totalAssets, List<AccountBalanceDTO> liabilities, BigDecimal totalLiabilities, BigDecimal netPosition) {
        this.assets = assets;
        this.totalAssets = totalAssets;
        this.liabilities = liabilities;
        this.totalLiabilities = totalLiabilities;
        this.netPosition = netPosition;
    }

    public List<AccountBalanceDTO> getAssets() { return assets; }
    public void setAssets(List<AccountBalanceDTO> assets) { this.assets = assets; }
    public BigDecimal getTotalAssets() { return totalAssets; }
    public void setTotalAssets(BigDecimal totalAssets) { this.totalAssets = totalAssets; }
    public List<AccountBalanceDTO> getLiabilities() { return liabilities; }
    public void setLiabilities(List<AccountBalanceDTO> liabilities) { this.liabilities = liabilities; }
    public BigDecimal getTotalLiabilities() { return totalLiabilities; }
    public void setTotalLiabilities(BigDecimal totalLiabilities) { this.totalLiabilities = totalLiabilities; }
    public BigDecimal getNetPosition() { return netPosition; }
    public void setNetPosition(BigDecimal netPosition) { this.netPosition = netPosition; }
}
