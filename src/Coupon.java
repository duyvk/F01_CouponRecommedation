import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The Class Coupon.
 * Phase: in phrase calculate similarity of two coupon
 * Purpose: Coupon object
 * Input:
 * Output:
 */
public class Coupon {

	/** The discount. */
	private String discount;
	
	/** The title. */
	private String title;
	
	/** The category. */
	private String category;
	
	/** The price. */
	private String price;
	
	/** The description. */
	private String description;
	
	/** The latitude. */
	private String latitude;
	
	/** The longitude. */
	private String longitude;
	
	/** The expiration. */
	private String expiration;
	
	/** The id. */
	private long id;
	
	/**
	 * Instantiates a new coupon.
	 *
	 * @param discount the discount
	 * @param title the title
	 * @param category the category
	 * @param price the price
	 * @param description the description
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param expiration the expiration date
	 * @param id : coupon ID
	 */
	public Coupon(String discount, String title, String category, String price,
			String description, String latitude, String longitude, String expiration, long id) {
		super();
		this.discount = discount;
		this.title = title;
		this.category = category;
		this.price = price;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.expiration = expiration;
		this.id = id;
	}
	
	/**
	 * Instantiates a new coupon.
	 *	pre-processing an item retrieved from xml file 
	 *  and pass it to a new declared Coupon
	 *  
	 * @param item the item is retrieved from raw xml file
	 */
	public Coupon(Item item){
		this.id = item.getId();
		
		Map<String, String> deal = item.getDeal();
		
		discount = ProcessData.removeStopwords(deal.get("discount").replace("%", ""));

		title = ProcessData.removeStopwords(deal.get("title"));
		
		category = ProcessData.removeStopwords(deal.get("category"));
		
		price = ProcessData.removeStopwords(deal.get("price").replace("$", ""));
		
		description = ProcessData.removeStopwords(deal.get("description"));
		
		expiration = deal.get("coupon_expiration");
		
		/*
		 * get merchant
		 * */
		Map<String, String> merchant = item.getMerchant();
		
		longitude = ProcessData.removeStopwords(merchant.get("longitude"));		
		
		latitude = ProcessData.removeStopwords(merchant.get("latitude"));
	}
	
	/**
	 * Instantiates a new coupon from coupon file.
	 * each line is formatted : id | discount | title ....
	 * 
	 * @param couponString the coupon string
	 */
	public Coupon(String couponString){
		String [] stringArray = couponString.split("\\|");
		this.id = Integer.parseInt(stringArray[0]);
		this.discount = stringArray[1];
		this.title = stringArray[2];
		this.category = stringArray[3];
		this.price = stringArray[4];
		this.description = stringArray[5];
		this.latitude = stringArray[6];
		this.longitude = stringArray[7];
		this.expiration = stringArray[8];
	}

	/**
	 * Gets the discount.
	 *
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * Sets the discount.
	 *
	 * @param discount the new discount
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the expiration.
	 *
	 * @return the expiration
	 */
	public String getExpiration() {
		return expiration;
	}

	/**
	 * Sets the expiration.
	 *
	 * @param expiration the new expiration
	 */
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Measuring the similarity between "discount" field of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score in term of "discount" information 
	 * @return the double
	 */
	public double discountSim(Coupon other, int threshold){
		return ProcessData.simByNumber(this.getDiscount(), other.getDiscount(), threshold);
	}
	
	/**
	 * Measuring the similarity between "title" field of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score in term of "title" information 
	 * @return the double
	 */
	public double tittleSim(Coupon other, int threshold){
		return ProcessData.simByCommonWord(this.getTitle(), other.getTitle() , threshold);
	}
	
	/**
	 * Measuring the similarity between "category" field of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score in term of "category" information 
	 * @return the double
	 */
	public double categorySim(Coupon other, int threshold){
		return (double) (this.getCategory().equals(other.getCategory()) ? 1 : 0);
	}
	
	/**
	 * Measuring the similarity between "price" field of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score in term of "price" information 
	 * @return the double
	 */
	public double priceSim(Coupon other, int threshold){
		return ProcessData.simByNumber(this.getPrice(), other.getPrice(), threshold);
	}
	
	/**
	 * Measuring the similarity between "description" field of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score in term of "description" information 
	 * @return the double
	 */
	public double descriptionSim(Coupon other, int threshold){
		return ProcessData.simByCommonWord(this.getDescription(), other.getDescription(), threshold);
	}
	
