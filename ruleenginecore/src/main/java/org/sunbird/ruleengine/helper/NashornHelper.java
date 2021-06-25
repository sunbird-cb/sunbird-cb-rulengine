package org.sunbird.ruleengine.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.service.GenericCron;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NashornHelper {
	private static final Logger logger = LogManager.getLogger(NashornHelper.class);
	static List<String[]> list = new ArrayList<>();
	{
		list.add(new String[] { "1233444", "442", "3","fordate", "1", "2" });
		//list.add(new String[] { "1233444", "442", "3","fordate", "2", "2" });
		//list.add(new String[] { "1233445", "442", "3","fordate", "2", "2" });
	}
    static String  three="function converter(holder, object)\r\n" + 
    		"{\r\n" + 
    		"return holder.configs['1'].currentResponse;\r\n" + 
    		"}";
    static String javascript = "function converter(holder, myCode) {return  myCode.configs['0'].values['2'].Response.Result} ";
	static String javascript2="	function converter(holder, myCode) {\r\n" + 
			"	        var parentDict = {};\r\n" + 
			"	        var ArrayList = Java.type('java.util.ArrayList');\r\n" + 
			"	        for (i = 0; i < myCode.length; i++) {\r\n" + 
			"	            if (parentDict[myCode[i][0]] === undefined) {\r\n" + 
			"	                parentDict[myCode[i][0]] = [];\r\n" + 
			"	                parentDict[myCode[i][0]].main=myCode[i];\r\n" + 
			"	                parentDict[myCode[i][0]].skus=new ArrayList();\r\n" + 
			"	                \r\n" + 
			"	            }\r\n" + 
			"	            var skus = [];\r\n" + 
			"	            skus[0]=myCode[i][7];//sku\r\n" + 
			"	            skus[1]=myCode[i][8];// quantity\r\n" + 
			"	            skus[2]=myCode[i][11];// discount\r\n" + 
			"	            skus[3]=myCode[i][14];//amount\r\n" + 
			"	            skus[4]=myCode[i][10];//unit price\r\n" + 
			"	            parentDict[myCode[i][0]].skus.add(skus);\r\n" + 
			"	        }\r\n" + 
			"	        var object = new ArrayList();;\r\n" + 
			"	        for (var key in parentDict) {\r\n" + 
			"	            object.add(parentDict[key]);\r\n" + 
			"	        }\r\n" + 
			"	        return object;\r\n" + 
			"	    }";
	static String javascript3="	function converter(holder, myCode) {\r\n" + 
			"	        var parentDict = {};\r\n" + 
			"myCode= myCode['env:Body'][0].OutputParameters[0].P_PO_DETAILS_TBL[0].P_PO_DETAILS_TBL_ITEM;" +
			"	        var ArrayList = Java.type('java.util.ArrayList');\r\n" + 
			"	        for (i = 0; i < myCode.length; i++) {\r\n" + 
			"		        //if (myCode[i][28] != 'S1') {\r\n" + 
			"		            if (parentDict[myCode[i].PO_NUMBER] === undefined) {\r\n" + 
			"		                parentDict[myCode[i].PO_NUMBER] = [];\r\n" + 
			"		                parentDict[myCode[i].PO_NUMBER].main=myCode[i];\r\n" + 
			"		                parentDict[myCode[i].PO_NUMBER].itemList=new ArrayList();\r\n" + 
			"		                \r\n" + 
			"		            }\r\n" + 
			"		            var item = new ArrayList();\r\n" + 
			"		            item.add(myCode[i].CONTACT);\r\n" + 
			"		            parentDict[myCode[i].PO_NUMBER].itemList.add(item);\r\n" + 
			"		        //}\r\n" + 
			"	        }\r\n" + 
			"	        var object = new ArrayList();;\r\n" + 
			"	        for (var key in parentDict) {\r\n" + 
			"		            object.add(parentDict[key]);\r\n" + 
			"	        }\r\n" + 
			"	        return object;\r\n" + 
			"	    }\r\n" + 
			"	    ";

	static String javascript4="	function converter(holder, myCode) {\r\n" + 
			"	        var parentDict = {};\r\n" + 
			"	        \r\n" + 
			"	        if( myCode['env:Body'][0].OutputParameters[0].P_PRDNOTE_DETAILS_TBL.size()==0)\r\n" + 
			"	        return null;\r\n" + 
			"	                    \r\n" + 
			"	   myCode=    myCode['env:Body'][0].OutputParameters[0].P_PRDNOTE_DETAILS_TBL[0].P_PRDNOTE_DETAILS_TBL_ITEM;\r\n" + 
			"	        var ArrayList = Java.type('java.util.ArrayList');\r\n" + 
			"	        for (i = 0; i < myCode.length; i++) {\r\n" + 
			"		        \r\n" + 
			"		            if (parentDict[myCode[i].JOB_NAME[0]] === undefined) {\r\n" + 
			"		                parentDict[myCode[i].JOB_NAME[0]] = [];\r\n" + 
			"		                var headerObject={};\r\n" + 
			"						headerObject[\"BUSINESS_UNIT\"]=myCode[i].BUSINESS_UNIT;\r\n" + 
			"						headerObject[\"JOB_NAME\"]=myCode[i].JOB_NAME;\r\n" + 
			"						headerObject[\"DATE_LAST_MOVED\"]=myCode[i].DATE_LAST_MOVED;\r\n" + 
			"		                parentDict[myCode[i].JOB_NAME[0]].header=headerObject;\r\n" + 
			"		                parentDict[myCode[i].JOB_NAME[0]].itemList=new ArrayList();\r\n" + 
			"		                \r\n" + 
			"		            }\r\n" + 
			"		            var item = new ArrayList();\r\n" + 
			"		            var obj={};\r\n" + 
			"		            obj[\"BUSINESS_UNIT\"]=myCode[i].BUSINESS_UNIT;\r\n" + 
			"					obj[\"ORGANIZATION_ID\"]=myCode[i].ORGANIZATION_ID;\r\n" + 
			"					obj[\"WIP_ENTITY_ID\"]=myCode[i].WIP_ENTITY_ID;\r\n" + 
			"					obj[\"ITEM_CODE\"]=myCode[i].ITEM_CODE;\r\n" + 
			"					obj[\"DESCRIPTION\"]=myCode[i].DESCRIPTION;\r\n" + 
			"					obj[\"DATE_LAST_MOVED\"]=myCode[i].DATE_LAST_MOVED;\r\n" + 
			"					obj[\"OPERATION_SEQ_NUM\"]=myCode[i].OPERATION_SEQ_NUM;\r\n" + 
			"					obj[\"AVAILABLE_QUANTITY\"]=myCode[i].AVAILABLE_QUANTITY;\r\n" + 
			"					\r\n" + 
			"							\r\n" + 
			"		            \r\n" + 
			"		            item.add(obj);\r\n" + 
			"		           \r\n" + 
			"		            parentDict[myCode[i].JOB_NAME[0]].itemList.add(item);\r\n" + 
			"		     \r\n" + 
			"	        }\r\n" + 
			"	        var object = new ArrayList();;\r\n" + 
			"	        for (var key in parentDict) {\r\n" + 
			"		            object.add(parentDict[key]);\r\n" + 
			"	        }\r\n" + 
			"	        return object;\r\n" + 
			"	    }\r\n" + 
			"	    \r\n" + 
			"	    \r\n" + 
			"\r\n" + 
			"";
	static String javascript5="function converter(holder, myCode) {\r\n" + 
			" var ArrayList = Java.type('java.util.ArrayList');\r\n" + 
			" var list = new ArrayList();\r\n" + 
			"  if(myCode.result!=true)\r\n" + 
			"  {\r\n" + 
			"  return null;\r\n" + 
			"  }\r\n" + 
			" for (i = 0; i < myCode.Outlets.length; i++) {\r\n" + 
			" \r\n" + 
			"var Outlet = myCode.Outlets[i]; \r\n" + 
			"var outletMaster = {};\r\n" + 
			"outletMaster.id = Outlet.id;\r\n" + 
			"if(Outlet.name.length)\r\n" + 
			"{\r\n" + 
			"outletMaster.name = Outlet.name.replace(/[<>&'\"]/g, function (c) {\r\n" + 
			"switch (c) {\r\n" + 
			"case '<': return '&lt;';\r\n" + 
			"case '>': return '&gt;';\r\n" + 
			"case '&': return '&amp;';\r\n" + 
			"case '\\'': return '&apos;';\r\n" + 
			"case '\"': return '&quot;';\r\n" + 
			"}\r\n" + 
			"});\r\n" + 
			"}\r\n" + 
			"if(Outlet.address.length)\r\n" + 
			"{\r\n" + 
			"            outletMaster.address = Outlet.address.replace(/[<>&'\"]/g, function (c) {\r\n" + 
			"switch (c) {\r\n" + 
			"case '<': return '&lt;';\r\n" + 
			"case '>': return '&gt;';\r\n" + 
			"case '&': return '&amp;';\r\n" + 
			"case '\\'': return '&apos;';\r\n" + 
			"case '\"': return '&quot;';\r\n" + 
			"}\r\n" + 
			"});\r\n" + 
			"};\r\n" + 
			"            outletMaster.keycustomeracc = Outlet.keycustomeracc;\r\n" + 
			"            outletMaster.phoneno = Outlet.phoneno;\r\n" + 
			"            outletMaster.email = Outlet.email;\r\n" + 
			"            outletMaster.ownername = Outlet.ownername;\r\n" + 
			"            outletMaster.ownermobile = Outlet.ownermobile;\r\n" + 
			"            outletMaster.taxno = Outlet.taxno;\r\n" + 
			"            outletMaster.cstno = Outlet.cstno;\r\n" + 
			"            outletMaster.isdistributor = Outlet.isdistributor;\r\n" + 
			"if(Outlet.type.length)\r\n" + 
			"{\r\n" + 
			" outletMaster.type = Outlet.type.replace(/[<>&'\"]/g, function (c) {\r\n" + 
			"switch (c) {\r\n" + 
			"case '<': return '&lt;';\r\n" + 
			"case '>': return '&gt;';\r\n" + 
			"case '&': return '&amp;';\r\n" + 
			"case '\\'': return '&apos;';\r\n" + 
			"case '\"': return '&quot;';\r\n" + 
			"}\r\n" + 
			"});\r\n" + 
			"}\r\n" + 
			"         outletMaster.outlet_level = Outlet.outlet_level;\r\n" + 
			"         outletMaster.longitude = Outlet.longitude;\r\n" + 
			"         outletMaster.latitude = Outlet.latitude;\r\n" + 
			"         outletMaster.created = Outlet.created;\r\n" + 
			"         outletMaster.modified = Outlet.modified;\r\n" + 
			"         outletMaster.approved = Outlet.approved;\r\n" + 
			"         outletMaster.code = Outlet.code;\r\n" + 
			"         outletMaster.pin = Outlet.pin;\r\n" + 
			"         outletMaster.createdbyuser_id = Outlet.createdbyuser_id;\r\n" + 
			"         outletMaster.outletstate_id = Outlet.outletstate_id;\r\n" + 
			"         outletMaster.discount_category_id = Outlet.discount_category_id;\r\n" + 
			"         outletMaster.erp_id = Outlet.erp_id;\r\n" + 
			"         outletMaster.panno = Outlet.panno;\r\n" + 
			"         outletMaster.user_id = Outlet.user_id;\r\n" + 
			"         outletMaster.headquarter_id = Outlet.headquarter_id;\r\n" + 
			"         outletMaster.headquarter = Outlet.headquarter;\r\n" + 
			"         outletMaster.outletcategory = Outlet.outletcategory;\r\n" + 
			"         outletMaster.beatname = Outlet.beatname;\r\n" + 
			"         outletMaster.beatid = Outlet.beatid;\r\n" + 
			"         outletMaster.city = Outlet.city;\r\n" + 
			"         outletMaster.state = Outlet.state;\r\n" + 
			"         outletMaster.area = Outlet.area;\r\n" + 
			"         outletMaster.areaId = Outlet.areaId;\r\n" + 
			"         outletMaster.warehouseId = Outlet.warehouseId;\r\n" + 
			"         outletMaster.warehouse_erpid = Outlet.warehouse_erpid;\r\n" + 
			"		 if(Outlet.warehouse.length)\r\n" + 
			"		 {\r\n" + 
			"         outletMaster.warehouse = Outlet.warehouse.replace(/[<>&'\"]/g, function (c) {\r\n" + 
			"switch (c) {\r\n" + 
			"case '<': return '&lt;';\r\n" + 
			"case '>': return '&gt;';\r\n" + 
			"case '&': return '&amp;';\r\n" + 
			"case '\\'': return '&apos;';\r\n" + 
			"case '\"': return '&quot;';\r\n" + 
			"}\r\n" + 
			"});\r\n" + 
			"};\r\n" + 
			"         outletMaster.zone = Outlet.zone;\r\n" + 
			"         outletMaster.zoneId = Outlet.zoneId;\r\n" + 
			"         outletMaster.balance = Outlet.balance;\r\n" + 
			"         outletMaster.credit_limit = Outlet.credit_limit ;\r\n" + 
			"         outletMaster.credit_days = Outlet.credit_days;\r\n" + 
			"         outletMaster.DiscountCategoryName = Outlet.DiscountCategoryName;\r\n" + 
			"\r\n" + 
			"list.add(outletMaster);\r\n" + 
			"}\r\n" + 
			"return list;\r\n" + 
			"}";
	
	Object process(String code, Object configObject, Object object) {
		ScriptEngineManager engineManager = new ScriptEngineManager(null);
		ScriptEngine engine = engineManager.getEngineByName("nashorn");
		Bindings bindings = new SimpleBindings();
		bindings.put("context", object);
		engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		Object result = null;
		try {
			engine.eval(code);
			result = ((Invocable) engine).invokeFunction("converter", configObject,object);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		} catch (ScriptException e) {
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}

		/*System.out.println(result);*/
		return result;
	}

	public static void main(String[] args) throws IOException {
		String resonseBody1="[[\"8111005405\",\"0002000019\",\"0002000019\",\"20\",\"1101\",\"MNSD06\",\"20180219\",\"90200097_290\",\" 1600.000\",\"JAR\",\" 348000\",\" 0.00\",\"JOCG 6JOSG 6\",\" 41760.00\",\" 389760.00\",\"MH-14/GD-7020\",\"\",\"\",\"\",\"\",\"\",\"\",\" 100.000\",\"BOX\",\"ZDOR\",\"TAN\",\" 348000.00\",\" 41760.00\",\" 389760.00\"]]";
	
		String responseBody1="[[\"\\\"\\\",\\\"\\\",\\\"12/13/2017\\\",\\\"12/13/2017 5:00:00 AM\\\",\\\"12/13/2017 5:12:05 AM\\\",\\\"50.00\\\",\\\"\\\",\\\"\\\",\\\"11\\\",\\\"IBLPN01\\\",\\\"100.00\\\",\\\"12/13/2017\\\",\\\"12/13/2017 5:00:00 AM\\\",\\\"12/13/2017 5:43:42 AM\\\",\\\"TESTUSER01\\\",\\\"50.00\\\",\\\"\\\",\\\"0.10\\\",\\\"0.10\\\",\\\"\\\",\\\"JL_COMPY\\\",\\\"201\\\",\\\"12/13/2017\\\",\\\"12/13/2017 5:00:00 AM\\\",\\\"12/13/2017 5:10:42 AM\\\",\\\"50.00\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"0\\\",\\\"0\\\",\\\"0\\\",\\\"0\\\",\\\"P04\\\",\\\"3\\\",\\\"11\\\",\\\"1602\\\",\\\"12/13/2017\\\",\\\"12/13/2017 2:44:22 PM\\\",\\\"TESTUSER01\\\",\\\"50.00\\\",\\\"ASN01\\\",\\\"\\\",\\\"\\\",\\\"12/13/2017\\\",\\\"Verified\\\",\\\"\\\",\\\"12/13/2017\\\",\\\"12/13/2017 2:00:00 PM\\\",\\\"12/13/2017 2:44:22 PM\\\",\\\"50.00\\\",\\\"TESTUSER01\\\",\\\"LASN01\\\",\\\"\\\",\\\"91-20-011-0023\\\",\\\"G.I. 24 SWG Ducting Material supply with fitting\\\"\",\"JL_COMPY_P04_BLR_Support_IB Shipment- GRN_5_30_2018_15_52_20_263.csv\",\"2\"]]";
		String resonseBody="{\r\n" + 
				"	\"env:Header\":[], \r\n" + 
				"	\"env:Body\":[\r\n" + 
				"			{\r\n" + 
				"			\"OutputParameters\":[\r\n" + 
				"								{\r\n" + 
				"									\"OUT_REQUEST_STATUS\":[\"FAILURE\"], \r\n" + 
				"									\"P_PO_DETAILS_TBL\":[]\r\n" + 
				"								}\r\n" + 
				"							]\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"}";
		
		
		
		String objectHolder	="{\"jobRunningId\":1391,\"runAgain\":false,\"startSequence\":null,\"endSequence\":null,\"batchSize\":null,\"currentDate\":1548754999825,\"lastScheduledStartTime\":1548754844000,\"nextRunTime\":1548758438000,\"jobRunSuccessCount\":0,\"jobRunFailureCount\":0,\"configs\":{\"0\":{\"parent\":null,\"currentResponse\":null,\"translatorMap\":{},\"values\":{\"1\":{\"94969219\":[\"1000024173\",\"2044\",\"94969219\",\"WB20181015\",\"27011-17R\",\"1000024173\",\"2018/11/27\",\"2018/11/27\",\"85131244\"],\"94969276\":[\"1000034418\",\"2044\",\"94969276\",\"WB20181016\",\"20454-23R\",\"1000034418\",\"2018/11/27\",\"2018/11/27\",\"85131633\"],\"94969334\":[\"1000025285\",\"2043\",\"94969334\",\"TN20180583\",\"26252-6R\",\"1000025285\",\"2018/11/27\",\"2018/11/27\",\"85132232\"],\"94969341\":[\"1000024780\",\"2040\",\"94969341\",\"MH20180850\",\"25737-4R\",\"1000024780\",\"2018/11/27\",\"2018/11/27\",\"85132242\"],\"94969344\":[\"1000033557\",\"2044\",\"94969344\",\"WB20181017\",\"27211-4R\",\"1000033557\",\"2018/11/27\",\"2018/11/27\",\"85132256\"],\"94969354\":[\"1000025274\",\"2043\",\"94969354\",\"TN20180584\",\"26240-4R\",\"1000025274\",\"2018/11/27\",\"2018/11/27\",\"85132264\"],\"94969357\":[\"1000024169\",\"2044\",\"94969357\",\"WB20181018\",\"26124-18R\",\"1000024169\",\"2018/11/27\",\"2018/11/27\",\"85132317\"],\"94969359\":[\"1000024794\",\"2040\",\"94969359\",\"MH20180851\",\"27097-2R\",\"1000024794\",\"2018/11/27\",\"2018/11/27\",\"85132327\"],\"94969367\":[\"1000024246\",\"2044\",\"94969367\",\"WB20181019\",\"26238-11R\",\"1000024246\",\"2018/11/27\",\"2018/11/27\",\"85132344\"],\"94969375\":[\"1000025286\",\"2043\",\"94969375\",\"TN20180585\",\"26368-6R\",\"1000025286\",\"2018/11/27\",\"2018/11/27\",\"85132368\"],\"94969385\":[\"1000024185\",\"2044\",\"94969385\",\"WB20181020\",\"26246-28R\",\"1000024185\",\"2018/11/27\",\"2018/11/27\",\"85132388\"],\"INV_KEY\":[\"DIST_CD\",\"SUPPLIER_CD\",\"INV_KEY\",\"INV_NO\",\"PO_NO\",\"SHIPTO_CD\",\"INV_DT\",\"DUE_DT\",\"DO_NO\"]}}}},\"dryRun\":false}\r\n" + 
				"";
		String responseBody3="{\"result\":true,\"reason\":\"\",\"Outlets\":[{\"id\":\"19165\",\"name\":\"chandana pet house\",\"address\":\"Dwarir rd.(N.S rd.opp sukanta park) sonarpur\",\"keycustomeracc\":\"0\",\"phoneno\":\"9681271529\",\"email\":\"\",\"ownername\":\"Deep Das\",\"ownermobile\":\"\",\"taxno\":\"\",\"cstno\":\"\",\"isdistributor\":\"0\",\"type\":\"PET SHOP\",\"outlet_level\":\"cash\",\"longitude\":\"88.2268374\",\"latitude\":\"22.4976631\",\"created\":\"2019-04-20 13:46:17\",\"modified\":\"2019-04-24 00:27:38\",\"approved\":\"1\",\"code\":\"\",\"pin\":\"700151\",\"createdbyuser_id\":\"226\",\"outletstate_id\":\"1\",\"discount_category_id\":\"0\",\"erp_id\":\"9431823\",\"panno\":\"\",\"user_id\":\"226\",\"headquarter_id\":\"0\",\"headquarter\":null,\"outletcategory\":\"C\",\"beatname\":\"RAJDEEP SEN_SAT__MANIKTALA, SALTLAKE, KHANNA\",\"beatid\":\"1176\",\"city\":\"KOLKATA\",\"state\":\"WEST BENGAL\",\"area\":\"KOLKATA_(KOLKATA CFA)\",\"areaId\":\"1318\",\"warehouse\":\"DAYAL ENTERPRISES (KOLKATA CFA)\",\"warehouseId\":\"311\",\"warehouse_erpid\":\"C009\",\"zone\":\"WEST BENGAL\",\"zoneId\":\"23\",\"balance\":\"0.00000\",\"credit_limit\":\"0.00000\",\"credit_days\":\"0\",\"DiscountCategoryName\":null},{\"id\":\"8746\",\"name\":\"THE PET SHOP C\\/OF DEVELOPMENT ENTERPRISE\",\"address\":\"NO. 6 BAJPAYEE SHOPPING CENTRE NAVSARI\",\"keycustomeracc\":\"0\",\"phoneno\":\"9913751117\",\"email\":\"maksudtai1117@gmail.com\",\"ownername\":\"MAKSUD TAI\",\"ownermobile\":\"\",\"taxno\":\"999\",\"cstno\":\"\",\"isdistributor\":\"0\",\"type\":\"PET SHOP\",\"outlet_level\":\"cash\",\"longitude\":\"72.9378763\",\"latitude\":\"20.9431729\",\"created\":\"2018-06-07 11:58:18\",\"modified\":\"2019-04-24 10:06:29\",\"approved\":\"1\",\"code\":\"\",\"pin\":\"396445\",\"createdbyuser_id\":\"196\",\"outletstate_id\":\"1\",\"discount_category_id\":\"0\",\"erp_id\":\"0\",\"panno\":\"\",\"user_id\":\"196\",\"headquarter_id\":\"0\",\"headquarter\":null,\"outletcategory\":\"A\",\"beatname\":\"Rakesh Kansara_MON & THUR_Weekly_\",\"beatid\":\"1017\",\"city\":\"Navsari\",\"state\":\"GUJARAT\",\"area\":\"NAVSARI_Dev Enterprise\",\"areaId\":\"933\",\"warehouse\":\"Dev Enterprise\",\"warehouseId\":\"228\",\"warehouse_erpid\":\"9429278\",\"zone\":\"GUJARAT\",\"zoneId\":\"21\",\"balance\":\"0.00000\",\"credit_limit\":\"0.00000\",\"credit_days\":\"0\",\"DiscountCategoryName\":null},{\"id\":\"19099\",\"name\":\"Shree Shantinath Medical Store\",\"address\":\"Opp. Jaliwali masjid, Near Char Rasta, Gopi pura momandwad, Surat\",\"keycustomeracc\":\"0\",\"phoneno\":\"9537548177\",\"email\":\"\",\"ownername\":\"SAJID MULLA\",\"ownermobile\":\"\",\"taxno\":\"\",\"cstno\":\"\",\"isdistributor\":\"0\",\"type\":\"CHEMIST\\/ PHARMACY\",\"outlet_level\":\"cash\",\"longitude\":\"\",\"latitude\":\"\",\"created\":\"2019-04-17 13:05:20\",\"modified\":\"2019-04-24 10:41:20\",\"approved\":\"1\",\"code\":\"\",\"pin\":\"395001\",\"createdbyuser_id\":\"2\",\"outletstate_id\":\"1\",\"discount_category_id\":\"0\",\"erp_id\":\"\",\"panno\":\"\",\"user_id\":\"2\",\"headquarter_id\":null,\"headquarter\":null,\"outletcategory\":\"C\",\"beatname\":\"SUMIT SARKAR_WEDNESDAY_WEEKLY\",\"beatid\":\"1846\",\"city\":\"Surat\",\"state\":\"GUJARAT\",\"area\":\"SURAT_Dev Enterprise\",\"areaId\":\"931\",\"warehouse\":\"Dev Enterprise\",\"warehouseId\":\"228\",\"warehouse_erpid\":\"9429278\",\"zone\":\"GUJARAT\",\"zoneId\":\"21\",\"balance\":\"0.00000\",\"credit_limit\":\"0.00000\",\"credit_days\":\"0\",\"DiscountCategoryName\":null},{\"id\":\"9234\",\"name\":\"DR.DESHMUKH PET CLINIC\",\"address\":\"SHOP NO. 13 AAGAM ARCADE VESU.SURAT\",\"keycustomeracc\":\"0\",\"phoneno\":\"9327247474\",\"email\":\"drdehmukhpetclinic@gmail.com\",\"ownername\":\"DR.VIPIN .V. DESHMUKH\",\"ownermobile\":\"\",\"taxno\":\"999\",\"cstno\":\"\",\"isdistributor\":\"0\",\"type\":\"VET\\/ PET CLINIC\\/VET HOSPITAL\",\"outlet_level\":\"cash\",\"longitude\":\"72.7733817\",\"latitude\":\"21.1454125\",\"created\":\"2018-06-12 12:58:02\",\"modified\":\"2019-04-24 12:01:15\",\"approved\":\"1\",\"code\":\"\",\"pin\":\"395007\",\"createdbyuser_id\":\"196\",\"outletstate_id\":\"1\",\"discount_category_id\":\"0\",\"erp_id\":\"0\",\"panno\":\"\",\"user_id\":\"196\",\"headquarter_id\":\"0\",\"headquarter\":null,\"outletcategory\":\"B\",\"beatname\":\"CHETAN VYAS_WED_WEEKLY\",\"beatid\":\"2064\",\"city\":\"Surat\",\"state\":\"GUJARAT\",\"area\":\"SURAT_Zaveri Enterprise\",\"areaId\":\"1021\",\"warehouse\":\"Zaveri Enterprise\",\"warehouseId\":\"242\",\"warehouse_erpid\":\"9430393\",\"zone\":\"GUJARAT\",\"zoneId\":\"21\",\"balance\":\"0.00000\",\"credit_limit\":\"0.00000\",\"credit_days\":\"0\",\"DiscountCategoryName\":null},{\"id\":\"19233\",\"name\":\"Malik provision store\",\"address\":\"D 720 shastri nagar ghaziabad\",\"keycustomeracc\":\"0\",\"phoneno\":\"9873496550\",\"email\":\"\",\"ownername\":\"gaurav\",\"ownermobile\":\"\",\"taxno\":\"\",\"cstno\":\"\",\"isdistributor\":\"0\",\"type\":\"DEPARTMENTAL STORE\",\"outlet_level\":\"cash\",\"longitude\":\"77.466074\",\"latitude\":\"28.6743488\",\"created\":\"2019-04-24 12:06:21\",\"modified\":\"2019-04-24 12:07:58\",\"approved\":\"1\",\"code\":\"\",\"pin\":\"201002\",\"createdbyuser_id\":\"346\",\"outletstate_id\":\"1\",\"discount_category_id\":\"0\",\"erp_id\":\"\",\"panno\":\"\",\"user_id\":\"346\",\"headquarter_id\":\"0\",\"headquarter\":null,\"outletcategory\":\"C\",\"beatname\":\"ANAND KUMAR SINGH_WEDNESDAY_WEEKLY\",\"beatid\":\"2070\",\"city\":\"Ghaziabad\",\"state\":\"UTTAR PRADESH\",\"area\":\"GHAZIABAD_Ch Ishwar Dass and company\",\"areaId\":\"606\",\"warehouse\":\"Ch Ishwar Dass and company\",\"warehouseId\":\"134\",\"warehouse_erpid\":\"9430411\",\"zone\":\"DELHI & NCR\",\"zoneId\":\"37\",\"balance\":\"0.00000\",\"credit_limit\":\"0.00000\",\"credit_days\":\"0\",\"DiscountCategoryName\":null}]}";
	
		
		String responseBody4="";
		
		
		
		
		ObjectMapper mapper= new ObjectMapper();
		//System.out.println(mapper.writeValueAsString( mapper.readValue(objectHolder, Object.class)));
		NashornHelper noshornHelper = new NashornHelper();
	   Object myObject=	mapper.readValue(responseBody1, Object.class);
	  // System.out.println(myObject);
		Object object=noshornHelper.process(javascript5, mapper.readValue(objectHolder, Object.class),  mapper.readValue(responseBody3, Object.class));
		ObjectMapper objectMapper=new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(object));
	}

}
