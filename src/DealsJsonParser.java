import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import preprocessing.Item;

public class DealsJsonParser {
	private static final String CATEGORY_ID = "categoryID";
	private static final String CATEGORY = "category";
	private static String[] dealsAttributes;
	private static String[] merchantAttributes;
	private static Map<String, String> dealsMappping;
	private static Map<String, String> merchantMappping;
	private static final String ID = "id";
	private static final String SOURCE = "source";
	private static final String TITLE = "title";
	private static final String PRICE = "price";
	private static final String VALUE = "value";
	private static final String DISCOUNT = "discount";
	private static final String DESCRIPTION = "description";
	private static final String LINK = "link";
	private static final String IMAGE = "image";
	private static final String COUPON_EXPIRATION = "coupon_expiration";

	private static final String NAME = "name";
	private static final String WEBSITE = "website";
	private static final String ADDRESS = "address";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String ZIP = "zip";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";

	private String categoriesFileName;

	static {
		dealsAttributes = new String[] { ID, SOURCE, TITLE, PRICE, VALUE,
				DISCOUNT, DESCRIPTION, LINK, IMAGE, COUPON_EXPIRATION };
		merchantAttributes = new String[] { NAME, WEBSITE, ADDRESS, CITY,
				STATE, ZIP, LATITUDE, LONGITUDE };
		dealsMappping = new HashMap<String, String>();
		dealsMappping.put(ID, "ID");
		dealsMappping.put(SOURCE, "dealSource");
		dealsMappping.put(TITLE, "dealTitle");
		dealsMappping.put(PRICE, "dealPrice");
		dealsMappping.put(VALUE, "dealOriginalPrice");
		dealsMappping.put(DISCOUNT, "dealDiscountPercent");
		dealsMappping.put(DESCRIPTION, "dealInfo");
		dealsMappping.put(LINK, "URL");
		dealsMappping.put(IMAGE, "showImage");
		dealsMappping.put(COUPON_EXPIRATION, "expirationDate");

		merchantMappping = new HashMap<String, String>();
		merchantMappping.put(NAME, "name");
		merchantMappping.put(WEBSITE, "storeURL");
		merchantMappping.put(ADDRESS, "address");
		merchantMappping.put(CITY, "CITY");
		merchantMappping.put(STATE, "state");
		merchantMappping.put(ZIP, "ZIP");
		merchantMappping.put(LATITUDE, "lat");
		merchantMappping.put(LONGITUDE, "lon");

	}

	public DealsJsonParser(String categoriesFileName) {
		this.categoriesFileName = categoriesFileName;
	}

	public List<Item> getAllDeals(String dealsFileName) {
		Map<String, String> catMap = new HashMap<String, String>();
		List<Item> items = new ArrayList<Item>();
		File dealsFile = new File(dealsFileName);
		StringBuffer dealsBuffer = new StringBuffer();
		StringBuffer catBuffer = new StringBuffer();
		try {
			FileInputStream dfs = new FileInputStream(dealsFile);
			DataInputStream in = new DataInputStream(dfs);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String str;
			while ((str = br.readLine()) != null) {
				dealsBuffer.append(str);
			}
			in.close();
			br.close();

			FileInputStream cfs = new FileInputStream(new File(
					categoriesFileName));
			DataInputStream cin = new DataInputStream(cfs);
			BufferedReader cbr = new BufferedReader(new InputStreamReader(cin));
			String s;
			while ((s = cbr.readLine()) != null) {
				catBuffer.append(s);
			}
			cin.close();
			cbr.close();

			JSONArray catArray = new JSONArray(catBuffer.toString());
			if (catArray.length() > 0) {
				for (int i = 0; i < catArray.length(); i++) {
					JSONObject obj = catArray.getJSONObject(i);
					if (obj.has(CATEGORY_ID) && obj.has(CATEGORY)) {
						catMap.put(obj.getString(CATEGORY_ID),
								obj.getString(CATEGORY));
					}
				}
			}
			JSONArray dealsArray = new JSONArray(dealsBuffer.toString());
			if (dealsArray.length() > 0) {
				for (int i = 0; i < dealsArray.length(); i++) {
					JSONObject obj = dealsArray.getJSONObject(i);
					Map<String, String> deal = new HashMap<String, String>();
					Map<String, String> merchant = new HashMap<String, String>();
					for (String key : dealsAttributes) {
						String jsonKey = dealsMappping.get(key);
						if (obj.has(jsonKey) && !obj.isNull(jsonKey)) {
							deal.put(key, obj.getString(jsonKey));
						}
					}
					if (obj.has("categoryID")) {
						deal.put(CATEGORY_ID, obj.getString("categoryID"));
						deal.put(CATEGORY,
								catMap.get(obj.getString(CATEGORY_ID)));
					}
					for (String key : merchantAttributes) {
						String jsonKey = merchantMappping.get(key);
						if (obj.has(jsonKey)) {
							merchant.put(key, obj.getString(jsonKey));
						}
					}
					Item item = new Item();
					item.deal = deal;
					item.merchant = merchant;
					items.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
}
