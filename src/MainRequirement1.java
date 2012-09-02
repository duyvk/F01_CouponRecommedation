import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.Coupon;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 * @author Administrator
 *
 */
public class MainRequirement1 {
	
	/*file contains coupon data in xml format
	public static final String couponFile = "data/coupon.xml";
	
	file contains  user-item rating information  (user id | item id | rating | timestamp)
	public static final String userItemFile = "100k-data/u.data";
	
	selected movieID to use as couponID
	public static final String idFIle = "data/idList.txt";*/
	
	/*input to build data model*/
	public static final String filteredFile = "data/u.csv"; //(2aii1ai)
	
	/*the number of testing "new" coupons*/
	public static final int numberNewItem = 2;

	/*
	 * number neighbor :
	 * number item recommend:
	 * */
	
	public static int NEIGHBOR_NUM = 3;
	public static int ITEM_RECOMMEN_NUM = 5;
	
	public static final String ROOT_PREDICTED_MODEL_FILE = "100k-data/temp/new-u";
	//(2aii1aiv)
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws TasteException the taste exception
	 */
	public static void main(String[] args) throws IOException, TasteException {
		//ItemVector.init();
		
		/*XMLParser parser = new XMLParser();
		
		
		 * get all item from coupon file
		 * 
		List <Item> items = parser.getAllItems(couponFile);//(1aii1ai)
		int itemNumber = items.size();
		
		Preprocessing preprocessing = new Preprocessing();
				
		preprocessing.extractIDFromMovieLensData(itemNumber, userItemFile, idFIle);

		//get list of couponIDs (idList)
		Set<Long> ids = preprocessing.getIdArray(idFIle);
		List<Long> idList = new ArrayList<Long>(ids);	//(1aii1aii)	((1aiii)
		
		
		 match id from coupon with movielens data, create itemvector correspond with each coupon in coupon.xml 
		 * return a map with key is id in movielens and matched Itemvector 
		 
		//Map<Long, ItemVector> itemVectorMap = preprocessing.matchCouponWithMovie(items, idList);
		Map<Long, Coupon> couponMap = preprocessing.matchCouponWithMovie(items, idList);//(1aii)
		
		
		// newCouponids : list of new coupons (1ai) for testing new  coupons
		List<Long> newCouponids = new ArrayList<Long>();
		
		// pop some ID from list of all coupons and add  them to newCouponids 
		for (int i=0; i< numberNewItem; i++){

			newCouponids.add(idList.remove(i));
		}
		
		// remove all of irrelevant  line in  user-item rating file.
		// Specifically, we find some tuples((user id | item id | rating | timestamp)) that have itemID that match with one of IDs in idList
		// and put those tuples to filteredFile
		preprocessing.filterMovieLens(idList, userItemFile, filteredFile);*/
		List<Long> newCouponids = loadIDFile("data/newID.txt");
		List<Long> idList = loadIDFile("data/oldID.txt");	//(1aii1aii)	((1aiii)
		/*
		 * load user - item data to model object (mahout)
		 * 
		 * */
		Map<Long, Coupon> couponMap = loadCouponFile("data/coupon.txt");
		//DataModel model = new FileDataModel(new File(filteredFile));
		
		Recommendation.predictModel(filteredFile, NEIGHBOR_NUM, ITEM_RECOMMEN_NUM, ROOT_PREDICTED_MODEL_FILE);//(2aii1)
		
		DataModel model = new FileDataModel(new File(ROOT_PREDICTED_MODEL_FILE + ".csv"));
		
		Set<Long> list = couponMap.keySet();
		for (Long long1 : list) { //(1aii1bi)
			System.out.println("-------------------");
			System.out.println(long1);
			couponMap.get(long1).printCoupon();
		}
		
		PrintWriter outItemSimilarity = new PrintWriter(new File("log/itemSimilarity.txt"));
		PrintWriter userSuggestion = new PrintWriter(new File("log/userSuggestion.txt"));
		
		// with each new coupon
		for (Long newCouponId : newCouponids) {
			outItemSimilarity.println(newCouponId);
			userSuggestion.println(newCouponId);
			//couponMap.get(newCouponId).printCoupon();
			/* create new object recommendation
			 * model : model of user - item loaded  
			 * newcouponID : id of new coupon
			 * idlist: id of old coupons
			 */
			//Recommendation recommendation = new Recommendation(model, newCouponId, idList, couponMap);
			
			/* 
			 * get most similar coupon with new item
			 * */
			List<RecommendedItem> resultItems = Recommendation.getTopItems(newCouponId, couponMap, idList); //(2ai)
							
			/*
			 * Logging
			 * 
			 * */
			for (RecommendedItem recommendedItem : resultItems) { //(1bi)
				outItemSimilarity.println(recommendedItem);
			}
			
			/* 
			 * get user for suggestion
			 * */
			List<RecommendedItem> resultUser = Recommendation.getTopUser(resultItems, model);
			/* user suggestion
			 * */
			
			// output test
			for (RecommendedItem recommendedUser : resultUser) {
				userSuggestion.println("User: " + recommendedUser.getItemID());
				userSuggestion.println("R: " + recommendedUser.getValue());
			}
		}
		
		outItemSimilarity.close();
		userSuggestion.close();
	}
	
	/**
	 * Load id file.
	 *
	 * @param fileName the id file
	 * @return the list of all id
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<Long> loadIDFile(String fileName) throws FileNotFoundException{
		List<Long> list = new ArrayList<Long>();
		Scanner scanner = new Scanner(new File(fileName));
		while (scanner.hasNextLong()){
			list.add(scanner.nextLong());
		}
		
		scanner.close();
		
		return list;
	}
	
	/**
	 * Load coupon file.
	 *
	 * @param fileName the file of coupon
	 * @return the map with id is key and coupon is value
	 * @throws FileNotFoundException the file not found exception
	 */
	public static Map<Long, Coupon> loadCouponFile(String fileName) throws FileNotFoundException{
		Map<Long, Coupon> map = new HashMap<Long, Coupon>();
		Scanner scanner = new Scanner(new File(fileName));
		while(scanner.hasNextLine()){
			Coupon coupon = new Coupon(scanner.nextLine());
			map.put(coupon.getId(), coupon);
		}
		scanner.close();
		
		return map;
	}
}
