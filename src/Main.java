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


public class Main {

	

	/**
	 * Extract id from movie lens data.
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<Integer> getIdArray(String idFile){
		Scanner idScanner;
		Set<Integer> ids = new HashSet<Integer>();
		try {
			idScanner = new Scanner(new File(idFile));
			
			while(idScanner.hasNextInt()){
				ids.add(idScanner.nextInt());
			}
			idScanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
	}
	
	public void filterMovieLens(Set<Integer> ids, String movieFile, String filteredMovieFile){
		try {
						
			Scanner scanner = new Scanner(new File(movieFile));
			PrintWriter printWriter = new PrintWriter(new File(filteredMovieFile));
			
			while (scanner.hasNextInt()){
				int uId = scanner.nextInt();
				int itemId = scanner.nextInt();
				int rate = scanner.nextInt();
				if (ids.contains(itemId)){
					printWriter.println( uId + "," + itemId + "," + rate);
				}
				
				scanner.nextLine();
			}
			
			scanner.close();
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String couponFile = "data/coupon.xml";
		String idFIle = "100k-data/idMovie.txt";
		XMLParser parser = new XMLParser();
		List <Item> items = parser.getAllItems(couponFile);
		
		Main main = new Main();
		
		main.extractIDFromMovieLensData(items.size(), "100k-data/u.data", idFIle);
		Set<Integer> ids = main.getIdArray(idFIle);
		main.filterMovieLens(ids, "100k-data/u.data", "100k-data/u.csv");
		
		List<Integer> idList = new ArrayList<Integer>(ids);
		
		Random random = new Random();
		
		for (Item item : items) {
			int idxList = random.nextInt(idList.size());
			int id = idList.remove(idxList);
			item.id = id;
		}
		
		for (Item item : items) {
			item.printItem();
		}
		System.out.println(idList.size());
	}

}
