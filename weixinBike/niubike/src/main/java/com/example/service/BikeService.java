package com.example.service;

import java.util.List;

import org.springframework.data.geo.GeoResult;

import com.example.pojo.Bike;


public interface BikeService {

	public void save(Bike bike);

	public List<GeoResult<Bike>> findNear(double longtitude, double latitude);

}
