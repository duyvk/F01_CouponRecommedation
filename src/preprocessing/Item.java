package preprocessing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Item {
	
	public Map<String, String> deal = new HashMap<String, String>();
	public Map<String, String> merchant = new HashMap<String, String>();
	public int id;
	
	public Map<String, String> getDeal() {
		return deal;
	}

	public Map<String, String> getMerchant() {
		return merchant;
	}

	public int getId() {
		return id;
	}

	/*print Item information (id, deal, merchant)*/
	public void printItem(){
		System.out.println(id);
		printMap("deal",deal);
		printMap("merchant",merchant);
		System.out.println("-----------------------------------------");
	}
	
	/*print all element of a map. using for "deal" and "merchant" map */
	public void printMap(String name,Map<String, String> mp){
		System.out.println("----"+name);
		//Get Map in Set interface to get key and value
        Set s=mp.entrySet();

        //Move next key and value of Map by iterator
        Iterator it=s.iterator();

        while(it.hasNext())
        {
            // key=value separator this by Map.Entry to get key and value
            Map.Entry m =(Map.Entry)it.next();

            // getKey is used to get key of Map
            String key=(String)m.getKey();

            // getValue is used to get value of key in Map
            String value=(String)m.getValue();

            System.out.println("--------"+key+" : "+value);
        }
	}
}
