package com.turing.mapper;

import com.turing.bean.Orders;
import com.turing.bean.OrdersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrdersMapper {
    long countByExample(OrdersExample example);

    int deleteByExample(OrdersExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Orders record);

    int insertSelective(Orders record);

    List<Orders> selectByExample(OrdersExample example);
    

    Orders selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Orders record, @Param("example") OrdersExample example);

    int updateByExample(@Param("record") Orders record, @Param("example") OrdersExample example);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);
    
    Orders selnum(@Param("num") String  num );
    
    List<Orders> selcoud(@Param("num") String  num);
    
    List<Orders> findid(@Param("num") long  num);
    
     List<Orders> selAll();
   
    List<Orders> selectAll(@Param("materialCode")String materialCode,@Param("materialName")String materialName,@Param("status")String status);
    
}