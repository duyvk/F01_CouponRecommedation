import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.security.auth.Refreshable;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.ByValueRecommendedItemComparator;
import org.apache.mahout.cf.taste.impl.recommender.GenericRecommendedItem;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.google.common.collect.Lists;


// TODO: Auto-generated Javadoc
/**
 * The Class Recommendation.
 */
public class Recommendation {
	
	/** Threshold of similarity score for all 18 attributes. */
	static int[] threshold = { 0, 2, 1, 1, 1, 1, 1, 2};
	
	/** The feature weights. */
	public static double[] featureWeights = { 0, 8, 10, 10, 10, 8, 5, 4};
	
	/** The maximum number of similar items that should be fetch 
	 * when comparing an item and the rest */
	public static int maxSimilarityItem = 10;
	
	/** The maximum number of users who new item should be suggested to. */
	public static int maxUserSuggestion = 10;
	
	/** The user-item rating model. */
	private DataModel model;
	
	/** The Id of new coupon. */
	private long newCouponId;
	
	/** list of all couponID. */
	private List<Long> idList;
	
	/* map <couponID - coupon data>. 
	 * Notice:coupon data doesn't contain couponId right now*/
	private Map<Long, Coupon> couponMap;
	
	
	/**
	 * Instantiates a new recommendation.
	 *
	 * @param model the model
	 * @param newCouponId the new coupon id
	 * @param idList : list of all coupon ID
	 * @param couponMap : map <couponID - coupon data>
	 */
	public Recommendation(DataModel model, long newCouponId, List<Long> idList, Map<Long, Coupon> couponMap) {
		super();
		this.model = model;
		this.newCouponId = newCouponId;
		this.idList = idList;
		this.couponMap = couponMap;
	}
	
	/**
	 * Gets the top similar coupon to new coupon (newCoupondID) from the list of old coupon (idList)
	 *
	 * @param newCouponId the new coupon id
	 * @param couponMap the coupon map
	 * @param idList the id list
	 * @return the top items
	 */
	public static List<RecommendedItem> getTopItems(long newCouponId, Map<Long, Coupon> couponMap, List<Long> idList){
		Coupon newCoupon = couponMap.get(newCouponId);
		
		
		Queue<RecommendedItem> topItems = new PriorityQueue<RecommendedItem>(maxSimilarityItem + 1,
				  Collections.reverseOrder(ByValueRecommendedItemComparator.getInstance()));
		boolean full = false;
		double lowestTopValue = Double.NEGATIVE_INFINITY;
		
		for(Long oldCouponId : idList){
			
			Coupon oldCoupon = couponMap.get(oldCouponId);
			
			double similarity = newCoupon.similarTo(oldCoupon, threshold, featureWeights);
			if (!Double.isNaN(similarity) && (!full || similarity > lowestTopValue)) {
	  			topItems.add(new GenericRecommendedItem(oldCouponId, (float) similarity));
	  			if (full) {
	  				topItems.poll();
	  			} else if (topItems.size() > maxSimilarityItem) {
	  				full = true;
	  				topItems.poll();
	  			}
	  			lowestTopValue = topItems.peek().getValue();
	  		}
		}
		
		int size = topItems.size();
		List<RecommendedItem> resultItems = Lists.newArrayListWithCapacity(size);
		resultItems.addAll(topItems);
		Collections.sort(resultItems, ByValueRecommendedItemComparator.getInstance());
		
		return resultItems;
	}
	//(2d)
	/**
	 * Gets the top user should be suggested, with score calculated by R += recommendedItem.getValue() * preferenceValue;
	 *
	 * @param resultItems the result items
	 * @param model the model
	 * @return the top user
	 * @throws TasteException the taste exception
	 */
	public static List<RecommendedItem> getTopUser(List<RecommendedItem> resultItems, DataModel model) throws TasteException{
		Queue<RecommendedItem> topUsers = new PriorityQueue<RecommendedItem>(maxUserSuggestion + 1,
				  Collections.reverseOrder(ByValueRecommendedItemComparator.getInstance()));
		boolean full = false;
		double lowestTopValue = Double.NEGATIVE_INFINITY;
		
		LongPrimitiveIterator userIterator =  model.getUserIDs();
		while (userIterator.hasNext()) {
			Long userID = (Long) userIterator.next();
			float R = 0;
			
			for (RecommendedItem recommendedItem : resultItems) {
				Float preferenceValue = model.getPreferenceValue(userID, recommendedItem.getItemID());
				if (preferenceValue != null){
					R += recommendedItem.getValue() * preferenceValue;
					if (!Double.isNaN(R) && (!full || R > lowestTopValue)) {
						topUsers.add(new GenericRecommendedItem(userID, (float) R));
			  			if (full) {
			  				topUsers.poll();
			  			} else if (topUsers.size() > maxUserSuggestion) {
			  				full = true;
			  				topUsers.poll();
			  			}
			  			lowestTopValue = topUsers.peek().getValue();
			  		}
				}
			}
			
		}
		
		int size = topUsers.size();
		List<RecommendedItem> resultUser = Lists.newArrayListWithCapacity(size);
		resultUser.addAll(topUsers);
		Collections.sort(resultUser, ByValueRecommendedItemComparator.getInstance());
		
		return resultUser; //(2aii1d)
	}
	
	//(2bi)
	/**
	 * Predict model.
	 *
	 * @param modelFile the model file
	 * @param neighborNum the neighbor num
	 * @param itemRecommNum the item recomm num
	 * @param predictedModelFile the predicted model file
	 * @throws TasteException the taste exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void predictModel(String modelFile, int neighborNum, int itemRecommNum, String predictedModelFile) throws TasteException, IOException{
		// clear folder temp
		File file = new File(predictedModelFile + ".csv");
		int indexFile = 1;
		while (file.exists()){
			if (file.delete()) System.out.println("deleted");;
			file = new File(predictedModelFile + indexFile + ".csv");
			indexFile ++;
		}
		
		// copy model file to new location
		File f1 = new File(modelFile);
		File f2 = new File(predictedModelFile + ".csv");
		InputStream in = new FileInputStream(f1);
		OutputStream out = new FileOutputStream(f2);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0){
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		
		// start predict
		
		DataModel model = null;		
		
		boolean modelChange = true;
		
		int i = 1;
		
		while (modelChange){
			
			model = new FileDataModel(new File(predictedModelFile + ".csv"));
			
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborNum, similarity, model);
			
			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			
			LongPrimitiveIterator iterator = model.getUserIDs();
			
			
			
			PrintWriter printWriter = new PrintWriter(new File(predictedModelFile+ i + ".csv"));
			
			i ++;
			modelChange = false;
			System.out.println("-----------------------------------");
			System.out.println(i);
			
			int count = 0;
			while (iterator.hasNext()) {
				Long userID = (Long) iterator.next();
				
				count += model.getItemIDsFromUser(userID).size();
				
				List<RecommendedItem> recommendeds = recommender.recommend(userID, itemRecommNum);
				if (recommendeds.size() > 0){
					for (RecommendedItem recommendedItem : recommendeds) {
						printWriter.println(userID + "," + recommendedItem.getItemID() + "," + recommendedItem.getValue());
					}
					
					modelChange = true;
				}
			}
			
			printWriter.close();
			
			if (modelChange){
				model.refresh(null);
			}
			System.out.println(count);
		}
	}
}
