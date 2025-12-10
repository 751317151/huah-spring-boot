package com.huah.huahspringbootlangchain4j.langchain4j.consultant.service;

import com.huah.huahspringbootlangchain4j.langchain4j.consultant.pojo.Reservation;
import com.huah.huahspringbootlangchain4j.langchain4j.consultant.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    //1.添加预约信息的方法
    public void insert(Reservation reservation) {
        reservationMapper.insert(reservation);
    }

    //2.查询预约信息的方法(根据手机号查询)
    public Reservation findByPhone(String phone) {
        return reservationMapper.findByPhone(phone);
    }
}
