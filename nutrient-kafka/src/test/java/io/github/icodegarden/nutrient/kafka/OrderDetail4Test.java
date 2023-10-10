package io.github.icodegarden.nutrient.kafka;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 
 * @author Fangfang.Xu
 *
 */
public class OrderDetail4Test implements Serializable {
	private static final long serialVersionUID = 1L;

	double amount = 100.5;
	long userId = 1;
	long goodsId = 2;
	String brand = "apple";
	String goodsType = "phone";
	int count = 1;
	ZonedDateTime createdAt = ZonedDateTime.now();
	String orderNum;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "OrderDetail [amount=" + amount + ", userId=" + userId + ", goodsId=" + goodsId + ", brand=" + brand
				+ ", goodsType=" + goodsType + ", count=" + count + ", createdAt=" + createdAt + ", orderNum="
				+ orderNum + "]";
	}

}