package org.sunbird.ruleengine.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class XmlToJsonConverter {

	public static Object convert(String xml) throws Exception {

		InputStream is = new ByteArrayInputStream(xml.getBytes());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(is);
		Map<String, Object> map = new HashMap<String, Object>();
		return createMap(document.getDocumentElement(), map);
	}

	private static Object createMap(Node node, Map<String, Object> parentMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		NodeList nodeList = node.getChildNodes();
		String parentName = node.getNodeName();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			String name = currentNode.getNodeName();
			if(name.contains(":"))
			{
				String[] nameArray =name.split(":");
				name = nameArray[nameArray.length-1];
			}
			Object value = null;
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				System.out.println(name);
				value = createMap(currentNode, map);
				if (map.containsKey(name)) {
					if (!(value instanceof Map && ((Map) value).isEmpty())) {
						Object os = map.get(name);
						((List<Object>) os).add(value);
					}
				} else {
					List<Object> objs = new LinkedList<Object>();
					if (!(value instanceof Map && ((Map) value).isEmpty())) {
						objs.add(value);
					}
					map.put(name, objs);
				}
			} else if (currentNode.getNodeType() == Node.TEXT_NODE
					|| currentNode.getNodeType() == Node.CDATA_SECTION_NODE) {
				if (!currentNode.getTextContent().trim().isEmpty()) {
					value = currentNode.getTextContent();
					if (parentMap.containsKey(parentName)) {
						Object os = parentMap.get(parentName);
						((List<Object>) os).add(value);
					} else {
						List<Object> objs = new LinkedList<Object>();
						objs.add(value);
						parentMap.put(parentName, objs);
					}
				}
			}

		}
		return map;
	}

	
	
	public static void main(String args[]) throws Exception
	{
		String xml= "<soap-env:Envelope xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap-env:Header/><soap-env:Body><n0:ZbapiDroolsOrderStatusResponse xmlns:n0=\"urn:sap-com:document:sap:soap:functions:mc-style\"><TBilldata><item><Vbeln>31100:00007</Vbeln><Posnr>000010</Posnr><Fkdat>2019-01-17</Fkdat><Matnr>000000000000930168</Matnr><Arktx>Drools L Puppy 3Kg</Arktx><Charg>DROOLS</Charg><Fkimg>1.0</Fkimg><Meins>EA</Meins><Netwr>525.0</Netwr><Mwsbp>94.5</Mwsbp><Brtwr>619.5</Brtwr><Netpr>525.0</Netpr></item><item><Vbeln>3110000007</Vbeln><Posnr>000020</Posnr><Fkdat>2019-01-17</Fkdat><Matnr>000000000000930210</Matnr><Arktx>Drools L Puppy 12Kg</Arktx><Charg>DROOLS</Charg><Fkimg>1.0</Fkimg><Meins>EA</Meins><Netwr>1890.0</Netwr><Mwsbp>340.2</Mwsbp><Brtwr>2230.2</Brtwr><Netpr>1890.0</Netpr></item></TBilldata><TOrder><item><Mandt>117</Mandt><Ordno>0000047294</Ordno><Itemn>0001</Itemn><Kunnr>0009430486</Kunnr><Name1>KIRAN ENTERPRISES</Name1><OutletNumber>0000001413</OutletNumber><OutletName>TOBBUS PET SHOP</OutletName><Budat>2019-01-17</Budat><Matnr>000000000000930168</Matnr><Maktx>Drools L Puppy 3Kg</Maktx><Batch/><Orderqty>1</Orderqty><Meins>EA</Meins><Scheme/><Sample/><Vbeln>3110000007</Vbeln><Billqty>0</Billqty><Cpudt>2019-01-17</Cpudt><Cputm>12:56:54</Cputm><Uname>RFCUSER</Uname><Empid>00105734</Empid><Empnm>RAHUL KAILASH DODKE</Empnm><Srlno>0000000014</Srlno><Blcrt>X</Blcrt><Primo/><Werks/><Pname/></item><item><Mandt>117</Mandt><Ordno>0000047294</Ordno><Itemn>0002</Itemn><Kunnr>0009430486</Kunnr><Name1>KIRAN ENTERPRISES</Name1><OutletNumber>0000001413</OutletNumber><OutletName>TOBBUS PET SHOP</OutletName><Budat>2019-01-17</Budat><Matnr>000000000000930210</Matnr><Maktx>Drools L Puppy 12Kg</Maktx><Batch/><Orderqty>1</Orderqty><Meins>EA</Meins><Scheme/><Sample/><Vbeln>3110000007</Vbeln><Billqty>0</Billqty><Cpudt>2019-01-17</Cpudt><Cputm>12:56:54</Cputm><Uname>RFCUSER</Uname><Empid>00105734</Empid><Empnm>RAHUL KAILASH DODKE</Empnm><Srlno>0000000014</Srlno><Blcrt>X</Blcrt><Primo/><Werks/><Pname/></item></TOrder></n0:ZbapiDroolsOrderStatusResponse></soap-env:Body></soap-env:Envelope>";
		System.out.println(convert(xml));
	}
}
