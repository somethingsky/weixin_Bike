//导包
var myUtils=require("../../utils/myUtils.js")

Page({
  data: {
    log: 0,
    lat: 0,
    controls:[],
    markers:[]
  },
  /*
  *首次加载页面调用
  */
  onLoad: function () {
    var that = this;  // 拿到当前页面的一个对象Page
    wx.getLocation({
      success: function(res) {
        var longitude=res.longitude
        var latitude=res.latitude
        that.setData({
          log: longitude,
          lat: latitude
        })
        // 查找单车信息
        findBikes(longitude, latitude, that);
      },
    })
    wx.getSystemInfo({
      success: function(res) {
        var windowWidth=res.windowWidth;
        var windowHeight=res.windowHeight;
        that.setData({
          controls: [
            {
               // 扫码按钮
              id: 1,
              iconPath: '/images/qrcode.png',
              position: {
                width: 100,
                heigth: 40,
                left: windowWidth / 2 -50,
                top: windowHeight-60
              },
              //是否可点击
              clickable:true
            },
            {
              // 定位按钮
              id: 2,
              iconPath: '/images/img1.png',
              position: {
                width: 40,
                height: 40,
                left: 10,
                top: windowHeight - 60
              },
              clickable: true
            },
            {
              // 中心点位置
              id: 3,
              iconPath: '/images/location.png',
              position: {
                width: 20,
                height: 35,
                left: windowWidth / 2 - 10,
                top: windowHeight / 2 - 40
              },
              clickable: true
            },
            {
              // 充值按钮
              id: 4,
              iconPath: '/images/pay.png',
              position: {
                width: 40,
                height: 40,
                left: windowWidth - 45,
                top: windowHeight - 100
              },
              clickable: true
            },
            {
              // 报修
              id: 5,
              iconPath: "/images/warn.png",
              position: {
                width: 35,
                height: 35,
                left: windowWidth - 42,
                top: windowHeight - 60
              },
              clickable: true
            },
            {
              // 添加单车
              id: 6,
              iconPath: "/images/add.png",
              position: {
                width: 35,
                height: 35
              },
              clickable: true
            }
          ]
        })
      },
    })

    
  },
  /**
   * 点击控件时触发
   */
  controltap:function(e){
    var that=this;
    //获取控件ID
    var cid=e.controlId;
    switch(cid){
      case 1: { // 扫码按钮

      var status=myUtils.get("status");
        // 根据用户的状态跳转到对应的页面
        // 如果是0，跳转到手机注册页面
        if (status == 0) {
          wx.navigateTo({
            url: '../register/register',
          })
        }
        // 如果是1，跳转到押金页面
        else if (status == 1) {
          wx.navigateTo({
            url: '../deposite/deposite',
          })
        }
        // 如果是2，跳转到认证页面
        else if (status == 2) {
          wx.navigateTo({
            url: '../identify/identify',
          })
        }
        break;
      }
      case 2: {// 定位按钮
        this.mapCtx.moveToLocation();
        break;
      }
      case 6: {// 添加车辆控件被点击
        // 获取当前已有的车辆
        var bikes=that.data.markers;
        this.mapCtx.getCenterLocation({
          success:function(res){
            var log=res.longitude;
            var lat=res.latitude;
            
            //将添加的单车数据发送到后台(SpringBoot)
            wx.request({
              url: "http://localhost:8080/bike/add",
              data:{
                //longitude:log,
                //latitude:lat,
                location:[log,lat],
                bikeno:100010,
                status:0
              },
              method:"POST",
              success:function(res){
                //查找单车，然后将单车显示在页面上
                findBikes(log, lat, that);
              }
            })
          }
        });
        break;
      }
    }
  },
  /**
   * 视野发生变化时触发
   */
  regionchange: function (e) {

    var that = this;
    // 获取移动后的位置
    var etype = e.type;
    if (e.type == 'end') {
      // 获取当前地图中心的经纬度
      this.mapCtx.getCenterLocation({
        success: function (res) {
          var longitude = res.longitude;
          var latitude = res.latitude;
          findBikes(longitude, latitude, that);
        }
      })
    }
  
  },


  /**
   * 首次渲染完成时调用
   */
  onReady: function () {
    // 创建map上下文:记录地图信息，方便回到原来位置
    this.mapCtx = wx.createMapContext('myMap'); // 这里的'myMap'要和wxml里map的id一样
  },
})




// 自定义方法
function findBikes(longitude, latitude, that) {
  wx.request({
    url: 'http://localhost:8080/bike/findNear',
    method: 'GET',
    data: {
      longitude: longitude,
      latitude: latitude
    },
    success: function (res) {
      var bikes = res.data.map((geoResult)=>{
        return {
          longitude: geoResult.content.location[0],
          latitude: geoResult.content.location[1],
          iconPath: '/images/bike.png',
          width: 35,
          height: 40,
          id: geoResult.content.id
        }
      })
      // 将bike数组set到当前页面的marker
      that.setData({
        markers: bikes
      })
    }
  })
}


