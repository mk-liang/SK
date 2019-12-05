package com.turing.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.turing.bean.Employee;
import com.turing.bean.IdMapping;
import com.turing.bean.Material;
import com.turing.bean.Orders;
import com.turing.bean.Stock;
import com.turing.bean.StockSupplier;
import com.turing.bean.SuppMaterial;
import com.turing.bean.Supplier;
import com.turing.bean.SysUsers;
import com.turing.mapper.EmployeeMapper;
import com.turing.mapper.IdMappingMapper;
import com.turing.mapper.MaterialMapper;
import com.turing.mapper.OrdersMapper;
import com.turing.mapper.StockMapper;
import com.turing.mapper.StockSupplierMapper;
import com.turing.mapper.SupplierMapper;

@Controller
public class StoreController {
	@Autowired
	private OrdersMapper  odao;
	@Autowired
	private EmployeeMapper onedao;
	@Autowired
	private SupplierMapper Smdao;
	@Autowired
	private MaterialMapper mdao;
	@Autowired
	private StockMapper sdao;
	@Autowired
	private StockSupplierMapper ssmdao;
	@Autowired
	private IdMappingMapper idao;
	
	private List<Orders> listid=new  ArrayList<>();
	
	private List<String> mt=new  ArrayList<>();
	
	@PostMapping("/detection")
	@ResponseBody
	public String detection(@RequestParam("id[]") String[] arrs) {
	mt.clear();
		for (int i = 0; i < arrs.length; i++) {
			String id=arrs[i];
           Material m =  mdao.findtype(id);
           mt.add(m.getMaterialType());
		}
		for(int i=0;i<mt.size();i++){
			//轮数
				for(int j=i+1;j<mt.size();j++){
				//比较次数
					if(mt.get(i).equals(mt.get(j))){
					//判断集合元素是否相同，相同则移除。
						mt.remove(j);
						j--;
						//这里减一是因为集合元素移除之后会自动降位。
					}
				}
			}
		if(mt.size()==1) {
			
			return "检验成功";
		}else {
			return "请选择购买同一种类型的物资的需求计划";
		}
		
		
		
	}
	
	
	
	@GetMapping("ToGo")
	public String ToGo(@RequestParam("arrs") int[] arrs,Model model,HttpSession sesson) {
		listid.clear();
		for (int i = 0; i < arrs.length; i++) {
			long id=arrs[i];
			Orders o=odao.selectByPrimaryKey(id);
			listid.add(o);
		}
		//存儲編號
		model.addAttribute("bh",addOrd());
		//获取当前登录员工
		 SysUsers use= (SysUsers) sesson.getAttribute("user");
		 Employee  em= onedao.find(use.getId());
		 model.addAttribute("em",em);
		 sesson.setAttribute("name", em.getEmpName());
		 //获取本地时间
		 Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String time= sdf.format(date);
		model.addAttribute("time",time);
		//获取总金额
		int sum=0;
		for (Orders orders : listid) {
			sum=sum+orders.getSumPrice().intValue();
		}
		model.addAttribute("sum",sum);
		Set<Orders> ts = new HashSet<Orders>(listid);
		listid.clear();
		listid.addAll(ts);
	   sesson.setAttribute("orders", listid);
		model.addAttribute("list",listid);
		return "planman/bianzhicaigoujihua.html";
		
		
	}
	
	public String addOrd() {
		List<Stock> list = sdao.selectByExample(null);

		// 获取编号
		String no = "200";
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String dd = simple.format(new Date());
		StringBuilder s = new StringBuilder(no);

		// 流水号
		String lsh = null;
		String zlsh = null;
		if (list.size() > 0) {
			lsh = list.get(list.size() - 1).getStockNum();
			int n = Integer.parseInt(lsh.substring(11)) + 1;
			if (n < 99999 & n > 9999) {
				zlsh = "" + n;
			} else if (n < 9999 & n > 999) {
				zlsh = "0" + n;
			} else if (n < 999 & n > 99) {
				zlsh = "00" + n;
			} else if (n < 99 & n > 9) {
				zlsh = "000" + n;
			} else {
				zlsh = "0000" + n;
			}

		}

		if (zlsh == null) {
			zlsh = "00001";
		}

		// 新的一年
		String bdd = dd.substring(4);
		if (bdd.equals("0101")) {
			zlsh = "00001";
		}
		// 追加
		s.append(dd);
		s.append(zlsh);

		return s.toString();

	}

	
	@PostMapping("/Toview")
	@ResponseBody
	public long Toview(String mename) {
		Material m=mdao.selectname(mename);
		return m.getId();
	}
	
	
	@GetMapping("/findSup")
	@ResponseBody
	public Map findAll(String id,HttpSession session) {
		System.out.println(id);
		long a=0;
		if(id==null) {
			a=0;
		}else {
			Material m=mdao.findtype1(id);
			System.out.println(m.getId());
			a=m.getId();
		}
		List  list = Smdao.Supplier1(a);
		PageInfo pageinfo = new PageInfo<>(list);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		return data;

	}
	@PostMapping("/edit")
	@ResponseBody
	public String edit(
			String id,String stocknum,String userid,String username,
			String type,String money,String text
			) {
		System.out.println("进入");
		BigDecimal bl=new BigDecimal(money);
		Stock stock=new Stock(id, stocknum, userid, username, type, bl, text);
		int i =sdao.insert(stock);
	    if(i==1) {
		  return "保存成功";
		  
	    }else {
	    	return "保存失败";
	    }
		
	}
	
