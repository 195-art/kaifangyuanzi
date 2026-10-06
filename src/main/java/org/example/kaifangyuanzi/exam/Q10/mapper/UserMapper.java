package org.example.kaifangyuanzi.exam.Q10.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;
import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;

@Mapper
public interface UserMapper {
    @Select("SELECT id,username,password,register_date FROM sys_user WHERE username = #{username}")
    SysUser selectByUsername(String username);

    @Insert("INSERT INTO sys_user (username, password) VALUES (#{username}, #{password})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertUser(SysUser sysUser);

    @Update("UPDATE sys_user SET password = #{password} WHERE id = #{id} AND password = #{oldPassword}")
    int updatePassword(@Param("id") Long id, @Param("oldPassword") String oldPassword, @Param("password") String password);

}
