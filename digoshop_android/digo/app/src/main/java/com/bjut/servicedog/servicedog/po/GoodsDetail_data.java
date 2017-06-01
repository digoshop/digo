package com.bjut.servicedog.servicedog.po;

/**
 * Created by apple02 on 16/7/14.
 */
public class GoodsDetail_data {
    private Product product;
    private Target target;
    private int residue;
    private int targetType;
    private int integral;
    private Product exchangeProductInfo;
    private Product productInfoDto;
    private Product auctionProductInfo;

    public Product getProductInfoDto() {
        return productInfoDto;
    }

    public void setProductInfoDto(Product productInfoDto) {
        this.productInfoDto = productInfoDto;
    }

    public Product getAuctionProductInfo() {
        return auctionProductInfo;
    }

    public void setAuctionProductInfo(Product auctionProductInfo) {
        this.auctionProductInfo = auctionProductInfo;
    }

    public Product getExchangeProductInfo() {
        return exchangeProductInfo;
    }

    public void setExchangeProductInfo(Product exchangeProductInfo) {
        this.exchangeProductInfo = exchangeProductInfo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public int getResidue() {
        return residue;
    }

    public void setResidue(int residue) {
        this.residue = residue;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }
}
