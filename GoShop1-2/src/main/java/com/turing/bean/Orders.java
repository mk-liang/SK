package com.turing.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Orders {
    private Long id;

    private String orderNum;

    private String orderSource;

    private String materialCode;

    private String materialName;

    private String amount;

    private String measureUnit;

    private BigDecimal unitPrice;

    private BigDecimal sumPrice;

    private String startDate;

    private String endDate;

    private String address;

    private String authorId;

    private String author;

    private String phone;

    private String fax;

    private String email;

    private String remark;
    
    private IdMapping  mapping;
    

    public IdMapping getMapping() {
		return mapping;
	}

	public void setMapping(IdMapping mapping) {
		this.mapping = mapping;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(BigDecimal sumPrice) {
        this.sumPrice = sumPrice;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String starttime) {
        this.startDate = starttime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String time) {
        this.endDate = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    //equals重写
 	@Override
 	public boolean equals(Object obj) {
 		Orders o=(Orders) obj;
 		boolean falg=false;
 		if(o.getMaterialName().equals(materialName)) {
 		    //数量相加
            int i=Integer.parseInt(o.getAmount())+Integer.parseInt(amount);
            //新集合设置数量
            o.setAmount(i+"");
            //新集合设置价格
            int  sum=o.getSumPrice().intValue()+sumPrice.intValue();
            BigDecimal j=new BigDecimal(sum);
            o.setSumPrice(j);
            falg=true;
 		}
 		return falg;
 	}
 
    //hashCode重写
    @Override
    public int hashCode() {
	  String in=materialName;
	  return in.hashCode();
    }
    
    @Override
	public String toString() {
		return "Orders [id=" + id + ", orderNum=" + orderNum + ", orderSource=" + orderSource + ", materialCode="
				+ materialCode + ", materialName=" + materialName + ", amount=" + amount + ", measureUnit="
				+ measureUnit + ", unitPrice=" + unitPrice + ", sumPrice=" + sumPrice + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", address=" + address + ", authorId=" + authorId + ", author=" + author
				+ ", phone=" + phone + ", fax=" + fax + ", email=" + email + ", remark=" + remark + ", mapping="
				+ mapping + "]";
	}
}