import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


// TODO: Auto-generated Javadoc
/**
 * The Class Preprocessing.
 */
public class Preprocessing {

	/**
	 * Extract id from movie lens data and print to idFile.
	 *
	 * @param noId the number of id from coupon's data
	 * @param movieFile the movie file
	 * @param idFile the id file
	 */
	public void extractIDFromMovieLensData(int noId, String movieFile, String idFile){
		try {
			Map<Integer, Integer> unsortedData = new HashMap<Integer, Integer>();
			Scanner scanner = new Scanner(new File(movieFile));
			while(scanner.hasNextInt()){
				scanner.nextInt();
				int movieID = scanner.nextInt();
				if (unsortedData.containsKey(movieID)){
					unsortedData.put(movieID, unsortedData.get(movieID) + 1);
				} else {
					unsortedData.put(movieID, 1);
				}
				scanner.nextLine();
			}
			scanner.close();
			
			SortedMap<Integer, Integer> sortedData = new TreeMap<Integer, Integer>(new MapValueSort.ValueComparer(unsortedData));
			sortedData.putAll(unsortedData);
	
			PrintWriter printWriter = new PrintWriter(new File(idFile));
			
			Set<Integer> keys = sortedData.keySet();
			Iterator<Integer> iterator = keys.iterator();
			int count = 0;
			while (count < noId){
				count ++;
				if (iterator.hasNext()) {
					int key = iterator.next();
					printWriter.println(key);
				}
			}
			printWriter.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the id array.
	 *
	 * @param idFile the id file
	 * @return the id array
	 */
	public Set<Long> getIdArray(String idFile){
		Scanner idScanner;
		Set<Long> ids = new HashSet<Long>();
		try {
			idScanner = new Scanner(new File(idFile));
			
			while(idScanner.hasNextLong()){
				ids.add(idScanner.nextLong());
			}
			idScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return ids;
	}
	
	
	/**
	 * Filter movie lens.
	 *
	 * @param ids the ids
	 * @param movieFile the movie file
	 * @param filteredMovieFile the filtered movie file
	 */
	public void filterMovieLens(List<Long> ids, String movieFile, String filteredMovieFile){
		try {
						
			Scanner scanner = new Scanner(new File(movieFile));
			PrintWriter printWriter = new PrintWriter(new File(filteredMovieFile));
			
			while (scanner.hasNextLong()){
				long uId = scanner.nextLong();
				long itemId = scanner.nextLong();
				int rate = scanner.nextInt();
				if (ids.contains(itemId)){
					printWriter.println( uId + "," + itemId + "," + rate);
				}
				
				scanner.nextLine();
			}
			
			scanner.close();
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Gets the coupon list.
	 *
	 * @param items the items
	 * @return the coupon list
	 */
	public ArrayList<Coupon> getCouponList(List <Item> items){
		ArrayList<Coupon> couponList = new ArrayList<Coupon>();
		for (Item item : items) {
			Coupon coupon = new Coupon(item);
			couponList.add(coupon);
		}
		return couponList;
	}
	
	/*public ArrayList<ItemVector> getItemVectors(List <Item> items){
		ArrayList<ItemVector> itemVectors = new ArrayList<ItemVector>();
		for (Item item : items) {
			ItemVector itemVector = new ItemVector(item);
			itemVectors.add(itemVector);
		}
		return itemVectors;
	}*/
	
	/*public Map<Long, ItemVector> matchCouponWithMovie(List <Item> items, List<Long> idList){
		ArrayList<ItemVector> itemVectors = getItemVectors(items);
		Random random = new Random();
		
		Map<Long, ItemVector> itemVectorMap = new HashMap<Long, ItemVector>();
		
		List<Long> tempList = new ArrayList<Long>(idList);
		
		for (ItemVector itemVector : itemVectors) {
			int idxList = random.nextInt(tempList.size());
			long id = tempList.remove(idxList);
			itemVector.setId(id);
			
			itemVectorMap.put(id, itemVector);
		}
		return itemVectorMap;
	}*/
	
	/**
	 * Match coupon with movie.
	 *
	 * @param items the items
	 * @param idList the id list
	 * @return the map
	 */
	public Map<Long, Coupon> matchCouponWithMovie(List <Item> items, List<Long> idList){
		ArrayList<Coupon> couponList = getCouponList(items);
		Random random = new Random();
		
		Map<Long, Coupon> couponMap = new HashMap<Long, Coupon>();
		
		List<Long> tempList = new ArrayList<Long>(idList);
		
		for (Coupon coupon : couponList) {
			int idxList = random.nextInt(tempList.size());
			long id = tempList.remove(idxList);
			coupon.setId(id);
			
			couponMap.put(id, coupon);
		}
		return couponMap;
	}
}