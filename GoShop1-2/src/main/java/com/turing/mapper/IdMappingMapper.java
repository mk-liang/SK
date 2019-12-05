package com.turing.mapper;

import com.turing.bean.IdMapping;
import com.turing.bean.IdMappingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdMappingMapper {
    long countByExample(IdMappingExample example);

    int deleteByExample(IdMappingExample example);

    int deleteByPrimaryKey(Long id);

    int insert(IdMapping record);
    int upsta(@Param("num") long num);

    int insertSelective(IdMapping record);

    List<IdMapping> selectByExample(IdMappingExample example);
    

    IdMapping selectByPrimaryKey(Long id);
    
    IdMapping findorid(@Param("num") long num);
    List<IdMapping> findStock(@Param("num") long num);

    int updateByExampleSelective(@Param("record") IdMapping record, @Param("example") IdMappingExample example);

    int updateByExample(@Param("record") IdMapping record, @Param("example") IdMappingExample example);

    int updateByPrimaryKeySelective(IdMapping record);

    int updateByPrimaryKey(IdMapping record);
    
    int Del(@Param("id") long id);
}