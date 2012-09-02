package preprocessing;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


// TODO: Auto-generated Javadoc
/**
 * The Class XMLParser.
 */
public class XMLParser {
	
	/**
	 * Gets the all items.
	 *
	 * @param fileName : input file (.xml)
	 * @return list of items
	 */
	public List <Item> getAllItems(String fileName) {
		List <Item> items = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			File file  = new File (fileName);
			if(file.exists()){
				Document doc = db.parse(file);
				Element docEle = doc.getDocumentElement();
			
				//Print root element of the document
				System.out.println("Root element of the document: "
						+ docEle.getNodeName());
				
				NodeList itemList = docEle.getElementsByTagName("item");
				System.out.println("Total items: " + itemList.getLength());
				
				items = new ArrayList<Item>();
				for(int i =0; i < itemList.getLength();i++){
					Item tempItemObject = new Item();
					Element tmpItemEle= (Element) itemList.item(i);
					
					NodeList deal = tmpItemEle.getElementsByTagName("deal");
					NodeList merchant = tmpItemEle.getElementsByTagName("merchant");
					
					addAttribute(deal.item(0).getChildNodes(), tempItemObject.deal);
					addAttribute(merchant.item(0).getChildNodes(), tempItemObject.merchant);
					
					items.add(tempItemObject);
					//System.out.println("Item N0:" + items.size() );
					//tempItemObject.printItem();
				}
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * Adds the attribute.
	 * --> pass all pairs of each child tag (<name>value<name>) of a node to a list
	 * @param nl : the node
	 * @param list <key, value>: key(nodename) - value (node's content)
	 */
	private void addAttribute(NodeList nl, Map<String,String> list){
		for(int i =0; i < nl.getLength();i++){
			Node temp = nl.item(i);
			String key ="", value = "";
			key = temp.getNodeName();
			if(temp.getChildNodes().getLength()==0)
				//System.out.println("N/A");
				value = "N/A";
			else
				//System.out.println(temp.getChildNodes().item(0).getNodeValue());
				value = temp.getChildNodes().item(0).getNodeValue();
			//System.out.println("------------");
			list.put(key,value);
		}
	}
	
	/**
	 * Parses the value.
	 *---> to read all text content inside a node
	 * @param node the node
	 * @return the string
	 */
	public static String parseValue (Node node){
		StringBuffer text = new StringBuffer();
		NodeList nodeChildren = node.getChildNodes();
		String value;
		Node tmpNode;
		
		for (int i = 0; i < nodeChildren.getLength();i++){
			tmpNode = nodeChildren.item(i);
			value = tmpNode.getNodeValue();
			if(value!= null){
				text.append(value);
			}
		}
		return text.toString();
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main (String []args){
		String fileName = "data/coupon.xml";
		XMLParser parser = new XMLParser();
		List <Item> items = parser.getAllItems(fileName);
	}



/*each Item instance include of  a "deal" map and a "merchant" map 
  * corresponding to "deal" node and "merchant" node respectively
 	
 */
}