	@PostMapping("/into")
	@ResponseBody
    public String  toin(@RequestParam("id[]") int[] arr) {
		int j=0;
		List<Stock> tock=sdao.selectByExample(null);
		Stock s=tock.get(tock.size()-1);
		long sid=s.getId();
		for (int i = 0; i < arr.length; i++) {
			 long a=arr[i];
			 IdMapping im= idao.findorid(a);
			 im.setStockId(sid);
			 im.setStatus("C001-30");
			j= idao.updateByPrimaryKeySelective(im);
		}
		if(j==1) {
			  return "保存成功";
			  
		    }else {
		    	return "保存失败";
		    }
		
    }
	
	@PostMapping("/into1")
	@ResponseBody
    public String  toin1(@RequestParam("id") long id,@RequestParam("sid[]") int[] arrs) {
		//获取采购计划最后一条
		List<Stock> tock=sdao.selectByExample(null);
		Stock s=tock.get(tock.size()-1);
		long sid=s.getId();
		int j=0;
		for (int i = 0; i < arrs.length; i++) {
			 long a=arrs[i];
			 IdMapping im= idao.findorid(a);
			 im.setStockId(sid);
			 im.setStatus("C001-30");
			j= idao.updateByPrimaryKeySelective(im);
		}
			StockSupplier  sto=new StockSupplier();
			sto.setStockId(sid);
			sto.setSupplierId(id);
			j=ssmdao.insertSelective(sto);

	   if(j==1) {
		   
		   return "保存成功"; 
	   }else {
		   
		   return "保存失败";
	   }
		
    }
	
   
	
    @GetMapping("/findtock")
	@ResponseBody
	public Map findAll(int page, int rows) {
		// 设置分页的条件
		PageHelper.startPage(page, rows);
		List liston = sdao.findAll();
		PageInfo pageinfo = new PageInfo<>(liston);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		return          data;

	}
    
    @GetMapping("/findtock1")
	@ResponseBody
	public Map findAll1(int page, int rows) {
		// 设置分页的条件
		PageHelper.startPage(page, rows);
		List liston = sdao.findThree();
		PageInfo pageinfo = new PageInfo<>(liston);
		Map data = new HashMap();
		data.put("total", pageinfo.getTotal());
		data.put("rows", pageinfo.getList());
		return          data;

	}
    
    @GetMapping("/findtock2")
   	@ResponseBody
   	public Map findAll2(int page, int rows) {
   		// 设置分页的条件
   		PageHelper.startPage(page, rows);
   		List liston = sdao.findfine();
   		PageInfo pageinfo = new PageInfo<>(liston);
   		Map data = new HashMap();
   		data.put("total", pageinfo.getTotal());
   		data.put("rows", pageinfo.getList());
   		return          data;

   	}
    @GetMapping("/UpSts1")
	public String UpStart1(long id,String s,String name,String nid) {
	 Stock  stock=sdao.selectByPrimaryKey(id);
	 //获取本地时间
	 Date date=new Date();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    stock.setSubmitDate(date);
    stock.setLeader(name);
    stock.setLeaderId(nid);
    stock.setLeadDate(date);
    if(s=="C001-50") {
    	  stock.setLeadAgree("S002-1");
    }else {
    	stock.setLeadAgree("S002-0");
    	
    }
  
    sdao.updateByPrimaryKeySelective(stock);	
		int i=0;
	List<IdMapping> sto= idao.findStock(id);
	for (IdMapping idMapping : sto) {
		idMapping.setStatus(s);
		i=idao.updateByPrimaryKeySelective(idMapping);
	}
	return "planman/Project_list";
		
	}
    
    @PostMapping("/UpSts2")
    @ResponseBody
   	public String UpStart2(long id) {
   		int i=0;
   		
   	List<IdMapping> sto= idao.findStock(id); 	
    Stock  s=sdao.selectByPrimaryKey(id);
	 //获取本地时间
	 Date date=new Date();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    s.setSubmitDate(date);
    sdao.updateByPrimaryKeySelective(s);
   	for (IdMapping idMapping : sto) {
   		idMapping.setStatus("C001-60");
   		i=idao.updateByPrimaryKeySelective(idMapping);
   	}
	  if(i==1) {
		  return "下达成功";
	  }else {
		  return "下达失败";
	  }
   	
   		
   	}
       
    

}
