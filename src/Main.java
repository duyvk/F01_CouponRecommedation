import java.util.List;

import preprocessing.Item;

public class Main {

	public static final String dealsFile = "deals.txt";
	public static final String categoriesFile = "categories.txt";

	public static void main(String[] args) {
		DealsJsonParser parser = new DealsJsonParser(categoriesFile);
		List<Item> items = parser.getAllDeals(dealsFile);
		System.out.println("Total Items Count :: " + items.size());
		for (Item item : items) {
			System.out.println(item.deal);
			System.out.println(item.merchant);
		}
	}

}
