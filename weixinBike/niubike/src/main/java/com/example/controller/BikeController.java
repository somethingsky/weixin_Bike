package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pojo.Bike;
import com.example.service.BikeService;


/**
 * 标记这个类是一个用于接收请求和响应用户的一个控制器
 * 加是@Controller注解后，Spring容器就对他实例化
 * @author li'zhe'lei
 *
 */
@RestController
@RequestMapping(value = {"/bike"})
public class BikeController {
    @Autowired
    private BikeService bikeService;

    @PostMapping(value = {"/add"})
    public String addBike(@RequestBody Bike bike) {
        bikeService.save(bike);
        return "success";
    }

    @GetMapping(value = {"/findNear"})
    public List<GeoResult<Bike>> findNear(double longitude, double latitude) {
        return bikeService.findNear(longitude, latitude);
    }
}


