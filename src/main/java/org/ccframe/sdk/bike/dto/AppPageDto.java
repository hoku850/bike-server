package org.ccframe.sdk.bike.dto;

import java.util.ArrayList;
import java.util.List;

public class AppPageDto {
	private String amount = "";
	private String deposit = "";
	private String ifChargeDeposit = "";
	private String result = "";
	private List<String> bikeLocationList = new ArrayList<String>();
	private String list = "";
	private String km = "";
	private String min = "";
	private String payMoney = "";
	private String startPos = "";
	private String endPos = "";
	private String bikeNumber = "";
	private String firstPos = "";
	private String polylinePath = "";
	private String burnCalories = "";
	private String reduceEmissions = "";
	
	
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getKm() {
		return km;
	}
	public void setKm(String km) {
		this.km = km;
	}
	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}
	public String getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}
	public String getStartPos() {
		return startPos;
	}
	public void setStartPos(String startPos) {
		this.startPos = startPos;
	}
	public String getEndPos() {
		return endPos;
	}
	public void setEndPos(String endPos) {
		this.endPos = endPos;
	}
	public String getBikeNumber() {
		return bikeNumber;
	}
	public void setBikeNumber(String bikeNumber) {
		this.bikeNumber = bikeNumber;
	}
	public String getFirstPos() {
		return firstPos;
	}
	public void setFirstPos(String firstPos) {
		this.firstPos = firstPos;
	}
	public String getPolylinePath() {
		return polylinePath;
	}
	public void setPolylinePath(String polylinePath) {
		this.polylinePath = polylinePath;
	}
	public String getBurnCalories() {
		return burnCalories;
	}
	public void setBurnCalories(String burnCalories) {
		this.burnCalories = burnCalories;
	}
	public String getReduceEmissions() {
		return reduceEmissions;
	}
	public void setReduceEmissions(String reduceEmissions) {
		this.reduceEmissions = reduceEmissions;
	}

	public List<String> getBikeLocationList() {
		return bikeLocationList;
	}
	public void setBikeLocationList(List<String> bikeLocationList) {
		this.bikeLocationList = bikeLocationList;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getIfChargeDeposit() {
		return ifChargeDeposit;
	}
	public void setIfChargeDeposit(String ifChargeDeposit) {
		this.ifChargeDeposit = ifChargeDeposit;
	}
	
	
}
