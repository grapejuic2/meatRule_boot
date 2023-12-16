package com.project.gogi.admin.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.project.gogi.admin.order.service.AdminOrderService;
import com.project.gogi.common.base.BaseController;
import com.project.gogi.vo.OrderVO;

@Controller("adminOrderController")
@RequestMapping(value="/admin/order")
public class AdminOrderControllerImpl extends BaseController implements AdminOrderController{
	@Autowired
	private AdminOrderService adminOrderService;
	
	@Override
	@RequestMapping(value="/adminOrderMain.do" ,method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView adminOrderMain(@RequestParam Map<String, String> dateMap,
			                          HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);

		//페이징
		String sectionStr = dateMap.get("section");
		String pageNumStr = dateMap.get("pageNum");
		//section과 pageNum에 기본값 1 설정
		int section = (sectionStr != null && !sectionStr.isEmpty()) ? Integer.parseInt(sectionStr) : 1;
		int pageNum = (pageNumStr != null && !pageNumStr.isEmpty()) ? Integer.parseInt(pageNumStr) : 1;
		//한 페이지에 보여질 글 개수
		int pageSize = 10;
		// offset 계산
	    int offset = (pageNum - 1) * pageSize;
		int total=adminOrderService.orderCount();
		// totalPageCount 계산
		int totalPageCount = (int) Math.ceil((double) total / pageSize);
		System.out.println("#######totalPageCount:"+total+"#######totalPageCount:"+totalPageCount);
		
		HashMap<String,Object> condMap=new HashMap<String,Object>();
		
		condMap.put("pageNum",pageNum);
		condMap.put("offset",offset);
		condMap.put("pageSize",pageSize);
		List<OrderVO> newOrderList=adminOrderService.listNewOrder(condMap);
		mav.addObject("newOrderList",newOrderList);
		mav.addObject("totalPageCount", totalPageCount);
		mav.addObject("section", section);
		mav.addObject("pageNum", pageNum);
		mav.addObject("total",total);
		return mav;
		
	}
	
	@Override
	@RequestMapping(value="/modifyDeliveryState.do" ,method={RequestMethod.POST})
	public ResponseEntity modifyDeliveryState(@RequestParam Map<String, String> deliveryMap, 
			                        		  HttpServletRequest request, HttpServletResponse response) throws Exception {
		adminOrderService.modifyDeliveryState(deliveryMap);
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		message  = "mod_success";
		resEntity =new ResponseEntity(message, responseHeaders, HttpStatus.OK);
		return resEntity;
	}
	
	@Override
	@RequestMapping(value="/orderDetail.do" ,method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView orderDetail(@RequestParam("order_id") int order_id, 
			                        HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		Map orderMap =adminOrderService.orderDetail(order_id);
		mav.addObject("orderMap", orderMap);
		return mav;
	}
	
}