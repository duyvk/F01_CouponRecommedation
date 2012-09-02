import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import model.Coupon;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;


/**
 * The Class CategoryRecommendation.
 */
public class CategoryRecommendation {
	
	public static float updateValue = 0.0f;
	/**
	 * Init interest of user with category using interest function .
	 *
	 * @param userIDList the user id list
	 * @param categoryIDList the category id list
	 * @return the data model represent user - category preference
	 */
	public static DataModel initInterest(List<Long> userIDList, List<Long> categoryIDList){
	
		Iterator<Long> userIDListIterator = userIDList.iterator();
		Iterator<Long> categoryIDListIterator = userIDList.iterator();
		
		int categoryNum = categoryIDList.size();
		
		FastByIDMap<PreferenceArray> preferences = 
				  new FastByIDMap<PreferenceArray>();
		
		Random random = new Random();
		
		while (userIDListIterator.hasNext()){
			int idxPreferenceArray = 0;
			Long userID = userIDListIterator.next();
			PreferenceArray prefsForUser = new GenericUserPreferenceArray(categoryNum);
			
			while (categoryIDListIterator.hasNext()){
				Long categoryID = categoryIDListIterator.next();
				
				prefsForUser.setUserID(idxPreferenceArray, userID);
				prefsForUser.setItemID(idxPreferenceArray, categoryID);
				
				float randomPreference = (float) (random.nextGaussian()) ;
				
				prefsForUser.setValue(idxPreferenceArray, randomPreference);
			}
			
			preferences.put(userID, prefsForUser);
		}

		DataModel model = new GenericDataModel(preferences);
		
		return model;
	}
	
	/**
	 * Gets the coupon id list C(n)
	 *
	 * @param couponInCategory all coupons in category
	 * @param interestValue the interest value of user to category
	 * @return the id list coupon C(n)
	 */
	public static List<Long> getCouponIDListWithSpecCategory(List<Coupon> couponInCategory, float interestValue){
		List<Long> couponsIDList = new ArrayList<Long>();
		
		int maxCoupon = (int) (couponInCategory.size() * interestValue);
		
		Random random = new Random();
		
		Set<Integer> idxCouponSet = new HashSet<Integer>();
		
		int couponInCategorySize = couponInCategory.size();
		
		for (int idxCoupon = 0; idxCoupon < maxCoupon ; idxCoupon ++){
			int pos = random.nextInt(couponInCategorySize);
			
			while (idxCouponSet.contains(pos)){
				pos = (pos++) % couponInCategorySize;
			}
			
			idxCouponSet.add(pos);
		}
		
		for (Integer idxCoupon : idxCouponSet) {
			couponsIDList.add(couponInCategory.get(idxCoupon).getId());
		}
		
		return couponsIDList;
	}
	
	/**
	 * Update preference of user with category to model.
	 *
	 * @param model the DataModel
	 * @param userID the id of user
	 * @param categoryID the id of category
	 * @return the updated DataModel
	 */
	@SuppressWarnings("finally")
	public static DataModel updateModel(DataModel model, long userID, long categoryID){
		
		try {
			model.setPreference(userID, categoryID, updateValue);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return model;
		}		
	}
}
