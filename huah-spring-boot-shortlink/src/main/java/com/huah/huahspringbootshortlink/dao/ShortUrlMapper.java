package com.huah.huahspringbootshortlink.dao;

import com.huah.huahspringbootshortlink.entity.ShortUrl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShortUrlMapper {

    // 使用 ShardingSphere 注解（或 XML）
    @Insert("INSERT INTO short_urls (id, code, origin_url, expire_at) " +
            "VALUES (#{id}, #{code}, #{originUrl}, #{expireAt})")
    void insert(ShortUrl url);

    @Select("SELECT id, code, origin_url, expire_at FROM short_urls WHERE code = #{code}")
    ShortUrl selectByCode(@Param("code") String code);
}