	/**
	 * Measuring the similarity between "GPS" field of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score in term of location coordinates information 
	 * @return the double
	 */
	public double gpsSim(Coupon other, int threshold){
		double lon1 = Double.parseDouble(longitude);
		double lat1 = Double.parseDouble(latitude);
		
		double lon2 = Double.parseDouble(other.getLongitude());
		double lat2 = Double.parseDouble(other.getLatitude());
		
		double temp1 = lon2 - lon1;
		double temp2 = lat2 - lat1;
		
		if (Math.abs(temp1 * temp1 - temp2 * temp2 ) > threshold){
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Measuring the similarity of 2 coupons
	 *
	 * @param other the other coupon
	 * @param threshold the threshold of similarity score
	 * @param featureWeights the weights for all features respectively
	 * @return the double
	 */
	public double similarTo(Coupon other, int[] threshold,
			double[] featureWeights) {
		// titleSim(other, threshold[0]) * featureWeights[0]
		double totalWeight = 0;
		for (double i : featureWeights) {
			totalWeight += i;
		}

		double sim = (discountSim(other, threshold[1]) * featureWeights[1]
				+ tittleSim(other, threshold[2]) * featureWeights[2]
				+ categorySim(other, threshold[3]) * featureWeights[3]
				+ priceSim(other, threshold[4]) * featureWeights[4]
				+ descriptionSim(other, threshold[5]) * featureWeights[5]
				+ gpsSim(other, threshold[7]) * featureWeights[7])
				/ (totalWeight);

		return sim;
	}
	
	/** The print writer. */
	public static PrintWriter printWriter ;
	
	/**
	 * Prints the coupon.
	 */
	public void printCoupon(){
		System.out.println("-----------------discount: " + discount);
		System.out.println("-----------------title: " + title);
		System.out.println("-----------------category: " + category);
		System.out.println("-----------------price: " + price);
		System.out.println("-----------------description: " + description);
		System.out.println("-----------------latitude: " + latitude);
		System.out.println("-----------------longitude: " + longitude);
		System.out.println("-----------------expiration: " + expiration);
		System.out.println("-----------------id: " + id);
	}
	
	/* return string of coupon object
	 * @see java.lang.Object#toString()
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append("|" + discount);
		builder.append("|" + title);
		builder.append("|" + category);
		builder.append("|" + price);
		builder.append("|" + description);
		builder.append("|" + latitude);
		builder.append("|" + longitude);
		builder.append("|" + expiration);
		
		return builder.toString();
	}
	
		/** Threshold of similarity score for 18 attributes */
	static int[] threshold = { 0, 2, 1, 1, 1, 1, 1, 2, 1, 1, 3, 3, 3, 1,
			1, 2, 1, 1 };
	
		/** The feature weights. */
		public static double[] featureWeights = { 0, 8, 10, 10, 10, 8, 5, 4, 8, 4,
			4, 2, 8, 10, 10, 6, 5, 5 };
	
	/**
	 * The main method.
	 * 	(for testing only)
	 * @param args the arguments
	 * @throws FileNotFoundException the file not found exception
	 */
	/*public static void main(String []args) throws FileNotFoundException{
		Scanner scanner = new Scanner(new File("data/coupon.txt"));
		while(scanner.hasNextLine()){
			Coupon coupon = new Coupon(scanner.nextLine());
			coupon.printCoupon();
		}
		scanner.close();
		
		
		Coupon.printWriter = new PrintWriter(new File("coupon.txt"));
		
		XMLParser parser = new XMLParser();
		List <Item> items = parser.getAllItems("data/coupon.xml");
		
		ArrayList<Coupon> arrayList = new ArrayList<Coupon>();
		int i = 1;
		for (Item item : items) {
			System.out.println("---------------------");
			System.out.println(i);
			Coupon coupon = new Coupon(item);
			arrayList.add(coupon);
			coupon.printCoupon();
			i++;
		}
		
		Coupon.printWriter.close();
		
		for (int i=0; i<arrayList.size(); i++){
			for (int j= i + 1; j<arrayList.size(); j++){
				System.out.println(arrayList.get(i).similarTo(arrayList.get(j), threshold, featureWeights));
			}
		}
	}*/
}
