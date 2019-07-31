package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.example.pojo.User;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@Override
	public boolean sendMsg(String countryCode, String phoneNum) {

		boolean flag = true;
		// 调用腾讯云短信APi
		int appid = Integer.parseInt(stringRedisTemplate.opsForValue().get("appid"));
		String appkey = stringRedisTemplate.opsForValue().get("appkey");

		// 生成随机的数字(4位)
		String code = (int) ((Math.random() * 9 + 1) * 1000) + "";
		SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		try {
			
			// 向对应手机发送验证码
			ssender.send(0, countryCode, phoneNum, "您的登录验证码为" + code, "", "");
			
			// 将发送的手机号作为key，验证码作为value保存到Redis中
			stringRedisTemplate.opsForValue().set(phoneNum,code,300, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			flag=false;
			e.printStackTrace();
		}
		

		return flag;
	}
	
	@Override
	public boolean verify(String phoneNum, String verifyCode) {
		boolean flag=false;
		//调用RedisTemplate，根据手机号的key，查找对应的验证码
		//Redis中保存的真实验证码
		String code=stringRedisTemplate.opsForValue().get(phoneNum);
		//将用户传入的验证码跟实际的验证码进行比对
		if(code!=null && code.equals(verifyCode)) {
			flag=true;
		}
		return flag;
		
	}

	@Override
	public void register(User user) {
		// 调用mongodb的dao，将用户数据保存起来
		
		mongoTemplate.insert(user);
	}

	@Override
	public void update(User user) {
		
		
		// 如果对应ID的数据不存在，就插入，存在就更新
		//根据用户的手机号，对数据进行更新，但是因为手机号不是主键_id
		//mongoTemplate.insert(user);
		//更新用户对应的属性
		// 更新对应用户的属性
        Update update = new Update();
        if (user.getDeposite() != null) {
            update.set("deposite", user.getDeposite());
        }
        if (user.getStatus() != null) {
            update.set("status", user.getStatus());
        }
        if (user.getName() != null) {
            update.set("name", user.getName());
        }
        if (user.getIdNum() != null) {
            update.set("idNum", user.getIdNum());
        }
		mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())), 
				update, User.class);
	}
	
	
	
	

}
