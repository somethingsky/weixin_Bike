package com.example.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//Bike这个类，以后跟mongoDB中的bikes collection关联上了
@Document(collection = "bikes")
public class Bike {
	
	//主键（唯一，建立索引),  id 对应的是MongoDB中的 _id
	@Id
	private String id;
	
	//private double longitude;
	
	//private double latitude;
	
	//表示经纬度的数组[经度，纬度]
	@GeoSpatialIndexed(type=GeoSpatialIndexType.GEO_2DSPHERE)
	private double[] location;
	
	private int status;
	
	
	//建立索引
	@Indexed
	private long bikeno;

	public long getBikeno() {
		return bikeno;
	}

	public void setBikeno(long bikeno) {
		this.bikeno = bikeno;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}**/

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}
	
	
}
